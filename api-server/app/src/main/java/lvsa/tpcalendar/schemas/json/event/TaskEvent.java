package lvsa.tpcalendar.schemas.json.event;

import lvsa.tpcalendar.schemas.json.ColorObj;
import lvsa.tpcalendar.schemas.json.ViewType;

public class TaskEvent extends BaseEvent {
    protected ColorObj color;
    protected boolean isdone;
    protected String datetime;

    public TaskEvent(int hashcode, String datetime, String name, String desc, ViewType viewtype, ColorObj color, boolean isdone) {
        super(hashcode, datetime, name, desc, viewtype);
        this.isdone = isdone;
        this.color = color;
        this.datetime = datetime;
    }
}
