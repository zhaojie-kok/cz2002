package managers;

import java.time.LocalDateTime;
import java.util.HashMap;
import readers.*;
import entities.*;
import entities.course_info.*;
import exceptions.FileReadingException;
import exceptions.InvalidInputException;
import exceptions.KeyClashException;
import exceptions.KeyNotFoundException;
import exceptions.MissingParametersException;

/**
 * Controller class for handling Student Entities
 */
public class StudentManager implements EntityManager {
    private HashMap<String, Student> students;
    private StudentReader sReader;

    /**
     * Constructor
     * 
     * @throws FileReadingException thrown if student details cannot be accessed
     */
    public StudentManager() throws FileReadingException {
        sReader = new StudentReader("data/students/");
        students = (HashMap<String, Student>) sReader.getData();
    }

    /**
     * Method to retrieve a Student object from system
     * 
     * @param identifier String identifier. Either userID or matriculation number
     * @return Returns student based on identifier (either matricNo or userId)
     * @throws KeyNotFoundException thrown if student cannot be identified
     */
    public Student getStudent(String identifier) throws KeyNotFoundException {
        Student toReturn = students.get(identifier);
        if (toReturn == null) {
            throw new KeyNotFoundException(identifier);
        }
        return toReturn;
    }

    /**
     * Method to get HashMap of all students in system
     */
    public HashMap<String, Student> getStudents() {
        return students;
    }

    /**
     * Method to create new student and add to system
     * 
     * @param userId       UserID of new student. Must be unique
     * @param name         Name of new student
     * @param gender       Gender of new student
     * @param nationality  Nationality of new student
     * @param email        Email address of new student
     * @param matricNo     Matriculation number of new student
     * @param accessPeriod Access period of new student
     * @throws KeyClashException          thrown if details required to be unique
     *                                    are not
     * @throws MissingParametersException thrown if any above fields are null
     */
    public void createStudent(String userId, String name, String gender, String nationality, String email,
            String matricNo, LocalDateTime[] accessPeriod) throws KeyClashException, MissingParametersException {
        /**
         * creates a new student based on information provided
         */
        if (students.containsKey(matricNo)){
            // If another student exists with the matricNo or userId, no student is created
            throw new KeyClashException("Matric Number " + matricNo);
        } else if (students.containsKey(userId)) {
            throw new KeyClashException("UserID " + userId);
        }
        Student newStudent = new Student(userId, name, gender, nationality, email, matricNo, accessPeriod,
                new HashMap<String, String>(), new HashMap<String, String>());
        students.put(matricNo, newStudent);
        saveState(newStudent);
    }

    /**
     * Method to try and register student for a course
     * Only allowed if student has sufficient academic units and slots are available
     * If no slots are available, student will be automatically waitlisted
     * 
     * @param course  Course to be registered
     * @param index   Index to be registered
     * @param student Student to register
     * @return int denoting status of registration (1. Successful registration, 0. Student waitlisted)
     * @throws InvalidInputException thrown if Student has insufficient academic units, or has already registered/waitlisted for the course
     */
    public int addCourse(Course course, Index index, Student student) throws InvalidInputException {
        if (student.isRegistered(course) || student.isWaitlisted(course)) {
            throw new InvalidInputException("Cannot register for a course already registered");
        } else if (student.getAcadUnits() + course.getAcadU() > student.getAcadUnitsAllowed()) {
            throw new InvalidInputException("Insufficient Academic Units to take this course");
        }

        if (index.getSlotsAvailable() > 0) {
            // register successfully
            student.addCourse(course.getCourseCode(), index.getIndexNo(), course.getAcadU());
            saveState(student);
            return 1;
        } else {
            // waitlisted
            student.addWaitlist(course, index.getIndexNo());
            saveState(student);
            return 0;
        }
    }

    /**
     * Allows a student to drop a course from either waitlist or list of registered courses
     * 
     * @param course  Course to drop
     * @param student Student wanting to drop
     * @throws InvalidInputException Thrown if student has not registered for course before (including waitlist)
     */
    public Student dropCourse(Course course, Student student) throws InvalidInputException {
        // Check if student is registered or waitlisted
        if (student.isRegistered(course)) {
            student.removeCourse(course);
            saveState(student);
        } else if (student.isWaitlisted(course)) {
            student.removeWaitlist(course);
            saveState(student);
        } else {
            throw new InvalidInputException(student.getUserId() + " is not registered for " + course.getCourseCode());
        }
        return student;
    }

    /**
     * Method to swop the indexes of 2 students for a course
     * 
     * @param s1     First student
     * @param s2     Second student
     * @param course Course to swop index
     * @throws KeyNotFoundException thrown if either student has not registered for the course
     */
    public void swopIndex(Student s1, Student s2, Course course) throws KeyNotFoundException {
        if (!(s1.isRegistered(course) && s2.isRegistered(course))) {
            throw new KeyNotFoundException("Both Students must be registered for the course");
        }

        String courseCode = course.getCourseCode();
        // update the student info only
        String i1 = s1.getCourseIndex(courseCode);
        String i2 = s2.getCourseIndex(courseCode);

        // do nothing if indexes are the same
        if (i1 == i2) {
            return;
        }

        s1.changeIndex(courseCode, i2);
        s2.changeIndex(courseCode, i1);
        saveState(s1);
        saveState(s2);
    }

    /**
     * Method to allow student to swop to another index within a course
     * 
     * @param student  Student swopping index
     * @param course   Course to swop
     * @param newIndex Index to swop to
     * @throws InvalidInputException thrown if newIndex has no available slots
     * @throws KeyNotFoundException thrown if student is not registered for the course
     */
    public void swopIndex(Student student, Course course, Index newIndex)
            throws InvalidInputException, KeyNotFoundException {
        if (!student.isRegistered(course)) {
            throw new KeyNotFoundException("Student is not registered for this course");
        }
        if (newIndex.getSlotsAvailable() <= 0) {
            throw new InvalidInputException("Cannot swop to an index that is full");
        }
        student.changeIndex(course.getCourseCode(), newIndex.getIndexNo());
        saveState(student);
    }

    /**
     * Method to change the access period of a student
     * 
     * @param student         Student to change access period
     * @param newAccessPeriod New access period to set
     * @throws FileReadingException thrown if student's file cannot be accessed
     */
    public void updateAccessPeriod(Student student, LocalDateTime[] newAccessPeriod) throws FileReadingException {
        LocalDateTime[] accessPeriod = student.getAccessPeriod();
		if (accessPeriod == newAccessPeriod){
            // same/updated already
            return;
        }

        try {
            student.changeAccessPeriod(newAccessPeriod);
            saveState(student);
        } catch (Exception e) {
            throw new FileReadingException("Error in changing accesss period. Please contact system administrator");
        }
    }

    /**
     * Method to return list of students
     */
    public String printAllStudents(){
        String toReturn = "";
        Student s;
        for (String i : students.keySet()){
            try {
                s = getStudent(i);
            } catch (KeyNotFoundException e) {
                continue;
            }
            if (i == s.getMatricNo()){
                continue;
            }
            toReturn += s.getLessInfo() + "\n";
        }
        return toReturn;
    }

    /**
     * Method to save the state of a student object to a file
     */
	@Override
    public void saveState(Object student) {
        Student s = (Student) student;
        sReader.writeData(s);
        students.put(s.getMatricNo(), s);
        students.put(s.getUserId(), s);

    }
}
