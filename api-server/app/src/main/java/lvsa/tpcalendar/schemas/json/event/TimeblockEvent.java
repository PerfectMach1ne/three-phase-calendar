package lvsa.tpcalendar.schemas.json.event;

import lvsa.tpcalendar.schemas.json.ViewType;

public class TimeblockEvent extends BaseEvent {
    public TimeblockEvent(int hashcode, String datetime, String name, String desc, ViewType viewtype) {
        super(hashcode, datetime, name, desc, viewtype);
    }
}
