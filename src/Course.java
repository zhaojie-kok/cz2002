import java.io.Serializable;
import java.sql.Time;
import java.util.HashMap;

public class Course implements Serializable{
    private static final long serialVersionUID = -9117232107080367454L;

    enum School {
        NBS,
        SCBE,
        SCEE,
        SCSE,
        SEEE,
        SMSE,
        SMAE,
        SADM,
        SOH,
        SOSS,
        WKWSCI,
        SBS,
        SPMS,
        ASE,
        LKCM
      }

    private String courseCode;
    private String courseName;
    private School school;
    private HashMap<Integer, Index> indexes;
    private int totalSlots;
    private int AU;
    private Time examDate;

    public Course(String courseCode, School school, HashMap<Integer, Index> indexes, int AU){
        this.courseCode = courseCode;
        this.school = school;
        this.indexes = indexes;
        this.AU = AU;
    }

    public String getCourseCode(){
        return courseCode;
    }
    public String getCourseName(){
        return courseName;
    }

    public String getSchool(){
        return school.toString();
    }

    public int getAvailableSlots(int index){
        return indexes.get(index).getSlotsAvailable();
    }

    public int getTotalSlots(){
        return totalSlots;
    }

    public int getAU(){
        return AU;
    }

    public Index getIndex(int index){
        return indexes.get(index);
    }

    public HashMap<Integer, Index> getIndexes(){
        return indexes;
    }

    public void changeAvailableSlots(int index, int change){
        /**
        Adds the change to the available slots mapped to index of course
        */
        if (change == 1){

        }
        else if (change == -1){

        }
        else{
            int slots = indexes.get(index).getSlotsAvailable();
            slots = slots + change;
            indexes.get(index).setSlotsAvailable(slots);
        }
    }

    public void setAvailableSlots(int index, int set){
        indexes.get(index).setSlotsAvailable(set);
    }
}