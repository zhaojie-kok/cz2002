package readers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Boundary abstract class too be realised by Boundary classes involved in 
 * reading and writing of files
 */
public abstract class FileReader {
	protected String filepath;

	/**
	 * Method to retrieve information from a file
	 * 
	 * @param params additional parameters for reading the file
	 * @return 		 Object type matching file data
	 * @throws Exception thrown depending on specific file type. Refer to concrete implementations for more details
	 */
	public abstract Object getData(String params) throws Exception;

	/**
	 * Method to write data to a source
	 * 
	 * @param o Serializable object to be written
	 * @return  int denoting status of writing to file. Refer to concrete implementations for more details
	 * @throws Exception thrown depending on specific file type. Refer to concrete implementations for more details
	 */
	public abstract int writeData(Serializable o) throws Exception;

	/**
	 * Method to delete the file storing data of an object from the system
	 * 
	 * @param filename name of the file, excluding directory path
	 * @return 		   true if deletion was successful, false otherwise
	 */
	public boolean deleteObject(String filename){
		File file = new File(filepath + filename);
		return file.delete();
	}

	/**
	 * Method to read a Serializable object from a file
	 * 
	 * @param filename name of the file, excluding directory path
	 * @return		   Serializable object read from the file
	 * @throws IOException			  Thrown if file could not be read
	 * @throws ClassNotFoundException Thrown if object cannot be found in file
	 */
    public static Object readSerializedObject(String filename) throws IOException, ClassNotFoundException {
		Object o = null;
		FileInputStream fis = null;
		ObjectInputStream in = null;
		
		fis = new FileInputStream(filename);
		in = new ObjectInputStream(fis);
		o = in.readObject();
		in.close();
		
		return o;
	}

	/**
	 * Method to write a Serializable object to a file
	 * 
	 * @param filename name of the file, excluding directory path
	 * @param o		   Serializable object to be written
	 */
	protected static void writeSerializedObject(String filename, Serializable o){
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

	/**
	 * Method to cast a an input to a desired class
	 * 
	 * @param input	   input
	 * @param outClass Desired class
	 * @return 		   input casted to the desired class, null if casting is not possible
	 */
	public static <I, O> O cast(I input, Class<O> outClass) {
		try {
			return outClass.cast(input);
		} catch (ClassCastException e) {
			return null;
		}
	}
}