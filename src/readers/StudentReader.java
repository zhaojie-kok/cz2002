package readers;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

import entities.Student;
import exceptions.FileReadingException;

/**
 * Boundary class for handling reading and writing of files relating to Staff entities
 */
public class StudentReader extends FileReader {
    /**
     * Construcutor
     * 
     * @param filedir path to directory storing the files relating to staff entities
     */
    public StudentReader(String filedir) {
        this.filepath = filedir;
    }

    /**
     * Method to get information from all the files in the directory
     * @param params Unused argument
     * @return HashMap containing information about all student users in system. Matriculation number and ID are used as keys
     */
    @Override
    public Object getData(String params) throws FileReadingException {
        return getData();
    }

    /**
     * Method to get information from all the files in the directory
     * Overloaded version of {@link #getData(String)}
     * 
     * @return HashMap containing information about all student users in system. Matriculation number and ID are used as keys
     * @throws FileReadingException thrown if a file cannot be read
     */
    public Object getData() throws FileReadingException {
        // iterate through all files in the folder
		File folder = new File(this.filepath);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null) {
            return new HashMap<>();
        }
		HashMap<String, Student> students = new HashMap<>();
		Student toAdd;

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
                try {
                    toAdd = cast(readSerializedObject(this.filepath + listOfFiles[i].getName()), Student.class);
                } catch (Exception e) {
                    throw new FileReadingException(
                            "Error in retrieving data from " + listOfFiles[i] + " Please contact system administrator");
                }
				if (toAdd != null){
                    // students can be identified using userId or with their matricNo, depending on context
                    // e.g: profs may identify by matric no, while admin staff may identify by userId
                    students.put(toAdd.getMatricNo(), toAdd);
                    students.put(toAdd.getUserId(), toAdd);
				}
			}
        }
        return students;
    }

    /**
     * Method to write a Student object to its corresponding file
     * @param o Student object to be written
     * @return  1. if file writing is successful, -1 otherwise
     */
    @Override
    public int writeData(Serializable o) {
        /* CODES FOR StudentReader.writeData:
        1: successfully changed
        -1: unable to read/write changes to file */
        Student updatedStudent = (Student) o;
        String fp = this.filepath + updatedStudent.getUserId();
        try {
            writeSerializedObject(fp, updatedStudent);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    
}
