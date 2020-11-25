package boundaries;


/**
 * General interface for UI's meant to be plugged used with the system
 * developers should implement this class as a general purpose UI
 */
public interface GeneralUI {
    /** 
     * the UI should also be able to retrieve and return user response
     * @return User's input
     */
    public abstract Object getUserInput();

    /**
     * all kinds of UI should be able to show output to user
     * 
     * @param toDisplay the object to be displayed through the UI
     */
    public abstract void displayOutput(Object toDisplay);

    /** 
     * Method to start the UI. 
     * Optionally, a shutdown method could also be implemented if necessary
     */
    public abstract void run();

    
}
