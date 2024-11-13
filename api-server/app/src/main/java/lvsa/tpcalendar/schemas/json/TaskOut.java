package lvsa.tpcalendar.schemas.json;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class TaskOut {
    private int hashcode;
    private String datetime;
    private String name;
    private String desc;
    private ColorObj color;
    private boolean isDone;

    public TaskOut(int hashcode, String datetime, String name, String desc, ColorObj color, boolean isDone) {
        this.hashcode = hashcode;
        this.datetime = datetime;
        this.name = name;
        this.desc = desc;
        this.color = color;
        this.isDone = isDone;
    }
}
