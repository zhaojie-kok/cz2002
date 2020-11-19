import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

import entities.Staff;
import entities.Student;
import exceptions.Filereadingexception;
import managers.StaffSystem;
import managers.StudentManager;
import managers.StudentSystem;
import readers.FileReader;
import readers.LoginReader;
import readers.StaffReader;

public class TestApp {
    public static void main(String[] args) {
        StaffSystem staffSystem;
        try {
            staffSystem = new StaffSystem("JOHN123");
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return;
        }

        // LoginReader tmp = new LoginReader("data/loginDetails");
        // try {
        // String[] details = (String[]) tmp.getData("JOHN123");
        // System.out.printf("%s - %s - %s", details[0], details[1]);
        // } catch (Exception e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

        // // TESTING ADD STAFF (DEBUG ONLY)
        // (String staffNo, String userId, String userType, String name, String gender,
        // String nationality
        StaffReader staffReader = new StaffReader("data/staff/");
        Staff newStaff = new Staff("admin00", "JOHN123", "staff", "John Lim", "M", "Singaporean");
        staffReader.writeData(newStaff);
        LoginReader loginReader = new LoginReader("data/loginDetails");
        String[] details = { newStaff.getUserId(), "password", "staff" };
        loginReader.writeData(details);
        try {
            String[] getDetails = (String[]) loginReader.getData("JOHN123");
            System.out.printf("%s  -  %s", getDetails[0], getDetails[1]);
            System.out.printf("%s\n", String.valueOf("password").hashCode());
            System.out.printf("%s\n", getDetails[0].equals("" + String.valueOf("password").hashCode())?"Equals":"Not");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // // TESTING ADD COURSE/INDEX
        // staffSystem.addCourse("CZ1001", "Java is fun!", School.SCSE, 4);
        // staffSystem.selectCourse("CZ1001");
        // staffSystem.addIndex("10001", 10);
        // staffSystem.selectIndex("10001");

        // // TESTING ADD STUDENT
        LocalDateTime[] newAccessPeriod = { LocalDateTime.of(2020, 11, 10, 0, 0),
                LocalDateTime.of(2020, 12, 20, 0, 0) };
        staffSystem.addStudent("PETE001", "Peter", "M", "American", "U1234001A", newAccessPeriod, "tenor");
        staffSystem.addStudent("PAUL001", "Paul", "M", "American", "U1234002B", newAccessPeriod, "baritone");
        staffSystem.addStudent("MARY001", "Mary Travers", "F", "American", "U1234003C", newAccessPeriod, "contralto");
        

        // // TESTING PRINT COURSE INFO
        // staffSystem.selectCourse("CZ1001");
        // System.out.printf("%s", staffSystem.getCourseInfo());

        // // TESTING REGISTER FOR COURSES
        // StudentSystem studentSystem;
        // try {
        //     studentSystem = new StudentSystem("MARY001");            
        //     studentSystem.selectCourse("CZ1001");
        //     studentSystem.selectIndex("10001");
        //     studentSystem.addCourse();
        // } catch (Filereadingexception e1) {
        //     // TODO Auto-generated catch block
        //     e1.printStackTrace();
        // }
    }
}