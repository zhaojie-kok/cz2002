package exceptions;

public class FileReadingException extends Exception {
    private String message;
    
    public FileReadingException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
