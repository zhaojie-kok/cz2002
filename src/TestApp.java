import java.util.HashMap;

import entities.Student;
import exceptions.Filereadingexception;
import managers.StaffSystem;
import managers.StudentManager;
import readers.FileReader;

public class TestApp {
    public static void main(String[] args) {
        StaffSystem staffSystem;
        try {
            FileReader.readSerializedObject("data/students/MARY001");
            staffSystem = new StaffSystem("JOHN123");
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return;
        }

        // // TESTING ADD COURSE/INDEX
        // staffSystem.addCourse("CZ1001", "Java is fun!", School.SCSE, 4);
        // staffSystem.selectCourse("CZ1001");
        // staffSystem.addIndex("10001", 10);
        // staffSystem.selectIndex("10001");

        // // TESTING ADD STUDENT
        // LocalDateTime[] newAccessPeriod = { LocalDateTime.of(2020, 11, 10, 0, 0),
        //         LocalDateTime.of(2020, 12, 20, 0, 0) };
        // staffSystem.addStudent("PETE001", "Peter", "M", "American", "U1234001A", newAccessPeriod, "tenor");
        // staffSystem.addStudent("PAUL001", "Paul", "M", "American", "U1234002B", newAccessPeriod, "baritone");
        // staffSystem.addStudent("MARY001", "Mary Travers", "F", "American", "U1234003C", newAccessPeriod, "contralto");


        // // TESTING REGISTER FOR COURSES
        // StudentSystem studentSystem = new StudentSystem("MARY001");
        // studentSystem.selectCourse("CZ1001");
        // studentSystem.selectIndex("10001");
        // studentSystem.addCourse();

        StudentManager sM;
        try {
            sM = new StudentManager();
            HashMap<String, Student> students = sM.getHashMap();
            for (Student s: students.values()){
                System.out.printf("%s: %s\n", s.getMatricNo(), s.getInfo());
            }
        } catch (Filereadingexception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        staffSystem.selectCourse("CZ1001");
        System.out.printf("%s", staffSystem.getCourseInfo());
    }
}