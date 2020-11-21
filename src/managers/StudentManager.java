package managers;

import java.time.LocalDateTime;
import java.util.HashMap;
import readers.*;
import entities.*;
import entities.course_info.*;
import exceptions.Filereadingexception;
import exceptions.KeyClashException;

public class StudentManager implements EntityManager {
    private HashMap<String, Student> students;
    StudentReader sReader;

    public StudentManager() throws Filereadingexception {
        // TODO: filepath
        sReader = new StudentReader("data/students/");
        students = (HashMap<String, Student>) sReader.getData();
    }

    public Student getStudent(String identifier){
        /**
         * Returns student based on identifier (either matricNo or userId)
         */
        return students.get(identifier);
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
        Student newStudent = new Student(userId, name, gender, nationality,
                                        matricNo, accessPeriod, new HashMap<String, String>(),
                                        new HashMap<String, String>());
        students.put(matricNo, newStudent);
        saveState(newStudent);
    }

    public boolean dropCourse(Course course, Student student) {
        /**
         * Returns boolean for dropping a course (true if successful, false otherwise)
         */
        // Check if student is registered
        if (student.isRegistered(course)){
            student.removeCourse(course.getCourseCode(), course.getAcadU());
            saveState(student);
            return true;
        }
        return false;
    }

    public int addCourse(Course course, Index index, Student student) {
        /**
         * Returns int. 1=successful registration, 0=waitlisted, negative if other results (Fail)
         * Note: error code -1 is returnedby student system
         */
        if (student.isRegistered(course) || student.isWaitlisted(course)) {
            // already registered
            return -2;
        } else if (student.getAcadUnits() + course.getAcadU() > student.getAcadUnitsAllowed()) {
            // overload
            return -3;
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

    public void swopIndex(Student s1, Student s2, String courseCode) {
        // update the student info only
        String i1 = s1.getCourseIndex(courseCode);
        String i2 = s2.getCourseIndex(courseCode);
        s1.changeIndex(courseCode, i2);
        s2.changeIndex(courseCode, i1);
        saveState(s1);
        saveState(s2);
    }

    public void swopIndex(Student student, Course course, Index newIndex) {
        student.changeIndex(course.getCourseCode(), newIndex.getIndexNo());
        saveState(student);
    }

    public boolean updateAccessPeriod(Student student, LocalDateTime[] newAccessPeriod) {
        LocalDateTime[] accessPeriod = student.getAccessPeriod();
		if (accessPeriod == newAccessPeriod){
            // same/updated alr
            return false; // TODO: change to exception
        }
        student.changeAccessPeriod(newAccessPeriod);
        saveState(student);
        return true;
    }

	@Override
    public void saveState(Object student) {
        Student s = (Student) student;
        sReader.writeData(s);
        students.replace(s.getMatricNo(), s);
    }
}
