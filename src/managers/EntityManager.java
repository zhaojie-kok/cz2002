package managers;

/**
 * Interface for controllers that handle entity objects directly
 */
public interface EntityManager {
    /**
     * Method to save the state of an entity object
     * @param o entity object to be saved
     */
    public void saveState(Object o);
}