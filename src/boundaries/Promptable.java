package boundaries;

public abstract class Promptable implements GeneralUI{
    // method to get choices from user
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