package entities.course_info;

import java.util.Calendar;

public class LessonDetails {

	private String lessonVenue;
	private String lessonType;
	private int lessonDay;
	private Calendar startTime;
	private Calendar endTime;
	
	
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
