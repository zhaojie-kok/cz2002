package exceptions;

public class FileReadingException extends Exception {
    /**
	 *
	 */
	private static final long serialVersionUID = -2789031215092909435L;
	private String message;
    
    public FileReadingException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
