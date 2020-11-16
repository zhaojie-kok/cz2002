package managers;

import readers.*;
import entities.course_info.*;
import entities.*;

import java.util.Calendar;
import java.util.HashMap;

public class StaffSystem implements System {
    private static HashMap<String, Staff> staff = FileReader.loadStaff();

    FileReader fileReader;
    String studentDetailsFilePath;
    String courseDetailsFilePath;



    public boolean updateAccessPeriod(Student student){
        StudentManager smgr = new StudentManager(); // check constructor
        Calendar[] accessPeriod = student.getAccessPeriod();

        // do we need to check accessPeriod? what do we update it to?
        Calendar[] newAccessPeriod;
		if (accessPeriod[] == newAccessPeriod){
            // same/updated alr
            return false;
        }
        student.changeAccessPeriod(newAccessPeriod);
        return true;
    }
    public void addStudent(Object[] details){
        // iterate through Object[] 
        // take each attribute
        // instantiate Student object by putting in
        // each of the attributes into args of constructor
        
        Student student = new Student()
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
