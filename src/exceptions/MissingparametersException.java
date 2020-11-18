package exceptions;

public class MissingparametersException extends Exception {
    private String message;

    public MissingparametersException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
