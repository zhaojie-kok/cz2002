import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

	public static String getHashedPassword(String userId){
        String line = "";
        String cvsSplitBy = ",";

        try {
            FileInputStream fis = new FileInputStream(csvFile);
            myInput = new DataInputStream(fis);
			while ((thisLine = myInput.readLine()) != null) {
                String strar[] = thisLine.split(",");
                for (int j = 0; j < strar.length; j++) {
                    if 
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}

    private static List readSerializedObject(String filename){
		List pDetails = null;
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try {
			fis = new FileInputStream(filename);
			in = new ObjectInputStream(fis);
			pDetails = (ArrayList) in.readObject();
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		return pDetails;
	}

	public static void writeSerializedObject(String filename, List list){
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream(filename);
			out = new ObjectOutputStream(fos);
			out.writeObject(list);
			out.close();
		//	System.out.println("Object Persisted");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

    public String getPassword(String userId){
        return "";
    }
    public String getInfo(String userId){
        return "";
    }
    public String getCourseInfo(String courseCode){
        return "";
    }
    public boolean changeUserDetails(String[] details){
        return true;
    }
}