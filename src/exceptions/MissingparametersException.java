package exceptions;

public class MissingParametersException extends Exception {
    /**
	 *
	 */
	private static final long serialVersionUID = -7718790102049628478L;
	private String message;

    public MissingParametersException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
