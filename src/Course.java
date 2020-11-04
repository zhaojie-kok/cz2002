import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Queue;

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
    private HashMap<String, Index> indexes; // <indexNo, Index>
    private HashMap<String, Queue<String>> waitlist; // <indexNo, Queue<matricNo>>
    private int AU;
    private Calendar examDate;

    public Course(String courseCode, 
                School school, 
                HashMap<String, Index> indexes, 
                HashMap<String, Queue<String>> waitlist,
                int AU, 
                Calendar examDate){
        this.courseCode = courseCode;
        this.school = school;
        this.indexes = indexes;
        this.waitlist = waitlist;
        this.AU = AU;
        this.examDate = examDate;
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

    public int getAvailableSlots(String indexNo){
        return indexes.get(indexNo).getSlotsAvailable();
    }

    public int getAU(){
        return AU;
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
            this.waitlist.put(indexNo, newList);
        }

        return true;
    }

    public HashMap<String, Index> getIndexes(){
        return indexes;
    }

    public void changeAvailableSlots(String indexNo, int change){
        /**
        Adds the change to the available slots mapped to indexNo of course
        */
        if (change == 1){

        }
        else if (change == -1){

        }
        else{
            int slots = indexes.get(indexNo).getSlotsAvailable();
            slots = slots + change;
            indexes.get(indexNo).setSlotsAvailable(slots);
        }
    }

    public void setAvailableSlots(String indexNo, int set){
        indexes.get(indexNo).setSlotsAvailable(set);
    }
}