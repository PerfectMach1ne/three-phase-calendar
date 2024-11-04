package lvsa.tpcalendar.schemas.json;

public class ColorObj {
    private boolean hasColor;
    private String hex;

    public ColorObj(boolean hasColor, String hex) {
        this.hasColor = hasColor;
        this.hex = hex;
    }
}
