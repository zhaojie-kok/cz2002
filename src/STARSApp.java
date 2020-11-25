import java.util.Scanner;

import managers.LoginMgr;
import boundaries.*;

/**
 * Main Application for our STARS system
 */
public class STARSApp extends Promptable implements HiddenInputUI {
    private static Scanner scn = new Scanner(System.in);
    private static LoginMgr loginMgr = new LoginMgr();
    private static String userId;

    /**
     * Main method
     */
    public static void main(String[] args) {
        STARSApp app = new STARSApp(); // to allow calling of non-static methods
        app.run();
    }

    /**
     * Method to start up the UI
     * See {@link boundaries.GeneralUI#run()}
     */
    @Override
    public void run() {
        int choice = 0;
        int loginStatus;
        loginStatus = promptLogin();

        while (loginStatus <= 0) {
            String[] options= {"Yes", "No"};
            choice = promptChoice("Would you like to login again?", options);
            if (choice != 0) {
                return;
            } else {
                loginStatus = promptLogin();
            }
        }

        // create and start up a system based on the loginStatus
        // at this point the login details have already been verified to the userId can be used to retrieve info
        GeneralUI newUI  = null;
        try {
            switch(loginStatus) {
                case 1:
                    newUI = new StudentUI(scn, userId);
                    break;
                case 2:
                    newUI = new StaffUI(scn, userId);
                    break;
            }
        } catch (Exception e) {
            // at this point, since the userId has been verified
            // any error in starting either system should be alerted to admin
            e.printStackTrace();
            displayOutput("Unable to find user records in system. Please inform system admin");
            return;
        }

        // show UI for each respective type of user
        newUI.run();
    }

    /**
     * Method to prompt user to login
     * @return login status. See {@link managers.LoginMgr#verifyLoginDetails(String, String)}
     */
    public int promptLogin() {
        displayOutput("Please Enter Username: ");
        userId = scn.nextLine();
        displayOutput("Enter Password: ");
        String password = (String) getHiddenInput();

        int result = 0;
        try {
            result = loginMgr.verifyLoginDetails(userId, password);
        } catch (Exception e) {
            displayOutput(e.getMessage());
            return -1;
        }

        return result;
    }
    
    /**
     * Method to get inputs from user
     */
    @Override
    public String getUserInput() {
        String userInput = scn.nextLine();
        return userInput;
    }

    /**
     * Method to get input from user without displaying input on console
     */
    @Override
    public Object getHiddenInput() {
        String toReturn;
        if (System.console() == null){
            toReturn = scn.nextLine();
        }else{
            char[] passString = System.console().readPassword();
            toReturn = new String(passString );
        }
        return toReturn;
    }

    /**
     * Method to dsplay output to user
     */
    @Override
    public void displayOutput(Object toDisplay) {
        System.out.println(toDisplay.toString());
    }

}