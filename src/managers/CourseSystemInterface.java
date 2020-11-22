package managers;

import exceptions.KeyNotFoundException;
import exceptions.MissingSelectionException;

/**
 * Interface to be implemented for systems that handle course/index objects
 */
public interface CourseSystemInterface extends Systems {
    /**
     * Method to select a course to be used in the system
     * @param courseCode course code of the course
     * @throws KeyNotFoundException thrown if the course code does not match any in the system
     */
    public void selectCourse(String courseCode) throws KeyNotFoundException;

    /**
     * Method to select an index to be used in the system
     * {@link #selectCourse(String)} method should always be run first
     * @param indexNo index number of the index
     * @throws KeyNotFoundException Used if no indexes match the given index number can be found for the course
     * @throws MissingSelectionException Used if the course has not yet been selected
     */
    public void selectIndex(String indexNo) throws KeyNotFoundException, MissingSelectionException;
}
