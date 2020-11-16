package entities.course_info;

import java.util.Calendar;

import entities.Printable;

public class LessonDetails{

	private String lessonVenue;
	private String lessonType;
	private int lessonDay;
	private boolean oddWeek;
	private boolean evenWeek;
	private Calendar startTime;
	private Calendar endTime;
	
	public LessonDetails(String lessonVenue, 
							String lessonType, 
							int lessonDay,
							int evenOdd,
							Calendar startTime,
							Calendar endTime
							){
		/**
		 * 
		 * @param evenOdd 0 if even, 1 if odd, 2 if both
		 */
		this.lessonDay = lessonDay;
		this.startTime = startTime;
	}
	
	public String getLessonVenue() {
		return lessonVenue;
	}
	
	public void setLessonVenue(String lessonVenue) {
		this.lessonVenue = lessonVenue;
	}
	
	public String getLessonType() {
		return lessonType;
	}
	
	public void setLessonType(String lessonType) {
		this.lessonType = lessonType;
	}
	
	public int getLessonDay() {
		return lessonDay;
	}
	
	public void setLessonDay(int lessonDay) {
		this.lessonDay = lessonDay;
	}
	

	
	public Calendar getStartTime() {
		return startTime;
	}
	
	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}
	
	public Calendar getEndTime() {
		return endTime;
	}
	
	public void setEndTime(Calendar endTime) {
		this.endTime = endTime;
	}
}
