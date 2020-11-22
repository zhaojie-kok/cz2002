package managers;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import entities.*;
import entities.course_info.*;
import exceptions.FileReadingException;
import exceptions.KeyNotFoundException;
import exceptions.MissingParametersException;
import exceptions.MissingSelectionException;
import exceptions.OutOfRangeException;

public class StudentSystem implements CourseSystemInterface {
    private CourseMgr courseMgr;
    private StudentManager studentManager;
    private CalendarMgr calendarMgr;

    private Student user;
    private Course selectedCourse = null;
    private Index selectedIndex = null;

    public StudentSystem(String userId) throws FileReadingException, KeyNotFoundException {
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
    public String checkRegisteredCourses() {
        /**
         * Converts to printable format
         */
        String format = "Course: %s\n Index: %s\n";
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
        this.selectedIndex = null;
    }

    @Override
    public void selectIndex(String indexNo) throws KeyNotFoundException, MissingSelectionException {
        selectedIndex = courseMgr.getCourseIndex(selectedCourse, indexNo);
    }

    @Override
    public String getSystemStatus() {
        String sc = selectedCourse == null ? "" : "Selected course: " + selectedCourse.getCourseName();
        String si = selectedIndex == null ? "" : "Selected index: " + selectedIndex.getIndexNo();
        return sc + "\n" + si;
    }

    @Override
    public void clearSelections() {
        selectedCourse = null;
        selectedIndex = null;
    }

    public String getTimeTable() throws FileReadingException {
        String[][] tableForm = new String[12][14]; // for 2 weeks, 8 am to 8pm in 30 min intervals
        HashMap<String, String> courses = user.getCourses();
        Course course;
        Index ind;
        List<LessonDetails>[] lessons;
        for (Map.Entry<String, String> entry: courses.entrySet()) {
            try {
                course = courseMgr.getCourse(entry.getKey());
                ind = courseMgr.getCourseIndex(course, entry.getValue());
                lessons = ind.getTimeTable();
                for (int i=0; i<lessons.length; i++) {
                    for (LessonDetails ld: lessons[i]) {
                        int startRow = (ld.getStartTime().getHour()-8) * 2 + (ld.getStartTime().getMinute() / 30);
                        int endRow = (ld.getEndTime().getHour() - 8) * 2 + (ld.getEndTime().getMinute() / 30);
                        for (int j=startRow; j<=endRow; j++) {
                            tableForm[j][i] = String.format("%15s", course.getCourseCode() + " " + ind.getIndexNo());
                        }
                    }
                }

                String output = "\nEVEN WEEKS\n|     |";
                for (int i=0; i<DayOfWeek.values().length; i++) {
                    output += String.format("%15s|", DayOfWeek.values()[i].toString());
                }
                for (int i=0; i<tableForm.length; i++) {
                    output += String.format("\n|%2d:%2d|", ((int) (i/2)) + 8, i%2 * 30);
                    for (int j=0; j<7; j++) {
                        output += String.format("%s|", tableForm[i][j]);
                    }
                }
                output += "\nODD WEEKS\n|     |";
                for (int i = 0; i < DayOfWeek.values().length; i++) {
                    output += String.format("%15s|", DayOfWeek.values()[i].toString());
                }
                for (int i = 0; i < tableForm.length; i++) {
                    output += String.format("\n|%2d:%2d|", ((int) (i / 2)) + 8, i % 2 * 30);
                    for (int j = 7; j < tableForm[0].length; j++) {
                        output += String.format("%s|", tableForm[i][j]);
                    }
                }

                return output;
            } catch (KeyNotFoundException e) {
                throw new FileReadingException("inconsistencies found in student data. Please contact system administrator");
            }
        }

        String timetable = "";
        for (int i=0; i<tableForm.length; i++) {
            for (int j=0; j<tableForm[0].length; j++) {
                timetable += tableForm[i][j];
            }
            timetable += "\n";
        }
        return timetable;
    }

    // These are called AFTER selectCourse/selectIndex
    ////// ++++++++ START +++++++++
    // FUNCTIONAL REQUIREMENT - Student: 1. Add course
    public int addCourse() throws KeyNotFoundException, MissingSelectionException, OutOfRangeException,
            MissingParametersException {

        if (selectedCourse == null) {
            throw new MissingSelectionException("Please select a course");
        }
        if (selectedIndex == null) {
            throw new MissingSelectionException("Please select an index");
        }

        // Check timing clash
        if (checkAddClash(user)) {
            throw new OutOfRangeException("Timetable class detected. Course registration not allowed");
        }

        // studentManager tries to register student for the course, or add student to the waitlist if the index is full
        int result = studentManager.addCourse(selectedCourse, selectedIndex, user);
        switch (result) {
            case 1:
                courseMgr.addStudent(user, selectedIndex, selectedCourse);
                break;
            case 0:
                courseMgr.enqueueWaitlist(user, selectedIndex, selectedCourse);
                break;
        }

        return result;
    }

    // FUNCTIONAL REQUIREMENT - Student: 2. Drop course
    public void dropCourse() throws OutOfRangeException, MissingParametersException {
        /**
         * 
         */
        studentManager.dropCourse(selectedCourse, user);
        courseMgr.removeStudent(user, selectedIndex, selectedCourse);
        clearSelections();
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
    public void swopIndexWithStudent(String identifier) throws KeyNotFoundException, MissingSelectionException,
            OutOfRangeException {
        /** 
         * Returns 0 and does nothing if either students will havetimetable clash, 
         * else swaps indexes and returns 1
         * -1: user or swopping student is not registered in the course
         */
        if (selectedCourse == null) {
            throw new MissingSelectionException("Please select a course first");
        }
        
        Student toSwapWith = studentManager.getStudent(identifier);

        // first ensure that the swopping students both are enrolled in the course
        if (!user.isRegistered(selectedCourse)) {
            throw new OutOfRangeException("you are not registered in this course");
        } else if (!toSwapWith.isRegistered(selectedCourse)) {
            throw new OutOfRangeException(identifier + " is not registered in this course");
        }

        String strToSwapTo = toSwapWith.getCourseIndex(selectedCourse.getCourseCode());
        Index toSwapTo = courseMgr.getCourseIndex(selectedCourse, strToSwapTo);
        String strCurr = user.getCourseIndex(selectedCourse.getCourseCode());
        Index currentIndex = courseMgr.getCourseIndex(selectedCourse, strCurr);

        // do nothing if both are registered in same course
        if (strToSwapTo.equals(strCurr)) {
            return;
        }

        if (!checkSwopClash(user, toSwapTo) && !checkSwopClash(toSwapWith, currentIndex)){
            courseMgr.swopStudents(user, toSwapWith, selectedCourse);
            studentManager.swopIndex(user, toSwapWith, selectedCourse);
        } else {
            throw new OutOfRangeException("Timetable clash detected. Index Swop not allowed");
        }
    }

    // FUNCTIONAL REQUIREMENT - Student: 5. Change index number of course
    public void swopToIndex() throws KeyNotFoundException, MissingSelectionException, OutOfRangeException,
            MissingParametersException {
        if (selectedCourse == null) {
            throw new MissingSelectionException("Please select a Course for swopping index");
        } else if (user.isRegistered(selectedCourse)) {
            throw new OutOfRangeException("You are not registered for this course");
        }
        
        if (selectedIndex == null) {
            throw new MissingSelectionException("Please select an Index to swop to");
        } else if (user.isRegistered(selectedIndex)){
            throw new OutOfRangeException("You are already in this index");
        }
        // no more slots
        if (selectedIndex.getSlotsAvailable() <= 0){
            throw new OutOfRangeException("The chosen index is full");
        }

        String current = user.getCourseIndex(selectedCourse.getCourseCode());
        if (checkSwopClash(user, selectedIndex)){
            throw new OutOfRangeException("Timetable clash detected. Index Swop not allowed");
        }
        courseMgr.removeStudent(user, courseMgr.getCourseIndex(selectedCourse, current), selectedCourse);
        courseMgr.addStudent(user, selectedIndex, selectedCourse);
        studentManager.swopIndex(user, selectedCourse, selectedIndex);
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
