package readers;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

import entities.course_info.Course;
import exceptions.Filereadingexception;

public class CourseReader extends FileReader {
    public CourseReader(String filedir) {
        this.filepath = filedir;
    }

    @Override
    public Object getData(String params) throws Filereadingexception {
        return getData();
    }

    public Object getData() throws Filereadingexception {
        /**
		Returns all courses (HashMap where key is courseCode) if the courseCode is null
        Returns only a specific course if the course can be identified with the courseCode
        Returns null if course cannot be identified
		 */

        // iterate through all files in the folder
		File folder = new File(this.filepath);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null) {
            return new HashMap<>(); // TODO: remove line
            // throw new Filereadingexception("Course Details Folder inaccessible. Please contact system administrator");
        }
		HashMap<String, Course> courses = new HashMap<>();
		Course toAdd;
		for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                try {
                    toAdd = cast(readSerializedObject(this.filepath + listOfFiles[i].getName()), Course.class);
                } catch (Exception e) {
                    throw new Filereadingexception(
                            "Error in retrieving data from " + listOfFiles[i] + " Please contact system administrator");
                }
				if (toAdd != null){
                    courses.put(toAdd.getCourseCode(), toAdd);
				}
			}
        }
        return courses;
    }

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
