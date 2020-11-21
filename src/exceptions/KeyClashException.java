package exceptions;

public class KeyClashException extends Exception{

    /**
     *
     */
    private static final long serialVersionUID = 506311170352870915L;
    private String message;

    public KeyClashException(String message) {
        this.message = message;
    }
}
