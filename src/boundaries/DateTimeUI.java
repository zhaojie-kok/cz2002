package boundaries;

import java.time.LocalDateTime;
import java.time.LocalTime;

/** Another user interface to be implemented by developers
 * To be implemented if date or time inputs are needed from the user 
 */
public interface DateTimeUI {
    /** Method to get a date time input from user
     * @return LocalDateTime object based on user input
     */
    public abstract LocalDateTime getDateInput();

    /** Method to get time input from user
     * @return LocalTime object based on user input
     */
    public abstract LocalTime getTimeInput();
}
