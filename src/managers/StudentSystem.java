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
import exceptions.InvalidInputException;
import exceptions.KeyNotFoundException;
import exceptions.MissingParametersException;
import exceptions.MissingSelectionException;
import exceptions.OutOfRangeException;

/**
 * Controller for the student system.
 * Used to pass user inputs from UI classes to entity manager classes
 * Also used to invoke entity manager methods based on user actions from UI classes
 */
public class StudentSystem implements CourseSystemInterface {
    private CourseMgr courseMgr;
    private StudentManager studentManager;
    private CalendarMgr calendarMgr;

    private Student user;
    private Course selectedCourse = null;
    private Index selectedIndex = null;

    /**
     * Constructor
     * 
     * @param userId UserID of student user logging into system
     * @throws FileReadingException thrown if student file of user cannot be accessed
     * @throws KeyNotFoundException thrown if student details cannot be found
     */
    public StudentSystem(String userId) throws FileReadingException, KeyNotFoundException {
        calendarMgr = new CalendarMgr();
        studentManager = new StudentManager();
        courseMgr = new CourseMgr();
        user = studentManager.getStudent(userId);
    }

    /**
     * Method to view all courses within the system 
     * @return All courses formatted as string
     */
    public String printAllCourses() {
        String toReturn = "";
        Course c;

        Iterator<Course> courses = courseMgr.getAllCourses().values().iterator();
        while (courses.hasNext()) {
            c = courses.next();
            toReturn += c.getInfo();
        }
        return toReturn;
    }

