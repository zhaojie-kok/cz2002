package managers;

import readers.*;
import entities.course_info.*;
import entities.*;

import java.util.Calendar;
import java.util.HashMap;

public class StaffSystem implements Systems {
    private static HashMap<String, Staff> staff = FileReader.loadStaff();

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
    public void addCourse(){
        String courseCode = course.getCourseCode();
        String school = course.getSchool();
        int acadU = course.getAcadU();
        Calendar examDate = course.getExamDate();

        CourseMgr cmgr = new CourseMgr(); // check constructor
        createCourse(courseCode, school, acadU, examDate);
        createIndex(courseCode, indexNo)
    }


    public int checkAvailableVacancies(Course course){
        CourseMgr cmgr = new CourseMgr(); // check constructor
        cmgr.checkAvailableVacancies(course);
        // anything else needs to be done?
    }

    public void printStudentsbyIndex(){
        HashMap<String, Student> students = FileReader.loadStudents();
        // TODO
    }

    public void printStudentsbyCourse(String courseCode){
        HashMap<String, Student> students = FileReader.loadStudents();
        // in the above hashmap string is matricNo
        for (Student s : students.values()){
            HashMap<String, String> courses = s.getCourses();
            for (String cc : courses.keySet())
                if (courseCode == cc){
                    System.out.printf(s.name + s.gender + s.nationality + "\n");
                }
        }
    }
    
}
