package managers;

import java.util.HashMap;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import entities.*;
import entities.course_info.*;
import exceptions.FileReadingException;
import exceptions.InvalidInputException;
import exceptions.KeyClashException;
import exceptions.KeyNotFoundException;
import exceptions.MissingParametersException;
import exceptions.OutOfRangeException;
import readers.CourseReader;
import boundaries.NotifSender;

/**
 * Controller class meant to handle course/index object related events
 */
public class CourseMgr implements EntityManager {
    private HashMap<String, Course> allCourses;
    private CourseReader cReader;
    private NotifSender notifSender;

    /**
     * Constructor
     * @throws FileReadingException thrown when there is an error in reading the persistent data files storing courses information
     */
    public CourseMgr() throws FileReadingException {
        cReader = new CourseReader("data/courses/");
        try {
            allCourses = (HashMap<String, Course>) cReader.getData();
        } catch (FileReadingException e) {
            throw e;
        }
        notifSender = new NotifSender();
    }

    /**
     * Method to create a new course
     * 
     * @param courseCode Course code of new course
     * @param courseName Name of new course
     * @param school     School hosting new course
     * @param acadU      Academic units carried by new course
     * @return new course that has been successfully created
     * @throws OutOfRangeException thrown if academic units are insufficient
     * @throws KeyClashException
     */
    public Course createCourse(String courseCode, String courseName, School school, int acadU)
            throws OutOfRangeException, KeyClashException {
        if (allCourses.containsKey(courseCode)) {
            throw new KeyClashException("Course Code " + courseCode);
        }
        Course c = new Course(courseCode, courseName, school, acadU);
        saveState(c);
        return c;
    }

    /**
     * Method to create a new index for a given course
     * 
     * @param course     Course index is to be added to
     * @param indexNo    Index number of new course
     * @param slotsTotal Total capacity of new course
     * @param timeTable  Time table for lessons in new index
     * @return Successfully created new index
     * @throws MissingParametersException thrown if parameters provided are incomplete
     * @throws OutOfRangeException thrown if slotsTotal is insufficient
     */
    public Index createIndex(Course course,
                            String indexNo,
                            int slotsTotal,
                            List<LessonDetails>[] timeTable) throws MissingParametersException, OutOfRangeException {
        Index i = new Index(indexNo, slotsTotal, timeTable);
        course.updateIndex(i);
        saveState(course);
        return i;
    }

