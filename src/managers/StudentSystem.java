package managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import entities.*;
import entities.course_info.*;
import readers.StudentReader;

public class StudentSystem implements Systems {
    private Student user;
    private Course selectedCourse;
    private Index selectedIndex;
    private CourseMgr courseMgr;
    private StudentManager studentManager;
    private CalenderMgr calendarMgr;
    private static StudentReader studentReader = new StudentReader("student_files/");

    public StudentSystem(Student student) {
        user = student;
        calendarMgr = new CalenderMgr();
        studentManager = new StudentManager();
        courseMgr = new CourseMgr();
    }

    public StudentSystem(String userId) {
        user = (Student) studentReader.getData(userId);
        calendarMgr = new CalenderMgr();
        studentManager = new StudentManager();
        courseMgr = new CourseMgr();
    }

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

    public int dropCourse() {
        /**
         * Checks if registered, then proceeds to drop. 1 for success. 0 for removed from waitlist. -1 else
         */
        if (user.isRegistered(selectedCourse)){
            user.removeCourse(selectedCourse.getCourseCode());
            return 1;
        }
        else if (user.isWaitlisted(selectedCourse)){
            user.removeWaitlist(selectedCourse);
            return 0;
        }
        // if successfully dropped, dequeue waitlist
        return -1;
    }

    public String printAllCourses(String format){
        /**
         * Converts to printable format
         */
        String toReturn = "";
        Course c;

        Iterator<Course> courses = courseMgr.getHashMap().values().iterator();
        while (courses.hasNext()) { 
            c = courses.next();
            toReturn += String.format(format, c.getCourseCode(), c.getCourseName());
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
                toReturn += String.format(format, c.getCourseCode(), c.getCourseName());
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
                toReturn += String.format(format, c);
            }
        }
        return toReturn;
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
        Course tmp = courseMgr.getCourse(courseCode);
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
            courseMgr.addStudent(user, toSwapTo, selectedCourse);
            courseMgr.addStudent(toSwapWith, currentIndex, selectedCourse);
            return 1;
        }
        return 0;
    }

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
