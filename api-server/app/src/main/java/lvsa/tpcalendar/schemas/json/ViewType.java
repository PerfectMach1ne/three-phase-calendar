package lvsa.tpcalendar.schemas.json;

public enum ViewType {
    static_task("static_task"),
    historic_task("historic_task"),
    routine_task("routine_task");

    private String viewType;

    public String toString() {
        return viewType;
    }

    ViewType(String viewType) {
        this.viewType = viewType;
    }
}
