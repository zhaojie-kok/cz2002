package managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import entities.*;
import entities.course_info.*;
import exceptions.Filereadingexception;
import exceptions.KeyNotFoundException;
import exceptions.MissingSelectionException;

public class StudentSystem implements CourseSystemInterface {
    private CourseMgr courseMgr;
    private StudentManager studentManager;
    private CalendarMgr calendarMgr;

    private Student user;
    private Course selectedCourse = null;
    private Index selectedIndex = null;

    public StudentSystem(String userId) throws Filereadingexception, KeyNotFoundException {
        calendarMgr = new CalendarMgr();
        studentManager = new StudentManager();
        courseMgr = new CourseMgr();
        user = studentManager.getStudent(userId);
    }

    public String printAllCourses() {
        /**
         * Converts to printable format
         */
        String toReturn = "";
        Course c;

        Iterator<Course> courses = courseMgr.getAllCourses().values().iterator();
        while (courses.hasNext()) {
            c = courses.next();
            toReturn += c.getInfo();
        }
        return toReturn;
    }

    public String printCoursesBySchool(School school, String format) {
        /**
         * Converts to printable format
         */
        String toReturn = "";
        Course c;

        Iterator<Course> courses = courseMgr.getAllCourses().values().iterator();
        while (courses.hasNext()) {
            c = courses.next();
            if (c.isSchool(school)) {
                toReturn += String.format(format, c.getInfo());
            }
        }
        return toReturn;
    }

    public String printCoursesByStringFilter(String filter, String format) {
        /**
         * Searched course name and code for the filter
         */
        String toReturn = "";
        Course c;

        Iterator<Course> courses = courseMgr.getAllCourses().values().iterator();
        while (courses.hasNext()) {
            c = courses.next();
            if (c.getCourseName().contains(filter) || c.getCourseCode().contains(filter)) {
                toReturn += String.format(format, c.getInfo());
            }
        }
        return toReturn;
    }

    // FUNCTIONAL REQUIREMENT - Student: 3. Check registered courses
    public String checkRegisteredCourses(String format) {
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

    /////////////// Select course/select index are necessary for the following
    /////////////// functions
    @Override
    public void selectCourse(String courseCode) throws KeyNotFoundException {
        selectedCourse = courseMgr.getCourse(courseCode);
    }

    @Override
    public void selectIndex(String indexNo) throws KeyNotFoundException, MissingSelectionException{
        selectedIndex = courseMgr.getCourseIndex(selectedCourse, indexNo);
    }

    @Override
    public String getSystemStatus(){
        String sc = selectedCourse == null ? "":"Selected course: " + selectedCourse.getCourseName();
        String si = selectedIndex == null ? "":"Selected index: " + selectedIndex.getIndexNo();
        return sc + "\n" + si;
    }

    @Override
    public void clearSelections() {
        selectedCourse = null;
        selectedIndex = null;
    }

    // These are called AFTER selectCourse/selectIndex
    ////// ++++++++ START +++++++++
    // FUNCTIONAL REQUIREMENT - Student: 1. Add course
    public int addCourse() throws KeyNotFoundException, MissingSelectionException {
        /**
         * Returns 1 is successfully registered, 
         * 0 if waitlisted,
         * negative if not possible
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
        
        // TODO: throw exceptions for negative results
        int result = studentManager.addCourse(selectedCourse, selectedIndex, user);

        // in the event that student has been successfully added or waitlisted,
        // the course needs to be updated
        switch(result) {
            case 1:
                courseMgr.addStudent(user, selectedIndex, selectedCourse);
                break;
            case 2:
                courseMgr.enqueueWaitlist(user, selectedIndex, selectedCourse);
                break;
        }

        return result;
    }

    // FUNCTIONAL REQUIREMENT - Student: 2. Drop course
    public int dropCourse() {
        /**
         * Checks if registered, then proceeds to drop. 
         * 1 for success. 
         * 0 for removed from waitlist. 
         * -1 else
         */
        // TODO: Go through StudentMgr
        int result = -1;
        if (user.isRegistered(selectedCourse)){
            user.removeCourse(selectedCourse.getCourseCode(), selectedCourse.getAcadU());
            result = 1;
        } else if (user.isWaitlisted(selectedCourse)){
            user.removeWaitlist(selectedCourse);
            result = 0;
        }
        
        // remove the student from the course after
        courseMgr.removeStudent(user, selectedIndex, selectedCourse);
        return result;
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
    public HashMap<String, Integer> checkVacanciesAvailable(){
        if (selectedCourse != null) {
            // if a particular slot has been selected then return the vacancy
            HashMap<String, Integer> compiled = new HashMap<>();
            HashMap<String, Index> indexes = selectedCourse.getIndexes();
            for (Index index: indexes.values()) {
                compiled.put(index.getIndexNo(), index.getSlotsAvailable());
            }
            return compiled;
        } else {
            return null;
        }
    }

    // FUNCTIONAL REQUIREMENT - Student: 6. Swop index number with student
    public int swopIndexWithStudent(String identifier) throws KeyNotFoundException, MissingSelectionException {
        /** 
         * Returns 0 and does nothing if either students will havetimetable clash, 
         * else swaps indexes and returns 1
         * -1: user or swopping student is not registered in the course
         */
        Student toSwapWith = studentManager.getStudent(identifier);

        // TODO: change to exceptions
        // first ensure that the swopping students both are enrolled in the course
        if (!user.isRegistered(selectedCourse)) {
            return -1;
        } else if (!toSwapWith.isRegistered(selectedCourse)) {
            return -1;
        }

        String strToSwapTo = toSwapWith.getCourseIndex(selectedCourse.getCourseCode());
        Index toSwapTo = courseMgr.getCourseIndex(selectedCourse, strToSwapTo);
        String strCurr = user.getCourseIndex(selectedCourse.getCourseCode());
        Index currentIndex = courseMgr.getCourseIndex(selectedCourse, strCurr);

        // raise error if both are registered in same course
        if (strToSwapTo.equals(strCurr)) {
            return -2;
        }

        if (!checkSwopClash(user, toSwapTo) && !checkSwopClash(toSwapWith, currentIndex)){
            studentManager.swopIndex(user, toSwapWith, selectedCourse.getCourseCode());
            int result = courseMgr.swopStudents(user, toSwapWith, selectedCourse);
            if (result == 1) {
                studentManager.swopIndex(user, toSwapWith, selectedCourse.getCourseCode());
            }
            return result;
        }
        return 0;
    }

    // FUNCTIONAL REQUIREMENT - Student: 5. Change index number of course
    public int swopToIndex() throws KeyNotFoundException, MissingSelectionException {
        // TODO: convert into exceptions
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
        studentManager.swopIndex(user, selectedCourse, selectedIndex);
        return selectedIndex.getSlotsAvailable();
    }

    private boolean checkAddClash(Student student) throws KeyNotFoundException, MissingSelectionException {
        /**
         * Returns false if no ADD clash. Else returns true.
         */
        HashMap<String, String> courses = student.getCourses();
        if (courses.size() == 0){
            return false;
        }
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

    private boolean checkSwopClash(Student student, Index newIndex) throws KeyNotFoundException,
            MissingSelectionException {
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
