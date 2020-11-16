package managers;

import readers.*;
import entities.course_info.*;
import entities.*;

import java.util.Calendar;

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

    
    public int selectStudent(String identifier){
        Student tmp = studentManager.getStudent(identifier);
        if (tmp == null){
            return 0;
        }
        selectedIndex = tmp;
        return 1;
    }

    public boolean updateAccessPeriod(String userId, Calendar[] newAccessPeriod){
        return studentManager.updateAccessPeriod(userId, newAccessPeriod);
    }

    public boolean addStudent(String userId, String name, String gender, String nationality,
                            String matricNo, Calendar[] accessPeriod, String password){
        // Call student manager
        if (studentManager.createStudent(userId, name, gender, nationality, matricNo, accessPeriod)){
            Object[] data = new Object[]{userId, password, "student"};
            loginReader.writeData(data);
            return true;
        }
        return false;
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

    public String printStudentsbyIndex(String indexNo){
        // TODO
    }

    public String printStudentsbyCourse(String courseCode){
        return courseMgr.getCourse(courseCode).getInfo();
    }
    
}
