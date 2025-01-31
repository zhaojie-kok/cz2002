package entities.course_info;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import entities.*;
import exceptions.MissingParametersException;
import exceptions.OutOfRangeException;

/**
 * Entity class to store detailed information about each index under a course
 */
public class Index implements Serializable, Printable{
	private static final long serialVersionUID = -4814561120331706322L;
	private String indexNo;
	private int slotsAvailable;
	private int slotsTotal;
	private List<Student> registeredStudents; 
	private List<LessonDetails>[] timeTable;
	private LinkedList<Student> waitlist;
	
	/**
	 * Constructor
	 * 
	 * @param indexNo    String identifier for each index. Must be unique within
	 *                   each course
	 * @param slotsTotal Total number of slots available for this index
	 * @param timeTable  An array of lists of LessonDetails, showing the timetable
	 *                   of the course across 2 weeks (even and odd weeks)
	 * @throws MissingParametersException thrown if any above fields are null
	 * @throws OutOfRangeException thrown if number of slots is insufficient (minimum 1)
	 */
	public Index(String indexNo, int slotsTotal, List<LessonDetails>[] timeTable)
			throws MissingParametersException, OutOfRangeException {
		if (indexNo == null) {
			throw new MissingParametersException("Index Number cannot be empty");
		}
		if (slotsTotal <= 0) {
			throw new OutOfRangeException("Index must have at least 1 slot");
		}
		if (timeTable == null) {
			throw new MissingParametersException("Index cannot have no timetable");
		}
		this.indexNo = indexNo;
		this.slotsAvailable = slotsTotal;
		this.slotsTotal = slotsTotal;
		this.registeredStudents = new ArrayList<>(slotsTotal);
		this.waitlist = new LinkedList<Student>();
		this.timeTable = timeTable;
	}
	
	/**
	 * Getter method for the index number
	 * @return index number
	 */
	public String getIndexNo() {
		return indexNo;
	}
	
	/**
	 * Setter method for the index number
	 * 
	 * @param indexNo String identifier for each index. Must be unique within each
	 *                course
	 */
	public void setIndexNo(String indexNo) {
		this.indexNo = indexNo;
	}
	
	/**
	 * Getter method for the slots available to the index
	 * @return empty slots available
	 */
	public int getSlotsAvailable() {
		return slotsAvailable;
	}
	
	/**
	 * Getter method for the total number of slots available to the index
	 * @return total slots available
	 */
	public int getSlotsTotal() {
		return slotsTotal;
	}
	
	/**
	 * Setter method for the total number of slots available to the index
	 * NOTE: new total number of slots cannot cause the available number of slots to become negative
	 * @param slotsTotal new total number of slots
	 * @throws OutOfRangeException thrown if number of slots given is less than students already registered
	 */
	public void setSlotsTotal(int slotsTotal) throws OutOfRangeException {
		if (slotsTotal < this.slotsTotal - this.slotsAvailable) {
			throw new OutOfRangeException("New number of slots cannot be less than number of students already registered");
		}
		this.slotsAvailable = this.slotsAvailable - (this.slotsTotal - slotsTotal);
		this.slotsTotal = slotsTotal;
	}
	
	/**
	 * Getter for the list of students registered for the course
	 * @return List of students registered
	 */
	public List<Student> getRegisteredStudents() {
		return registeredStudents;
	}

	
	/**
	 * Getter for list of students in waitlist
	 * @return List of students in waitlist
	 */
	public List<Student> getWaitlistedStudents() {
		return new ArrayList<Student>(waitlist);
	}
	
	/**
	 * Setter for the list of registered students for a course
	 * @param registeredStudents new list of students registered for a course
	 */
	public void setRegisteredStudents(List<Student> registeredStudents) {
		this.registeredStudents = registeredStudents;
		this.slotsAvailable = this.slotsTotal - registeredStudents.size();
	}
	
	/**
	 * Getter for the timetable of the course
	 * @return Timetable as an array of lists (array of 14 slots, each representing 1 day for 2 weeks)
	 */
	public List<LessonDetails>[] getTimeTable() {
		return timeTable;
	}
	
	/**
	 * Setter for the timetable of the course
	 * @param timeTable new timetable for the index
	 */
	public void setTimeTable(List<LessonDetails>[] timeTable) {
		this.timeTable = timeTable;
	}

	/**
	 * Method to dequeue the waitlist. Returns null of the waitlist is empty
	 * @return the student at the front of the waitlist
	 */
	public Student dequeueWaitlist(){
		try{
			return waitlist.remove();
		}
		catch(NoSuchElementException e){
			return null;
		}
	}

	/**
	 * Method to enqueue a student to the end of the waitlist
	 * @param student the student to be waitlisted
	 */
	public void enqueueWaitlist(Student student){
		waitlist.add(student);
	}

	/**
	 * Method to remove a student from a waitlist
	 * @param student Student to be removed from waitlist
	 */
	public void removeFromWaitList(Student student) {
		waitlist.remove(student);
	}

	/**
	 * @return index number
	 */
	@Override
	public String getLessInfo(){
		return this.indexNo + "\n";
	}

	/**
	 * Retrieves information about the index
	 * @return a string of information on the index
	 * Eg.
	 * Index: 20001
	 * Slots available: 10 out of 20
	 * LEC        | Mon   | 1130-1330 | LKC-LT
	 * LEC/STUDIO | Tues  | 1430-1630 | LT28
	 * TUT        | Thurs | 0930-1000 | LHN-TR+12
	 */
	@Override
	public String getInfo() {
		/**
		
		 */
		String toReturn = String.format("Index: %s\nSlots available: %d out of %d\n", indexNo, slotsAvailable, slotsTotal);
		
		toReturn += "Even:\n";
		for (int i = 0; i < 7; i++){
			List<LessonDetails> lessons = timeTable[i];
			if (lessons == null){
				continue;
			}
			for (LessonDetails lesson:lessons){
				toReturn += lesson.getMoreInfo();
			}
		}

		toReturn += "Odd:\n";
		for (int i = 7; i < 14; i++){
			List<LessonDetails> lessons = timeTable[i];
			if (lessons == null){
				continue;
			}
			for (LessonDetails lesson:lessons){
				toReturn += lesson.getMoreInfo();
			}
		}
		return toReturn + "\n";
	}

	/**
	 * Retrieves a string of students associated with index
	 * @return String of students registered and waitlisted
	 * Eg.
	 * Registered students:
	 * 1. 
	 * Waitlisted students:
	 * 1. 
	 */
	@Override
	public String getMoreInfo() {
		String toReturn = getInfo() + "\nRegistered students:\n";
		int i = 1;
		if (registeredStudents.size() != 0){
			for (Student s: registeredStudents){
				toReturn += i + ". " + s.getLessInfo() + "\n";
				i++;
			}
		}
		else{
			toReturn += "None\n";
		}

		toReturn += "\nWaitlisted:\n";
		i = 1;
		if (waitlist.size() != 0){
			for (Student s: waitlist){
				toReturn += i + ". " + s.getInfo() + "\n";
				i++;
			}
		}
		else{
			toReturn += "None\n";
		}
		return toReturn + "\n";
	}
}

