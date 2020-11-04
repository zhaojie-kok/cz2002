import java.util.HashMap;
import java.util.List;

public class CourseMgr {
    private HashMap<String, Course> hashMap = FileReader.loadCourses();
    
    public Course getCourse(String courseCode){
        return hashMap.get(courseCode);
    }
    
    public boolean removeStudent(String matricNo, int index, String courseCode){
        Course c = getCourse(courseCode);
        if (c == null){
            System.out.println("No such course: " + courseCode);
            return false;
        }
        Index i = c.getIndex(index);
        if (i == null){
            System.out.println("No such index: " + index);
            return false;
        }
        List<Student> l = i.getRegisteredStudents();
        Student s = StudentManager.getStudent(matricNo);
        if (s == null){
            System.out.println("No such student: " + matricNo);
            return false;
        }
        boolean removed = l.remove(s);
        if (removed){
            i.setRegisteredStudents(l);
            i.addSlotsAvailable();
        }
        return removed;
    }

    public boolean addStudent(String matricNo, int index, String courseCode){
        Course c = getCourse(courseCode);
        if (c == null){
            System.out.println("No such course: " + courseCode);
            return false;
        }
        Index i = c.getIndex(index);
        if (i == null){
            System.out.println("No such index: " + index);
            return false;
        }
        List<Student> l = i.getRegisteredStudents();
        Student s = StudentManager.getStudent(matricNo);
        if (s == null){
            System.out.println("No such student: " + matricNo);
            return false;
        }
        if (!l.contains(s)){
            l.add(s);
            i.setRegisteredStudents(l);
            i.minusSlotsAvailable();
            return true;
        }
        return false;
    }

    public boolean addStudent(Student s, Index i){
        if (s == null || i == null){
            return false;
        }
        List<Student> l = i.getRegisteredStudents();
        if (!l.contains(s)){
            l.add(s);
            i.setRegisteredStudents(l);
            i.minusSlotsAvailable();
            return true;
        }
        return false;
    }

    public void dequeueWaitlist(int index, String courseCode){
        Course c = getCourse(courseCode);
        Index i = c.getIndex(index);
        if (i == null){
            return;
        }
        Student s = i.dequeueWaitlist();
        if (s != null && addStudent(s, i)){
            informWaitlistSuccess(s, c, i);
        }
    }

    public void enqueueWaitlist(Student s, int index, String courseCode){
        Index i = getCourse(courseCode).getIndex(index);
        if (s != null && s != null){
            i.enqueueWaitlist(s);
        }
    }

    public HashMap<Integer, List<Student>> checkStudentsRegistered(Course course){
        HashMap<Integer, Index> indexes = course.getIndexes();
        HashMap<Integer, List<Student>> sHashMap = new HashMap<Integer, List<Student>>();
        for (Index i: indexes.values()){
            sHashMap.put(i.getIndexNo(), i.getRegisteredStudents());
        }
        return sHashMap;
    }

    public List<Student> checkStudentsRegistered(Course course, int index){
        return course.getIndex(index).getRegisteredStudents();
    }

    public HashMap<Integer, Integer> checkVacanciesAvailable(Course course){
        HashMap<Integer, Index> indexes = course.getIndexes();
        HashMap<Integer, Integer> sHashMap = new HashMap<Integer, Integer>();
        for (Index i: indexes.values()){
            sHashMap.put(i.getIndexNo(), i.getSlotsAvailable());
        }
        return sHashMap;
    }

    private void informWaitlistSuccess(Student s, Course c, Index i){
        String body = "You have successfully received a slot for " 
                        + c.getCourseName() 
                        + " (" + c.getCourseCode() + ") "
                        + "with Index " + i.getIndexNo();
        NotifSender.sendNotif("Successful application for " + c, body, "placeholder@gmail.com");
    }
}