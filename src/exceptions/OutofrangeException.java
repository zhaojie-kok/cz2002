package exceptions;

public class OutOfRangeException extends Exception {
    /**
	 *
	 */
	private static final long serialVersionUID = -2372627810797511854L;
	private String message;

    public OutOfRangeException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
