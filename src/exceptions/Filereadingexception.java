package exceptions;

public class Filereadingexception extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 5350034780950150931L;
    private String message;
    
    public Filereadingexception(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
