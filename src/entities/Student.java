package entities;

import java.time.LocalDateTime;
import java.util.HashMap;
import entities.course_info.*;
import exceptions.KeyNotFoundException;

public class Student extends User implements Printable{
    /**
     *
     */
    private static final long serialVersionUID = 7073083131568074880L;
    private String matricNo;
    private LocalDateTime[] accessPeriod;
    private HashMap<String, String> courses; // <Course Code, Index No>
    private int acadUnits;
    private int acadUnitsAllowed = 0;
    private HashMap<String, String> waitlist; // <Course Code, Index No>

    public Student(
        String userId, String name, String gender, String nationality,
        String matricNo, LocalDateTime[] accessPeriod, HashMap<String, String> courses,
        HashMap<String, String> waitlist) {
        super(userId, "student", name, gender, nationality);
    
        this.acadUnits = 0;
        this.acadUnitsAllowed = 21;
        this.matricNo = matricNo;
        this.accessPeriod = accessPeriod;
        this.courses = courses;
        this.waitlist = waitlist;
    }

    // accessor methods
    public String getMatricNo() {
        return this.matricNo;
    }

    public LocalDateTime[] getAccessPeriod() {
        return this.accessPeriod;
    }

    public HashMap<String, String> getCourses() {
        return this.courses;
    }

    public String getCourseIndex(String courseCode) throws KeyNotFoundException {
        if (this.courses.containsKey(courseCode)) {
            return this.courses.get(courseCode);
        } else {
            throw new KeyNotFoundException(this.userId + " is not registered for " + courseCode);
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
    public void changeAccessPeriod(LocalDateTime[] newAccessPeriod) {
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

    public void changeIndex(String courseCode, String newIndex){
        this.courses.put(courseCode, newIndex);
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
         *  as specified in functional requirements (Admin 6.)
         *  Eg. John Doe - Male - Singaporean
         */
        return String.format("%s - %s - %s", this.name, this.gender, this.nationality); 
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
