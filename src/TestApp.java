import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import entities.School;
import entities.Staff;
import entities.Student;
import entities.course_info.Course;
import entities.course_info.Index;
import entities.course_info.LessonDetails;
import exceptions.FileReadingException;
import exceptions.KeyClashException;
import exceptions.KeyNotFoundException;
import managers.CourseMgr;
import managers.LoginMgr;
import managers.StaffSystem;
import managers.StudentManager;
import managers.StudentSystem;
import readers.FileReader;
import readers.LoginReader;
import readers.StaffReader;

public class TestApp {
    public static void main(String[] args) throws Exception {
        // LoginReader loginReader = new LoginReader("data/loginDetails");
        // String[] details = { newStaff.getUserId(), "password", "staff", null};
        // try {
        //     loginReader.writeData(details);
        // } catch (FileReadingException e1) {
        //     // TODO Auto-generated catch block
        //     e1.printStackTrace();
        // }

        // s = sMgr.getStudent("STUD002");
        // s.changeIndex("BU8201", "00402");
        // sMgr.saveState(s);
        // String[] studentsToRegister = {"STUA002","STUC002", "STUE002", "ARIE003", "BASH001", "BELL003", "CIND003", "DOC0001", "DOPE001", "HUAM003"};
        // for (String st:studentsToRegister){
        //     StudentSystem studentSystem = new StudentSystem(st);
        //     studentSystem.selectCourse("CZ2007");
        //     studentSystem.selectIndex("10154");
        //     studentSystem.addCourse();
        // }
        // StaffSystem staffSystem;
        // try {
        //     staffSystem = new StaffSystem("JOHN123");
        //     LoginReader lr = new LoginReader("data/loginDetails/");
        //     Object[] details = (Object[]) lr.getData("SLEE001");
        //     LocalDateTime[] ld = (LocalDateTime[])details[2];
        //     System.out.println(ld[0]);
        //     System.out.println(ld[1]);
        // } catch (Exception e1) {
        //     // TODO Auto-generated catch block
        //     e1.printStackTrace();
        //     return;
        // }

        // try {
        //     CourseMgr cMgr = new CourseMgr();
        //     Index i = cMgr.getCourseIndex(cMgr.getCourse("EG0001"), "10238");
        //     System.out.println(i.getMoreInfo());
        //     for (List<LessonDetails> l : i.getTimeTable()) {
        //         for (LessonDetails ll : l){
        //             if (ll == null){}
        //             System.out.println(ll.getInfo());
        //         }
        //     };
        // } catch (Exception e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
        

        // LoginReader tmp = new LoginReader("data/loginDetails");
        // try {
        // String[] details = (String[]) tmp.getData("JOHN123");
        // System.out.printf("%s - %s - %s", details[0], details[1]);
        // } catch (Exception e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

        // // TESTING ADD STAFF (DEBUG ONLY)
        // // String staffNo, String userId, String userType, String name, String gender, String nationality
        // StaffReader staffReader = new StaffReader("data/staff/");
        // Staff newStaff = new Staff("admin01", "STAF123", "staff", "Staff A", "M", "Singaporean");
        // staffReader.writeData(newStaff);
        // LoginReader loginReader = new LoginReader("data/loginDetails");
        // String[] details = { newStaff.getUserId(), "password", "staff", null};
        // try {
        //     loginReader.writeData(details);
        // } catch (FileReadingException e1) {
        //     // TODO Auto-generated catch block
        //     e1.printStackTrace();
        // }

        // // TESTING ADD COURSE/INDEX
        // try {
        //     staffSystem.addCourse("BS3006", "Business and staff", School.SBS, 4);
        //     staffSystem.selectCourse("BS3006");
        //     staffSystem.addIndex("50305", 10);
        //     staffSystem.addIndex("50306", 15);
        //     staffSystem.addCourse("AM2002", "Watercolor", School.SADM, 3);
        //     staffSystem.selectCourse("AM2002");
        //     staffSystem.addIndex("20202", 10);
        //     staffSystem.addIndex("20203", 10);
        // } catch (Exception e) {
        //     // TODO Auto-generated catch block
        //     System.out.println(e.getMessage());
        // }

        // TESTING ADD STUDENT
        // LocalDateTime[] newAccessPeriod = { LocalDateTime.of(2020, 11, 10, 0, 0),
        //         LocalDateTime.of(2020, 12, 20, 0, 0) };
        // try {
            
        //     staffSystem.addStudent("SLEE001", "Sleepy", "M", "German", "notanemail@email.com", "U1234003C", newAccessPeriod, "colvig");
        //     // staffSystem.addStudent("DOC0001", "Doc", "M", "German", "notanemail@email.com", "U1234001A", newAccessPeriod, "cummings");
        //     // staffSystem.addStudent("SNEE001", "Sneezy", "M", "German", "notanemail@email.com", "U1234002B", newAccessPeriod, "gilbert");
        //     // staffSystem.addStudent("BASH001", "Bashful", "M", "German", "notanemail@email.com", "U1234004D", newAccessPeriod, "mattraw");
        //     // staffSystem.addStudent("DOPE001", "Dopey", "M", "German", "notanemail@email.com", "U1234005E", newAccessPeriod, "collins");
            
        //     // staffSystem.addStudent("SNOW003", "Snow White", "F", "German", "notanemail@email.com", "U2020001A", newAccessPeriod, "apple");
        //     // staffSystem.addStudent("ARIE003", "Ariel", "F", "Danish", "notanemail@email.com", "U2020002B", newAccessPeriod, "ocean");
        //     // staffSystem.addStudent("BELL003", "Belle", "F", "French", "notanemail@email.com", "U2020003C", newAccessPeriod, "beast");
        //     // staffSystem.addStudent("CIND003", "Cinderella", "F", "French", "notanemail@email.com", "U2020004D", newAccessPeriod, "slipper");
        //     // staffSystem.addStudent("HUAM003", "Mulan", "F", "Chinese", "notanemail@email.com","U2020005E", newAccessPeriod, "mushu");
        //     // staffSystem.addStudent("MERI003", "Merida", "F", "Scottish", "notanemail@email.com", "U2020006F", newAccessPeriod, "arrow");
        //     // staffSystem.addStudent("JASM003", "Jasmine", "F", "Arabian", "notanemail@email.com", "U2020007G", newAccessPeriod, "carpet");
            
        //     // staffSystem.addStudent("STUA002", "Student A", "M", "Singaporean", "czassignment482@gmail.com", "U20202511A", newAccessPeriod, "singlish");
        //     // staffSystem.addStudent("STUB002", "Student B", "F", "Singaporean", "czassignment482@gmail.com", "U20202511B", newAccessPeriod, "singlish");
        //     // staffSystem.addStudent("STUC002", "Student C", "M", "Singaporean", "czassignment482@gmail.com", "U20202511C", newAccessPeriod, "singlish");
        //     // staffSystem.addStudent("STUD002", "Student D", "F", "Singaporean", "czassignment482@gmail.com", "U20202511D", newAccessPeriod, "singlish");
        //     // staffSystem.addStudent("STUE002", "Student E", "M", "Singaporean", "czassignment482@gmail.com", "U20202511E", newAccessPeriod, "singlish");

        // } catch (Exception e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }

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
    }
}