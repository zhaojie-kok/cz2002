import java.util.HashMap;

// public class StudentManager implements EntityManager{
//     public int dropCourse(Course course, String indexNo, Student student) {
//         String courseCode = course.getCourseCode();
//         if (student.getCourseIndex(courseCode) == null) {
//             return -1;
//         }
        // student.removeCourse(courseCode);
        //         // TODO: removeStudent, dequeue waitlist
        //         // CourseMgr.removeStudent()
        //         return 1;
        //     }

public class StudentManager implements EntityManager {
    private HashMap<String, Student> students = FileReader.loadStudents();

    public Student getStudent(String matricNo){
        return students.get(matricNo);
    }

    public void dropCourse(String courseCode, String indexNo, Student student) {
        return;
    }

    public int addCourse(Course course, Index index, Student student) {
        /**
         * Returns int. 1=successful registration, 0=waitlisted.
         */
        if (index.getSlotsAvailable() > 0) {
            student.addCourse(course.getCourseCode(), index.getIndexNo());
            saveState(student);
            return 1;
        } else {
            student.addWaitlist(course, index.getIndexNo());
            saveState(student);
            return 0;
        }
    }

    public void swopIndex(Student s1, Student s2, String courseCode) {
        // update the student info
        String i1 = s1.getCourseIndex(courseCode);
        String i2 = s2.getCourseIndex(courseCode);
        s1.changeIndex(courseCode, i1, i2);
        s2.changeIndex(courseCode, i2, i1);
        saveState(s1);
        saveState(s2);
    }

    public int isAddable(Student student, Course course){
        /**
         * Returns 1 if able to be added. Else 0 for already registered, -1 for exceeding AU
         */
        HashMap <String, String> courses = student.getCourses();
        HashMap <String, String> waitlist = student.getWaitlist();
        
        // not allowed to add a course already registered for or requires overloading
        if (courses.containsKey(course.getCourseCode()) || waitlist.containsKey(course.getCourseCode())) {
            // already registered
            return 0;
        } else if (student.getAcadUnits() + course.getAcadU() > student.getAcadUnitsAllowed()) {
            // overload
            return -1;
        }
        return 1;
    }

	@Override
    public void saveState(Object student) {
        Student s = (Student) student;
        FileReader.writeStudent((Student) s);
        students.replace(s.getMatricNo(), s);
    }
}
