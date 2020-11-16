package readers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class FileReader {
    /**
     * Reads and writes files
     */
	protected String filepath;

	public abstract Object getData(String params);

	public abstract int writeData(Serializable o);

    protected static Object readSerializedObject(String filename){
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

	public static <I, O> O cast(I input, Class<O> outClass) {
		try {
			return outClass.cast(input);
		} catch (ClassCastException e) {
			return null;
		}
	}
}