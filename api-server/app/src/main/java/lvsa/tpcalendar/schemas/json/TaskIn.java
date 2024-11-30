package lvsa.tpcalendar.schemas.json;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class TaskIn {
    private transient int hashcode;
    private String datetime;
    private String name;
    private String desc;
    @SuppressWarnings("unused")
    private ViewType viewtype;
    private ColorObj color;
    private boolean isdone;

    public TaskIn(String datetime, String name, String desc, ViewType viewType, ColorObj color, boolean isdone) {
        this.datetime = datetime;
        this.name = name;
        this.desc = desc;
        this.viewtype = viewType;
        this.color = color;
        this.isdone = isdone;
    }

    @Override
    public int hashCode() throws NullPointerException {
        LocalDateTime ldt = null;
        try {
            ldt = LocalDateTime.parse(this.datetime); 
        } catch (DateTimeParseException dtpe) {
            // Assume this is because of that stupid T letter.
            this.datetime = this.datetime.substring(0, 10) + "T" + this.datetime.substring(11);
            ldt = LocalDateTime.parse(this.datetime); 
            if (ldt == null) {
                dtpe.printStackTrace();
                throw new NullPointerException("datetime attribute parsing in TaskIn.hashCode() failed horribly.");
            }
        }
        
        this.hashcode = ((ldt.getYear() + ldt.getMonthValue() + ldt.getDayOfMonth() + ldt.getHour() + ldt.getMinute() + ldt.getSecond())
                          + this.name.hashCode()
                          + this.desc.hashCode()
                          + this.color.hashCode())
                          * (this.isdone ? -1 : 1);

        System.out.println(this.hashcode);
        return this.hashcode;
    }

    /**
     * An atrocity that's a price to pay for my stupidity.
     * @param taskIn
     */
    public static void initHashCode(TaskIn taskIn) {
        taskIn.hashCode();
    }

    public int getHashcode() {
        return hashcode;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public ViewType getViewType() {
        return viewtype;
    }

    public ColorObj getColor() {
        return color;
    }

    public boolean isDone() {
        return isdone;
    }
}
