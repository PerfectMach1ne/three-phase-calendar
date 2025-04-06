package lvsa.tpcalendar.schemas.json;

import lvsa.tpcalendar.schemas.json.event.TimeblockEvent;

public class TimeblockOut extends TimeblockEvent {
    public TimeblockOut(int hashcode, String start_datetime, String end_datetime, String name, String desc,
            ViewType viewtype, ColorObj color) {
        super(hashcode, start_datetime, end_datetime, name, desc, viewtype, color);
    }
}