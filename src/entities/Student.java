package entities;

import java.time.LocalDateTime;
import java.util.HashMap;
import entities.course_info.*;
import exceptions.KeyNotFoundException;

/**
 * Student entities meant to encapsulate student user details
 */
public class Student extends User implements Printable{
    private static final long serialVersionUID = 7073083131568074880L;
    private String matricNo;
    private LocalDateTime[] accessPeriod;
    private HashMap<String, String> courses; // <Course Code, Index No>
    private int acadUnits;
    private int acadUnitsAllowed = 0;
    private HashMap<String, String> waitlist; // <Course Code, Index No>

    /**
     * Constructor
     * 
     * @param userId String identifier for student user. Must be unique among all users
     * @param name Name of student user
     * @param gender Gender of student user
     * @param nationality Nationality of student user
     * @param matricNo Matriculation number for student user. Must be unique among all students
     * @param accessPeriod Access period for student user
     * @param courses Courses taken by student. Hashmap mapping course codes to indexes
     * @param waitlist Waitlist of student. Hashmap mapping course codes to indexes
     */
    public Student(
        String userId, String name, String gender, String nationality, String email,
        String matricNo, LocalDateTime[] accessPeriod, HashMap<String, String> courses,
        HashMap<String, String> waitlist) {
        super(userId, "student", name, gender, nationality, email);
    
        this.acadUnits = 0;
        this.acadUnitsAllowed = 21;
        this.matricNo = matricNo;
        this.accessPeriod = accessPeriod;
        this.courses = courses;
        this.waitlist = waitlist;
    }

    /**
     * Getter for student matriculation number
     */
    public String getMatricNo() {
        return this.matricNo;
    }

    /**
     * Getter for student access period
     */
    public LocalDateTime[] getAccessPeriod() {
        return this.accessPeriod;
    }

    /**
     * Getter for student's registered courses
     */
    public HashMap<String, String> getCourses() {
        return this.courses;
    }

    /**
     * Method to check which index number student is registered under for a certain course
     * @param courseCode course code of course to check
     * @throws KeyNotFoundException thrown when student is not registered for course code inputted
     */
    public String getCourseIndex(String courseCode) throws KeyNotFoundException {
        if (this.courses.containsKey(courseCode)) {
            return this.courses.get(courseCode);
        } else if (this.waitlist.containsKey(courseCode)){
            return this.waitlist.get(courseCode);
        } else {
            throw new KeyNotFoundException(this.userId + " is not registered for " + courseCode);
        }
    }

    /**
     * Method to check if student is registered for a certain course
     * Does not include waitlisted courses
     * @param course course to check
     */
    public boolean isRegistered(Course course){
        return this.courses.containsKey(course.getCourseCode());
    }

    /**
     * Method to check if student is registered under a given index for a course
     * 
     * @param course course to be checked
     * @param index index to be checked
     * @throws KeyNotFoundException thrown if student has not registered for the course
     */
    public boolean isRegistered(Course course, Index index) throws KeyNotFoundException {
        if (this.courses.containsKey(course.getCourseCode())) {
            return (index.getIndexNo() == this.courses.get(course.getCourseCode()));
        } else {
            throw new KeyNotFoundException("Student is not registered for course " + course.getCourseCode());
        }
    }

    /**
     * Method to check if student is waitlisted for a course
     * @param course course to be checked
     */
    public boolean isWaitlisted(Course course) {
        return this.waitlist.containsKey(course.getCourseCode());
    }

    /**
     * Method to check if student is waitlisted under a given index for a course
     * 
     * @param course course to be checked
     * @param index  index to be checked
     * @throws KeyNotFoundException thrown if student is not waitlisted for the course
     */
    public boolean isWaitlisted(Course course, Index index) throws KeyNotFoundException {
        if (this.waitlist.containsKey(course.getCourseCode())) {
            return (index.getIndexNo() == this.waitlist.get(course.getCourseCode()));
        } else {
            throw new KeyNotFoundException("Student is not waitlisted for course " + course.getCourseCode());
        }
    }

    /**
     * Getter for academic units student is registered for
     */
    public int getAcadUnits() {
        return this.acadUnits;
    }

    /**
     * Getter for maximum number of academic units student is allowed to have
     */
    public int getAcadUnitsAllowed() {
        return this.acadUnitsAllowed;
    }

    /**
     * Getter for student's waitlist
     * @return HashMap<Course Code, Index Number>
     */
    public HashMap<String, String> getWaitlist() {
        return this.waitlist;
    }

    /**
     * Method to change the student's access period
     * @param newAccessPeriod new access period for student
     */
    public void changeAccessPeriod(LocalDateTime[] newAccessPeriod) {
        this.accessPeriod = newAccessPeriod;
    }

    /**
     * Method to add a new course to student's list of registered courses
     * 
     * @param courseCode course code of new course
     * @param indexNo Index number for new course
     * @param acadUnits Academic units carried by new course
     */
    public void addCourse(String courseCode, String indexNo, int acadUnits) {
        this.courses.put(courseCode, indexNo);
        this.acadUnits += acadUnits;
    }

    /**
     * Method to remove a course from student's list of registered courses
     * @param course The course to be removed
     */
    public void removeCourse(Course course) {
        this.courses.remove(course.getCourseCode());
        this.acadUnits -= course.getAcadU();
    }

    /**
     * Method to change the index of a course student has registered for
     * @param courseCode Course code of course
     * @param newIndex new index number
     */
    public void changeIndex(String courseCode, String newIndex){
        this.courses.put(courseCode, newIndex);
    }

    /**
     * Add a course to the student's waitlist
     * @param course course to be added
     * @param indexNo index number of course to be added
     */
    public void addWaitlist(Course course, String indexNo) {
        this.waitlist.put(course.getCourseCode(), indexNo);
    }

    /**
     * Remove a course from the student's waitlist
     * @param course course to be removed
     */
    public void removeWaitlist(Course course) {
        this.waitlist.remove(course.getCourseCode());
    }

    /**
     * Same as getInfo()
     */
    @Override
    public String getLessInfo(){
        return getInfo();
    }

    /**
     * Method to get basic information about the student
     * Returns string with information on student as specified in functional
     * requirements (Admin 6.) Eg. John Doe - Male - Singaporean
     */
    @Override
    public String getInfo() {
        return String.format("%s - %s - %s", this.name, this.gender, this.nationality); 
    }

    /**
     * Method to get detailed information about the student
     * Returns string with more information on student Eg. U1900000A - R19000 - John
     * Doe Singaporean, Male AUs: 16 (max 21);
     */
    @Override
    public String getMoreInfo() {
        return String.format("%s - %s - %s\n%s, %s\nAUs: %i (max %i)\n",
                            this.matricNo, this.userId, this.name,
                            this.nationality, this.gender,
                            this.acadUnits, this.acadUnitsAllowed); 
    }

    @Override
    public boolean equals(Object obj){
        try{
            Student toCompare = (Student) obj;            
            if (toCompare.getUserId().equals(userId)){
                return true;
            }
        }
        catch (ClassCastException e){
            return false;
        }
        return false;
    }
}
