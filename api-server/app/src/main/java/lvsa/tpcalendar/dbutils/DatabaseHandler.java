package lvsa.tpcalendar.dbutils;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;

public class DatabaseHandler {

    public DatabaseHandler() throws SQLException {
        try( 
            Connection conn = DriverManager.getConnection("jdbc:sqlite:sample.db");
            Statement stat = conn.createStatement();
        ) {
            stat.setQueryTimeout(30); 
            stat.executeUpdate("create table person (id integer, name string)");
            stat.executeUpdate("insert into person values(1, 'Vivi')");
            stat.executeUpdate("insert into person values(2, 'Silva')");

            ResultSet rs = stat.executeQuery("select * from person");
            while(rs.next()) {
                // read the result set
                System.out.println("name = " + rs.getString("name"));
                System.out.println("id = " + rs.getInt("id"));
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        
    }

}