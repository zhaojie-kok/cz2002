package exceptions;

public class MissingSelectionException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = -7358761558962669134L;
    String message;
    public MissingSelectionException(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

}