package managers;

import java.io.FileNotFoundException;
import java.io.IOException;

import exceptions.FileReadingException;
import exceptions.InvalidInputException;
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
     * @return         1: validated as student user, 2: validated as staff user, -1: validated details but no information retrieved (only for debugging)
     * @throws FileNotFoundException thrown if userID cannot be mapped to any login details
     * @throws InvalidInputException thrown if password provided is incorrect
     * @throws FileReadingException thrown if files found cannot be accessed
     */
    public int verifyLoginDetails(String userId, String password) throws FileNotFoundException, InvalidInputException,
            FileReadingException {
        Object data = null;
        try {
            data = loginReader.getData(userId);
        } catch (FileNotFoundException f) {
            throw new FileNotFoundException("\n|||||Unknown user id|||||\n");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        if (data == null) {
            throw new FileNotFoundException("\n|||||Unknown user id|||||\n");
        } else {
            String[] details = (String[]) data;
            if (details.length == 0) {
                return -1;
            } else if (!loginReader.hashPassword(password).equals(details[0])) {
                throw new InvalidInputException("Wrong Password");
            } else {
                // return positive numerics instead of just boolean for success to make it easy to add more classes
                switch(details[1]) {
                    case "student":
                        return 1;
                    case "staff":
                        return 2;
                    default: // the default case would imply an error in the data from the file, meaning an unknown error
                        throw new FileReadingException("Unknown error in reading login details. Please contact system administrator");
                }
            }
        }
    }
}