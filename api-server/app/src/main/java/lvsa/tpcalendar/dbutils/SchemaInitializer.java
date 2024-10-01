package lvsa.tpcalendar.dbutils;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class SchemaInitializer {
	Connection conn;

	public SchemaInitializer() {
		try(DatabaseHandler dbhandler = new DatabaseHandler()) {
			conn = dbhandler.getDBConnection();
			if (!checkIfTableExists(conn, "taskevents")) {
				createTaskEvents(conn);
			} 
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		// here we GOOOOO
		// the master plan:
		// 1. create tables if not exists
		// 2. later add roles and privileges to it
	}

	private void createTaskEvents(Connection conn) throws SQLException {
		Statement stat = conn.createStatement(); 
		stat.execute("""
			CREATE TABLE IF NOT EXISTS taskevents (
				hashId TEXT PRIMARY KEY,
				datetime timestamp NOT NULL,
				name TEXT,
				description TEXT,
				color VARCHAR(7) NOT NULL,
				isDone BOOLEAN,
				createdAt timestamp,
				updatedAt timestamp
			);
			""");
	}

	private boolean checkIfTableExists(Connection conn, String tablename) throws SQLException {
		Statement stat = conn.createStatement(
			ResultSet.TYPE_SCROLL_INSENSITIVE,
			ResultSet.CONCUR_READ_ONLY
		);
		String query = "SELECT * FROM pg_catalog.pg_tables WHERE tablename = '" + tablename + "';";
		ResultSet rs = stat.executeQuery(query);
		if (!rs.next()) {
			return false;
		} else {
			return true;
		}
	}
}
