import java.util.HashMap;

public class CourseMgr implements EntityManager{
    public CourseMgr(){
        
    }
    public void removeStudent(Student student){

    }

    public void addStudent(Student student, String indexNo){

    }

    public void dequeueWaitlist(String indexNo){

    }

    public void enqueueWaitlist(Student student, Course course, String indexNo){
        // no need to check again since the clashes would've been checked by student manager before calling this
        course.enqueueWaitlist(student, indexNo);
        student.addWaitlist(course, indexNo);
    }

    public static Index getCourseIndex(String courseCode, String indexNo) {
        // create an index from courseCoude and indexNo
        return null;
    }

    // public HashMap<String, Student[]> checkStudentsRegistered(Course course){

    // }

    // public Student[] checkStudentsRegistered(Course course, Index i){
    // }

    // public HashMap<String, Integer> checkVacanciesAvailable(Course course){
    // }

    @Override
    public void saveState(Object course) {
        FileReader.writeCourse((Course) course);
    }

    private void informWaitlistDequeued(Student s, Course c, Index i){
        String body = "You have successfully received a slot for " 
                        + c.getCourseName() 
                        + " (" + c.getCourseCode() + ") "
                        + "with Index " + i.getIndexNo();
        NotifSender.sendNotif("Successful application for " + c, body, "placeholder@gmail.com");
    }
}