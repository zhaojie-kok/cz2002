import java.io.Serializable;
import java.sql.Time;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Queue;

public class Course implements Serializable{
    private static final long serialVersionUID = -9117232107080367454L;
    
    private String courseCode;
    private String courseName;
    private HashMap<String, Index> indexes; // <indexNo, Index object>
    private HashMap<String, Integer> availableSlots;
    private int totalSlots;
    private Calendar examDate;
    private HashMap<String, Queue<String>> waitlist; // <indexNo, List<matricNo>>
    private int acadUnits;

    public Course(String courseCode, 
                    String courseName, 
                    Index[] indexes,
                    int totalSlots,
                    Calendar examDate,
                    HashMap<String, Queue<String>> waitlist,
                    int acadUnits) 
    {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.totalSlots = totalSlots;
        this.examDate = examDate;
        this.waitlist = waitlist;
        this.acadUnits = acadUnits;

        HashMap<String, Index> indexHM = new HashMap<>();
        HashMap<String, Integer> slotsAvail = new HashMap<>();
        for (Index ind: indexes) {
            indexHM.put(ind.getIndexNo(), ind);
            slotsAvail.put(ind.getIndexNo(), ind.getSlotsAvailable());
        }
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

    public int getAcadUnits() {
        return this.acadUnits;
    }

    public Index getIndex(String indexNo) {
        if (this.indexes.containsKey(indexNo)) {
            return (Index) this.indexes.get(indexNo);
        }

        return null;
    }

    public boolean enqueueWaitlist(Student student, String indexNo) {
        if (!this.indexes.containsKey(indexNo)) {
            return false;
        }

        if (this.waitlist.get(indexNo) != null) {
            Queue<String> newList = this.waitlist.get(indexNo);
            newList.add(student.getMatricNo());

        }

        return true;
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