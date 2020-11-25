package managers;

import entities.Student;
import entities.course_info.Course;
import entities.course_info.Index;
import exceptions.FileReadingException;

/**
 * Abstracted system to be extended by concrete systems Meant to support Systems
 * interface by allowing for non-final attributes
 */
public class AbstractSystem {
    // managers that will be needed by all types of systems
    // Since all systems in STARS need to handle students, courses, and scheduling
    // (Calendar)
    protected CourseMgr courseMgr;
    protected CalendarMgr calendarMgr;
    protected StudentManager studentManager;

    // Entities that all systems in STARS need to work with
    protected Student selectedStudent = null;
    protected Course selectedCourse = null;
    protected Index selectedIndex = null;

    public AbstractSystem() throws FileReadingException {
        calendarMgr = new CalendarMgr();
        studentManager = new StudentManager();
        courseMgr = new CourseMgr();
    }
}
