package exceptions;

/**
 * Exception for when a parameter or argument that is required is missing or null
 */
public class MissingParametersException extends Exception {
	private static final long serialVersionUID = -7718790102049628478L;
	private String message;

    /**
     * Constructor
     * 
     * @param message error message to be accessed with {@link #getMessage()} method
     */
    public MissingParametersException(String message) {
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
