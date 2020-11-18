package managers;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

import entities.course_info.LessonDetails;

public class LessonDetailMaker {
    private String lessonVenue;
    private String lessonType;
    private DayOfWeek lessonDay;
    private int evenOdd = -1;
    private LocalTime startTime;
    private LocalTime endTime;
    private LessonDetails lessonDetails;

    public LessonDetails makeLessonDetails() throws MissingparametersException{
        // first ensure all required information exists
        if (lessonVenue == null) {
            throw new MissingparametersException("lesson venue required");
        }
        if (lessonType == null) {
            throw new MissingparametersException("lesson type required");
        }
        if (lessonDay == null) {
            throw new MissingparametersException("lesson day required");
        }
        if (evenOdd == -1) {
            throw new MissingparametersException("required to choose if lesson is on even or odd week");
        }
        if (startTime == null) {
            throw new MissingparametersException("start time required");
        }
        if (endTime == null) {
            throw new MissingparametersException("end time required");
        }

        lessonDetails = new LessonDetails(lessonVenue, lessonType, lessonDay, evenOdd, startTime, endTime);
        return lessonDetails;
    }

    public void setLessonVenue(String lessonVenue) {
        this.lessonVenue = lessonVenue;
    }

    public void setLessonType(String lessonType) {
        this.lessonType = lessonType;
    }

    public void setLessonDay(int lessonDay) throws OutofrangeException {
        if (!(lessonDay>=1 && lessonDay<=7)) {
            throw new OutofrangeException("Lesson Day must be between 1 (Monday) and 7 (Sunday)");
        }
        lessonDay--;

        this.lessonDay = DayOfWeek.values()[lessonDay];
    }

    public void setEvenOdd(int evenOdd) throws OutofrangeException {
        if (!(evenOdd>=0 || evenOdd<=2)) {
            throw new OutofrangeException("Choice must be 0 (even weeks), 1 (odd weeks), or 2 (both)");
        }

        this.evenOdd = evenOdd;
    }

    public void setStartTime(LocalTime startTime) throws OutofrangeException {
        LocalTime newStart = startTime;
        if (endTime == null || newStart.isBefore(endTime)) {
            this.startTime = newStart;
        } else {
            throw new OutofrangeException("new start time must be before the existing end time of the lesson");
        }
    }

    public void setEndTime(LocalTime endTime) throws OutofrangeException {
        LocalTime newEnd = endTime;
        if (startTime == null || newEnd.isAfter(startTime)) {
            this.endTime = newEnd;
        } else {
            throw new OutofrangeException("new end time must be after the existing start time of the lesson");
        }
    }

    public void clearChoices() {
        this.lessonVenue = null;
        this.lessonType = null;
        this.lessonDay = null;
        this.evenOdd = -1;
        this.startTime = null;
        this.endTime = null;
        this.lessonDetails = null;
    }
}