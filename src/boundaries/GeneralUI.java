package boundaries;

public abstract class GeneralUI {
    // a generalised UI interface so that other kinds of UI can be created from this

    // all kinds of UI should be able to prompt user for input
    // and store it in some object
    public abstract void getUserInput(Object storageObject);

    // alternatively, the UI should also be able to retrieve and return user response
    public abstract Object getUserInput();

    // all kinds of UI should be able to show output to user
    public abstract void displayOutput(Object toDisplay);

    // method to run the UI
    public abstract void run();

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
                choice = Integer.parseInt((String) getUserInput());
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
