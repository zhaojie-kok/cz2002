package managers;

import exceptions.KeyNotFoundException;

/**
 * Interface to be implemented for systems that handle Student objects
 */
public interface StudentSystemInterface extends Systems {
    /**
     * Method to select a student to be used by system
     * @param identifier String identifier for student
     * @throws KeyNotFoundException thrown if student cannot be identified based on identifier
     */
    public void selectStudent(String identifier) throws KeyNotFoundException;
}
