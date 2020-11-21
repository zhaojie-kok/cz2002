package managers;

import exceptions.KeyNotFoundException;

public interface StudentSystemInterface extends Systems {
    public void selectStudent(String identifier) throws KeyNotFoundException;
}
