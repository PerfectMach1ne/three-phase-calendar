package lvsa.tpcalendar.schemas.json.event;

import lvsa.tpcalendar.schemas.json.ColorObj;
import lvsa.tpcalendar.schemas.json.ViewType;

public class TimeblockEvent extends BaseEvent {
    protected ColorObj color;
    protected String start_datetime;
    protected String end_datetime;

    public TimeblockEvent(int hashcode, String start_datetime, String end_datetime, String name, String desc, ViewType viewtype, ColorObj color) {
        super(hashcode, null, name, desc, viewtype);
        this.color = color;
        this.start_datetime = start_datetime;
        this.end_datetime = end_datetime;
    }
}
