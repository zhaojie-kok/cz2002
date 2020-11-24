package entities.course_info;

import java.io.Serializable;
import java.util.HashMap;
import entities.*;
import exceptions.InvalidInputException;
import exceptions.OutOfRangeException;

/**
 * Course entity to store information regarding a course
 * Details specific to each index in the course are stored in the Index class
 */
public class Course implements Serializable, Printable{
    private static final long serialVersionUID = -9117232107080367454L;

    private String courseCode;
    private String courseName;
    private School school;
    private HashMap<String, Index> indexes; // <indexNo, Index>
    private int acadU;

    /**
     * Constructor
     * 
     * @param courseCode String showing the course code
     * @param courseName String showing the course name
     * @param school School that hosts the course
     * @param acadU Number of academic units carried by the course
     * @throws OutOfRangeException thrown if the academic unit is less than 1
     */
    public Course(String courseCode, String courseName, School school, int acadU) throws OutOfRangeException {
        this.indexes = new HashMap<String,Index>();
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.school = school;
        if (acadU <= 0) {
            throw new OutOfRangeException("Courses must carry at least 1 academic unit");
        }
        this.acadU = acadU;
    }

    /**
     * Setter method for course code
     * 
     * @param courseCode String showing the course code
     */
    public void setCourseCode(String courseCode){
        this.courseCode = courseCode;
    }

    /**
     * Setter method for the course name
     * 
     * @param courseName String showing the course name
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     * setter method for the school
     * 
     * @param school School that hosts the course
     */
    public void setCourseSchool(School school) {
        this.school = school;
    }
    
    /**
     * Mutator method for the indexes available to the course
     * 
     * @param index A new index for the course
     */
    public void updateIndex(Index index){
        indexes.put(index.getIndexNo(), index);
    }

    /**
     * Mutator method for existing indices in the 
     * @param index
     * @param oldIndexNo
     */
    public void updateIndex(Index index, String oldIndexNo){
        indexes.remove(oldIndexNo);
        indexes.put(index.getIndexNo(), index);
    }

    /**
     * Method to allow deletion of indexes NOTE: indexes cannot be deleted if
     * students have registered for them
     * 
     * @param indexNo String with the index number of the index
     * @return the Index that had been deleted
     * @throws InvalidInputException thrown if a course with students registered is to be deleted
     */
    public Index deleteIndex(String indexNo) throws InvalidInputException {
        if (indexes.containsKey(indexNo)) {
            int studentsRegistered = indexes.get(indexNo).getRegisteredStudents().size();
            if (studentsRegistered > 0) {
                throw new InvalidInputException("Cannot delete a course with students still registered");
            }
        }
        return indexes.remove(indexNo);
    }

    /**
     * Getter method for the course code
     */
    public String getCourseCode(){
        return courseCode;
    }

    /**
     * Getter metthod for the course name
     */
    public String getCourseName(){
        return courseName;
    }

    /**
     * Getter method for the school hosting the couse
     * @return A School enum
     */
    public School getSchool() {
        return this.school;
    }

    /**
     * Getter method for the number of avaiblable slots for a particular index
     * @param indexNo index number identifying the index
     * @return integer of the number of slots available
     */
    public int getAvailableSlots(String indexNo){
        return indexes.get(indexNo).getSlotsAvailable();
    }

    /**
     * Getter method for the number of academic units carried by the course
     */
    public int getAcadU(){
        return acadU;
    }

    /**
     * Getter method to get a particular Index object that is part of this course
     * @param indexNo index number identifying the index
     * @return the index object identified by the index number
     */
    public Index getIndex(String indexNo) {
        if (indexes.containsKey(indexNo)) {
            return (Index) this.indexes.get(indexNo);
        }
        return null;
    }

    /**
     * Getter method to access all the indexes under the course
     */
    public HashMap<String, Index> getIndexes(){
        return indexes;
    }

    /**
     * Checks if the course is hosted by a school
     * 
     * @param school School to be checked
     * @return true if school is indeed hosting the course, false otherwise
     */
    public boolean isSchool(School school) {
        return this.school == school;
    }

    /**
     * Returns a one liner
     * AC2101 - Accounting Recognition and Measurement
     */
    @Override
    public String getLessInfo(){
        return String.format("%s - %s\n", courseCode, courseName);
    }

    /**
     * Retrieves information about the course
     * Returns formatted string of course information
     * Eg:
     * AC2101 - Accounting Recognition and Measurement
     * .....
     * (see {@link entities.course_info.Index#getInfo()})
     */
    @Override
    public String getInfo() {
        String toReturn = String.format("%s - %s\n", courseCode, courseName);
        for (Index index: indexes.values()){
            toReturn += index.getInfo();
        }
        return toReturn;
    }

    /**
     * Retrieves detailed information about the course
     * Returns formatted string of course information
     * Eg.
     * AC2101 - Accounting Recognition and Measurement
     * .....
     * .....
     * (see {@link entities.course_info.Index#getInfo()})
     */
    @Override
    public String getMoreInfo() {
        String toReturn = String.format("%s - %s - %s\n", courseCode, courseName, school);
        for (Index index: indexes.values()){
            toReturn += index.getMoreInfo();
        }
        return toReturn;
    }
}