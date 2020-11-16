package boundaries;

import java.util.Scanner;

public class StudentUI extends GeneralUI {
    private static Scanner scn;

    public StudentUI(Scanner scn) {
        this.scn =  scn;
    }
    @Override
    public void getUserInput(Object storageObject) {
        storageObject = scn.nextLine();
    }

    @Override
    public Object getUserInput() {
        return scn.nextLine();
    }

    @Override
    public void displayOutput(Object toDisplay) {
        System.out.println(toDisplay.toString());
    }    

    public void mainMenuOptions() {
        
    }
}
