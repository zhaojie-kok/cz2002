import java.time.LocalDateTime;
import java.util.HashMap;

import entities.School;
import entities.Student;
import managers.StaffSystem;
import managers.StudentManager;
import managers.StudentSystem;
public class TestApp {
    public static void main(String[] args) {
        StaffSystem staffSystem = new StaffSystem("JOHN123");
        staffSystem.addCourse("CZ1001", "Java is fun!", School.SCSE , 4);
        staffSystem.selectCourse("CZ1001");
        staffSystem.addIndex("10001", 10);
        staffSystem.selectIndex("10001");


        LocalDateTime[] newAccessPeriod = {LocalDateTime.of(2020, 11, 10, 0, 0), LocalDateTime.of(2020, 12, 20, 0, 0)};
        staffSystem.addStudent("PETE001", "Peter", "M", "American", "U1234001A", newAccessPeriod, "tenor");
        staffSystem.addStudent("PAUL001", "Paul", "M", "American", "U1234002B", newAccessPeriod, "baritone");
        staffSystem.addStudent("MARY001", "Mary Travers", "F", "American", "U1234003C", newAccessPeriod, "contralto");

        StudentManager sM = new StudentManager();
        HashMap<String, Student> students = sM.getStudents();
        for (Student s: students.values()){
            System.out.printf("%s\n", s.getUserId());
        }
        // StudentSystem studentSystem = new StudentSystem("MARY001");
        // studentSystem.selectCourse("CZ1001");
        // studentSystem.selectIndex("10001");
        // studentSystem.addCourse();

        System.out.printf("%s", staffSystem.getCourseInfo());
    }
}