import java.util.Date;
import java.util.HashMap;

public class Student extends User{
    private String matricNo;
    private Date[] accessPeriod;
    private HashMap<String, String> courses; // <Course Code, Index No>
    private int acadUnits;
    private int acadUnitsAllowed = 0;
    private HashMap<String, String> waitlist; // <Course Code, Index No>

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

    public String getCourseIndex(String courseCode) {
        if (this.courses.containsKey(courseCode)) {
            return this.courses.get(courseCode);
        } else {
            return null;
        }
    }

    public boolean isRegistered(Course course){
        return this.courses.containsKey(course.getCourseCode());
    }

    public boolean isRegistered(Index index){
        return this.courses.containsValue(index.getIndexNo());
    }
    
    public boolean isWaitlisted(Course course){
        return this.waitlist.containsKey(course.getCourseCode());
    }

    public boolean isWaitlisted(Index index){
        return this.waitlist.containsValue(index.getIndexNo());
    }

    public int getAcadUnits() {
        return this.acadUnits;
    }

    public int getAcadUnitsAllowed() {
        return this.acadUnitsAllowed;
    }

    public HashMap<String, String> getWaitlist() {
        return this.waitlist;
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

    public void changeIndex(String courseCode, String oldIndex, String newIndex){
        this.courses.replace(courseCode, oldIndex, newIndex);
    }

    public void addWaitlist(Course course, String indexNo) {
        this.waitlist.put(course.getCourseCode(), indexNo);
    }

    public void removeWaitlist(Course course) {
        this.waitlist.remove(course.getCourseCode());
    }
}