    /**
     * Method to view all courses within the system for a chosen school
     * 
     * @param school School selected
     * @param format Desired format of course information
     * @return Course information formatted based on format provided
     */
    public String printCoursesBySchool(School school, String format) {
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

    /**
     * Method to view all courses within the system matching a String filter
     * Searches course name and code for the filter
     * 
     * @param filter Keyword filter
     * @param format Desired format of course information
     * @return Course information formatted based on format provided
     */
    public String printCoursesByStringFilter(String filter, String format) {
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

    /**
     * Method to get all the indexes of a course selected
     * 
     * @param format String format for information
     */
    public String getIndexesOfCourse(String format) {
        String toReturn = "";
        Iterator<Index> indexes = selectedCourse.getIndexes().values().iterator();
        while (indexes.hasNext()) {
            toReturn += String.format(format, indexes.next().getInfo());
        }
        return toReturn;
    }

    /**
     * Method to check courses a student has registered for
     */
    public String checkRegisteredCourses() {
        // FUNCTIONAL REQUIREMENT - Student: 3. Check registered courses
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

    /**
     * Method to select a course used for other methods See
     * {@link CourseSystemInterface#selectCourse(String)} method;
     */
    @Override
    public void selectCourse(String courseCode) throws KeyNotFoundException {
        selectedCourse = courseMgr.getCourse(courseCode);
        this.selectedIndex = null;
    }

    /**
     * Method to select an index used for other methods See
     * {@link CourseSystemInterface#selectIndex(String)} method
     */
    @Override
    public void selectIndex(String indexNo) throws KeyNotFoundException, MissingSelectionException {
        selectedIndex = courseMgr.getCourseIndex(selectedCourse, indexNo);
    }

    /**
     * Method to check current objects selected by system 1. Course 2. Index 3.
     * Student
     * 
     * Blank fields indicate no selected made See {@link Systems#getSystemStatus()}
     * method
     */
    @Override
    public String getSystemStatus() {
        String sc = selectedCourse == null ? "" : "Selected course: " + selectedCourse.getCourseName();
        String si = selectedIndex == null ? "" : "Selected index: " + selectedIndex.getIndexNo();
        return sc + "\n" + si;
    }

    /**
     * Method to clear all previously made selections See
     * {@link Systems#clearSelections()} method
     */
    @Override
    public void clearSelections() {
        selectedCourse = null;
        selectedIndex = null;
    }

    /**
     * Method to view the timetable for the user's registered courses
     * @return Timetable formatted as a String
     * @throws FileReadingException thrown if problems are found with student's data
     */
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

    /**
     * Method to register a student for a course
     * 
     * @return int denoting status of registration (1. Registration successful, 0. Waitlisted)
     * @throws KeyNotFoundException thrown if index selected is not under course selected
     * @throws MissingSelectionException thrown if course or index have not been selected
     * @throws MissingParametersException thrown if course or index have not been selected or user is invalid
     * @throws InvalidInputException thrown if student cannot register for the course
     */
    public int addCourse() throws KeyNotFoundException, MissingSelectionException,
    MissingParametersException, InvalidInputException {
        // FUNCTIONAL REQUIREMENT - Student: 1. Add course

        if (selectedCourse == null) {
            throw new MissingSelectionException("Please select a course");
        }
        if (selectedIndex == null) {
            throw new MissingSelectionException("Please select an index");
        }

        // Check timing clash
        if (checkAddClash(user)) {
            throw new InvalidInputException("Timetable class detected. Course registration not allowed");
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

    /**
     * Method to allow student user to drop course
     * 
     * @throws InvalidInputException thrown if student has not registered for selected course
     * @throws MissingParametersException thrown if course or index have not been selected
     */
    public void dropCourse() throws InvalidInputException, MissingParametersException {
        // FUNCTIONAL REQUIREMENT - Student: 2. Drop course
        studentManager.dropCourse(selectedCourse, user);
        courseMgr.removeStudent(user, selectedIndex, selectedCourse);
        clearSelections();
    }

    /**
     * Method to check vacancies available for selected course
     * 
     * @throws MissingSelectionException thrown if course has yet to be selected
     */
    public HashMap<String, Integer> checkVacanciesAvailable() throws MissingSelectionException {
        // FUNCTIONAL REQUIREMENT - Student: 4. Check vacancies available
        if (selectedCourse != null) {
            // if a particular slot has been selected then return the vacancy
            HashMap<String, Integer> compiled = new HashMap<>();
            HashMap<String, Index> indexes = selectedCourse.getIndexes();
            for (Index index: indexes.values()) {
                compiled.put(index.getIndexNo(), index.getSlotsAvailable());
            }
            return compiled;
        } else {
            throw new MissingSelectionException("Course must first be selected");
        }
    }

    /**
     * Method to swop index with another student
     * 
     * @param identifier String identifier of student other than user to swop with
     * @throws KeyNotFoundException      If either student has not registered for
     *                                   course
     * @throws MissingSelectionException If course to swop has not been selected
     * @throws InvalidInputException     thrown if student tries to swop with self
     * @throws OutOfRangeException       thrown if timetable clash is detected, preventing swop
     */
    public void swopIndexWithStudent(String identifier)
            throws KeyNotFoundException, MissingSelectionException, InvalidInputException, OutOfRangeException {
        // FUNCTIONAL REQUIREMENT - Student: 6. Swop index number with student
        if (selectedCourse == null) {
            throw new MissingSelectionException("Please select a course first");
        }
        
        if (identifier == user.getMatricNo() || identifier == user.getUserId()) {
            throw new InvalidInputException("Cannot swop with yourself");
        }
        Student toSwapWith = studentManager.getStudent(identifier);

        // first ensure that the swopping students both are enrolled in the course
        if (!user.isRegistered(selectedCourse)) {
            throw new KeyNotFoundException("you are not registered in this course");
        } else if (!toSwapWith.isRegistered(selectedCourse)) {
            throw new KeyNotFoundException(identifier + " is not registered in this course");
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

    /**
     * Method to change to another index for the selected course
     * 
     * @throws KeyNotFoundException       thrown if chosen index does not exist for the selected course
     * @throws MissingSelectionException  thrown if either course or index has not been selected
     * @throws MissingParametersException thrown if either course or index has not been selected
     * @throws InvalidInputException      thrown if user is not registered for course, already in the new index, new index is full, or timetable clashes
     */
    public void swopToIndex() throws KeyNotFoundException, MissingSelectionException,
    MissingParametersException, InvalidInputException {
        // FUNCTIONAL REQUIREMENT - Student: 5. Change index number of course
        if (selectedCourse == null) {
            throw new MissingSelectionException("Please select a Course for swopping index");
        } else if (user.isRegistered(selectedCourse)) {
            throw new InvalidInputException("You are not registered for this course");
        }
        
        if (selectedIndex == null) {
            throw new MissingSelectionException("Please select an Index to swop to");
        } else if (user.isRegistered(selectedCourse, selectedIndex)){
            throw new InvalidInputException("You are already in this index");
        }
        // no more slots
        if (selectedIndex.getSlotsAvailable() <= 0){
            throw new InvalidInputException("The chosen index is full");
        }

        String current = user.getCourseIndex(selectedCourse.getCourseCode());
        if (checkSwopClash(user, selectedIndex)){
            throw new InvalidInputException("Timetable clash detected. Index Swop not allowed");
        }
        courseMgr.removeStudent(user, courseMgr.getCourseIndex(selectedCourse, current), selectedCourse);
        courseMgr.addStudent(user, selectedIndex, selectedCourse);
        studentManager.swopIndex(user, selectedCourse, selectedIndex);
    }

    /**
     * Method to check whether or not for a particular student, adding the selected
     * index will result in a clash
     * 
     * @param student student to be checked
     * @return true if a clash is detected, false otherwise
     * @throws KeyNotFoundException      thrown if an index under student is found
     *                                   to not be under the course stated
     * @throws MissingSelectionException thrown if an index is yet to be selected
     */
    private boolean checkAddClash(Student student) throws KeyNotFoundException, MissingSelectionException {
        if (selectedIndex == null) {
            throw new MissingSelectionException("Please first select an index");
        }
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


    /**
     * Method to check if swopping to a new index
     * 
     * @param student  Student to check
     * @param newIndex Index to be swopped to
     * @return         true if a clash is detected. false otherwise
     * @throws KeyNotFoundException      thrown if an index under student is found
     *                                   to not be under the course stated
     * @throws MissingSelectionException thrown if course or index is yet to be selected
     */
    private boolean checkSwopClash(Student student, Index newIndex) throws KeyNotFoundException,
            MissingSelectionException {
        if (selectedCourse == null) {
            throw new MissingSelectionException("Please select a course first");
        }
        if (selectedIndex == null) {
            throw new MissingSelectionException("Please select an index first");
        }
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
}
