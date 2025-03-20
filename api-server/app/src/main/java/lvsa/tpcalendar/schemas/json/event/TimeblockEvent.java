package lvsa.tpcalendar.schemas.json.event;

import lvsa.tpcalendar.schemas.json.ColorObj;
import lvsa.tpcalendar.schemas.json.ViewType;

public class TimeblockEvent extends BaseEvent {
    protected ColorObj color;

    public TimeblockEvent(int hashcode, String datetime, String name, String desc, ViewType viewtype, ColorObj color) {
        super(hashcode, datetime, name, desc, viewtype);
        this.color = color;
    }
}
