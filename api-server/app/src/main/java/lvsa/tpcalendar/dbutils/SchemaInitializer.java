package lvsa.tpcalendar.dbutils;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

public final class SchemaInitializer {
	Connection conn;

	public SchemaInitializer() {
		try(DBConnProvider dbhandler = new DBConnProvider()) {
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
				id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
				hashcode INT UNIQUE NOT NULL,
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
}
