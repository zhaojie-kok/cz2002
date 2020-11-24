package readers;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

import entities.course_info.Course;
import exceptions.FileReadingException;

/**
 * Boundary class meant to handle reading and writing of files relating to Course entities
 */
public class CourseReader extends FileReader {
    /**
     * Constructor
     * 
     * @param filedir directory of the files for course entities
     */
    public CourseReader(String filedir) {
        this.filepath = filedir;
    }

    /**
     * Method to read data from a file.
     * See {@link FileReader#getData()}
     */
    @Override
    public Object getData(String params) throws FileReadingException {
        return getData();
    }

    /**
     * Method to get data from a file
     * Overloaded and refined version of {@link #getData(String)}
     * 
     * @return HashMap containing information of courses read from each course's file 
     * @throws FileReadingException thrown if a file cannot be read
     */
    public Object getData() throws FileReadingException {
        /**
		Returns all courses (HashMap where key is courseCode) if the courseCode is null
        Returns only a specific course if the course can be identified with the courseCode
        Returns null if course cannot be identified
		 */

        // iterate through all files in the folder
		File folder = new File(this.filepath);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null) {
            return new HashMap<>();
        }
		HashMap<String, Course> courses = new HashMap<>();
		Course toAdd;
		for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                try {
                    toAdd = cast(readSerializedObject(this.filepath + listOfFiles[i].getName()), Course.class);
                } catch (Exception e) {
                    throw new FileReadingException(
                            "Error in retrieving data from " + listOfFiles[i] + " Please contact system administrator");
                }
				if (toAdd != null){
                    courses.put(toAdd.getCourseCode(), toAdd);
				}
			}
        }
        return courses;
    }

    /**
     * Method to update information about a course
     * 
     * @return 1 if course information was written to file successfully, -1 otherwise
     */
    @Override
    public int writeData(Serializable o) {
        /* CODES FOR CourseReader.writeData:
        1: successfully changed
        -1: unable to read/write changes to file */
        Course updatedCourse = (Course) o;
        String fp = this.filepath + updatedCourse.getCourseCode();
        try {
            writeSerializedObject(fp, updatedCourse);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
