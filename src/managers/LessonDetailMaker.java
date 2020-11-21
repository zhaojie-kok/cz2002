package managers;

import java.time.DayOfWeek;
import java.time.LocalTime;

import entities.course_info.LessonDetails;
import exceptions.MissingParametersException;
import exceptions.OutOfRangeException;

public class LessonDetailMaker implements Systems {
    private String lessonVenue;
    private String lessonType;
    private DayOfWeek lessonDay;
    private int evenOdd = -1;
    private LocalTime startTime;
    private LocalTime endTime;
    private LessonDetails lessonDetails;

    public LessonDetails makeLessonDetails() throws MissingParametersException{
        // first ensure all required information exists
        if (lessonVenue == null) {
            throw new MissingParametersException("lesson venue required");
        }
        if (lessonType == null) {
            throw new MissingParametersException("lesson type required");
        }
        if (lessonDay == null) {
            throw new MissingParametersException("lesson day required");
        }
        if (evenOdd == -1) {
            throw new MissingParametersException("required to choose if lesson is on even or odd week");
        }
        if (startTime == null) {
            throw new MissingParametersException("start time required");
        }
        if (endTime == null) {
            throw new MissingParametersException("end time required");
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

    public void setLessonDay(int lessonDay) throws OutOfRangeException {
        if (!(lessonDay>=1 && lessonDay<=7)) {
            throw new OutOfRangeException("Lesson Day must be between 1 (Monday) and 7 (Sunday)");
        }
        lessonDay--;

        this.lessonDay = DayOfWeek.values()[lessonDay];
    }

    public void setEvenOdd(int evenOdd) throws OutOfRangeException {
        if (!(evenOdd>=0 || evenOdd<=2)) {
            throw new OutOfRangeException("Choice must be 0 (even weeks), 1 (odd weeks), or 2 (both)");
        }

        this.evenOdd = evenOdd;
    }

    public void setStartTime(LocalTime startTime) throws OutOfRangeException {
        LocalTime newStart = startTime;
        if (endTime == null || newStart.isBefore(endTime)) {
            this.startTime = newStart;
        } else {
            throw new OutOfRangeException("new start time must be before the existing end time of the lesson");
        }
    }

    public void setEndTime(LocalTime endTime) throws OutOfRangeException {
        LocalTime newEnd = endTime;
        if (startTime == null || newEnd.isAfter(startTime)) {
            this.endTime = newEnd;
        } else {
            throw new OutOfRangeException("new end time must be after the existing start time of the lesson");
        }
    }

    @Override
    public void clearSelections() {
        this.lessonVenue = null;
        this.lessonType = null;
        this.lessonDay = null;
        this.evenOdd = -1;
        this.startTime = null;
        this.endTime = null;
        this.lessonDetails = null;
    }

    @Override
    public String getSystemStatus() {
        String info = "";
        info += String.format("%20s", "Venue") + (lessonVenue != null ? lessonVenue : "");
        info += String.format("\n%20s", "Type") + (lessonType != null ? lessonType : "");
        info += String.format("\n%20s", "Day") + (lessonDay != null ? lessonDay.toString() : "");
        info += String.format("\n%20s", "Even or Odd weeks");
        switch(evenOdd) {
            case 0:
                info += "even weeks";
                break;
            case 1:
                info += "odd weeks";
                break;
            case 2:
                info += "all weeks";
                break;
            default:
                break;
        }
        info += String.format("\n%20s", "Start Time") + (startTime != null ? startTime.toString() : "");
        info += String.format("\n%20s", "End Time") + (endTime != null ? endTime.toString() : "");

        return info;
    }
}