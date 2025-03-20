package lvsa.tpcalendar.schemas.json.event;

import lvsa.tpcalendar.schemas.json.ViewType;

public class BaseEvent {
    protected int hashcode;
    protected transient String datetime;
    protected String name;
    protected String desc;
    protected ViewType viewtype;

    public BaseEvent(int hashcode, String datetime, String name, String desc, ViewType viewtype) {
        this.hashcode = hashcode;
        this.datetime = datetime;
        this.name = name;
        this.desc = desc;
        this.viewtype = viewtype;
    }
}
