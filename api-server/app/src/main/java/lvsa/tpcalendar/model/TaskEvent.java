package lvsa.tpcalendar.model;

import java.time.LocalDateTime;

import lvsa.tpcalendar.colors.Colors;

public class TaskEvent implements Event {

    private String taskName = "Unnamed task"; 
    private String taskDescription = "";
    private LocalDateTime updatedDate = createdDate;
    private boolean isDone;
    private boolean hasColor;
    private Colors color;

    /*
     * BEGIN GETTER HELL
     */
    @Override
    public String getName() {
        return taskName;
    }

    @Override
    public String getDescription() {
        return this.taskDescription;
    }

    @Override
    public String getCreatedDate() {
        return createdDate.toString();
    }

    @Override
    public String getUpdatedDate() {
        return updatedDate.toString();
    }

    public String getColor() {
        return color.getHexColor();
    }
    /*
     * END GETTER HELL
     */

     /*
      * BEGIN SETTER HELL
      */
    @Override
    public void setName(String name) {
        this.taskName = name;
        update((byte)1);
    }

    @Override
    public void setDescription(String desc) {
        this.taskDescription = desc;
    }

    public void setColor(String hexColor) {
        this.color = Colors.getColorFromHex(hexColor);
    }

    public void setColor(Colors color) { 
        this.color = color;
    }
    /*
     * END SETTER HELL
     */

    @Override
    public void create() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    // 1 -> setter mode
    public void update(byte mode) {
        if (mode == 1) {
            updatedDate = LocalDateTime.now();
        }
    }

    @Override
    public void delete() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
    
}
