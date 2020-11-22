package boundaries;

/**
 * Parent class for UI types that need to get a choice from user
 */
public abstract class Promptable implements GeneralUI{
    /**
     * Method to get the choice from the user based on a list of options
     * 
     * @param prompt String displayed to user to prompt user to choose
     * @param options Options presented to user for user to choose from
     * @return User's choice (corresponding to index of choice in options)
     */
    public int promptChoice(String prompt, Object[] options) {
        displayOutput(prompt);

        for (int i=0; i<options.length; i++) {
            String option = i+1 + ". " + options[i].toString();
            displayOutput(option);
        }

        int choice;
        do {
            try {
                choice = Integer.parseInt((String) getUserInput());
            } catch (NumberFormatException e) {
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