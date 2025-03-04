package lvsa.tpcalendar.schemas.json;

import lvsa.tpcalendar.schemas.json.event.TaskEvent;

public class TaskOut extends TaskEvent {
    public TaskOut(int hashcode, String datetime, String name, String desc,
            ViewType viewtype, ColorObj color, boolean isdone) {
        super(hashcode, datetime, name, desc, viewtype, color, isdone);
    }
}