    /**
     * Method to update details regarding existing course
     * 
     * @param course     Course to change details
     * @param courseCode New course code to change to. Must be unique from other
     *                   courses
     * @param courseName New name of course
     * @param school     School hosting the course
     * @return true if change was successful, false if otherwise
     * @throws KeyClashException
     */
    public boolean updateCourse(Course course, String courseCode, String courseName, School school)
            throws KeyClashException {
        if (courseCode != course.getCourseCode()){
            // If courseCode is different,
            if (allCourses.containsKey(courseCode)){
                // If courseCode already exists, do not override
                throw new KeyClashException(courseCode);
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

    /**
     * Update information about an index of a given course
     * 
     * @param course     Course to change details
     * @param index      Index to change details
     * @param indexNo    New index number of index
     * @param slotsTotal New total capacity of index
     * @throws OutOfRangeException thrown if slotsTotal is insufficient
     * @throws KeyClashException thrown if indexNo clases with another index under the same course
     */
    public void updateIndex(Course course, Index index, String indexNo, int slotsTotal) throws OutOfRangeException,
            KeyClashException {
        // Update indexNo
        if (indexNo != index.getIndexNo()){
            if (course.getIndex(indexNo) != null){
                // Index already exists, cannot overwrite
                throw new KeyClashException(indexNo);
            }
            String oldIndexNo = index.getIndexNo();
            index.setIndexNo(indexNo);
            course.updateIndex(index, oldIndexNo);
        }

        // Update slots Total
        if (slotsTotal > 0){        
            int changeInSlots = slotsTotal - index.getSlotsTotal();
            if (changeInSlots != 0){
                if (index.getSlotsAvailable() < changeInSlots){
                    throw new OutOfRangeException("new total slots cannot be less than number of students registered");
                }
            }
            index.setSlotsTotal(slotsTotal);
            course.updateIndex(index);
        }
        
        saveState(course);
    }

    /**
     * Method to access all courses in the system
     */
    public HashMap<String, Course> getAllCourses(){
        return allCourses;
    }

    /**
     * Method to accesss a specific course in the system
     * 
     * @param courseCode Course code of the course to be accessed
     * @return           Course with matching course code
     * @throws KeyNotFoundException thrown if no courses in system match the given course code
     */
    public Course getCourse(String courseCode) throws KeyNotFoundException {
        Course toReturn = allCourses.get(courseCode);
        if (toReturn == null){
            throw new KeyNotFoundException(courseCode);
        }
        return toReturn;
    }

    /**
     * Method to access a specific index under a given course
     * 
     * @param course    Course to be checked
     * @param indexNo   Index number of index to be accessed
     * @return          Index with given index number
     * @throws KeyNotFoundException thrown if no matching index can be found;
     */
    public Index getCourseIndex(Course course, String indexNo) throws KeyNotFoundException {
        Index toReturn = course.getIndex(indexNo);
        if (toReturn == null){
            throw new KeyNotFoundException(indexNo);
        }
        return toReturn;
    }

    /**
     * Method to register a student to a course
     * 
     * @param student   student to be registered
     * @param index     Index to register the student under
     * @param course    Course to register the student under
     * @return          true if student is successfully added
     * @throws MissingParametersException thrown if arguments provided are incomplete
     * @throws InvalidInputException thrown if student has already registered for the course, or if the index is full
     */
    public boolean addStudent(Student student, Index index, Course course)
            throws MissingParametersException, InvalidInputException {
        if (student == null || index == null || course == null) {
            throw new MissingParametersException("Missing Arguments provided, please check inputs");
        }
        List<Student> l = index.getRegisteredStudents();
        if (!l.contains(student) && index.getSlotsAvailable() > 0) {
            l.add(student);
            index.setRegisteredStudents(l);
            course.updateIndex(index);
            saveState(course);
            return true;
        } else if (l.contains(student)) {
            throw new InvalidInputException("Cannot register for a course already registered");
        } else {
            throw new InvalidInputException("Cannot add to an index that is full");
        }
    }

    /**
     * Method to remove a student from a course
     * 
     * @param student   Student to be removed
     * @param index     Index to remove student from
     * @param course    Course to remove student from
     * @throws MissingParametersException thrown if arguments provided are incomplete
     * @throws InvalidInputException thrown if student has not registered for the course
     */
    public void removeStudent(Student student, Index index, Course course) throws MissingParametersException,
            InvalidInputException {
        if (student == null || index == null || course == null){
            throw new MissingParametersException("Missing arguments provided, please check inputs");
        }
        List<Student> studentList = index.getRegisteredStudents();
        List<Student> studentWaitlist = index.getWaitlistedStudents();
        if (studentList.remove(student)){
            index.setRegisteredStudents(studentList);
            course.updateIndex(index);
            dequeueWaitlist(course, index);
            saveState(course);
        } else if (studentWaitlist.remove(student)) { // check waitlist if not in the list of registered students
            index.removeFromWaitList(student);
            course.updateIndex(index);
            saveState(course);
        } else {
            throw new InvalidInputException(student.getUserId() + " is not registered for " + index.getIndexNo());
        }
    }

    /**
     * Method to swop the index of 2 students registered for the same course
     * 
     * @param s1     first student to be swopped
     * @param s2     second stuednt to be swopped
     * @param course Course to swop student indexes
     * @throws KeyNotFoundException thrown if course cannot be found under either student's registered courses
     * @throws InvalidInputException thrown if either student has not registered for the course
     */
    public void swopStudents (Student s1, Student s2, Course course) throws KeyNotFoundException, InvalidInputException {
        if (!(s1.isRegistered(course) && s2.isRegistered(course))) {
            throw new InvalidInputException("Both Students must be registered for the course");
        }

        // get the indexes of each student
        String indexNo1 = s1.getCourseIndex(course.getCourseCode());
        Index i1 = course.getIndex(indexNo1);
        String indexNo2 = s2.getCourseIndex(course.getCourseCode());
        Index i2 = course.getIndex(indexNo2);

        if (indexNo1.equals(indexNo2)) {
            return;
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
        } else {
            throw new KeyNotFoundException("Inconsistencies found for student data. Please contact system administrator");
        }

    }

    /**
     * Method to dequeue the waitlist of an index for a course
     * 
     * @param course course hosting index
     * @param index  Index to dequeue waitlist
     */
    private void dequeueWaitlist(Course course, Index index){
        if (index == null || course == null){
            return;
        }
        Student student = index.dequeueWaitlist();
        try {
            if (student != null && addStudent(student, index, course)) {
                informWaitlistSuccess(student, course, index);
                course.updateIndex(index);
                saveState(course);
            }
        } catch (InvalidInputException e) {
            if (index.getSlotsAvailable() == 0) {
                return;
            } else {
                dequeueWaitlist(course, index);
            }
        } catch (MissingParametersException m) {
            m.printStackTrace();
        }
    }

    /**
     * Method to add a student to an index's waitlist
     * 
     * @param student Student to add to waitlist
     * @param index   Index to enqueue student under
     * @param course  Course to enqueue student under
     */
    public void enqueueWaitlist(Student student, Index index, Course course){
        index.enqueueWaitlist(student);
        course.updateIndex(index);
        saveState(course);
    }

    /**
     * Method to check which students are registered under which index for a course
     * 
     * @param course course to check
     * @return       HashMap of Index numbers, mapped to Lists of students registered for each index
     */
    public HashMap<String, List<Student>> checkStudentsRegistered(Course course){
        HashMap<String, Index> indexes = course.getIndexes();
        HashMap<String, List<Student>> sHashMap = new HashMap<String, List<Student>>();
        for (Index ind: indexes.values()){
            sHashMap.put(ind.getIndexNo(), ind.getRegisteredStudents());
        }
        return sHashMap;
    }

    /**
     * method to get the List of students registered under an index
     * @param index index to check
     * @return      List of students registered
     */
    public List<Student> checkStudentsRegistered(Index index){
        return index.getRegisteredStudents();
    }

    /**
     * Method to check the waitlists for all indexes under a given course
     * @param course course to check
     * @return       HashMap of index numebrs mapping to List of students waitlisted
     */
    public HashMap<String, List<Student>> checkStudentsWaitlisted(Course course){
        HashMap<String, Index> indexes = course.getIndexes();
        HashMap<String, List<Student>> sHashMap = new HashMap<String, List<Student>>();
        for (Index ind: indexes.values()){
            sHashMap.put(ind.getIndexNo(), ind.getWaitlistedStudents());
        }
        return sHashMap;
    }

    /**
     * Method to check the students waitlisted under a given index
     * 
     * @param index Index to be checked
     * @return      List of students waitlisted
     */
    public List<Student> checkStudentsWaitlisted(Index index){
        return index.getWaitlistedStudents();
    }

    /**
     * Method to retrieve the vacancies available for each index under a course
     * 
     * @param course course to check
     * @return       HashMap of index numbers mapping to vacancies in each index
     */
    public HashMap<String, Integer> checkVacanciesAvailable(Course course){
        HashMap<String, Index> indexes = course.getIndexes();
        HashMap<String, Integer> sHashMap = new HashMap<String, Integer>();
        for (Index ind: indexes.values()){
            sHashMap.put(ind.getIndexNo(), ind.getSlotsAvailable());
        }
        return sHashMap;
    }

    /**
     * Method to check the vacancies available for a specific index
     * @param index index to check
     * @return      number of vacancies available
     */
    public int checkVacanciesAvailable(Index index){
        return index.getSlotsAvailable();
    }

    /**
     * Method to save the state of a Course
     */
    @Override
    public void saveState(Object course) {
        Course c = (Course) course;
        cReader.writeData(c);
        allCourses.put(c.getCourseCode(), c);
    }

    /**
     * Method to notify a student that they have been dequeued from the waitlist
     * 
     * @param s student that was dequeued
     * @param c course accepting the student
     * @param i index accepting the student
     */
    private void informWaitlistSuccess(Student s, Course c, Index i){
        String body = "Dear " + s.getName() + ",\n"
                        + "You have successfully received a slot for " 
                        + c.getCourseName() 
                        + " (" + c.getCourseCode() + ") "
                        + "with Index " + i.getIndexNo();
        
        try {
            notifSender.sendNotif("Successful application for " + c.getCourseName(), body, s.getEmail(), "email");
        } catch (AddressException a) {
            System.out.println(a.getMessage());
        } catch (MessagingException m) {
            System.out.println(m.getMessage());
        }
    }
}