import java.util.Scanner;

import managers.Systems;
import managers.StaffSystem;
import managers.StudentSystem;
import managers.LoginMgr;
import boundaries.GeneralUI;

public class STARSApp implements GeneralUI{
    private static Scanner scn = new Scanner(System.in);
    private static LoginMgr loginMgr = new LoginMgr();
    private static String userId;

    public static void main(String[] args) {
        int loginStatus;
        int choice = 0;
        STARSApp app = new STARSApp(); // to allow calling of non-static methods
        loginStatus = app.promptLogin();

        while (loginStatus <= 0) {
            String[] options= {"Yes", "No"};
            choice = app.promptChoice("Would you like to login again?", options);
            if (choice != 0) {
                return;
            } else {
                loginStatus = app.promptLogin();
            }
        }

        // create and start up a system based on the loginStatus
        // at this point the login details have already been verified to the userId can be used to retrieve info
        Systems system;
        switch(loginStatus) {
            case 1:
                system = new StudentSystem(STARSApp.userId);
        }
    }

    @Override
    public int promptLogin() {
        displayOutput("Please Enter Username: ");
        userId = scn.nextLine();
        displayOutput("Enter Password: ");
        String password = scn.nextLine();

        int result = loginMgr.verifyLoginDetails(userId, password);
        switch(result) {
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

    // method to get choices from user
    public int promptChoice(String prompt, String[] options) {
        displayOutput(prompt);

        for (int i=0; i<options.length; i++) {
            String option = i+1 + ". " + options[i];
            displayOutput(option);
        }

        int choice;
        do {
            try {
                choice = Integer.parseInt(getUserInput());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                choice = -1;
            }

            if (!(choice>=1 && choice<=options.length)) {
                displayOutput("Please make a choice between 1 and " + options.length);
            } else {
                return choice-1;
            }
        } while (true);
        
    }
}