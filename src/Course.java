import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;

public class Course implements Serializable{
    private static final long serialVersionUID = -9117232107080367454L;

    private String courseCode;
    private String courseName;
    private School school;
    private HashMap<String, Index> indexes; // <indexNo, Index>
    private int acadU;
    private Calendar examDate;

    public Course(String courseCode, 
                School school,
                int acadU, 
                Calendar examDate){
        this.indexes = new HashMap<String,Index>();
        this.courseCode = courseCode;
        this.school = school;
        this.acadU = acadU;
        this.examDate = examDate;
    }

    public String getCourseCode(){
        return courseCode;
    }
    public String getCourseName(){
        return courseName;
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

    public void updateIndex(Index index){
        /**
         * Can add new index or modify existing index (if another index with same index no exists)
         */
        Index prev = indexes.get(index.getIndexNo());
        if (prev != null){
            indexes.replace(index.getIndexNo(), prev, index);
        }
        else{
            indexes.put(index.getIndexNo(), index);
        }
    }

    public Index deleteIndex(String indexNo){
        return indexes.remove(indexNo);
    }

    public HashMap<String, Index> getIndexes(){
        return indexes;
    }
}