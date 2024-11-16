package lvsa.tpcalendar.schemas.json;

@SuppressWarnings("unused")
public class TaskOut {
    private int hashcode;
    private String datetime;
    private String name;
    private String desc;
    private ColorObj color;
    private boolean isdone;

    public TaskOut(int hashcode, String datetime, String name, String desc, ColorObj color, boolean isdone) {
        this.hashcode = hashcode;
        this.datetime = datetime;
        this.name = name;
        this.desc = desc;
        this.color = color;
        this.isdone = isdone;
    }
}
