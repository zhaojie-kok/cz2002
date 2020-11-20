package entities.course_info;

import java.io.Serializable;
import java.util.HashMap;
import entities.*;

public class Course implements Serializable, Printable{
    private static final long serialVersionUID = -9117232107080367454L;

    private String courseCode;
    private String courseName;
    private School school;
    private HashMap<String, Index> indexes; // <indexNo, Index>
    private int acadU;

    public Course(String courseCode, 
                String courseName,
                School school,
                int acadU){
        this.indexes = new HashMap<String,Index>();
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.school = school;
        this.acadU = acadU;
    }

    public void setCourseCode(String courseCode){
        this.courseCode = courseCode;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseSchool(School school) {
        this.school = school;
    }
    
    public void updateIndex(Index index){
        indexes.put(index.getIndexNo(), index);
    }

    public void updateIndex(Index index, String oldIndexNo){
        indexes.remove(oldIndexNo);
        indexes.put(index.getIndexNo(), index);
    }

    public Index deleteIndex(String indexNo){
        return indexes.remove(indexNo);
    }

    public String getCourseCode(){
        return courseCode;
    }
    public String getCourseName(){
        return courseName;
    }

    public School getSchool() {
        return this.school;
    }

    public boolean isSchool(School school){
        return this.school == school;
    }

    public int getAvailableSlots(String indexNo){
        return indexes.get(indexNo).getSlotsAvailable();
    }

    public int getAcadU(){
        return acadU;
    }

    public Index getIndex(String indexNo) {
        if (indexes.containsKey(indexNo)) {
            return (Index) this.indexes.get(indexNo);
        }
        return null;
    }


    public HashMap<String, Index> getIndexes(){
        return indexes;
    }

    @Override
    public String getInfo() {
        /**
         * Returns formatted string of course information
         * Eg.
         * AC2101 - Accounting Recognition and Measurement
         * .....
         * (see index.getInfo)
         */
        String toReturn = String.format("%s - %s\n", courseCode, courseName);
        for (Index index: indexes.values()){
            toReturn += index.getInfo();
        }
        return toReturn;
    }

    @Override
    public String getMoreInfo() {
        /**
         * Returns formatted string of course information
         * Eg.
         * AC2101 - Accounting Recognition and Measurement
         * .....
         * .....
         * (see index.getMoreInfo)
         */
        String toReturn = String.format("%s - %s - %s\n", courseCode, courseName, school);
        for (Index index: indexes.values()){
            toReturn += index.getMoreInfo();
        }
        return toReturn;
    }
}