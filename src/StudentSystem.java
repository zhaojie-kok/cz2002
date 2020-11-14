import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StudentSystem {
    Student user;
    Course selectedCourse;
    Index selectedIndex;
    CourseMgr courseMgr;
    StudentManager studentManager;
    CalendarMgr calendarMgr;

    public StudentSystem(Student student) {
        user = student;
        calendarMgr = new CalendarMgr();
        studentManager = new StudentManager();
        courseMgr = new CourseMgr();
    }

    public int addCourse() {
        /**
         * Returns 1 is successfully registered, 0 if waitlisted, and -1 if not possible
         * to add.
         */
        // Check addable (AU requirement and whether already registered)

        // Check timing clash

        // Check index has vacancy

        // If no vacancy, add to waitlist
        return 0;
    }

    public int dropCourse() {
        /**
         * Checks if registered, then proceeds to drop. 1 for success. Else 0.
         */
        return 0;
    }

    public String printAllCourses(String format){
        /**
         * Converts to printable format
         */
        String toReturn = "";

        Iterator<String> courses = courseMgr.getHashMap().keySet().iterator();
        while (courses.hasNext()) { 
            toReturn += String.format(format, courses.next());
        }
        return toReturn;
    }

    public String printCoursesBySchool(School school, String format){
        return "";
    }

    public String printCoursesByStringFilter(String filter, String format){
        return "";
    }

    public HashMap<String, String> printRegisteredCourses() {
        /**
         * If no parameters provided, returns HashMap<String,String>
         */
        return user.getCourses();
    }

    public String printRegisteredCourses(String format) {
        /**
         * Converts to printable format
         */
        String toReturn = "";
        String toAdd;
        HashMap<String, String> hMap = user.getCourses();
        List<String> indexes = new ArrayList<String>(hMap.values());
        List<String> courses = new ArrayList<String>(hMap.keySet());
        for (int i = 0; i < hMap.size(); i++) {
            toAdd = String.format(format, courses.get(i), indexes.get(i));
            toReturn += toAdd;
        }
        return toReturn;
    }

    public int selectCourse(String courseCode){
        Course tmp = courseMgr.getCourse(courseCode)
        if (tmp == null){
            return 0;
        }
        selectedCourse = tmp;
        return 1;
    }

    public int selectIndex(String indexNo){
        if (selectedCourse == null){
            return 0;
        }
        Index tmp = courseMgr.getCourseIndex(selectedCourse, indexNo);
        if (tmp == null){
            return 0;
        }
        selectedIndex = tmp;
        return 1;
    }

    public HashMap<String, Index> getIndexesOfCourse() {
        /**
         * If no parameters provided, returns HashMap<String,Index>
         */
        return selectedCourse.getIndexes();
    }

    public String getIndexesOfCourse(String format) {
        /**
         * Converts to printable format
         */
        String toReturn = "";
        Iterator<String> indexes = selectedCourse.getIndexes().keySet().iterator();
        while (indexes.hasNext()) { 
            toReturn += String.format(format, indexes.next());
        }
        return toReturn;
    }

    public int checkVacanciesAvailable(){
        return selectedIndex.getSlotsAvailable();
    }

    public int swopIndexWithStudent(String matricNo){
        // Check timing clash
        Student toSwapWith = studentManager.getStudent(matricNo);

        String strToSwapTo = toSwapWith.getCourseIndex(selectedCourse.getCourseCode());
        Index toSwapTo = courseMgr.getCourseIndex(selectedCourse, strToSwapTo);

        String strCurr = user.getCourseIndex(selectedCourse.getCourseCode());
        Index currentIndex = courseMgr.getCourseIndex(selectedCourse, strCurr);

        if (!checkSwopClash(user, toSwapTo) && !checkSwopClash(toSwapWith, currentIndex)){
            studentManager.swopIndex(user, toSwapWith, selectedCourse.getCourseCode());
            courseMgr.removeStudent(user, currentIndex, selectedCourse);
            courseMgr.removeStudent(toSwapWith, toSwapTo, selectedCourse);
            courseMgr.addStudent(user, toSwapTo);
            courseMgr.addStudent(toSwapWith, currentIndex);
            return 1;
        }
        return 0;
    }

    public int swopIndexWithAvailableSlot(Index toSwapTo){
        if (toSwapTo.getSlotsAvailable() <= 0){
            return -1;
        }
        String current = user.getCourseIndex(selectedCourse.getCourseCode());
        courseMgr.removeStudent(user, courseMgr.getCourseIndex(selectedCourse, current), selectedCourse);
        courseMgr.addStudent(user, toSwapTo);
        return toSwapTo.getSlotsAvailable();
    }

    public boolean checkAddClash(Student student) {
        /**
         * Returns false if no ADD clash. Else returns true.
         */
        HashMap<String, String> courses = student.getCourses();
        Course course;
        Index registered;

        // iterate through all of student's registered courses to check for clash
        for (Map.Entry<String, String> entry: courses.entrySet()) {
            course = courseMgr.getCourse(entry.getKey());
            registered = courseMgr.getCourseIndex(course, entry.getValue());
            if (calendarMgr.checkClash(registered, selectedIndex)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkSwopClash(Student student, Index newIndex) {
        /**
         * Returns false if no SWOP clash. Else returns true.
         */
        HashMap<String, String> courses = student.getCourses();
        Course course;
        Index registered;

        // iterate through all of student's registered courses to check for clash
        for (Map.Entry<String, String> entry: courses.entrySet()) {
            if (entry.getKey() == selectedCourse.getCourseCode()){
                continue;
            }
            course = courseMgr.getCourse(entry.getKey());
            registered = courseMgr.getCourseIndex(course, entry.getValue());
            if (calendarMgr.checkClash(registered, newIndex)) {
                return true;
            }
        }
        return false;
    }
}
