package lvsa.tpcalendar.model;

import java.io.StringReader;
import java.sql.Connection;
import java.io.IOException;
import java.time.LocalDateTime;
import java.sql.SQLException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import lvsa.tpcalendar.colors.Colors;
import lvsa.tpcalendar.dbutils.DatabaseHandler;

public class TaskEvent implements Event {

    private String taskName = "Unnamed task"; 
    private String taskDescription = "";
    private LocalDateTime updatedDate = createdDate;
    private boolean isDone = false;
    private boolean hasColor = false;
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
    }

    @Override
    public void setDescription(String desc) {
        this.taskDescription = desc;
    }

    public void setColor(String hexColor) {
        this.hasColor = true;
        this.color = Colors.getColorFromHex(hexColor);
    }

    public void setColor(Colors color) { 
        this.hasColor = true;
        this.color = color;
    }
    /*
     * END SETTER HELL
     */

    @Override
    public void create(JsonObject jsonObj) {
        String name = jsonObj.get("name").getAsString();
        String desc = jsonObj.get("desc").getAsString();
        JsonObject colorObj = jsonObj.getAsJsonObject("color");
        boolean hasColor = colorObj.get("hasColor").getAsBoolean();
        Colors color = null;
        if (hasColor) {
            int decColor = colorObj.get("hex").getAsInt();
            color = Colors.getColorFromHex(Integer.toHexString(decColor));
        }
        
        try(DatabaseHandler db = new DatabaseHandler()) {
            Connection conn = db.getDBConnection();
            updatedDate = LocalDateTime.now();
            /*
             * uhhh sql statements go there
             */
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    @Override
    public void update(JsonObject jsonObj) {
        updatedDate = LocalDateTime.now();
    }

    @Override
    public void update() {
        updatedDate = LocalDateTime.now();
    }

    @Override
    public void delete(int hashCode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public LocalDateTime getDateTime() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDateTime'");
    }
    
}
