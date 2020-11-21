package exceptions;

public class KeyNotFoundException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = -7820625580190434595L;
    String message;
    public KeyNotFoundException(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

}