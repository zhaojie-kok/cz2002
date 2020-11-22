package exceptions;

/**
 * Exception for when an invalid input is used for a method
 * E.g: wrong password or invalid value provided
 */
public class InvalidInputException extends Exception {
    private static final long serialVersionUID = 9166406267773204003L;
    private String message;

    /**
     * Constructor
     * 
     * @param message error message to be accessed with {@link #getMessage()} method
     */
    public InvalidInputException(String message) {
        this.message = message;
    }

    /**
     * Method for showing error message
     */
    @Override
    public String getMessage() {
        return this.message;
    }
}
