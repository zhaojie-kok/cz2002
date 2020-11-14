import java.util.HashMap;
import java.util.Map;

public class StudentSystem {
    Student user;
    CourseMgr courseMgr;
    StudentManager studentManager;
    CalendarMgr calendarMgr;

    public StudentSystem(Student student){
        user = student;
        calendarMgr = new CalendarMgr();
        studentManager = new StudentManager();
        courseMgr = new CourseMgr();
    }

    public int addCourse(){
        // Check addable (AU requirement and already registered)

        // Check timing clash

        // Check index has vacancy

        // If no vacancy, add to waitlist
        return 0;
    }

    public int dropCourse(){
        // Check if registered
        return 0;
    }

    public HashMap<String,String> printRegisteredCourses(){
        return user.getCourses();
    }

    public HashMap<String,Index> getIndexesOfCourse(Course c){
        return c.getIndexes();
    }

    public int checkVacanciesAvailable(Index i){
        return i.getSlotsAvailable();
    }

    public int swopIndexWithStudent(){
        // Check timing clash

        return 0;
    }

    public boolean checkAddClash(Student s, Index i) {
        HashMap<String, String> courses = s.getCourses();
        Index registeredInd;

        // iterate through all of student's registered courses to check for clash
        for (Map.Entry<String, String> entry: courses.entrySet()) {
            registeredInd = courseMgr.getCourseIndex(entry.getKey(), entry.getValue());
            if (calMgr.checkClash(registeredInd, i)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkSwopClash(Student s, String courseCode, Index newIndex) {
        HashMap<String, String> courses = s.getCourses();

        // remove the course to be changed from the list of courses
        courses.remove(courseCode);
        Index registeredInd;

        // iterate through all of student's registered courses to check for clash
        for (Map.Entry<String, String> entry: courses.entrySet()) {
            registeredInd = CourseMgr.getCourseIndex(entry.getKey(), entry.getValue());
            if (checkClash(registeredInd, newIndex)) {
                return true;
            }
        }
        return false;
    }
}
