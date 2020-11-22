package readers;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

import entities.Student;
import exceptions.FileReadingException;

public class StudentReader extends FileReader {
    // student files are named by their user id inside a folder
    public StudentReader(String filedir) {
        this.filepath = filedir;
    }

    @Override
    public Object getData(String params) throws FileReadingException {
        return getData();
    }

    public Object getData() throws FileReadingException {
        /*
        Returns all students (HashMap where key is matric no of student) if the identifier is null
        Returns only a specific student if the student can be identified with the identifier (matricNo or userId)
        Returns null if student cannot be identified
		 */

        // iterate through all files in the folder
		File folder = new File(this.filepath);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null) {
            return new HashMap<>(); // TODO: remove line
            // throw new FileReadingException("Student Details Folder inaccessible. Please contact system administrator");
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
