import java.io.Serializable;
import java.sql.Time;
import java.util.HashMap;

public class Course implements Serializable{
    private static final long serialVersionUID = -9117232107080367454L;
    
    private String courseCode;
    private String courseName;
    private Index[] indexes;
    private HashMap<String, Integer> availableSlots;
    private int totalSlots;
    private Time examDate;
    private HashMap<String, String[]> waitlist;

    public Course(){

    }

    public String getCourseCode(){
        return courseCode;
    }
    public String getCourseName(){
        return courseName;
    }

    public int getAvailableSlots(String index){
        return availableSlots.get(index);
    }

    public int getTotalSlots(){
        return totalSlots;
    }

    public void updateAvailableSlots(String index, int change){
        /**
        Adds the change to the available slots mapped to index of course
        */
        int slots = availableSlots.get(index);
        slots = slots + change;
        availableSlots.replace(index, slots);
    }
}