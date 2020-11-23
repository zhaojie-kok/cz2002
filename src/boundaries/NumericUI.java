package boundaries;

/** 
 * Another user interface to be implemented by developers
 * To be implemented if numeric inputs are needed from the user
 */
public interface NumericUI {
    /** 
     * Method to get unbounded integer input from user
     * 
     * @return int of value equal to user input
     */
    public abstract int promptIntegerInput();

    /**
     * Method to get bounded integer input from user
     * 
     * @param upperlim Upper limit (inclusive) of values that can be accepted.
     * @param lowerlim Lower limit (inclusive) of values that can be accepted.
     * @return int of value equal to user input
     */
    public abstract int promptIntegerInput(int upperlim, int lowerlim);
}
