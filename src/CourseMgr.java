import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class CourseMgr implements EntityManager {
    private HashMap<String, Course> hashMap;

    public CourseMgr(){
        hashMap = FileReader.loadCourses();
    }

    public Course createCourse(String courseCode,
                            Course.School school,
                            int acadU, 
                            Calendar examDate){
        Course c = new Course(courseCode, school, acadU, examDate);
        hashMap.put(courseCode, c);
        saveState(c);
        return c;
    }

    public Index createIndex(String courseCode,
                            String indexNo,
                            int slotsTotal,
                            List<LessonDetails>[] timeTable){
        Index i = new Index(indexNo, slotsTotal, timeTable);
        Course c = getCourse(courseCode);
        c.updateIndex(i);
        saveState(c);
        return i;
    }

    public boolean updateIndexTotalSlots(Course course, Index index, int slotsTotal){
        /**
         * Returns boolean of whether the new slotsTotal is a valid change
         * Eg. if there are only 2 available slots, reducing slotsTotal by more than 2
         * will return false
         */
        int changeInSlots = slotsTotal - index.getSlotsTotal();
        if (index.getSlotsAvailable() < changeInSlots){
            return false;
        }
        index.setSlotsTotal(slotsTotal);
        index.setSlotsAvailable(index.getSlotsAvailable() - changeInSlots);
        course.updateIndex(index);
        saveState(course);
        return true;
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

    public boolean addStudent(Student student, Index index){
        if (student == null || index == null){
            return false;
        }
        List<Student> l = index.getRegisteredStudents();
        if (!l.contains(student)){
            l.add(student);
            index.setRegisteredStudents(l);
            index.minusSlotsAvailable();
            return true;
        }
        return false;
    }

    public void dequeueWaitlist(Course course, Index index){
        if (index == null || course == null){
            return;
        }
        Student student = index.dequeueWaitlist();
        if (student != null && addStudent(student, index)){
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
        FileReader.writeCourse(c);
        hashMap.replace(c.getCourseCode(), c);
    }

    private void informWaitlistSuccess(Student s, Course c, Index i){
        String body = "You have successfully received a slot for " 
                        + c.getCourseName() 
                        + " (" + c.getCourseCode() + ") "
                        + "with Index " + i.getIndexNo();
        NotifSender.sendNotif("Successful application for " + c, body, "placeholder@gmail.com");
    }
}