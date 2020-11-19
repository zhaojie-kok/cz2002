package exceptions;

public class KeyClashException extends Exception{

    /**
     *
     */
    private static final long serialVersionUID = 506311170352870915L;
    String key;
    public KeyClashException(String key){
        this.key = key;
    }

    public String getMessage(){
        return "Key " + key + " already exists in map";
    }
}
