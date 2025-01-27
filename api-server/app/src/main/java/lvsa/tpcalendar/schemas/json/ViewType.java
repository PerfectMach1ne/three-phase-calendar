package lvsa.tpcalendar.schemas.json;

public enum ViewType {
    static_task("static_task"),
    historic_task("historic_task"),
    routine_task("routine_task");

    private String viewType;

    public String toString() {
        return viewType;
    }

    public static ViewType toViewType(String str) {
        return str == static_task.toString()   ? static_task : (
               str == historic_task.toString() ? historic_task :
                                                 routine_task
        );
    }

    ViewType(String viewType) {
        this.viewType = viewType;
    }
}
