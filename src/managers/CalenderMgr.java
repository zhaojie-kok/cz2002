package managers;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.*;
import entities.course_info.*;

public class CalenderMgr {  
    public boolean checkClash(Index i1, Index i2) {
        List<LessonDetails>[] timetable1 = i1.getTimeTable();
        List<LessonDetails>[] timetable2 = i2.getTimeTable();
        int slot1, slot2;
        LessonDetails lesson1, lesson2;

        // iterate through the days of the week
        for (int i = 0; i < timetable1.length; i++) {
            slot1 = 0;
            slot2 = 0;
            if (timetable1[i] == null || timetable2[i] == null) {
                continue;
            }
            
            // iterate through the lessons for each day
            while (slot1 < timetable1[i].size() && slot2 < timetable2[i].size()) {
                lesson1 = timetable1[i].get(slot1);
                lesson2 = timetable2[i].get(slot2);

                if (lessonClash(lesson1, lesson2)) {
                    return true;
                }

                // whichever index's lesson ends earlier, increase the slot
                // increase both if both lessons end together
                if (lesson1.getEndTime().equals(lesson2.getEndTime())) {
                    slot1++;
                    slot2++;
                } else if (lesson1.getEndTime().before(lesson2.getEndTime())) {
                    slot1++;
                } else {
                    slot2++;
                }
            }
        }

        return false;
    }

    private boolean lessonClash(LessonDetails lesson1, LessonDetails lesson2) {
        Calendar start1 = lesson1.getStartTime();
        Calendar end1 = lesson1.getEndTime();
        Calendar start2 = lesson2.getStartTime();
        Calendar end2 = lesson2.getEndTime();

        // only check if there is no clash since it is easier
        if (start1.equals(end2) || start2.equals(end1)) {
            return false;
        } else if (start1.after(end2) || start2.after(end1)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean checkAddClash(Student s, Index i) {
        HashMap<String, String> courses = s.getCourses();
        Index registeredInd;

        // iterate through all of student's registered courses to check for clash
        for (Map.Entry<String, String> entry: courses.entrySet()) {
            registeredInd = CourseMgr.getCourseIndex(entry.getKey(), entry.getValue());
            if (checkClash(registeredInd, i)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkSwopClash(Student s, String courseCode, Index newIndex) {
        HashMap<String, String> courses = s.getCourses();

        // remove the course to be changed from the list of courses
        courses.remove(courseCode);
        Index registeredInd;

        // iterate through all of student's registered courses to check for clash
        for (Map.Entry<String, String> entry: courses.entrySet()) {
            registeredInd = CourseMgr.getCourseIndex(entry.getKey(), entry.getValue());
            if (checkClash(registeredInd, newIndex)) {
                return true;
            }
        }
        return false;
    }
}
