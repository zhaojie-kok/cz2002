import java.util.Scanner;

import managers.Systems;
import managers.StaffSystem;
import managers.StudentSystem;
import managers.LoginMgr;
import boundaries.*;

public class STARSApp extends GeneralUI {
    private static Scanner scn = new Scanner(System.in);
    private static LoginMgr loginMgr = new LoginMgr();
    private static String userId;
    private int loginStatus;

    public static void main(String[] args) {
        STARSApp app = new STARSApp(); // to allow calling of non-static methods
        
        app.run();
    }

    @Override
    public void run() {
        int choice = 0;
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
            System.out.println("Unable to find user records in system. Please inform system admin");
            return;
        }

        // show the choices of what to do according to each system
        // TODO: throw these into each system later on
        newUI.run();

    }

    public int promptLogin() {
        displayOutput("Please Enter Username: ");
        userId = scn.nextLine();
        displayOutput("Enter Password: ");
        String password = scn.nextLine();

        int result = loginMgr.verifyLoginDetails(userId, password);
        switch(result) {
            // TODO: change to try catch
            case -1:
                displayOutput("Username not found");
                break;
            case -2:
                displayOutput("Wrong password");
                break;
            case -3:
                displayOutput("System error, please contact administrator");
                break;
        }

        return result;
    }

    // use this method to get user inputs and store in specific object
	@Override
	public void getUserInput(Object storageObject) {
        String userInput = scn.nextLine();
        storageObject = userInput;		
	}
    
    // use this method to get and return inputs from the user
    @Override
    public String getUserInput() {
        String userInput = scn.nextLine();
        return userInput;
    }

    // use this method to display output/prompt to user
    @Override
    public void displayOutput(Object toDisplay) {
        System.out.println(toDisplay.toString());
    }

}