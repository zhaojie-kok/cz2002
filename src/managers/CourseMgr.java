package managers;

import java.util.HashMap;
import java.util.List;
import entities.*;
import entities.course_info.*;
import readers.CourseReader;
import boundaries.NotifSender;

public class CourseMgr implements EntityManager {
    private HashMap<String, Course> hashMap;
    private CourseReader cReader;

    public CourseMgr(){
        cReader = new CourseReader("data/courses/");
        hashMap = (HashMap<String, Course>) cReader.getData();
    }

    public Course createCourse(String courseCode,
                            School school,
                            int acadU){
        Course c = new Course(courseCode, school, acadU);
        saveState(c);
        return c;
    }

    public Index createIndex(Course course,
                            String indexNo,
                            int slotsTotal,
                            List<LessonDetails>[] timeTable){
        Index i = new Index(indexNo, slotsTotal, timeTable);
        course.updateIndex(i);
        saveState(course);
        return i;
    }

    public boolean updateCourse(Course course, String courseCode, School school){
        if (courseCode != course.getCourseCode()){
            // If courseCode is different,
            if (hashMap.containsKey(courseCode)){
                // If courseCode already exists, do not override
                return false;
            }
            else{
                // Delete file
                cReader.deleteObject(course.getCourseCode());
                // Remove from hashMap
                hashMap.remove(course.getCourseCode());
                // Change course code and save
                course.setCourseCode(courseCode);
                saveState(course);
            }
        }
        return true;
    }

    public boolean updateIndex(Course course, Index index, String indexNo, int slotsTotal){
        // Update indexNo
        if (indexNo != index.getIndexNo()){
            if (course.getIndex(indexNo) != null){
                // Index already exists, cannot overwrite
                return false;
            }
            String oldIndexNo = index.getIndexNo();
            index.setIndexNo(indexNo);
            course.updateIndex(index, oldIndexNo);
        }

        // Update slots Total
        int changeInSlots = slotsTotal - index.getSlotsTotal();
        if (changeInSlots != 0){
            if (index.getSlotsAvailable() < changeInSlots){
                return false;
            }
        }   
        index.setSlotsTotal(slotsTotal);
        index.setSlotsAvailable(index.getSlotsAvailable() - changeInSlots);
        course.updateIndex(index);
        saveState(course);
        return true;
    }

    public HashMap<String, Course> getHashMap(){
        return hashMap;
    }
    
    public Course getCourse(String courseCode){
        return hashMap.get(courseCode);
    }

    public Index getCourseIndex(Course course, String indexNo){
        return course.getIndex(indexNo);
    }

    public boolean removeStudent(Student student, Index index, Course course){
        if (student == null || index == null || course == null){
            return false;
        }
        List<Student> studentList = index.getRegisteredStudents();
        boolean removed = studentList.remove(student);
        if (removed){
            index.setRegisteredStudents(studentList);
            index.addSlotsAvailable();
            course.updateIndex(index);
            saveState(course);
        }
        return removed;
    }

    public boolean addStudent(Student student, Index index, Course course){
        if (student == null || index == null){
            return false;
        }
        List<Student> l = index.getRegisteredStudents();
        if (!l.contains(student)){
            l.add(student);
            index.setRegisteredStudents(l);
            index.minusSlotsAvailable();
            course.updateIndex(index);
            saveState(course);
            return true;
        }
        return false;
    }

    public void dequeueWaitlist(Course course, Index index){
        if (index == null || course == null){
            return;
        }
        Student student = index.dequeueWaitlist();
        if (student != null && addStudent(student, index, course)){
            informWaitlistSuccess(student, course, index);
            course.updateIndex(index);
            saveState(course);
        }
    }

    public void enqueueWaitlist(Student student, Index index, Course course){
        if (student != null && index != null){
            return;
        }
        index.enqueueWaitlist(student);
        course.updateIndex(index);
        saveState(course);
    }

    public HashMap<String, List<Student>> checkStudentsRegistered(Course course){
        HashMap<String, Index> indexes = course.getIndexes();
        HashMap<String, List<Student>> sHashMap = new HashMap<String, List<Student>>();
        for (Index ind: indexes.values()){
            sHashMap.put(ind.getIndexNo(), ind.getRegisteredStudents());
        }
        return sHashMap;
    }

    public List<Student> checkStudentsRegistered(Index index){
        return index.getRegisteredStudents();
    }

    public HashMap<String, Integer> checkVacanciesAvailable(Course course){
        HashMap<String, Index> indexes = course.getIndexes();
        HashMap<String, Integer> sHashMap = new HashMap<String, Integer>();
        for (Index ind: indexes.values()){
            sHashMap.put(ind.getIndexNo(), ind.getSlotsAvailable());
        }
        return sHashMap;
    }

    public int checkVacanciesAvailable(Index index){
        return index.getSlotsAvailable();
    }

    @Override
    public void saveState(Object course) {
        Course c = (Course) course;
        cReader.writeData(c);
        hashMap.put(c.getCourseCode(), c);
    }

    private void informWaitlistSuccess(Student s, Course c, Index i){
        String body = "You have successfully received a slot for " 
                        + c.getCourseName() 
                        + " (" + c.getCourseCode() + ") "
                        + "with Index " + i.getIndexNo();
        NotifSender.sendNotif("Successful application for " + c, body, "placeholder@gmail.com");
    }
}