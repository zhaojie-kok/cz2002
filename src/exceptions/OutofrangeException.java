package exceptions;

/**
 * Exception when input provided is out of some acceptable range
 */
public class OutOfRangeException extends Exception {
	private static final long serialVersionUID = -2372627810797511854L;
	private String message;

    /**
     * Constructor
     * 
     * @param message error message to be accessed with {@link #getMessage()} method
     */
    public OutOfRangeException(String message) {
        this.message = message;
    }

    /**
     * Method for showing error message
     */
    @Override
    public String getMessage() {
        return message;
    }
}
