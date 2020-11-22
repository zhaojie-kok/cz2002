package boundaries;

import java.time.LocalDateTime;
import java.time.LocalTime;

public interface DateTimeUI {
    public abstract LocalDateTime getDateInput();

    public abstract LocalTime getTimeInput();
}
