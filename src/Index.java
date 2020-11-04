import java.io.Serializable;
import java.util.List;

public class Index implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = -4814561120331706322L;
	private int indexNo;
	private int slotsAvailable;
	private int slotsTotal;
	private List<Student> registeredStudents; 
	private List<LessonDetails> timeTable;
	
	public int getIndexNo()
	{
		return indexNo;
	}
	
	public void setIndexNo(int indexNo)
	{
		this.indexNo = indexNo;
	}
	
	public int getSlotsAvailable()
	{
		return slotsAvailable;
	}
	
	public void setSlotsAvailable(int slotsAvailable)
	{
		this.slotsAvailable = slotsAvailable;
	}
	
	public int getSlotsTotal()
	{
		return slotsTotal;
	}
	
	public void setSlotsTotal(int slotsTotal)
	{
		this.slotsTotal = slotsTotal;
	}
	
	public List<Student> getRegisteredStudents()
	{
		return registeredStudents;
	}
	
	public void setRegisteredStudents(List<Student> registeredStudents)
	{
		this.registeredStudents = registeredStudents;
	}
	
	public List<LessonDetails> getTimeTable()
	{
		return timeTable;
	}
	
	public void setTimeTable(List<LessonDetails> timeTable)
	{
		this.timeTable = timeTable;
	}
	
	public void addSlotsAvailable()
	{
		this.slotsAvailable++;
	}
	
	public void minusSlotsAvailable()
	{
		this.slotsAvailable--;
	}
	
	public void addSlotsTotal()
	{
		this.slotsTotal++;
	}
	
	public void minusSlotsTotal()
	{
		this.slotsTotal--;
	}
}

