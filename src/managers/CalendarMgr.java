package managers;

import java.time.LocalTime;
import java.util.List;

import entities.course_info.*;

/**
 * Controller class for DateTime related functionality.
 * Primarily meant to help check for clashes between schedules/timetables
 */
public class CalendarMgr {
    /**
     * Method used to check for clashes between lesson details of 2 indexes
     * 
     * @param i1 first index to check
     * @param i2 second index to check
     * @return true if a clash is detected, false otherwise
     */
    public boolean checkClash(Index i1, Index i2) {
        // each timetable is a array of length 14, each slot for a day of the week for 2 weeks
        // the List of lessonDetails in each slot represents the lessons in each day
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
                } else if (lesson1.getEndTime().isBefore(lesson2.getEndTime())) {
                    slot1++;
                } else {
                    slot2++;
                }
            }
        }

        return false;
    }

    /**
     * Generalised version of {@link #checkClash(Index, Index)} method
     * Meant to check if there are any clashes between 2 lessons
     * @param lesson1 first lesson's details 
     * @param lesson2 second lesson's details
     * @return true if clash is detected, false otherwise
     */
    public boolean lessonClash(LessonDetails lesson1, LessonDetails lesson2) {
        int evenOdd1 = lesson1.getEvenOdd();
        int evenOdd2 = lesson2.getEvenOdd();
        if (evenOdd1 == 1 && evenOdd2 == 0) {
            return false;
        } else if (evenOdd2 == 1 && evenOdd1 == 0) {
            return false;
        }
        
        LocalTime start1 = lesson1.getStartTime();
        LocalTime end1 = lesson1.getEndTime();
        LocalTime start2 = lesson2.getStartTime();
        LocalTime end2 = lesson2.getEndTime();

        // only check if there is no clash since it is easier
        if (start1.equals(end2) || start2.equals(end1)) {
            return false;
        } else if (start1.isAfter(end2) || start2.isAfter(end1)) {
            return false;
        } else {
            return true;
        }
    }
}
