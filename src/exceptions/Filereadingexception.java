package exceptions;

/**
 * Exceptions for when there is an error trying to read or write a file storing persistent app data
 */
public class FileReadingException extends Exception {
	private static final long serialVersionUID = -2789031215092909435L;
	private String message;
    
    /**
     * Constructor
     * @param message error message to be accessed with {@link #getMessage()} method
     */
    public FileReadingException(String message) {
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
