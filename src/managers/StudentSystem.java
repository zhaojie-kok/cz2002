package managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import entities.*;
import entities.course_info.*;

public class StudentSystem implements Systems {
    private Student user;
    private Course selectedCourse = null;
    private Index selectedIndex = null;
    private CourseMgr courseMgr;
    private StudentManager studentManager;
    private CalendarMgr calendarMgr;

    public StudentSystem(String userId) {
        calendarMgr = new CalendarMgr();
        studentManager = new StudentManager();
        studentManager.getStudent(userId);
        courseMgr = new CourseMgr();
        user = studentManager.getStudent(userId);
    }

    public String printAllCourses(){
        /**
         * Converts to printable format
         */
        String toReturn = "";
        Course c;

        Iterator<Course> courses = courseMgr.getHashMap().values().iterator();
        while (courses.hasNext()) { 
            c = courses.next();
            toReturn += c.getInfo();
        }
        return toReturn;
    }

    public String printCoursesBySchool(School school, String format){
        /**
         * Converts to printable format
         */
        String toReturn = "";
        Course c;

        Iterator<Course> courses = courseMgr.getHashMap().values().iterator();
        while (courses.hasNext()) { 
            c = courses.next();
            if (c.isSchool(school)){
                toReturn += c.getInfo();
            }
        }
        return toReturn;
    }

    public String printCoursesByStringFilter(String filter, String format){
        /**
         * Searched course name and code for the filter
         */
        String toReturn = "";
        Course c;

        Iterator<Course> courses = courseMgr.getHashMap().values().iterator();
        while (courses.hasNext()) { 
            c = courses.next();
            if (c.getCourseName().contains(filter) || c.getCourseCode().contains(filter)){
                toReturn += c.getInfo();
            }
        }
        return toReturn;
    }

    // FUNCTIONAL REQUIREMENT - Student: 3. Check registered courses
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

    /////////////// Select course/select index are necessary for the following functions
    public int selectCourse(String courseCode){
        Course tmp = courseMgr.getCourse(courseCode);
        if (tmp == null){
            return 0;
        }
        selectedCourse = tmp;
        return 1;
    }

    public int selectIndex(String indexNo){
        if (selectedCourse == null){
            return -1;
        }
        Index tmp = courseMgr.getCourseIndex(selectedCourse, indexNo);
        if (tmp == null){
            return 0;
        }
        selectedIndex = tmp;
        return 1;
    }

    public String getSystemStatus(){
        String sc = selectedCourse == null ? "":"Selected course: " + selectedCourse.getCourseName();
        String si = selectedIndex == null ? "":"Selected index: " + selectedIndex.getIndexNo();
        return sc + "\n" + si;
    }

    // These are called AFTER selectCourse/selectIndex
    ////// ++++++++ START +++++++++
    // FUNCTIONAL REQUIREMENT - Student: 1. Add course
    public int addCourse() {
        /**
         * Returns 1 is successfully registered, 0 if waitlisted, and negative if not possible
         * to add.
         *  
         * -1 : Timetable clash
         * -2 : Already registered
         * -3 : Exceeds AU
         */

        // Check timing clash
        if (checkAddClash(user)){
            return -1;
        }

        return studentManager.addCourse(selectedCourse, selectedIndex, user);
    }

    // FUNCTIONAL REQUIREMENT - Student: 2. Drop course
    public int dropCourse() {
        /**
         * Checks if registered, then proceeds to drop. 1 for success. 0 for removed from waitlist. -1 else
         */
        // TODO: Go through StudentMgr
        if (user.isRegistered(selectedCourse)){
            user.removeCourse(selectedCourse.getCourseCode(), selectedCourse.getAcadU());
            return 1;
        }
        else if (user.isWaitlisted(selectedCourse)){
            user.removeWaitlist(selectedCourse);
            return 0;
        }
        // if successfully dropped, dequeue waitlist
        return -1;
    }
    
    public String getIndexesOfCourse(String format) {
        /**
         * Converts to printable format
         */
        String toReturn = "";
        Iterator<Index> indexes = selectedCourse.getIndexes().values().iterator();
        while (indexes.hasNext()) { 
            toReturn += String.format(format, indexes.next().getInfo());
        }
        return toReturn;
    }

    // FUNCTIONAL REQUIREMENT - Student: 4. Check vacancies available
    public int checkVacanciesAvailable(){
        return selectedIndex.getSlotsAvailable();
    }

    // FUNCTIONAL REQUIREMENT - Student: 6. Swop index number with student
    public int swopIndexWithStudent(String matricNo){
        /** 
         * Returns 0 and does nothing if either students will havetimetable clash, 
         * else swaps indexes and returns 1
         */
        Student toSwapWith = studentManager.getStudent(matricNo);

        String strToSwapTo = toSwapWith.getCourseIndex(selectedCourse.getCourseCode());
        Index toSwapTo = courseMgr.getCourseIndex(selectedCourse, strToSwapTo);

        String strCurr = user.getCourseIndex(selectedCourse.getCourseCode());
        Index currentIndex = courseMgr.getCourseIndex(selectedCourse, strCurr);

        if (!checkSwopClash(user, toSwapTo) && !checkSwopClash(toSwapWith, currentIndex)){
            studentManager.swopIndex(user, toSwapWith, selectedCourse.getCourseCode());
            courseMgr.removeStudent(user, currentIndex, selectedCourse);
            courseMgr.removeStudent(toSwapWith, toSwapTo, selectedCourse);
            courseMgr.addStudent(user, toSwapTo, selectedCourse);
            courseMgr.addStudent(toSwapWith, currentIndex, selectedCourse);
            return 1;
        }
        return 0;
    }

    // FUNCTIONAL REQUIREMENT - Student: 5. Change index number of course
    public int swopToIndex(){
        // already registered for index
        if (user.isRegistered(selectedIndex)){
            return -1;
        }
        // no more slots
        if (selectedIndex.getSlotsAvailable() <= 0){
            return -2;
        }
        String current = user.getCourseIndex(selectedCourse.getCourseCode());
        if (checkSwopClash(user, selectedIndex)){
            // clashes with current timetable, not including current index
            return -3;
        }
        courseMgr.removeStudent(user, courseMgr.getCourseIndex(selectedCourse, current), selectedCourse);
        courseMgr.addStudent(user, selectedIndex, selectedCourse);
        return selectedIndex.getSlotsAvailable();
    }

    private boolean checkAddClash(Student student) {
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

    private boolean checkSwopClash(Student student, Index newIndex) {
        /**
         * Returns false if no SWOP clash. Else returns true.
         */
        HashMap<String, String> courses = student.getCourses();
        Course course;
        Index registered;

        // iterate through all of student's registered courses to check for clash
        for (Map.Entry<String, String> entry: courses.entrySet()) {
            // except for the course intending to switch out (since they will likely clash)
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
    ////// ++++++++ END +++++++++
}
