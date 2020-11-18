package exceptions;

public class IntegerOutOfRangeException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    int min;
    int max;
    int input;
    public IntegerOutOfRangeException(int min, int max, int input){
        this.min = min;
        this.max = max;
        this.input = input;
    }

    public String toString(){
        return "IntegerOutOfRange: Input value " + input + 
        " exceeds range (" + min + ", " + max + ")\n";
    }

}