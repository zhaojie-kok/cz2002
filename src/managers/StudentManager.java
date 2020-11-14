package managers;

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
    private static HashMap<String, Student> students = FileReader.loadStudents();

    public static Student getStudent(String matricNo){
        return students.get(matricNo);
    }

    public void dropCourse(String courseCode, String indexNo, Student student) {
        return;
    }

        

    public int addCourse(Course course, String indexNo, Student student) {
        HashMap <String, String> courses = student.getCourses();
        
        // not allowed to add a course already registered for or requires overloading
        if (courses.containsKey(course.getCourseCode())) {
            return -1;
        } else if (student.getAcadUnits() + course.getAU() > student.getAcadUnitsAllowed()) {
            return -1;
        }

        // first ensure that there's no clash
        Index newIndex = course.getIndex(indexNo);
        CalenderMgr cMgr = new CalenderMgr();

        boolean clash = cMgr.checkAddClash(student, newIndex);

        // add or reject respectively
        if (clash) {
            return -1;
        } else if (newIndex.getSlotsAvailable() > 0) {
            student.addCourse(course.getCourseCode(), indexNo);
            // TODO: CourseMgr.addStudent(course, indexNo, student);
            saveState(student);
            return 1;
        } else {
            CourseMgr courseMgr = new CourseMgr();
            // update the course
            courseMgr.enqueueWaitlist(student, course, indexNo);
            courseMgr.saveState(course);
            // update the student
            student.addWaitlist(course, indexNo);
            saveState(student);
            return 0;
        }
    }

    public int swopIndex(Student s1, Student s2, String courseCode) {
        CalenderMgr cMgr = new CalenderMgr();
        Index i1 = CourseMgr.getCourseIndex(courseCode, s1.getCourseIndex(courseCode));
        Index i2 = CourseMgr.getCourseIndex(courseCode, s2.getCourseIndex(courseCode));

        boolean clash = (cMgr.checkSwopClash(s1, courseCode, i2) || cMgr.checkSwopClash(s2, courseCode, i1));
        if (clash) {
            return -1;
        } else {
            // update the student info
            s1.removeCourse(courseCode);
            s1.addCourse(courseCode, i2.getIndexNo());
            s2.removeCourse(courseCode);
            s2.addCourse(courseCode, i1.getIndexNo());
            saveState(s1);
            saveState(s2);
            return 1;
        }
    }

	@Override
    public void saveState(Object student) {
        FileReader.writeStudent((Student) student);
    }
}
