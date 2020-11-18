package exceptions;

public class KeyNotFoundException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = -7820625580190434595L;
    String key;
    public KeyNotFoundException(String key){
        this.key = key;
    }

    public String toString(){
        return "Key " + key + " does not exist in map";
    }

}