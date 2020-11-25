package managers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;

import entities.User;
import exceptions.FileReadingException;
import exceptions.InvalidInputException;
import exceptions.KeyNotFoundException;
import exceptions.OutOfRangeException;
import readers.LoginReader;

/**
 * Controller for handling login related methods
 */
public class LoginMgr {
    private static LoginReader loginReader;

    /**
     * Constructor
     */
    public LoginMgr() {
        loginReader = new LoginReader("data/loginDetails/");
    }

    /**
     * Alternative constructor to allow for possibility of storing login details in seperate locations
     * @param loginDetailsFilePath file/folder path to login details
     */
    public LoginMgr(String loginDetailsFilePath) {
        loginReader = new LoginReader(loginDetailsFilePath);
    }

    /**
     * Method to verify the login details provided
     * 
     * @param userId   userID to be verified
     * @param password password to be verified
     * @return 1: validated as student user, 2: validated as staff user, -1:
     *         validated details but no information retrieved (only for debugging)
     * @throws FileNotFoundException thrown if userID cannot be mapped to any login
     *                               details
     * @throws InvalidInputException thrown if password provided is incorrect
     * @throws FileReadingException  thrown if files found cannot be accessed
     * @throws KeyNotFoundException  thrown if user id cannot be found in system
     * @throws OutOfRangeException   thrown if logging in outside of access period
     */
    public int verifyLoginDetails(String userId, String password) throws FileNotFoundException, InvalidInputException,
            FileReadingException, KeyNotFoundException, OutOfRangeException {
        Object rawData = null;
        try {
            rawData = loginReader.getData(userId);
        } catch (ClassNotFoundException | IOException | KeyNotFoundException f) {
            throw new KeyNotFoundException("User ID " + userId);
        }
        
        if (rawData == null) {
            throw new FileNotFoundException("User ID " + userId);
        } else {
            Object[] data = (Object []) rawData;
            String hashedPassword = (String) data[0];
            String userType = (String) data[1];
            LocalDateTime[] accessPeriod = (LocalDateTime[]) data[2];
            if (data.length == 0) {
                return -1;
            } else if (password == null || !loginReader.hashPassword(password).equals(hashedPassword)) {
                throw new InvalidInputException("Wrong Password");
            } else {
                // return positive numerics instead of just boolean for success to make it easy to add more classes
                switch(userType) {
                    case "student":
                        LocalDateTime now = LocalDateTime.now();
                        if (now.isBefore(accessPeriod[0])) {
                            throw new OutOfRangeException("You can only login after " + accessPeriod[0].toString());
                        }
                        if (now.isAfter(accessPeriod[1])) {
                            throw new OutOfRangeException("You can only login before " + accessPeriod[1].toString());
                        }
                        return 1;
                    case "staff":
                        // for staff no need to validate access period
                        return 2;
                    default: // the default case would imply an error in the data from the file, meaning an unknown error
                        throw new FileReadingException("Unknown error in reading login details. Please contact system administrator");
                }
            }
        }
    }

    public void createNewLoginDetails(Object[] details) throws FileReadingException {
        loginReader.writeData(details);
    }

    /**
     * Method to allow changing of access period
     * 
     * @param user            User to be updated
     * @param newAccessPeriod New access period
     * @throws FileReadingException   Thrown if file cannot be accessed
     * @throws ClassNotFoundException Thrown if wrong/invalid class found in file
     * @throws IOException			  Thrown if error resulted during file access
     * @throws KeyNotFoundException	  Thrown if file cannot be found
     */
    public void updateAccessPeriod(User user, LocalDateTime[] newAccessPeriod)
            throws ClassNotFoundException, IOException, FileReadingException, KeyNotFoundException {
        Object[] newDetails = new Object[4];
        newDetails[0] = user.getUserId();
        Object[] oldDetails = (Object[]) loginReader.getData(user.getUserId());
        newDetails[1] = (String) oldDetails[0];
        newDetails[2] = user.getUserType();
        newDetails[3] = newAccessPeriod;

        loginReader.updateData(newDetails, true);
    }
}