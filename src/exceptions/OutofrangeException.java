package exceptions;

public class OutofrangeException extends Exception {
    private String message;

    public OutofrangeException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
