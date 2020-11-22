package entities.course_info;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import entities.*;

public class Index implements Serializable, Printable{

	/**
	 *
	 */
	private static final long serialVersionUID = -4814561120331706322L;
	private String indexNo;
	private int slotsAvailable;
	private int slotsTotal;
	private List<Student> registeredStudents; 
	private List<LessonDetails>[] timeTable;
	private LinkedList<Student> waitlist;
	
	public Index(String indexNo,
				int slotsTotal,
				List<LessonDetails>[] timeTable){
		this.indexNo = indexNo;
		this.slotsAvailable = slotsTotal;
		this.slotsTotal = slotsTotal;
		this.registeredStudents = new ArrayList<>(slotsTotal);
		this.waitlist = new LinkedList<Student>();
		this.timeTable = timeTable;
	}
	
	public String getIndexNo() {
		return indexNo;
	}
	
	public void setIndexNo(String indexNo) {
		this.indexNo = indexNo;
	}
	
	public int getSlotsAvailable() {
		return slotsAvailable;
	}
	
	public void setSlotsAvailable(int slotsAvailable) {
		this.slotsAvailable = slotsAvailable;
	}
	
	public int getSlotsTotal() {
		return slotsTotal;
	}
	
	public void setSlotsTotal(int slotsTotal) {
		this.slotsTotal = slotsTotal;
	}
	
	public List<Student> getRegisteredStudents() {
		return registeredStudents;
	}
	
	public void setRegisteredStudents(List<Student> registeredStudents) {
		this.registeredStudents = registeredStudents;
	}
	
	public List<LessonDetails>[] getTimeTable() {
		return timeTable;
	}
	
	public void setTimeTable(List<LessonDetails>[] timeTable) {
		this.timeTable = timeTable;
	}
	
	public void addSlotsAvailable() {
		this.slotsAvailable++;
	}
	
	public void minusSlotsAvailable() {
		this.slotsAvailable--;
	}
	
	public void addSlotsTotal() {
		this.slotsTotal++;
	}
	
	public void minusSlotsTotal() {
		this.slotsTotal--;
	}

	public Student dequeueWaitlist(){
		try{
			return waitlist.remove();
		}
		catch(NoSuchElementException e){
			return null;
		}
	}

	public void enqueueWaitlist(Student student){
		waitlist.add(student);
	}

	public void removeFromWaitList(Student student) {
		waitlist.remove(student);
	}

	@Override
	public String getInfo() {
		/**
		 * Returns a string of information on the index
		 * Eg.
		 * Index: 20001
		 * Slots available: 10 out of 20
		 * LEC        | Mon   | 1130-1330 | LKC-LT
		 * LEC/STUDIO | Tues  | 1430-1630 | LT28
		 * TUT        | Thurs | 0930-1000 | LHN-TR+12
		 */
		String toReturn = String.format("Index: %s\nSlots available: %d out of %d\n", indexNo, slotsAvailable, slotsTotal);
		
		for (int i = 0; i < timeTable.length; i++){
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

	@Override
	public String getMoreInfo() {
		
		/**
		 * Returns a string of students registered/waitlisted
		 * Eg.
		 * Registered students:
		 * 1. 
		 * Waitlisted students:
		 * 1. 
		 */
		String toReturn = getInfo() + "\nRegistered students:\n";
		int i = 1;
		if (registeredStudents.size() != 0){
			for (Student s: registeredStudents){
				toReturn += i + ". " + s.getInfo() + "\n";
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
