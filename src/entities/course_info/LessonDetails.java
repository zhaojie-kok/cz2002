package entities.course_info;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;

import entities.Printable;

public class LessonDetails implements Serializable, Comparable, Printable {

	/**
	 *
	 */
	private static final long serialVersionUID = 7313328423766023263L;
	private String lessonVenue;
	private String lessonType;
	private DayOfWeek lessonDay;
	private int evenOdd;
	private LocalTime startTime;
	private LocalTime endTime;
	
	public LessonDetails(String lessonVenue, 
							String lessonType, 
							DayOfWeek lessonDay,
							int evenOdd,
							LocalTime startTime,
							LocalTime endTime
							){
		/**
		 * 
		 * @param evenOdd 0 if even, 1 if odd, 2 if both
		 */
		this.lessonVenue = lessonVenue;
		this.lessonType = lessonType;
		this.lessonDay = lessonDay;
		this.evenOdd = evenOdd;
		this.startTime = startTime;
		this.endTime = endTime;
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
	
	public DayOfWeek getLessonDay() {
		return lessonDay;
	}
	
	public void setLessonDay(DayOfWeek lessonDay) {
		this.lessonDay = lessonDay;
	}
	
	public LocalTime getStartTime() {
		return startTime;
	}
	
	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}
	
	public LocalTime getEndTime() {
		return endTime;
	}
	
	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	@Override
	public int compareTo(Object o) {
		LessonDetails compared = (LessonDetails) o;
		return this.startTime.compareTo(compared.startTime);
	}

	@Override
	public String getInfo() {
		return this.startTime.toString() + " - " + this.endTime.toString();
	}

	@Override
	public String getMoreInfo() {
		String info = this.lessonVenue;
		info += " | " + this.lessonType;
		info += " | " + this.lessonDay.toString();
		switch(this.evenOdd) {
			case 0:
				info += " | even weeks";
			case 1:
				info += " | odd weeks";
			case 2:
				info += " | all weeks";
		}
		info += " | " + getInfo();

		return info;
	}
}
