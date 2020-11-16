package managers;

import java.util.Calendar;
import java.util.HashMap;
import readers.*;
import entities.*;
import entities.course_info.*;

// public class StudentManager implements EntityManager{
//     public int dropCourse(Course course, String indexNo, Student student) {
//         String courseCode = course.getCourseCode();
//         if (student.getCourseIndex(courseCode) == null) {
//             return -1;
//         }
        // student.removeCourse(courseCode);
        //         // TODO: removeStudent, dequeue waitlist
        //         // CourseMgr.removeStudent()
        //         return 1;
        //     }

public class StudentManager implements EntityManager {
    private HashMap<String, Student> students = FileReader.loadStudents();

    public Student getStudent(String matricNo){
        return students.get(matricNo);
    }

    public boolean createStudent(String userId, String name, String gender, String nationality,
    String matricNo, Calendar[] accessPeriod){
        // TODO: Does userId need to be unique
        if (students.containsKey(matricNo)){
            return false;
        }
        Student newStudent = new Student(userId, name, gender, nationality,
                                        matricNo, accessPeriod, new HashMap<String, String>());
        students.put(matricNo, newStudent);
        saveState(newStudent);
        return true;
    }

    public boolean dropCourse(Course course, Student student) {
        if (student.isRegistered(course)){
            student.removeCourse(course.getCourseCode());
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
            student.addCourse(course.getCourseCode(), index.getIndexNo());
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
        // update the student info
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
        FileReader.writeStudent((Student) s);
        students.replace(s.getMatricNo(), s);
    }
}
