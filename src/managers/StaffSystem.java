package managers;

import readers.*;
import entities.course_info.*;
import exceptions.Filereadingexception;
import exceptions.MissingparametersException;
import exceptions.OutofrangeException;
import entities.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StaffSystem implements StudentSystemInterface, CourseSystemInterface {
    private Staff user;

    private CourseMgr courseMgr;
    private StudentManager studentManager;
    private CalendarMgr calendarMgr;
    private LoginReader loginReader;
    private LessonDetailMaker lessonDetailMaker;

    private Course selectedCourse;
    private Index selectedIndex;
    private Student selectedStudent;
    private List<LessonDetails>[] timetable = new ArrayList[7];

    public StaffSystem(String userId) throws Filereadingexception {
        loginReader = new LoginReader("data/loginDetails/"); // TODO:change to default folder path
        calendarMgr = new CalendarMgr();
        try {
            studentManager = new StudentManager();
            courseMgr = new CourseMgr();
        } catch (Filereadingexception e) {
            throw e;
        }
        lessonDetailMaker = new LessonDetailMaker();
    }

    @Override
    public int selectCourse(String courseCode){
        /**
         * Returns 1 if course is selected, else 0
         */
        Course tmp = courseMgr.getCourse(courseCode);
        if (tmp == null){
            return 0;
        }
        selectedCourse = tmp;
        return 1;
    }

    @Override
    public int selectIndex(String indexNo){
        /**
         * Returns 1 if index is selected, else 0
         */
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

    @Override
    public int selectStudent(String identifier){
        /**
         * Returns 1 if student is selected, else 0
         */
        Student tmp = studentManager.getStudent(identifier);
        if (tmp == null){
            return 0;
        }
        selectedStudent = tmp;
        return 1;
    }

    @Override
    public String getSystemStatus() {
        String info = "";
        return info; //TODO: complete method
    }

    @Override
    public void clearSelections() {
        selectedCourse = null;
        selectedIndex = null;
        selectedStudent = null;
        timetable = new ArrayList[7];
    }

    public void selectLessonDetails(String lessonVenue, 
							String lessonType, 
							int lessonDay,
							int evenOdd,
							LocalTime startTime,
							LocalTime endTime) throws MissingparametersException, OutofrangeException {
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
        LessonDetails newLesson =  lessonDetailMaker.makeLessonDetails();
        for (LessonDetails lesson: timetable[lessonDay-1]) {
            if (calendarMgr.lessonClash(lesson, newLesson)) {
                throw new OutofrangeException("Clashes with existing lesson at " + lesson.getInfo());
            }
        }
        timetable[lessonDay-1].add(newLesson);
        Collections.sort(timetable[lessonDay - 1]);
        lessonDetailMaker.clearSelections();
    }

    public boolean updateAccessPeriod(LocalDateTime[] newAccessPeriod){
        // TODO: change to exceptions
        return studentManager.updateAccessPeriod(selectedStudent, newAccessPeriod);
    }

    public boolean addStudent(String userId, String name, String gender, String nationality,
                            String matricNo, LocalDateTime[] accessPeriod, String password){
        // Call student manager TODO: exceptions
        if (studentManager.createStudent(userId, name, gender, nationality, matricNo, accessPeriod)){
            // If student is created, then create login details
            Object[] data = new Object[]{userId, password, "student"};
            loginReader.writeData(data);
            return true;
        }
        return false;
    }

    public void updateCourse(String courseCode, String courseName, School school) {
        /**
         * updates the courseCode, courseName and school of a course
         * To change other details about the course, use the updateIndex, addIndex, or removeIndex method
         */

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

    public void updateIndex(String indexNo, int slotsTotal) throws OutofrangeException {
        courseMgr.updateIndex(selectedCourse, selectedIndex, indexNo, slotsTotal);
    }

    public void addIndex(String indexNo, int slotsTotal){
        courseMgr.createIndex(selectedCourse, indexNo, slotsTotal, this.timetable);
    }

    public void addCourse(String courseCode,
                            School school,
                            int acadU){
        courseMgr.createCourse(courseCode, school, acadU);
    }


    public int checkAvailableVacancies(){
        return selectedIndex.getSlotsAvailable();
    }

    public String printStudentsbyIndex(){
        if (selectedCourse == null) {
            return null;
        }
        return selectedIndex.getMoreInfo();
    }

    public String printStudentsbyCourse(){
        return selectedCourse.getMoreInfo();
    }
    
    public String getCourseInfo() {
        return selectedCourse.getMoreInfo();
    }
    public void deleteIndex(Course course) {}
}
