package managers;

/**
 * Interface to be implemented for systems that handle user actions from UI and the entity controllers
 */
public interface Systems {
    /**
     * Method to query the state of the system
     * @return String describing state of system
     */
    public String getSystemStatus();

    /**
     * Method to clear any selections made by the system
     */
    public void clearSelections();
}
