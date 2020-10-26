import java.util.Date;
import java.util.HashMap;

public class Student extends User{
    private String matricNo;
    private Date[] accessPeriod;
    private HashMap<String, String> courses; // <Course Code, Index No>

    public Student(
        String userId, String name, String gender, String nationality,
        String matricNo, Date[] accessPeriod, HashMap<String, String> courses) {
        super(userId, "student", name, gender, nationality);
        
        // TODO: throw exception if any of these fields are null or invalid
        this.matricNo = matricNo;
        this.accessPeriod = accessPeriod;
        this.courses = courses;
    }

    // accessor methods
    public String getMatricNo() {
        return this.matricNo;
    }

    public Date[] getAccessPeriod() {
        return this.accessPeriod;
    }

    public HashMap<String, String> getCourses() {
        return this.courses;
    }

    // mutator methods
    public void changeAccessPeriod(Date[] newAccessPeriod) {
        this.accessPeriod = newAccessPeriod;
    }

    public void addCourse(String courseCode, String IndexNo) {
        this.courses.put(courseCode, IndexNo);
    }

    public void removeCourse(String courseCode) {
        this.courses.remove(courseCode);
    }

}
