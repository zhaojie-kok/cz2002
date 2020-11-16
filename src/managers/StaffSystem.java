package managers;

import readers.*;
import entities.course_info.*;
import entities.*;

import java.util.Calendar;
import java.util.HashMap;

public class StaffSystem {
    FileReader fileReader;
    String studentDetailsFilePath;
    String courseDetailsFilePath;
    CourseMgr courseMgr;
    StudentManager studentManager;
    CalendarMgr calendarMgr;
    LoginReader loginReader;

    public StaffSystem(){
        loginReader = new LoginReader("");
        calendarMgr = new CalendarMgr();
        studentManager = new StudentManager();
        courseMgr = new CourseMgr();
    }

    public boolean updateAccessPeriod(String userId, Calendar[] newAccessPeriod){
        return studentManager.updateAccessPeriod(userId, newAccessPeriod);
    }

    public void addStudent(String userId, String name, String gender, String nationality,
                            String matricNo, Calendar[] accessPeriod, String password){
        // Call student manager
        studentManager.createStudent(userId, name, gender, nationality, matricNo, accessPeriod);
        Object[] data = new Object[]{userId, password, "student"};
        loginReader.writeData(data);
    }




    public void updateCourse(String courseCode, Object[] details){
        
    }
    public void addCourse(String courseCode,
                            School school,
                            int acadU, 
                            Calendar examDate){
        courseMgr.createCourse(courseCode, school, acadU, examDate);
    }

    

    public int checkAvailableVacancies(Course course){
        return 0;
    }

    public void printStudentsbyIndex(){
        // TODO
    }

    public void printStudentsbyCourse(String courseCode){
        // in the above hashmap string is matricNo
    }
    
}
