import java.util.HashMap;

public class CourseMgr {
    public CourseMgr(){
        
    }
    public void removeStudent(Student student){

    }

    public void addStudent(Student student, String indexNo){

    }

    public void dequeueWaitlist(String indexNo){

    }

    public void enqueueWaitlist(Student student, String indexNo){

    }

    // public HashMap<String, Student[]> checkStudentsRegistered(Course course){

    // }

    // public Student[] checkStudentsRegistered(Course course, Index i){
    // }

    // public HashMap<String, Integer> checkVacanciesAvailable(Course course){
    // }

    private void informWaitlistDequeued(Student s, Course c, Index i){
        String body = "You have successfully received a slot for " 
                        + c.getCourseName() 
                        + " (" + c.getCourseCode() + ") "
                        + "with Index " + i.getIndexNo();
        NotifSender.sendNotif("Successful application for " + c, body, "placeholder@gmail.com");
    }
}