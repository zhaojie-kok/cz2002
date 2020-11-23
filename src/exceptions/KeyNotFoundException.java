package exceptions;

/**
 * Exception for when identifier for an object cannot be found
 */
public class KeyNotFoundException extends Exception {
    private static final long serialVersionUID = -7820625580190434595L;
    String message;

    /**
     * Constructor
     * 
     * @param message error message to be accessed with {@link #getMessage()} method
     */
    public KeyNotFoundException(String key){
        this.message = key + " not found";
    }
    
    /**
     * Method for showing error message
     */
    @Override
    public String getMessage(){
        return message;
    }

}