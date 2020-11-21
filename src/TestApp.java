import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import entities.School;
import entities.Staff;
import entities.Student;
import entities.course_info.LessonDetails;
import exceptions.FileReadingException;
import exceptions.KeyClashException;
import exceptions.KeyNotFoundException;
import managers.CourseMgr;
import managers.StaffSystem;
import managers.StudentManager;
import managers.StudentSystem;
import readers.FileReader;
import readers.LoginReader;
import readers.StaffReader;

public class TestApp {
    public static void main(String[] args) {
    //     StaffSystem staffSystem;
    //     try {
    //         staffSystem = new StaffSystem("JOHN123");
    //     } catch (Exception e1) {
    //         // TODO Auto-generated catch block
    //         e1.printStackTrace();
    //         return;
    //     }

        // LoginReader tmp = new LoginReader("data/loginDetails");
        // try {
        // String[] details = (String[]) tmp.getData("JOHN123");
        // System.out.printf("%s - %s - %s", details[0], details[1]);
        // } catch (Exception e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

        // // TESTING ADD STAFF (DEBUG ONLY)
        // // String staffNo, String userId, String userType, String name, String
        // gender, String nationality
        // StaffReader staffReader = new StaffReader("data/staff/");
        // Staff newStaff = new Staff("admin00", "JOHN123", "staff", "John Lim", "M",
        // "Singaporean");
        // staffReader.writeData(newStaff);
        // LoginReader loginReader = new LoginReader("data/loginDetails");
        // String[] details = { newStaff.getUserId(), "password", "staff" };
        // loginReader.writeData(details);
        // try {
        // String[] getDetails = (String[]) loginReader.getData("JOHN123");
        // System.out.printf("%s - %s", getDetails[0], getDetails[1]);
        // System.out.printf("%s\n", String.valueOf("password").hashCode());
        // System.out.printf("%s\n", getDetails[0].equals("" +
        // String.valueOf("password").hashCode())?"Equals":"Not");
        // } catch (Exception e) {
        // e.printStackTrace();
        // }

        // // TESTING ADD COURSE/INDEX
        // try {
        //     staffSystem.addCourse("BS3006", "Business and staff", School.SBS, 4);
        //     staffSystem.selectCourse("BS3006");
        //     staffSystem.addIndex("50383", 30);
        //     staffSystem.selectIndex("50383");
        // } catch (Exception e) {
        //     // TODO Auto-generated catch block
        //     System.out.println(e.getMessage());
        // }

        // // // TESTING ADD STUDENT
        // LocalDateTime[] newAccessPeriod = { LocalDateTime.of(2020, 11, 10, 0, 0),
        //         LocalDateTime.of(2020, 12, 20, 0, 0) };
        // staffSystem.addStudent("PETE001", "Peter", "M", "American", "U1234001A", newAccessPeriod, "tenor");
        // staffSystem.addStudent("PAUL001", "Paul", "M", "American", "U1234002B", newAccessPeriod, "baritone");
        // staffSystem.addStudent("MARY001", "Mary Travers", "F", "American", "U1234003C", newAccessPeriod, "contralto");

        // // // TESTING PRINT COURSE INFO
        // try {
        //     staffSystem.selectCourse("CZ1001");
        // } catch (KeyNotFoundException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
        // System.out.printf("%s", staffSystem.getCourseInfo());

        // StudentSystem studentSystem;
        // // TESTING REGISTER FOR COURSES
        // try {
        //     studentSystem = new StudentSystem("MARY001");            
        //     studentSystem.selectCourse("CZ1001");
        //     studentSystem.selectIndex("10001");
        //     studentSystem.addCourse();
        // } catch (Filereadingexception e1) {
        //     // TODO Auto-generated catch block
        //     e1.printStackTrace();
        // }

        // try {
        //     CourseMgr cMgr = new CourseMgr();
        //     cMgr.getCourse("CZ1001");
        // } catch (Exception e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
        String str = "Hello ";
        System.out.println(str);
        TestApp t = new TestApp();
        t.storeHere(str);
        System.out.println(str);
    }

    public void storeHere(Object str) {
        str = "there.";
    }
}