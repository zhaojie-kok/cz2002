package managers;

import java.util.HashMap;
import java.util.List;
import entities.*;
import entities.course_info.*;
import exceptions.Filereadingexception;
import exceptions.OutofrangeException;
import readers.CourseReader;
import boundaries.NotifSender;

public class CourseMgr implements EntityManager {
    private HashMap<String, Course> allCourses;
    private CourseReader cReader;

    public CourseMgr() throws Filereadingexception {
        cReader = new CourseReader("data/courses/");
        try {
            allCourses = (HashMap<String, Course>) cReader.getData();
        } catch (Filereadingexception e) {
            throw e;
        }
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

    public boolean updateCourse(Course course, String courseCode, String courseName, School school){
        if (courseCode != course.getCourseCode()){
            // If courseCode is different,
            if (allCourses.containsKey(courseCode)){
                // If courseCode already exists, do not override
                return false;
            }
            else{
                // Delete file
                cReader.deleteObject(course.getCourseCode());
                // Remove from hashMap
                allCourses.remove(course.getCourseCode());
                // Change details and save
                course.setCourseCode(courseCode);
                course.setCourseName(courseName);
                course.setCourseSchool(school);
                saveState(course);
            }
        }
        return true;
    }

    public boolean updateIndex(Course course, Index index, String indexNo, int slotsTotal) throws OutofrangeException {
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
                throw new OutofrangeException("new total slots cannot be less than number of students registered");
            }
        }   
        index.setSlotsTotal(slotsTotal);
        index.setSlotsAvailable(index.getSlotsAvailable() - changeInSlots);
        course.updateIndex(index);
        saveState(course);
        return true;
    }

    public HashMap<String, Course> getAllCourses(){
        return allCourses;
    }
    
    public Course getCourse(String courseCode){
        return allCourses.get(courseCode);
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
            dequeueWaitlist(course, index);
            saveState(course);
            return true;
        } else if (!studentList.contains(student)) { // check waitlist if unable to remove
            index.removeFromWaitList(student);
            course.updateIndex(index);
            saveState(course);
            return true;
        }

        return false;
    }

    public boolean addStudent(Student student, Index index, Course course){
        if (student == null || index == null){
            return false;
        }
        List<Student> l = index.getRegisteredStudents();
        if (!l.contains(student) && index.getSlotsAvailable() > 0){
            l.add(student);
            index.setRegisteredStudents(l);
            index.minusSlotsAvailable();
            course.updateIndex(index);
            saveState(course);
            return true;
        }
        return false;
    }

    public int swopStudents (Student s1, Student s2, Course course) {
        if (!(s1.isRegistered(course) && s2.isRegistered(course))) {
            return -1; // TODO: convert to exception
        }

        // get the indexes of each student
        String indexNo1 = s1.getCourseIndex(course.getCourseCode());
        Index i1 = course.getIndex(indexNo1);
        String indexNo2 = s2.getCourseIndex(course.getCourseCode());
        Index i2 = course.getIndex(indexNo2);

        if (indexNo1.equals(indexNo2)) {
            return -2; // TODO: convert to exception
        }

        // remove student s1 from index i1 and add to index i2
        // then do the same for student s2 with i2 and i1 respectively
        List<Student> list1 = i1.getRegisteredStudents();
        List<Student> list2 = i2.getRegisteredStudents();
        boolean removed1 = list1.remove(s1);
        boolean removed2 = list2.remove(s1);
        if (removed1 && removed2) {
            list1.add(s2);
            list2.add(s1);
            i1.setRegisteredStudents(list1);
            i2.setRegisteredStudents(list2);
            course.updateIndex(i1);
            course.updateIndex(i2);
            saveState(course);
            return 1;
        } else  {
            return -3; // TODO: convert to exception
        }

    }

    private void dequeueWaitlist(Course course, Index index){
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
        allCourses.put(c.getCourseCode(), c);
    }

    private void informWaitlistSuccess(Student s, Course c, Index i){
        String body = "You have successfully received a slot for " 
                        + c.getCourseName() 
                        + " (" + c.getCourseCode() + ") "
                        + "with Index " + i.getIndexNo();
        NotifSender.sendNotif("Successful application for " + c, body, "placeholder@gmail.com");
    }
}