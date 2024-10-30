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
			createUsers(conn);
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

	private void createUsers(Connection conn) throws SQLException {
		Statement stat = conn.createStatement();
		stat.execute("""
			CREATE TABLE IF NOT EXISTS users (
				id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
				name VARCHAR(36),
				email TEXT,
				password VARCHAR(36),
				createdAt timestamp NOT NULL,
				updatedAt timestamp NOT NULL
			);
		""");
	}
}
