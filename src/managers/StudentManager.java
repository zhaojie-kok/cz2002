package managers;

import java.time.LocalDateTime;
import java.util.HashMap;
import readers.*;
import entities.*;
import entities.course_info.*;
import exceptions.FileReadingException;
import exceptions.KeyClashException;
import exceptions.KeyNotFoundException;
import exceptions.OutOfRangeException;

public class StudentManager implements EntityManager {
    private HashMap<String, Student> students;
    StudentReader sReader;

    public StudentManager() throws FileReadingException {
        sReader = new StudentReader("data/students/");
        students = (HashMap<String, Student>) sReader.getData();
    }

    public Student getStudent(String identifier) throws KeyNotFoundException {
        /**
         * Returns student based on identifier (either matricNo or userId)
         */
        Student toReturn = students.get(identifier);
        if (toReturn == null){
            throw new KeyNotFoundException(identifier);
        }
        return toReturn;
    }

    public HashMap<String, Student> getStudents() {
        return students;
    }

    public void createStudent(String userId, String name, String gender, String nationality,
    String matricNo, LocalDateTime[] accessPeriod) throws KeyClashException {
        /**
         * creates a new student based on information provided
         */
        if (students.containsKey(matricNo)){
            // If another student exists with the matricNo or userId, no student is created
            throw new KeyClashException("Matric Number already exists");
        } else if (students.containsKey(userId)) {
            throw new KeyClashException("UserID already exists");
        }
        Student newStudent = new Student(userId, name, gender, nationality, matricNo, accessPeriod,
                new HashMap<String, String>(), new HashMap<String, String>());
        students.put(matricNo, newStudent);
        saveState(newStudent);
    }

    public void dropCourse(Course course, Student student) throws OutOfRangeException {
        /**
         * Returns boolean for dropping a course (true if successful, false otherwise)
         */
        // Check if student is registered
        if (student.isRegistered(course)) {
            student.removeCourse(course.getCourseCode(), course.getAcadU());
            saveState(student);
        } else if (student.isWaitlisted(course)) {
            student.removeWaitlist(course);
            saveState(student);
        } else {
            throw new OutOfRangeException(student.getUserId() + " is not registered for " + course.getCourseCode());
        }
    }

    public int addCourse(Course course, Index index, Student student) throws OutOfRangeException {
        /**
         * 
         */
        if (student.isRegistered(course) || student.isWaitlisted(course)) {
            throw new OutOfRangeException("Cannot register for a course already registered");
        } else if (student.getAcadUnits() + course.getAcadU() > student.getAcadUnitsAllowed()) {
            throw new OutOfRangeException("Insufficient Academic Units to take this course");
        }

        if (index.getSlotsAvailable() > 0) {
            // register successfully
            student.addCourse(course.getCourseCode(), index.getIndexNo(), course.getAcadU());
            saveState(student);
            return 1;
        } else {
            // waitlisted
            student.addWaitlist(course, index.getIndexNo());
            saveState(student);
            return 0;
        }
    }

    public void swopIndex(Student s1, Student s2, Course course) throws KeyNotFoundException, OutOfRangeException {
        if (!(s1.isRegistered(course) && s2.isRegistered(course))) {
            throw new OutOfRangeException("Both Students must be registered for the course");
        }

        String courseCode = course.getCourseCode();
        // update the student info only
        String i1 = s1.getCourseIndex(courseCode);
        String i2 = s2.getCourseIndex(courseCode);

        // do nothing if indexes are the same
        if (i1 == i2) {
            return;
        }

        s1.changeIndex(courseCode, i2);
        s2.changeIndex(courseCode, i1);
        saveState(s1);
        saveState(s2);
    }

    public void swopIndex(Student student, Course course, Index newIndex) {
        student.changeIndex(course.getCourseCode(), newIndex.getIndexNo());
        saveState(student);
    }

    public void updateAccessPeriod(Student student, LocalDateTime[] newAccessPeriod) throws FileReadingException {
        LocalDateTime[] accessPeriod = student.getAccessPeriod();
		if (accessPeriod == newAccessPeriod){
            // same/updated already
            return;
        }

        try {
            student.changeAccessPeriod(newAccessPeriod);
            saveState(student);
        } catch (Exception e) {
            throw new FileReadingException("Error in changing accesss period. Please contact system administrator");
        }
    }

	@Override
    public void saveState(Object student) {
        Student s = (Student) student;
        sReader.writeData(s);
        students.replace(s.getMatricNo(), s);
    }
}
