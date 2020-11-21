package readers;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import exceptions.FileReadingException;

public class LoginReader extends FileReader {
    // login details should be in format userId, password, userType
    public LoginReader(String filepath) {
        this.filepath = filepath;
    }

    public String hashPassword(String password) {
        return "" + String.valueOf(password.hashCode());
    }

    @Override
    public Object getData(String userId) throws ClassNotFoundException, IOException {
        // since the login details are saved hashMaps, the userId can be looked up
        // directly
        HashMap<String, String[]> allDetails;
        allDetails = (HashMap<String, String[]>) readSerializedObject(filepath);
        if (allDetails == null) {
            return null; // return null if no file read
        }
        String[] defaultVal = { "", "" };
        String[] details = allDetails.getOrDefault(userId, defaultVal); // return null of userId cant be found
        return details;
    }

    @Override
    public int writeData(Serializable o) throws FileReadingException {
        /* CODES FOR LoginReader.writeData:
         * 1: successfully changed
         */

        // ensuer object provided is of correct class and format
        // assertion instead of try catch since user should never be able to access this method directly,
        // hence this checking is primarily for testing and debugging
        assert (o.getClass() == String[].class) : "New login details must be a String[]";
        assert ((String[]) o).length == 3 : "Improper format for saving";

        // convert into proper format
        String[] newDetails = (String[]) o;

        
        File tempFile = new File(filepath);
        if (tempFile.exists()){
            try {
                // read the entire file
                HashMap<String, String[]> allDetails = (HashMap<String, String[]>) readSerializedObject(filepath);
                // In case allDetails is not created yet/cannot be read
                if (allDetails == null){
                    allDetails = new HashMap<String, String[]>();
                }
                allDetails.put(newDetails[0], new String[]{hashPassword(newDetails[1]), newDetails[2]});
                writeSerializedObject(filepath, allDetails);
                return 1;
            } catch (Exception e) {
                // e.printStackTrace();
                throw new FileReadingException("Error in saving login details. Please contact system administrator");
            }
        }
        else{
            try {
                HashMap<String, String[]> allDetails = new HashMap<String, String[]>();
                allDetails.put(newDetails[0], new String[]{hashPassword(newDetails[1]), newDetails[2]});
                writeSerializedObject(filepath, allDetails);
                return 1;
            } catch (Exception e) {
            // e.printStackTrace();
            throw new FileReadingException("Error in saving login details. Please contact system administrator");
            }
        }
    }
}
