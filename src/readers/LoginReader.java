package readers;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;

import exceptions.FileReadingException;

public class LoginReader extends FileReader {
    // login details should be in format userId, password, userType, access period
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
        String[] defaultVal = {"", "", null}; // {hashedPw, usertype, accessperiod}
        Object[] details = allDetails.getOrDefault(userId, defaultVal); // return null of userId cant be found
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
        assert (o.getClass() == Object[].class) : "New login details must be a String[]";
        assert ((Object[]) o).length == 4 : "login details should be in format userId, password, userType, access period";

        // convert into array and check
        Object[] newDetails = (Object[]) o;
        assert (newDetails[3].getClass() == LocalDateTime[].class) : "Access period should be a LocalDateTime array";

        // convert to proper format and names
        String userID = (String) newDetails[0];
        String hashedPassword = hashPassword((String) newDetails[1]);
        String userType = (String) newDetails[2];
        LocalDateTime[] accessPeriod;
        if (userType.equals("student")) {
            accessPeriod = (LocalDateTime[]) newDetails[3];
        } else {
            accessPeriod = null;
        }
        
        File tempFile = new File(filepath);
        // if the file for the user exists
        if (tempFile.exists()){
            try {
                // read the entire file
                HashMap<String, Object[]> allDetails = (HashMap<String, Object[]>) readSerializedObject(filepath);
                // In case allDetails is not created yet/cannot be read
                if (allDetails == null){
                    allDetails = new HashMap<String, Object[]>();
                }
                allDetails.put(userID, new Object[]{hashedPassword, userType, accessPeriod});
                writeSerializedObject(filepath, allDetails);
                return 1;
            } catch (Exception e) {
                // e.printStackTrace();
                throw new FileReadingException("Error in saving login details. Please contact system administrator");
            }
        }
        // if a new file needs to be created
        else{
            try {
                HashMap<String, Object[]> allDetails = new HashMap<String, Object[]>();
                allDetails.put(userID, new Object[]{hashedPassword, userType, accessPeriod});
                writeSerializedObject(filepath, allDetails);
                return 1;
            } catch (Exception e) {
            // e.printStackTrace();
            throw new FileReadingException("Error in saving login details. Please contact system administrator");
            }
        }
    }

    public int updateData(Object[] newDetails, boolean hashed) throws FileReadingException {
        // convert to proper format and names
        String userID = (String) newDetails[0];
        String hashedPassword = (String) newDetails[1];
        if (!hashed) {
            hashedPassword = hashPassword(hashedPassword);
        }
        String userType = (String) newDetails[2];
        LocalDateTime[] accessPeriod;
        if (userType.equals("student")) {
            accessPeriod = (LocalDateTime[]) newDetails[3];
        } else {
            accessPeriod = null;
        }

        File tempFile = new File(filepath);
        if (tempFile.exists()){
            try {
                // read the entire file
                HashMap<String, Object[]> allDetails = (HashMap<String, Object[]>) readSerializedObject(filepath);
                // In case allDetails is not created yet/cannot be read
                if (allDetails == null){
                    allDetails = new HashMap<String, Object[]>();
                }
                allDetails.put(userID, new Object[]{hashedPassword, userType, accessPeriod});
                writeSerializedObject(filepath, allDetails);
                return 1;
            } catch (Exception e) {
                // e.printStackTrace();
                throw new FileReadingException("Error in saving login details. Please contact system administrator");
            }
        }
        // if a new file needs to be created
        else {
            throw new FileReadingException("Login details file must first be created");
        }
    }
}
