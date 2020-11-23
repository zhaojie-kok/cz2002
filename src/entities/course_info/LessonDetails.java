package entities.course_info;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;

import entities.Printable;

/**
 * An entity class containing detailed scheduling information about lessons for an index
 */
public class LessonDetails implements Serializable, Comparable, Printable {
	private static final long serialVersionUID = 7313328423766023263L;
	private String lessonVenue;
	private String lessonType;
	private DayOfWeek lessonDay;
	private int evenOdd;
	private LocalTime startTime;
	private LocalTime endTime;
	
	/**
	 * Constructor
	 * @param lessonVenue String with address/location of the lesson venue
	 * @param lessonType String describing the lesson type
	 * @param lessonDay DayOfWeek from java.time for represent which day lesson falls on
	 * @param evenOdd int denoting if lesson is on even or odd weeks (0: even, 1: odd, 2: both)
	 * @param startTime starting time of the lesson
	 * @param endTime ending time of the lesson
	 */
	public LessonDetails(String lessonVenue, 
							String lessonType, 
							DayOfWeek lessonDay,
							int evenOdd,
							LocalTime startTime,
							LocalTime endTime
							){
		this.lessonVenue = lessonVenue;
		this.lessonType = lessonType;
		this.lessonDay = lessonDay;
		this.evenOdd = evenOdd;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	/**
	 * Getter for lesson venue
	 */
	public String getLessonVenue() {
		return lessonVenue;
	}
	
	/**
	 * Setter for lesson venue
	 * 
	 * @param lessonVenue String with address/location of the lesson venue
	 */
	public void setLessonVenue(String lessonVenue) {
		this.lessonVenue = lessonVenue;
	}
	
	/**
	 * Getter for lesson type
	 */
	public String getLessonType() {
		return lessonType;
	}
	
	/**
	 * Setter for lesson type
	 * 
	 * @param lessonTypeString describing the lesson type
	 */
	public void setLessonType(String lessonType) {
		this.lessonType = lessonType;
	}
	
	/**
	 * Getter for the DayOfWeek lesson lies on
	 */
	public DayOfWeek getLessonDay() {
		return lessonDay;
	}
	
	/**
	 * setter for DayOfWeek lesson lies on
	 * 
	 * @param lessonDayDayOfWeek from java.time for represent which day lesson falls on
	 */
	public void setLessonDay(DayOfWeek lessonDay) {
		this.lessonDay = lessonDay;
	}

	/**
	 * Getter for evenOdd of lesson
	 * 0: even weeks
	 * 1: odd weeks
	 * 2: both
	 */
	public int getEvenOdd() {
		return evenOdd;
	}

	/**
	 * Setter for evenOdd of lesson
	 * 0: even weeks
	 * 1: odd weeks
	 * 2: both
	 */
	public void setEvenOdd(int evenOdd) {
		this.evenOdd = evenOdd;
	}
	
	/**
	 * Getter for start time of the lesson
	 */
	public LocalTime getStartTime() {
		return startTime;
	}
	
	/**
	 * Setter for start time of the lesson
	 * @param startTime LocalTime object containing start time of lesson
	 */
	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}
	
	/**
	 * Getter for end time of the lesson
	 */
	public LocalTime getEndTime() {
		return endTime;
	}
	
	/**
	 * Setter for end time of the lesson
	 * @param endTime LocalTime object containing starttime of lesson
	 */
	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	/**
	 * method for comparisons between 2 LessonDetails objects
	 * only the startTime is used for comparison
	 * returns the equivalent of comparing 2 LocalTime objects
	 */
	@Override
	public int compareTo(Object o) {
		LessonDetails compared = (LessonDetails) o;
		return this.startTime.compareTo(compared.startTime);
	}

	/**
	 * Method to get the start to end timing of the entire lesson in a String
	 */
	@Override
	public String getInfo() {
		return this.startTime.toString() + " - " + this.endTime.toString();
	}

	/**
	 * Returns a String detailed of information about the lesson
	 * e.g:
	 * LEC        | Mon   | 1130-1330 | LKC-LT
	 */
	@Override
	public String getMoreInfo() {
		String info = String.format("|%10s|", this.lessonType);
		String[] daysOfWeek = {"Mon", "Tues", "Wed", "Thurs", "Fri", "Sat", "Sun"};
		info += String.format(" %5s |", daysOfWeek[this.lessonDay.getValue()]);
		info += String.format(" %02d%02d-%02d%02d |", this.startTime.getHour(), this.startTime.getMinute(),
					this.endTime.getHour(), this.endTime.getMinute());
		info += String.format(" %15s\n", this.lessonVenue);

		return info;
	}
}
