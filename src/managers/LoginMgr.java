package managers;

import java.io.FileNotFoundException;
import java.io.IOException;

import exceptions.FileReadingException;
import exceptions.InvalidInputException;
import readers.LoginReader;

public class LoginMgr {
    private static LoginReader loginReader;

    public LoginMgr() {
        loginReader = new LoginReader("data/loginDetails/");
    }

    // overloaded for future upgrades, where the filepath will depend on the
    // application design
    public LoginMgr(String loginDetailsFilePath) {
        loginReader = new LoginReader(loginDetailsFilePath);
    }

    public int verifyLoginDetails(String userId, String password) throws FileNotFoundException, InvalidInputException,
            FileReadingException {
        /*
         * CODES FOR LoginMgr.verifyLoginDetails: 2: successful & user is a staff 1:
         * successful & user is a student -1: username not found -2: wrong password -3:
         * unknown error
         */
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