package exceptions;

/**
 * Exception for when an identifier meant to be unique is repeated or duplicated
 */
public class KeyClashException extends Exception{
    private static final long serialVersionUID = 506311170352870915L;
    String key;

    /**
     * Constructor
     * 
     * @param key name of key in error message to be accessed with {@link #getMessage()} method
     */
    public KeyClashException(String key){
        this.key = key;
    }

    /**
     * Method for showing error message
     */
    @Override
    public String getMessage(){
        return key + " already exists";
    }
}
