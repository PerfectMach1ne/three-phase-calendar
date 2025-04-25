package lvsa.tpcalendar.schemas.json;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class TimeblockIn /* extends TimeblockEvent */ {
    private transient int hashcode;
    private String start_datetime;
    private String end_datetime;
    private String name;
    private String desc;
    private ViewType viewtype;
    private ColorObj color;

    /* Somehow doesn't need to be public, as it never gets directly 
     * instantiated in the API.
     */
    TimeblockIn(int hashcode, String start_datetime, String end_datetime, String name, String desc, ViewType viewtype, ColorObj color) {
        this.start_datetime = start_datetime; 
        this.end_datetime = end_datetime;
        this.name = name;
        this.desc = desc; 
        this.viewtype = viewtype;
        this.color = color;
    }
    
    @Override
    public int hashCode() {
        LocalDateTime start_ldt, end_ldt = null;
        try {
            start_ldt = LocalDateTime.parse(this.start_datetime);
            end_ldt = LocalDateTime.parse(this.end_datetime);
        } catch(DateTimeParseException dtpe) {
            // Assume this is because of that stupid T letter.
            this.start_datetime = this.start_datetime.substring(0, 10) + "T" + this.start_datetime.substring(11);
            this.end_datetime = this.end_datetime.substring(0, 10) + "T" + this.end_datetime.substring(11);
            start_ldt = LocalDateTime.parse(this.start_datetime);
            end_ldt = LocalDateTime.parse(this.end_datetime);
            if (start_ldt == null || end_ldt == null) {
                dtpe.printStackTrace();
                this.hashcode = 0; // In case of nullptr access.
                return this.hashcode;
            }
        }

        this.hashcode = start_ldt.getYear() ^ -end_ldt.getYear()
                      + start_ldt.getMonthValue() ^ -end_ldt.getMonthValue()
                      + start_ldt.getDayOfMonth() ^ -end_ldt.getDayOfMonth()
                      + start_ldt.getHour() ^ -end_ldt.getHour()
                      + start_ldt.getMinute() ^ -end_ldt.getMinute()
                      + start_ldt.getSecond() ^ -end_ldt.getSecond()
                      + this.name.hashCode()
                      + this.desc.hashCode()
                      + this.color.hashCode();

        System.out.println("[DEBUG] TimeblockIn.hashcode == " + this.hashcode);
        return this.hashcode;
    }

    /**
     * An atrocity that's a price to pay for my stupidity; ensure Java's default object hashcode is overriden by running the code in <code>hashCode()</code>.
     * @param timeblock
     */
    public static void initHashCode(TimeblockIn timeblock) {
        timeblock.hashCode();
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
        return desc;
    }

    public ViewType getViewType() {
        return viewtype;
    }

    public ColorObj getColor() {
        return color;
    }
}