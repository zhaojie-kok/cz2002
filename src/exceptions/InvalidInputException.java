package exceptions;

public class InvalidInputException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 9166406267773204003L;
    private String message;
    public InvalidInputException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
