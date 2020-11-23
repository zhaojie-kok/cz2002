package entities;

/**
 * Entity interface for entities that have information meant to be shown to the user
 */
public interface Printable {
    /** 
     * Method to retrieve an even shorter version of getInfo, for use in long lists 
     * @return A string containing the information
     */
    public String getLessInfo();
    /**
     * Method to retrieve basic or most commonly used information about the entity
     * @return A string containing the information
     */
    public String getInfo();

    /**
     * Method to retrieve detailed or rarely used information about the entity
     * @return A string containing the information
     */
    public String getMoreInfo();
}
