/*
 * Shoutout to a real one:
 * https://github.com/PerfectMach1ne/Jaccal/blob/main/src/main/java/util/Colors.java
 */

package lvsa.tpcalendar.util;

public enum Colors {
    PINK("#f291eb"), RED("#fa3e3d"), ORANGE("#f58547"), YELLOW("#fde668"),
    LIGHT_GREEN("#b8fd68"), GREEN("#41a630"), LIGHT_BLUE("#92fff6"), BLUE("#41b3ff"),
    DARK_BLUE("#2e63cd"), LAVENDER("#c493d3"), PURPLE("#8c48ff"), MAGENTA("#ff34ef"),
    WHITE("#ffffff"), LIGHT_GRAY("#cdcdcd"), GRAY("#818081"), BLACK("#404040");

    private String hexColor;

    Colors(String hexColor) {
        this.hexColor = hexColor;
    }

    public String getHexColor() {
        return hexColor;
    }

    public static Colors getColorFromHex(String color) {
        switch (color) {
            case "#f291eb":
                return PINK;
            case "#fa3e3d":
                return RED;
            case "#f58547":
                return ORANGE;
            case "#fde668":
                return YELLOW;
            case "#b8fd68":
                return LIGHT_GREEN;
            case "#41a630":
                return GREEN;
            case "#92fff6":
                return LIGHT_BLUE;
            case "#41b3ff":
                return BLUE;
            case "#2e63cd":
                return DARK_BLUE;
            case "#c493d3":
                return LAVENDER;
            case "#8c48ff":
                return PURPLE;
            case "#ff34ef":
                return MAGENTA;
            case "#ffffff":
                return WHITE;
            case "#cdcdcd":
                return LIGHT_GRAY;
            case "#818081":
                return GRAY;
            case "#404040":
                return BLACK;
            default:
        }
        return WHITE;
    }
        
    public static String getPrettyNameFromColor(Colors colors) {
        switch (colors) {
            case PINK:
                return "Pink";
            case RED:
                return "Red";
            case ORANGE:
                return "Orange";
            case YELLOW:
                return "Yellow";
            case LIGHT_GREEN:
                return "Light green";
            case LIGHT_BLUE:
                return "Light blue";
            case BLUE:
                return "Blue";
            case LAVENDER:
                return "Lavender";
            case WHITE:
                return "White";
            case LIGHT_GRAY:
                return "Light gray";
            case MAGENTA:
                return "Magenta";
            case GREEN:
                return "Green";
            case DARK_BLUE:
                return "Dark blue";
            case PURPLE:
                return "Purple";
            case GRAY:
                return "Gray";
            case BLACK:
                return "Black";
            default:
        }
        return "Color wizard: IDK";
    }
}
