package lvsa.tpcalendar.dbutils;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

public final class SchemaInitializer {
	Connection conn;

	public SchemaInitializer() {
		try(DBConnProvider dbhandler = new DBConnProvider()) {
			conn = dbhandler.getDBConnection();

			createTypes(conn);

			createTaskEvents(conn);
			// createCalendarspace(conn);
			createUsers(conn);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

	private void createTypes(Connection conn) throws SQLException {
		Statement stat = conn.createStatement();
		stat.execute("""
			DO $$ BEGIN
				CREATE TYPE taskview
				AS ENUM ('static_task', 'historic_task', 'routine_task');
			EXCEPTION WHEN duplicate_object THEN
				RAISE NOTICE 'taskview ENUM type already exists; not creating.';
			END $$;
		""");
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
				viewtype taskview NOT NULL,
				color VARCHAR(7) NOT NULL,
				isdone BOOLEAN NOT NULL,
				created_at timestamp NOT NULL DEFAULT now(),
				updated_at timestamp NOT NULL DEFAULT now()
			);
		""");
	}

	private void createCalendarspace(Connection conn) throws SQLException {
		Statement stat = conn.createStatement();
		stat.execute("""
			CREATE TABLE IF NOT EXISTS calendarspace (
				id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
				user_id INT FOREIGN KEY,
				tasksevents_id_arr integer[],	
				periodevents_id_arr integer[],
				textevents_id_arr integer[],
				created_at timestamp NOT NULL DEFAULT now(),
				updated_at timestamp NOT NULL DEFAULT now()
			);
		""");
	}

	private void createUsers(Connection conn) throws SQLException {
		Statement stat = conn.createStatement();
		stat.execute("""
			CREATE TABLE IF NOT EXISTS users (
				id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
				name VARCHAR(36) NOT NULL,
				email TEXT NOT NULL,
				password VARCHAR(36) NOT NULL,
				created_at timestamp NOT NULL DEFAULT now(),
				updated_at timestamp NOT NULL DEFAULT now()
			);
		""");
	}
}
