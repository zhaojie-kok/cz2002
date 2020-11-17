package managers;

import readers.LoginReader;

public class LoginMgr{
    private static LoginReader loginReader;

    public LoginMgr() {
        loginReader = new LoginReader("data/loginDetails/"); // TODO: change to a default file path
    }

    // overloaded for future upgrades, where the filepath will depend on the application design
    public LoginMgr(String loginDetailsFilePath){
        loginReader = new LoginReader(loginDetailsFilePath);
    }

    private String hashPassword(String password){
        return String.valueOf(password.hashCode());
    }

    public int verifyLoginDetails(String userId, String password) {
        /* CODES FOR LoginMgr.verifyLoginDetails:
        2: successful & user is a staff
        1: successful & user is a student
        -1: username not found
        -2: wrong password
        -3: unknown error
        */
        Object data = loginReader.getData(userId);
        
        // TODO: change to exceptions
        if (data == null) {
            return -3;
        } else {
            String[] details = (String[]) data;
            if (details.length == 0) {
                return -1;
            } else if (!hashPassword(password).equals(details[1])) {
                return -2;
            } else {
                // return positive numerics instead of just boolean for success to make it easy to add more classes
                switch(details[2]) {
                    case "student":
                        return 1;
                    case "staff":
                        return 2;
                    default: // the default case would imply an error in the data from the file, meaning an unknown error
                        return -3;
                }
            }
        }
    }
}