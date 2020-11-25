package boundaries;

/**
 * Interface to be implemented if a hidden field is required from the user. 
 * E.g: passwords
 */
public interface HiddenInputUI {
    /**
     * Method for user to provide input without input without input being shown
     * @return The user's input
     */
    public Object getHiddenInput();
}
