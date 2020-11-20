package managers;

import exceptions.KeyNotFoundException;
import exceptions.MissingSelectionException;

public interface CourseSystemInterface extends Systems {
    
    public void selectCourse(String courseCode) throws KeyNotFoundException;

    public void selectIndex(String indexNo) throws KeyNotFoundException, MissingSelectionException;
}
