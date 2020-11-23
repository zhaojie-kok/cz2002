package exceptions;

/**
 * Exception for when a selection which is required has not yet been performed
 */
public class MissingSelectionException extends Exception {
    private static final long serialVersionUID = -7358761558962669134L;
    String message;
    
    /**
     * Constructor
     * 
     * @param message error message to be accessed with {@link #getMessage()} method
     */
    public MissingSelectionException(String message){
        this.message = message;
    }

    /**
     * Method for showing error message
     */
    @Override
    public String getMessage(){
        return message;
    }

}