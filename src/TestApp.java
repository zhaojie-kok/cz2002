import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import entities.School;
import entities.Staff;
import entities.Student;
import entities.course_info.Index;
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
        StaffSystem staffSystem;
        try {
            staffSystem = new StaffSystem("JOHN123");
            
            staffSystem.selectCourse("BU8201");
            for (int i = 0; i < 14; i++) {
                staffSystem.timetable[i] = new ArrayList<LessonDetails>();
            }
            
            staffSystem.selectLessonDetails("LT28", "LEC", 4, 3,
                LocalTime.of(10, 30), LocalTime.of(11, 30));
        

            for (List<LessonDetails> l : staffSystem.timetable) {
                System.out.println(l.size());
                for (LessonDetails ll : l){
                    if (ll != null){
                        System.out.println(ll.getLessonType());
                    }
                    else{
                        System.out.println("OOPS");
                    }
                }
            };
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return;
        }

        // try {
        //     CourseMgr cMgr = new CourseMgr();
        //     Index i = cMgr.getCourseIndex(cMgr.getCourse("BU8201"), "00402");
        //     for (List<LessonDetails> l : i.getTimeTable()) {
        //         for (LessonDetails ll : l){
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

        // // TESTING ADD STUDENT
        // LocalDateTime[] newAccessPeriod = { LocalDateTime.of(2020, 11, 10, 0, 0),
        //         LocalDateTime.of(2020, 12, 20, 0, 0) };
        // try {
        //     staffSystem.addStudent("SNOW003", "Snow White", "F", "German", "U2020001A", newAccessPeriod, "apple");
        //     staffSystem.addStudent("ARIE003", "Ariel", "F", "Danish", "U2020002B", newAccessPeriod, "ocean");
        //     staffSystem.addStudent("BELL003", "Belle", "F", "French", "U2020003C", newAccessPeriod, "beast");
        //     staffSystem.addStudent("CIND003", "Cinderella", "F", "French", "U2020004D", newAccessPeriod, "slipper");
        //     staffSystem.addStudent("HUAM003", "Mulan", "F", "Chinese", "U2020005E", newAccessPeriod, "mushu");
        // } catch (Exception e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
        // staffSystem.addStudent("SNEE001", "Sneezy", "M", "German", "U1234001A", newAccessPeriod, "gilbert");
        // staffSystem.addStudent("SLEE001", "Sleepy", "M", "German", "U1234002B", newAccessPeriod, "colvig");
        // staffSystem.addStudent("BASH001", "Bashful", "M", "German", "U1234003C", newAccessPeriod, "mattraw");
        // staffSystem.addStudent("DOPE001", "Dopey", "M", "German", "U1234004D", newAccessPeriod, "collins");

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