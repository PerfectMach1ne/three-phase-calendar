package lvsa.tpcalendar.schemas.json;

public class TimeblockIn /* extends TimeblockEvent */ {
    private transient int hashcode;
    private String start_datetime;
    private String end_datetime;
    private String name;
    private String description;
    private ViewType viewtype;
    private ColorObj color;

    /* Somehow doesn't need to be public, as it never gets directly 
     * instantiated in the API.
     */
    TimeblockIn(int hashcode, String start_datetime, String end_datetime, String name, String description, ViewType viewtype, ColorObj color) {
        this.start_datetime = start_datetime; 
        this.end_datetime = end_datetime;
        this.name = name;
        this.description = description; 
        this.viewtype = viewtype;
        this.color = color;
    }
    
    @Override
    public int hashCode() {
        return 0;
    }

    /**
     * An atrocity that's a price to pay for my stupidity; ensure Java's default object hashcode is overriden by running the code in <code>hashCode()</code>.
     * @param taskIn
     */
    public static void initHashCode(TaskIn taskIn) {
        taskIn.hashCode();
    }

    public int getHashcode() {
        return hashcode;
    }

    public String getStartDatetime() {
        return start_datetime;
    }

    public String getEndDatetime() {
        return end_datetime;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return description;
    }

    public ViewType getViewType() {
        return viewtype;
    }

    public ColorObj getColor() {
        return color;
    }
}