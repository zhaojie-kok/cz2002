package managers;

import java.util.Calendar;
import java.util.HashMap;
import readers.*;
import entities.*;
import entities.course_info.*;

public class StudentManager implements EntityManager {
    private HashMap<String, Student> students;
    StudentReader sReader;

    public StudentManager(){
        // TODO: filepath
        StudentReader sReader = new StudentReader("PLACEHOLDER");
        students = (HashMap<String, Student>) sReader.getData();
    }

    public Student getStudent(String identifier){
        /**
         * Returns student based on identifier (either matricNo or userId)
         */
        return students.get(identifier);
    }

    public boolean createStudent(String userId, String name, String gender, String nationality,
    String matricNo, Calendar[] accessPeriod){
        /**
         * Returns boolean for creating a student (true if created, false otherwise)
         */
        if (students.containsKey(matricNo) || students.containsKey(userId)){
            // If another student exists with the matricNo or userId, no student is created
            return false;
        }
        Student newStudent = new Student(userId, name, gender, nationality,
                                        matricNo, accessPeriod, new HashMap<String, String>());
        students.put(matricNo, newStudent);
        saveState(newStudent);
        return true;
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
        s1.changeIndex(courseCode, i1, i2);
        s2.changeIndex(courseCode, i2, i1);
        saveState(s1);
        saveState(s2);
    }

    public boolean updateAccessPeriod(String matricNo, Calendar[] newAccessPeriod){
        Student student = getStudent(matricNo);
        Calendar[] accessPeriod = student.getAccessPeriod();
		if (accessPeriod == newAccessPeriod){
            // same/updated alr
            return false;
        }
        student.changeAccessPeriod(newAccessPeriod);
        return true;
    }

	@Override
    public void saveState(Object student) {
        Student s = (Student) student;
        sReader.writeData(s);
        students.replace(s.getMatricNo(), s);
    }
}
