package managers;

import entities.course_info.*;
import exceptions.FileReadingException;
import exceptions.InvalidInputException;
import exceptions.KeyClashException;
import exceptions.KeyNotFoundException;
import exceptions.MissingSelectionException;
import exceptions.MissingParametersException;
import exceptions.OutOfRangeException;
import entities.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Controller for the staff system. Used to pass user inputs from UI classes to
 * entity manager classes Also used to invoke entity manager methods based on
 * user actions from UI classes
 */
public class StaffSystem extends AbstractSystem implements StudentSystemInterface, CourseSystemInterface {
    private LoginMgr loginMgr;
    private LessonDetailMaker lessonDetailMaker;

    private List<LessonDetails>[] timetable = new ArrayList[14];

    /**
     * Constructor Used to instantiated only if a staff user has successfully logged
     * in to system
     * 
     * @param userId User ID of the staff user logged in to the system
     * @throws FileReadingException thrown if files cannot be read or found
     */
    public StaffSystem(String userId) throws FileReadingException {
        super();
        for (int i = 0; i < 14; i++) {
            timetable[i] = new ArrayList<LessonDetails>();
        }
        loginMgr = new LoginMgr("data/loginDetails");
        lessonDetailMaker = new LessonDetailMaker();
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
     * Method to select a student used for other methods See
     * {@link StudentSystemInterface#selectStudent(String)} method;
     */
    @Override
    public void selectStudent(String identifier) throws KeyNotFoundException {
        selectedStudent = studentManager.getStudent(identifier);
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
        String info = "";
        info += String.format("\n%20s", "Selected course:")
                + (selectedCourse != null ? selectedCourse.getCourseCode() : "");
        info += String.format("\n%20s", "Selected index:") + (selectedIndex != null ? selectedIndex.getIndexNo() : "");
        info += String.format("\n%20s", "Selected student:")
                + (selectedStudent != null ? selectedStudent.getUserId() : "");
        return info;
    }

    /**
     * Method to clear all previously made selections See
     * {@link Systems#clearSelections()} method
     */
    @Override
    public void clearSelections() {
        selectedCourse = null;
        selectedIndex = null;
        selectedStudent = null;
        for (int i = 0; i < 14; i++) {
            timetable[i] = new ArrayList<LessonDetails>();
        }
    }

    /**
     * Used to create lesson details used for other methods User needs to input
     * arguments for {@link LessonDetailMaker#makeLessonDetails()} method
     * 
     * @param lessonVenue Venue of lesson
     * @param lessonType  Type of lesson
     * @param lessonDay   DayOfWeek of lesson
     * @param evenOdd     int indicating weeks lesson is held (0. Even weeks, 1. Odd
     *                    weeks, 2. All weeks)
     * @param startTime   Start time of lesson
     * @param endTime     End time of lesson
     * @throws MissingParametersException thrown if arguments required are missing
     * @throws OutOfRangeException        thrown if inputs are outside acceptable
     *                                    constraints (See
     *                                    {@link LessonDetailMaker#makeLessonDetails()})
     */
    public void selectLessonDetails(String lessonVenue, String lessonType, int lessonDay, int evenOdd,
            LocalTime startTime, LocalTime endTime) throws MissingParametersException, OutOfRangeException {
        // for each argument, only update those that are not null
        // this way user doesnt need to update everything in one go
        if (lessonVenue != null) {
            lessonDetailMaker.setLessonVenue(lessonVenue);
        }
        if (lessonType != null) {
            lessonDetailMaker.setLessonType(lessonType);
        }
        if (lessonDay > 0) {
            lessonDetailMaker.setLessonDay(lessonDay);
        }
        if (evenOdd >= 0) {
            lessonDetailMaker.setEvenOdd(evenOdd);
        }
        if (startTime != null) {
            lessonDetailMaker.setStartTime(startTime);
        }
        if (endTime != null) {
            lessonDetailMaker.setEndTime(endTime);
        }

        // add the new lesson detail to the time table
        LessonDetails newLesson = lessonDetailMaker.makeLessonDetails();
        for (LessonDetails lesson : timetable[lessonDay - 1]) {
            if (calendarMgr.lessonClash(lesson, newLesson)) {
                throw new OutOfRangeException("Clashes with existing lesson at " + lesson.getInfo());
            }
        }

        // add to timetable, based on even or odd week
        if (evenOdd == 1) {
            lessonDay = lessonDay + 7;
        } else if (evenOdd == 2) { // if both even and odd then add to even weeks first then to odd weeks
            timetable[lessonDay - 1].add(newLesson);
            Collections.sort(timetable[lessonDay - 1]);
            lessonDay = lessonDay + 7;
        }
        timetable[lessonDay - 1].add(newLesson);
        Collections.sort(timetable[lessonDay - 1]);
        lessonDetailMaker.clearSelections();
    }

    /**
     * Method to change the access period for the course selected
     * 
     * @param newAccessPeriod LocalDateTime array indicating new access period
     * @throws FileReadingException   thrown if student file cannot be accessed
     * @throws IOException            thrown if file cannot be read
     * @throws ClassNotFoundException thrown if file is read but contains the wrong
     *                                class
     * @throws KeyNotFoundException   thrown if selectedStudent is not within system
     */
    public void updateAccessPeriod(LocalDateTime[] newAccessPeriod)
            throws FileReadingException, ClassNotFoundException, IOException, KeyNotFoundException {
        studentManager.updateAccessPeriod(selectedStudent, newAccessPeriod);
        loginMgr.updateAccessPeriod(selectedStudent, newAccessPeriod);
    }

    /**
     * Method to create new student and add to system
     * 
     * @param userId       UserID of new student. Must be unique
     * @param name         Name of new student
     * @param gender       Gender of new student
     * @param nationality  Nationality of new student
     * @param email        Email address of new student
     * @param matricNo     Matriculation number of new student
     * @param accessPeriod Access period of new student
     * @param password     Password for new userID
     * @return String with list of students
     * @throws KeyClashException          thrown if details required to be unique
     *                                    are not
     * @throws FileReadingException       thrown if new file cannot be created
     * @throws MissingParametersException thrown if any above fields are null
     */
    public String addStudent(String userId, String name, String gender, String nationality, String email,
            String matricNo, LocalDateTime[] accessPeriod, String password)
            throws KeyClashException, FileReadingException, MissingParametersException {
        // Call student manager to create the student
        boolean isUnique = false;
        try {
            loginMgr.verifyLoginDetails(userId, null);
        } catch (KeyNotFoundException e) {
            isUnique = true;
        } catch (Exception e) {}

        if (!isUnique) {
            throw new KeyClashException("UserID " + userId);
        }

        try {
            studentManager.createStudent(userId, name, gender, nationality, email, matricNo, accessPeriod);
            // If student is created, then create login details
            Object[] data = { userId, password, "student", accessPeriod };
            loginMgr.createNewLoginDetails(data);
            return studentManager.printAllStudents();
        } catch (KeyClashException e) {
            throw e;
        } catch (FileReadingException f) {
            throw f;
        }
    }

    /**
     * Method to update the courseCode, courseName and school of a course To change
     * other details about the course, use the updateIndex, addIndex, or removeIndex
     * method
     * 
     * @param courseCode New course code
     * @param courseName New course name
     * @param school new school hosting course
     * 
     * @throws MissingSelectionException thrown if course has yet to be selected
     * @throws KeyClashException         thrown if new course code is not unique
     */
    public void updateCourse(String courseCode, String courseName, School school)
            throws KeyClashException, MissingSelectionException {
        if (selectedCourse == null) {
            throw new MissingSelectionException("Course must first be selected");
        }
        // if any of the arguments are null then set them to the existing values
        if (courseCode == null) {
            courseCode = selectedCourse.getCourseCode();
        }
        if (courseName == null) {
            courseName = selectedCourse.getCourseName();
        }
        if (school == null) {
            school = selectedCourse.getSchool();
        }
        courseMgr.updateCourse(selectedCourse, courseCode, courseName, school);
    }

    /**
     * Method to change index number and total capacity of an index
     * 
     * @param indexNo    index number to be changed to
     * @param slotsTotal New total capacity of index
     * @throws OutOfRangeException       thrown if slotsTotal is less than number of
     *                                   students currently registered for index
     * @throws KeyClashException         thrown if indexNo is not unique within the
     *                                   course
     * @throws MissingSelectionException thrown if course or index have yet to be
     *                                   selected
     * @throws InvalidInputException 	 thrown if any input is found to be invalid (e.g: if any are null)
     * @throws KeyNotFoundException		 thrown if selected index cannot be found under selected course
     */
    public void updateIndex(String indexNo, int slotsTotal) throws OutOfRangeException, KeyClashException,
            MissingSelectionException, InvalidInputException, KeyNotFoundException {
        if (selectedCourse == null) {
            throw new MissingSelectionException("Course must first be selected");
        }
        if (selectedIndex == null) {
            throw new MissingSelectionException("Index must first be selected");
        }
        courseMgr.updateIndex(selectedCourse, selectedIndex, indexNo, slotsTotal);
        Index newIndex = courseMgr.getCourseIndex(selectedCourse, indexNo);
        for (Student s : selectedIndex.getRegisteredStudents()){
            studentManager.dropCourse(selectedCourse, s);
            studentManager.addCourse(selectedCourse, newIndex, s);
        }
        for (Student s: selectedIndex.getWaitlistedStudents()){
            studentManager.dropCourse(selectedCourse, s);
            studentManager.addCourse(selectedCourse, newIndex, s);
        }
    }

    /**
     * Method to create a new index for an existing course in the system
     * 
     * @param indexNo    Index number of new index
     * @param slotsTotal Total number of slots in index
     * @throws KeyClashException thrown if indexNo is not unique within the course
     * @throws MissingParametersException thrown if course or timetable is yet to be selected. See {@link #selectCourse(String)} and {@link #selectLessonDetails(String, String, int, int, LocalTime, LocalTime)}
     * @throws OutOfRangeException thrown if slotsTotal is insufficient
     */
    public void addIndex(String indexNo, int slotsTotal) throws KeyClashException, MissingParametersException,
            OutOfRangeException {
        courseMgr.createIndex(selectedCourse, indexNo, slotsTotal, this.timetable);
        for (int i = 0; i < 14; i++) {
            timetable[i] = new ArrayList<LessonDetails>();
        }
    }

    /**
     * Method to add course to system using {@link CourseMgr#createCourse(String, String, School, int)} method
     * 
     * @param courseCode Course code of new course
     * @param courseName Name of new course
     * @param school     School hosting new course
     * @param acadU      Academic units carried by new course
     * @throws KeyClashException Thrown if courseCode is not unique
     * @throws OutOfRangeException Thrown if acadU is insufficient
     */
    public void addCourse(String courseCode, String courseName, School school, int acadU) throws KeyClashException,
            OutOfRangeException {
        courseMgr.createCourse(courseCode, courseName, school, acadU);
    }

    /**
     * Method to retrieve vacancies available to the selected index
     * @return number of slots empty for selected index
     * @throws MissingSelectionException thrown if course or index have yet to be selected
     */
    public int checkAvailableVacancies() throws MissingSelectionException {
        if (selectedCourse == null) {
            throw new MissingSelectionException("Course not yet selected");
        }

        if (selectedIndex == null) {
            throw new MissingSelectionException("Index not yet selected");
        }
        
        return selectedIndex.getSlotsAvailable();
    }

    /**
     * Method to view all students registered for an index
     * @param b whether or not to see detailed info about students in index
     * @return String of student info registered/slot capacity for index
     * @throws MissingSelectionException thrown if course or index are yet to be selected
     */
    public String printStudentsbyIndex(boolean b) throws MissingSelectionException {
        if (selectedCourse == null) {
            throw new MissingSelectionException("Course not yet selected");
        }

        if (selectedIndex == null) {
            throw new MissingSelectionException("Index not yet selected");
        }
        if (b){
            return selectedIndex.getMoreInfo();
        }
        return selectedIndex.getInfo();
    }

    /**
     * Method to view all students registered in a course
     * @return String of all students in course
     * @throws MissingSelectionException thrown if course has yet to be selected
     */
    public String printStudentsbyCourse() throws MissingSelectionException {
        if (selectedCourse == null) {
            throw new MissingSelectionException("Course not yet selected");
        }
        return selectedCourse.getMoreInfo();
    }

    /**
     * Method to retrieve information about a course, including students
     * registered
     * @return String of information about a course
     * @throws MissingSelectionException thrown if course has yet to be selected
     */
    public String getCourseInfo() throws MissingSelectionException {
        if (selectedCourse == null) {
            throw new MissingSelectionException("Course not yet selected");
        }
        return selectedCourse.getMoreInfo();
    }
    
    /**
     * Method to retrieve information about all courses
     * @return String of information about all courses
     */
    public String printAllCourses(){
        String toReturn = "";
        Course c;

        Iterator<Course> courses = courseMgr.getAllCourses().values().iterator();
        while (courses.hasNext()) {
            c = courses.next();
            toReturn += c.getLessInfo();
        }
        return toReturn;
    }
}
