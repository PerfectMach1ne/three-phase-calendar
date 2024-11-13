package lvsa.tpcalendar.schemas.json;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class TaskIn {
    private transient int hashcode;
    private String datetime;
    private String name;
    private String desc;
    private ColorObj color;
    private boolean isDone;

    public TaskIn(String datetime, String name, String desc, ColorObj color, boolean isDone) {
        this.datetime = datetime;
        this.name = name;
        this.desc = desc;
        this.color = color;
        this.isDone = isDone;
        
    }

    @Override
    public int hashCode() {
        LocalDateTime ldt = null;
        try {
            ldt = LocalDateTime.parse(this.datetime); 
        } catch (DateTimeParseException dtpe) {
            // Assume this is because of that stupid T letter.
            this.datetime = this.datetime.substring(0, 10) + "T" + this.datetime.substring(11);
            ldt = LocalDateTime.parse(this.datetime); 
            if (ldt == null) {
                dtpe.printStackTrace();
                /* then make it die */
            }
        }
        
        this.hashcode = ((ldt.getYear() + ldt.getMonthValue() + ldt.getDayOfMonth() + ldt.getHour() + ldt.getMinute() + ldt.getSecond())
                          + this.name.hashCode()
                          + this.desc.hashCode()
                          + this.color.hashCode())
                          * (this.isDone ? 1 : -1);

        return this.hashcode;
    }
}
