import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileReader {
    /**
     * Reads and writes files
     */

	private static String dataPath = "data/";
	private static String coursesPath = "courses/";
	private static String studentsPath = "students/";
	private static String staffPath = "staffs/";
	private static String userLoginDetailsPath = "loginDetails/";
    public FileReader(){

    }

    public static void main(String args[]){
        List<Integer> mylist = new ArrayList<Integer>();
        writeSerializedObject(dataPath + "filename", mylist);
	}

	public static String[] getUserDetails(String userId){
        String line = "";
		String tvsSplitBy = "\\t";
		String tsvFile = userLoginDetailsPath + "TSV.tsv";
		BufferedReader br;
		String[] details = null;

        try {
            FileInputStream fis = new FileInputStream(tsvFile);
			InputStreamReader isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
                details = line.split(tvsSplitBy);
                if (details[0] == userId){
					br.close();
					break;
				}
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
		}
		return details;
	}
	
	public static Course[] loadCourses(){
		File folder = new File(coursesPath);
		File[] listOfFiles = folder.listFiles();
		Course[] courses = new Course[listOfFiles.length];

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				courses[i] = cast(readSerializedObject(coursesPath + listOfFiles[i].getName()), Course.class);
			}
		}
		return courses;
	}

	public static Staff[] loadStaff(){
		File folder = new File(coursesPath);
		File[] listOfFiles = folder.listFiles();
		Staff[] staff = new Staff[listOfFiles.length];

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				staff[i] = cast(readSerializedObject(staffPath + listOfFiles[i].getName()), Staff.class);
			}
		}
		return staff;
	}

	public static Student[] loadStudents(){
		File folder = new File(coursesPath);
		File[] listOfFiles = folder.listFiles();
		Student[] students = new Student[listOfFiles.length];

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				students[i] = cast(readSerializedObject(coursesPath + listOfFiles[i].getName()), Student.class);
			}
		}
		return students;
	}

	public static void writeCourse(Course course){
		writeSerializedObject(coursesPath + course.getCourseName(), course);
	}

	public static void writeStaff(Staff staff){
		writeSerializedObject(staffPath + staff.getStaffNo(), staff);
	}
	
	public static void writeStudent(Student student){
		writeSerializedObject(studentsPath + student.getMatricNo(), student);
	}

    private static Object readSerializedObject(String filename){
		Object o = null;
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try {
			fis = new FileInputStream(filename);
			in = new ObjectInputStream(fis);
			o = in.readObject();
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		return o;
	}

	private static void writeSerializedObject(String filename, Object o){
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream(filename, false);
			out = new ObjectOutputStream(fos);
			out.writeObject(o);
			out.close();
		//	System.out.println("Object Persisted");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static <I, O> O cast(I input, Class<O> outClass) {
		try {
			return outClass.cast(input);
		} catch (ClassCastException e) {
			return null;
		}
	}
}