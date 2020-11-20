package exceptions;

public class OutOfRangeException extends Exception {
    private String message;

    public OutOfRangeException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
