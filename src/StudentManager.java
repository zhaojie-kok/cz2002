import java.util.HashMap;

public class StudentManager {
    public void dropCourse(String courseCode, String indexNo, Student student) {
        return;
    }

    public int addCourse(String courseCode, String indexNo, Student student) {
        // TODO: consider having timetable clashes checked here
        /* -1: failed, 0: waitlist, 1: succcess */
        return 0;
    }

    public void swopIndex(Student s1, Student s2, String courseCode) {
        // TODO: consider checking timetable clashses here
        return;
    }

    public HashMap<String, String> checkWaitlist(Student student) {
        return null;
    }

    public HashMap<String, String> checkCoursesRegistered(Student student) {
        return student.getCourses();
    }
}
