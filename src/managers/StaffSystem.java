package managers;

import readers.*;
import entities.course_info.*;
import entities.*;

import java.util.Calendar;
import java.util.List;

public class StaffSystem {
    FileReader fileReader;
    String studentDetailsFilePath;
    String courseDetailsFilePath;
    CourseMgr courseMgr;
    StudentManager studentManager;
    CalendarMgr calendarMgr;
    LoginReader loginReader;

    Course selectedCourse;
    Index selectedIndex;
    Student selectedStudent;

    public StaffSystem(){
        loginReader = new LoginReader("");
        calendarMgr = new CalendarMgr();
        studentManager = new StudentManager();
        courseMgr = new CourseMgr();
    }

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

    public int selectIndex(String indexNo){
        /**
         * Returns 1 if index is selected, else 0
         */
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

    public boolean updateAccessPeriod(String userId, Calendar[] newAccessPeriod){
        return studentManager.updateAccessPeriod(userId, newAccessPeriod);
    }

    public boolean addStudent(String userId, String name, String gender, String nationality,
                            String matricNo, Calendar[] accessPeriod, String password){
        // Call student manager
        if (studentManager.createStudent(userId, name, gender, nationality, matricNo, accessPeriod)){
            // If student is created, then create login details
            Object[] data = new Object[]{userId, password, "student"};
            loginReader.writeData(data);
            return true;
        }
        return false;
    }

    public void updateCourse(String courseCode,
                            School school){
        courseMgr.updateCourse(selectedCourse, courseCode, school);
    }
    
    public void updateIndex(String indexNo, int slotsTotal){
        courseMgr.updateIndex(selectedCourse, selectedIndex, indexNo, slotsTotal);
    }

    public void addCourse(String courseCode,
                            School school,
                            int acadU){
        courseMgr.createCourse(courseCode, school, acadU);
    }

    public void addIndex(String indexNo, int slotsTotal, List<LessonDetails>[] timeTable){
        courseMgr.createIndex(selectedCourse, indexNo, slotsTotal, timeTable);
    }

    public int checkAvailableVacancies(){
        return selectedIndex.getSlotsAvailable();
    }

    public String printStudentsbyIndex(String indexNo){
        return courseMgr.getCourseIndex(selectedCourse, indexNo).getMoreInfo();
    }

    public String printStudentsbyCourse(String courseCode){
        return courseMgr.getCourse(courseCode).getMoreInfo();
    }
    
    public void deleteIndex(Course course)
}
