package managers;

import java.time.DayOfWeek;
import java.time.LocalTime;

import entities.course_info.LessonDetails;
import exceptions.MissingParametersException;
import exceptions.OutOfRangeException;

/**
 * Controller used to create lesson details to be used by other controllers/entities
 */
public class LessonDetailMaker implements Systems {
    private String lessonVenue;
    private String lessonType;
    private DayOfWeek lessonDay;
    private int evenOdd = -1;
    private LocalTime startTime;
    private LocalTime endTime;
    private LessonDetails lessonDetails;

    /**
     * Method to create a LessonDetails object based on previously provided inputs
     * 
     * @return susccessfully created LussonDetails object
     * @throws MissingParametersException thrown if any require parameters are not yet provided
     */
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

    /**
     * Setter for the venue of the lesson
     * @param lessonVenue String description of lesson venue
     */
    public void setLessonVenue(String lessonVenue) {
        this.lessonVenue = lessonVenue;
    }

    /**
     * Setter for the type of lesson
     * @param lessonType String description for type of lesson
     */
    public void setLessonType(String lessonType) {
        this.lessonType = lessonType;
    }

    /**
     * Setter for the day of lesson
     * @param lessonDay int between 1(Mon) and 7 (Sun) representing the day of the week lesson is on
     * @throws OutOfRangeException thrown if lessonDay is not between 1 and 7 inclusive
     */
    public void setLessonDay(int lessonDay) throws OutOfRangeException {
        if (!(lessonDay>=1 && lessonDay<=7)) {
            throw new OutOfRangeException("Lesson Day must be between 1 (Monday) and 7 (Sunday)");
        }
        lessonDay--;

        this.lessonDay = DayOfWeek.values()[lessonDay];
    }

    /**
     * Setter for whether lesson lies on even, odd, or all weeks
     * @param evenOdd int describing what weeks lessons are on. 0 (even), 1 (odd), 2 (both)
     * @throws OutOfRangeException thrown if evenOdd is not between 0 and 2 inclusive
     */
    public void setEvenOdd(int evenOdd) throws OutOfRangeException {
        if (!(evenOdd>=0 || evenOdd<=2)) {
            throw new OutOfRangeException("Choice must be 0 (even weeks), 1 (odd weeks), or 2 (both)");
        }

        this.evenOdd = evenOdd;
    }

    /**
     * Setter for starting time of lesson
     * @param startTime LocalTime object containing start time for lesson
     * @throws OutOfRangeException thrown if start time is not before end time
     */
    public void setStartTime(LocalTime startTime) throws OutOfRangeException {
        LocalTime newStart = startTime;
        if (endTime == null || newStart.isBefore(endTime)) {
            this.startTime = newStart;
        } else {
            throw new OutOfRangeException("new start time must be before the existing end time of the lesson");
        }
    }

    /**
     * Setter for ending time of lesson
     * @param endTime LocalTime object containing end time for lesson
     * @throws OutOfRangeException thrown if end time is not after start time
     */
    public void setEndTime(LocalTime endTime) throws OutOfRangeException {
        LocalTime newEnd = endTime;
        if (startTime == null || newEnd.isAfter(startTime)) {
            this.endTime = newEnd;
        } else {
            throw new OutOfRangeException("new end time must be after the existing start time of the lesson");
        }
    }

    /**
     * Method to clear all previously set parameters as well as created lessonDetails object
     */
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

    /**
     * Method to check the status of this System object
     */
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