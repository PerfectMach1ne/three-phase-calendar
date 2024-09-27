package lvsa.tpcalendar.model;

import java.io.StringReader;
import java.sql.Connection;
import java.io.IOException;
import java.time.LocalDateTime;
import java.sql.SQLException;
import java.sql.Statement;

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
    private Colors color = null;

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
        return Colors.getPrettyNameFromColor(this.color) + ": " + this.color.getHexColor();
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
        this.setName(jsonObj.get("name").getAsString());
        this.setDescription(jsonObj.get("desc").getAsString());
        JsonObject colorObj = jsonObj.getAsJsonObject("color");
        this.hasColor = colorObj.get("hasColor").getAsBoolean();
        if (this.hasColor) {
            int decColor = colorObj.get("hex").getAsInt();
            String hexColor = "#" + Integer.toHexString(decColor);
            this.setColor(hexColor);
        }
        
        try(DatabaseHandler db = new DatabaseHandler()) {
            Connection conn = db.getDBConnection();
            Statement stat = conn.createStatement(); 
            stat.setQueryTimeout(30);

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
