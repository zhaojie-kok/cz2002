import java.util.HashMap;

public class StaffSystem {
    private static HashMap<String, Staff> staff = FileReader.loadStaff();

    FileReader fileReader;
    String studentDetailsFilePath;
    String courseDetailsFilePath;


    public boolean updateAccessPeriod(String matricNo){
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
    public void addCourse(Course course){
        
    }
    
}
