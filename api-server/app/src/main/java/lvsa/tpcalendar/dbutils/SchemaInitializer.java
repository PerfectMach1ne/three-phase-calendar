package lvsa.tpcalendar.dbutils;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SchemaInitializer {
	Connection conn;

	public SchemaInitializer() {
		try(DatabaseHandler dbhandler = new DatabaseHandler()) {
			conn = dbhandler.getDBConnection();
			createTaskEvents(conn);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

	private void createTaskEvents(Connection conn) throws SQLException {
		Statement stat = conn.createStatement(); 
		stat.execute("""
			CREATE TABLE IF NOT EXISTS taskevents (
				hashId INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
				datetime timestamp NOT NULL,
				name TEXT,
				description TEXT,
				color VARCHAR(7) NOT NULL,
				isDone BOOLEAN NOT NULL,
				createdAt timestamp NOT NULL,
				updatedAt timestamp NOT NULL
			);
			""");
	}

	// ToDo: Pointless, actually, and adds unnecessary computation unless ran for more than
	// one table. Potentially reuse this as a diagnostic check later.
	private boolean checkIfTableExists(Connection conn, String tablename) throws SQLException {
		Statement stat = conn.createStatement(
			ResultSet.TYPE_SCROLL_INSENSITIVE,
			ResultSet.CONCUR_READ_ONLY
		);
		String query = "SELECT * FROM pg_catalog.pg_tables WHERE tablename = '" + tablename + "';";
		ResultSet rs = stat.executeQuery(query);
		if (!rs.next()) {
			System.out.println("[DEBUG] CREATING the table.");
			return false;
		} else {
			System.out.println("[DEBUG] NOT creating the table.");
			return true;
		}
	}
}
