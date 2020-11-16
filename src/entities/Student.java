package entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import entities.course_info.*;

public class Student extends User implements Printable, Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 7073083131568074880L;
    private String matricNo;
    private Calendar[] accessPeriod;
    private HashMap<String, String> courses; // <Course Code, Index No>
    private int acadUnits;
    private int acadUnitsAllowed = 0;
    private HashMap<String, String> waitlist; // <Course Code, Index No>

    public Student(
        String userId, String name, String gender, String nationality,
        String matricNo, Calendar[] accessPeriod, HashMap<String, String> courses) {
        super(userId, "student", name, gender, nationality);
    
        this.acadUnits = 0;
        this.acadUnitsAllowed = 21;
        this.matricNo = matricNo;
        this.accessPeriod = accessPeriod;
    }

    // accessor methods
    public String getMatricNo() {
        return this.matricNo;
    }

    public Calendar[] getAccessPeriod() {
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
    public void changeAccessPeriod(Calendar[] newAccessPeriod) {
        this.accessPeriod = newAccessPeriod;
    }

    public void addCourse(String courseCode, String indexNo, int acadUnits) {
        this.courses.put(courseCode, indexNo);
        this.acadUnits += acadUnits;
    }

    public void removeCourse(String courseCode, int acadUnits) {
        this.courses.remove(courseCode);
        this.acadUnits -= acadUnits;
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

    @Override
    public String getInfo() {
        /** Returns string with information on student
         *  Eg. U1900000A - R19000 - John Doe
         */
        return String.format("%s - %s - %s", this.matricNo, this.userId, this.name); 
    }

    @Override
    public String getMoreInfo() {
        /**
         * Returns string with more information on student
         * Eg.
         * U1900000A - R19000 - John Doe
         * Singaporean, Male
         * AUs: 16 (max 21);
         */
        return String.format("%s - %s - %s\n%s, %s\nAUs: %i (max %i)\n",
                            this.matricNo, this.userId, this.name,
                            this.nationality, this.gender,
                            this.acadUnits, this.acadUnitsAllowed); 
    }
}
