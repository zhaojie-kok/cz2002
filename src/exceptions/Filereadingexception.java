package exceptions;

public class Filereadingexception extends Exception {
    private String message;
    
    public Filereadingexception(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
