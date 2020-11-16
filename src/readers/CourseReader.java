package readers;

import java.io.File;
import java.util.HashMap;

import entities.course_info.Course;

public class CourseReader extends FileReader {
    public CourseReader(String filedir) {
        this.filepath = filedir;
    }

    @Override
    public Object getData(String courseCode) {
        /**
		Returns all courses (HashMap where key is courseCode) if the courseCode is null
        Returns only a specific course if the course can be identified with the courseCode
        Returns null if course cannot be identified
		 */

        // iterate through all files in the folder
		File folder = new File(this.filepath);
		File[] listOfFiles = folder.listFiles();
		HashMap<String, Course> courses = new HashMap<>();
		Course toAdd;
		for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                toAdd = cast(readSerializedObject(this.filepath + listOfFiles[i].getName()), Course.class);
				if (toAdd != null){
                    courses.put(toAdd.getCourseCode(), toAdd);
				}
			}
		}
        
        if (courseCode == null) {
            return courses;
        } else {
            if (courses.containsKey(courseCode)) {
                return courses.get(courseCode);
            } else {
                return null;
            }
        }
    }

    @Override
    public int writeData(Object o) {
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
