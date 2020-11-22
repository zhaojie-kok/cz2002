package managers;

import readers.*;
import entities.course_info.*;
import exceptions.FileReadingException;
import exceptions.KeyClashException;
import exceptions.KeyNotFoundException;
import exceptions.MissingSelectionException;
import exceptions.MissingParametersException;
import exceptions.OutOfRangeException;
import entities.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StaffSystem implements StudentSystemInterface, CourseSystemInterface {
    private CourseMgr courseMgr;
    private StudentManager studentManager;
    private CalendarMgr calendarMgr;
    private LoginReader loginReader;
    private LessonDetailMaker lessonDetailMaker;

    private Course selectedCourse;
    private Index selectedIndex;
    private Student selectedStudent;
    private List<LessonDetails>[] timetable = new ArrayList[14];

    public StaffSystem(String userId) throws FileReadingException {
        for (int i = 0; i < 14; i++) {
            timetable[i] = new ArrayList<LessonDetails>();
        }
        loginReader = new LoginReader("data/loginDetails");
        calendarMgr = new CalendarMgr();
        try {
            studentManager = new StudentManager();
            courseMgr = new CourseMgr();
        } catch (FileReadingException e) {
            throw e;
        }
        lessonDetailMaker = new LessonDetailMaker();
    }

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
    public void selectStudent(String identifier) throws KeyNotFoundException {
        selectedStudent = studentManager.getStudent(identifier);
    }

    @Override
    public String getSystemStatus() {
        String info = "";
        info += String.format("\n%20s", "Selected course:") + (selectedCourse != null ? selectedCourse.getCourseCode() : "");
        info += String.format("\n%20s", "Selected index:") + (selectedIndex != null ? selectedIndex.getIndexNo() : "");
        info += String.format("\n%20s", "Selected student:") + (selectedStudent != null ? selectedStudent.getUserId() : "");
        return info;
    }

    @Override
    public void clearSelections() {
        selectedCourse = null;
        selectedIndex = null;
        selectedStudent = null;
        for (int i = 0; i < 14; i++) {
            timetable[i] = new ArrayList<LessonDetails>();
        }
    }

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

    public void updateAccessPeriod(LocalDateTime[] newAccessPeriod) throws FileReadingException {
        studentManager.updateAccessPeriod(selectedStudent, newAccessPeriod);
    }

    public void addStudent(String userId, String name, String gender, String nationality, String email,
                            String matricNo, LocalDateTime[] accessPeriod, String password) throws KeyClashException, FileReadingException {
        // Call student manager to create the student
        try {
            studentManager.createStudent(userId, name, gender, nationality, email, matricNo, accessPeriod);
            // TODO: check validity of email
            // If student is created, then create login details
            String[] data = { userId, password, "student" };
            loginReader.writeData(data);
        } catch (KeyClashException e) {
            throw e;
        } catch (FileReadingException f) {
            throw f;
        }
    }

    public void updateCourse(String courseCode, String courseName, School school) {
        /**
         * updates the courseCode, courseName and school of a course To change other
         * details about the course, use the updateIndex, addIndex, or removeIndex
         * method
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

    public void updateIndex(String indexNo, int slotsTotal) throws OutOfRangeException, KeyClashException {
        courseMgr.updateIndex(selectedCourse, selectedIndex, indexNo, slotsTotal);
    }

    public void addIndex(String indexNo, int slotsTotal) throws KeyClashException, MissingParametersException,
            OutOfRangeException {
        courseMgr.createIndex(selectedCourse, indexNo, slotsTotal, this.timetable);
        for (int i = 0; i < 14; i++) {
            timetable[i] = new ArrayList<LessonDetails>();
        }
    }

    public void addCourse(String courseCode, String courseName, School school, int acadU) throws KeyClashException,
            OutOfRangeException {
        courseMgr.createCourse(courseCode, courseName, school, acadU);
    }

    public int checkAvailableVacancies() throws MissingSelectionException {
        if (selectedCourse == null) {
            throw new MissingSelectionException("Course not yet selected");
        }

        if (selectedIndex == null) {
            throw new MissingSelectionException("Index not yet selected");
        }
        
        return selectedIndex.getSlotsAvailable();
    }

    public String printStudentsbyIndex() throws MissingSelectionException {
        if (selectedCourse == null) {
            throw new MissingSelectionException("Course not yet selected");
        }

        if (selectedIndex == null) {
            throw new MissingSelectionException("Index not yet selected");
        }
        return selectedIndex.getMoreInfo();
    }

    public String printStudentsbyCourse() throws MissingSelectionException {
        if (selectedCourse == null) {
            throw new MissingSelectionException("Course not yet selected");
        }
        return selectedCourse.getMoreInfo();
    }
    
    public String getCourseInfo() throws MissingSelectionException {
        if (selectedCourse == null) {
            throw new MissingSelectionException("Course not yet selected");
        }
        return selectedCourse.getMoreInfo();
    }
}
