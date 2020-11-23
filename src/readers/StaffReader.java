package readers;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import entities.Staff;
import exceptions.FileReadingException;

/**
 * Boundary class for handling reading and writing of files relating to Staff entities
 */
public class StaffReader extends FileReader {
    /**
     * Constructor 
     * 
     * @param filedir path to directory storing the files relating to staff entities
     */
    public StaffReader(String filedir) {
        this.filepath = filedir;
    }

    /**
     * Method to get information from all the files in the directory
     * Overloaded version of {@link #getData(String)}
     */
    public Object getData() throws FileReadingException {
        return getData("");
    }

    /**
     * Method to get information from all the files in the directory
     * @param params Unused argument
     * @return HashMap containing information about all staff users in system. Staff number and ID are used as keys
     * @throws FileReadingException thrown if a file cannot be read
     */
    @Override
    public Object getData(String params) throws FileReadingException {
        /*
         * Returns all staff (HashMap where key is matric no of student) if the
         * identifier is null Returns only a specific staff if the staff can be
         * identified with the identifier (staffNo or userId) Returns null if staff
         * cannot be identified
         */

        // iterate through all files in the folder
        File folder = new File(this.filepath);
        File[] listOfFiles = folder.listFiles();
        HashMap<String, Staff> students = new HashMap<>();
        Staff toAdd;

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                try {
                    toAdd = cast(readSerializedObject(this.filepath + listOfFiles[i].getName()), Staff.class);
                } catch (Exception e) {
                    throw new FileReadingException("Error in retrieving data from " + listOfFiles[i] + " Please contact system administrator");
                }
                if (toAdd != null) {
                    // Staff can be identified using userId or with their staffNo, depending on
                    // context
                    // for e.g: the system admin may need their userId, whereas HR may want their
                    // staffNo
                    students.put(toAdd.getStaffNo(), toAdd);
                    students.put(toAdd.getUserId(), toAdd);
                }
            }
        }

        return students;
    }

    /**
     * Method to write a Staff object to its corresponding file
     * @param o Staff object to be written
     * @return  1. if file writing is successful, -1 otherwise
     */
    @Override
    public int writeData(Serializable o) {
        /*
         * CODES FOR StaffReader.writeData: 1: successfully changed -1: unable to
         * read/write changes to file
         */
        Staff updatedStaff = (Staff) o;
        String fp = this.filepath + updatedStaff.getUserId();
        try {
            writeSerializedObject(fp, updatedStaff);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    
}
