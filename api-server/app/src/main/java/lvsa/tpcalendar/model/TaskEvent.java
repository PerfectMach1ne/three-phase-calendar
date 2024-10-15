package lvsa.tpcalendar.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeParseException;
import java.time.LocalDateTime;

import com.google.gson.JsonObject;

import lvsa.tpcalendar.http.HTTPStatusCode;
import lvsa.tpcalendar.dbutils.DatabaseHandler;
import lvsa.tpcalendar.util.Colors;

public class TaskEvent implements Event {

    private LocalDateTime datetime;
    private String taskName = "Unnamed task"; 
    private String taskDescription = "";
    private LocalDateTime updatedDate = createdDate;
    private boolean isDone = false;
    private boolean hasColor = false;
    private Colors color = null;

    public TaskEvent() {
        super();
    }

    @Override
    public LocalDateTime getDateTime() {
        return this.datetime;
    }

    @Override
    public String getName() {
        return this.taskName;
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

    @Override
    public void setDateTime(LocalDateTime localdatetime) {
        this.datetime = localdatetime;
    }

    @Override
    public void setName(String name) {
        this.taskName = name;
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

    public static Object[] findAndFetchFromDB(int hashcode) {
        JsonObject jsonTask = null;

        try (
            DatabaseHandler db = new DatabaseHandler();
            Connection conn = db.getDBConnection();
        ) {
            jsonTask = db.queryByHashcode(hashcode);
            if (jsonTask.isEmpty()) {
                return new Object[]{HTTPStatusCode.HTTP_404_NOT_FOUND, null};
            }
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }

        System.out.println(jsonTask);
        return new Object[]{HTTPStatusCode.HTTP_200_OK, new TaskEvent()};
    }

    @Override
    public HTTPStatusCode read(int hashCode) {
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    @Override
    public HTTPStatusCode create(JsonObject jsonObj) {
        String dateTimeString = jsonObj.get("datetime").getAsString(); 
        try {
            LocalDateTime datetime = LocalDateTime.parse(dateTimeString);
            this.datetime = datetime;
        } catch (DateTimeParseException dtpe) {
            dtpe.printStackTrace();
        }

        this.setName(jsonObj.get("name").getAsString());
        this.setDescription(jsonObj.get("desc").getAsString());

        JsonObject colorObj = jsonObj.getAsJsonObject("color");
        this.hasColor = colorObj.get("hasColor").getAsBoolean();
        if (this.hasColor) {
            int decColor = colorObj.get("hex").getAsInt();
            String hexColor = "#" + Integer.toHexString(decColor);
            this.setColor(hexColor);
        } else {
            this.setColor(Colors.getColorFromHex(""));
        }

        try (
            DatabaseHandler db = new DatabaseHandler();
            Connection conn = db.getDBConnection();
            Statement stat = conn.createStatement();
            ) {
            updatedDate = LocalDateTime.now();
            stat.setQueryTimeout(30);
            // performInsert(conn: Connection, this: TaskEvent);
            // return an error if it fucks up
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return HTTPStatusCode.HTTP_201_CREATED;
        
        //         if (hashrs.getInt("hashId") == hashCode()) {
        //             System.out.println("NO");
        //         } else {
        //             PreparedStatement prepStat = conn.prepareStatement(
        //                 "INSERT INTO taskevents " +
        //                     "(hashId, datetime, name, description," +
        //                     "color, createdAt, updatedAt)" +
        //                     "VALUES(?, ?, ?, ?, ?, ?, ?)");
        //             prepStat.setInt(1, this.hashCode());
        //             prepStat.setString(2, this.getDateTime().toString());
        //             prepStat.setString(3, this.getName());
        //             prepStat.setString(4, this.getDescription());
        //             prepStat.setString(5, this.color.toString());
        //             prepStat.setString(6, getCreatedDate());
        //             prepStat.setString(7, getUpdatedDate());

        //             prepStat.executeUpdate();

        //             ResultSet rs = stat.executeQuery("SELECT * FROM taskevents;");
        //             while (rs.next()) {
        //                 System.out.println("Data straight from the database!\n" +
        //                 "Task hash code = " + rs.getInt("hashId") + "\n" + 
        //                 "Taskname & date: " + rs.getString("name") + " " + rs.getString("datetime") + "\n" +
        //                 rs.getString("description") + "\n" +
        //                 "Color = #" + Colors.valueOf(rs.getString("color")) + "\n" +
        //                 "updatedAt: " + rs.getString("updatedAt") +
        //                 "createdAt: " + rs.getString("createdAt"));
        //     }
        //         }
        //     } catch (SQLException sqle) {
        //         sqle.printStackTrace();
        //     }
            
            
    }

    @Override
    public HTTPStatusCode update() {
        updatedDate = LocalDateTime.now();
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    @Override
    public HTTPStatusCode delete(int hashCode) {
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    @Override
    public HTTPStatusCode update(JsonObject jsonObj) {
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }
}
