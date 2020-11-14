package managers;

import java.util.HashMap;
import java.util.List;
import entities.*;
import entities.course_info.*;
import readers.FileReader;
import boundaries.NotifSender;

public class CourseMgr implements EntityManager {
    private HashMap<String, Course> hashMap = FileReader.loadCourses();
    
    public Course getCourse(String courseCode){
        return hashMap.get(courseCode);
    }
    
    public boolean removeStudent(String matricNo, String indexNo, String courseCode){
        Course course = getCourse(courseCode);
        if (course == null){
            System.out.println("No such course: " + courseCode);
            return false;
        }
        Index ind = course.getIndex(indexNo);
        if (ind == null){
            System.out.println("No such index: " + indexNo);
            return false;
        }
        List<Student> studentList = ind.getRegisteredStudents();
        Student student = StudentManager.getStudent(matricNo);
        if (student == null){
            System.out.println("No such student: " + matricNo);
            return false;
        }
        boolean removed = studentList.remove(student);
        if (removed){
            ind.setRegisteredStudents(studentList);
            ind.addSlotsAvailable();
        }
        return removed;
    }

    public boolean addStudent(String matricNo, String indexNo, String courseCode){
        Course course = getCourse(courseCode);
        if (course == null){
            System.out.println("No such course: " + courseCode);
            return false;
        }
        Index ind = course.getIndex(indexNo);
        if (ind == null){
            System.out.println("No such index: " + indexNo);
            return false;
        }
        List<Student> l = ind.getRegisteredStudents();
        Student student = StudentManager.getStudent(matricNo);
        if (student == null){
            System.out.println("No such student: " + matricNo);
            return false;
        }
        if (!l.contains(student)){
            l.add(student);
            ind.setRegisteredStudents(l);
            ind.minusSlotsAvailable();
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

    public void dequeueWaitlist(String indexNo, String courseCode){
        Course c = getCourse(courseCode);
        Index i = c.getIndex(indexNo);
        if (i == null){
            return;
        }
        Student s = i.dequeueWaitlist();
        if (s != null && addStudent(s, i)){
            informWaitlistSuccess(s, c, i);
        }
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

    public void enqueueWaitlist(Student s, String indexNo, String courseCode){
        Index i = getCourse(courseCode).getIndex(indexNo);
        if (s != null && s != null){
            i.enqueueWaitlist(s);
        }
    }

    public HashMap<String, List<Student>> checkStudentsRegistered(Course course){
        HashMap<String, Index> indexes = course.getIndexes();
        HashMap<String, List<Student>> sHashMap = new HashMap<String, List<Student>>();
        for (Index ind: indexes.values()){
            sHashMap.put(ind.getIndexNo(), ind.getRegisteredStudents());
        }
        return sHashMap;
    }

    public List<Student> checkStudentsRegistered(Course course, String indexNo){
        return course.getIndex(indexNo).getRegisteredStudents();
    }

    public HashMap<String, Integer> checkVacanciesAvailable(Course course){
        HashMap<String, Index> indexes = course.getIndexes();
        HashMap<String, Integer> sHashMap = new HashMap<String, Integer>();
        for (Index ind: indexes.values()){
            sHashMap.put(ind.getIndexNo(), ind.getSlotsAvailable());
        }
        return sHashMap;
    }

    @Override
    public void saveState(Object course) {
        FileReader.writeCourse((Course) course);
    }

    private void informWaitlistSuccess(Student s, Course c, Index i){
        String body = "You have successfully received a slot for " 
                        + c.getCourseName() 
                        + " (" + c.getCourseCode() + ") "
                        + "with Index " + i.getIndexNo();
        NotifSender.sendNotif("Successful application for " + c, body, "placeholder@gmail.com");
    }
}