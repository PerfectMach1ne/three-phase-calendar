package lvsa.tpcalendar.dbutils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class SchemaInitializer {
	Connection conn;

	public SchemaInitializer() {
		try(DBConnProvider dbhandler = new DBConnProvider()) {
			conn = dbhandler.getDBConnection();

			// nukeAndPave(conn);
			createTypes(conn);

			createTaskEvents(conn);
			createTimeblockEvents(conn);
			createTextEvents(conn);
			// createCalendarspace(conn);
			createUsers(conn);

			addTimestampCols(conn);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

	/**
	 * Keeping this on prod is straight up vile.
	 * @param conn
	 * @throws SQLException
	 */
	public static void nukeAndPave(Connection conn) throws SQLException {
		Statement nuclearCode = conn.createStatement();
		nuclearCode.execute("""
			DROP TABLE taskevents, textevents, timeblockevents, users;
		""");
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
				isdone BOOLEAN NOT NULL
			);
		""");
	}

	private void createTimeblockEvents(Connection conn) throws SQLException {
		Statement stat = conn.createStatement();
		stat.execute("""
			CREATE TABLE IF NOT EXISTS timeblockevents (
				id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
				hashcode INT UNIQUE NOT NULL,
				start_datetime timestamp NOT NULL,
				end_datetime timestamp NOT NULL,
				name TEXT,
				description TEXT,
				viewtype taskview NOT NULL,
				color VARCHAR(7) NOT NULL
			);
		""");
	}

	private void createTextEvents(Connection conn) throws SQLException {
		Statement stat = conn.createStatement();
		// TODO: In the future, substitute text for XML to support SOME features of HTML.
		// TODO: (unless we realize this idea sucks ass LOL)
		stat.execute("""
			CREATE TABLE IF NOT EXISTS textevents (
				id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
				hashcode INT UNIQUE NOT NULL,
				content TEXT
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
				timeblockevents_id_arr integer[],
				textevents_id_arr integer[]
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
				password VARCHAR(36) NOT NULL
			);
		""");
	}

	private void addTimestampCols(Connection conn) throws SQLException {
		Statement stat = conn.createStatement();
		stat.execute("""
			CREATE OR REPLACE FUNCTION add_created_at_col(tablename regclass)
			RETURNS BOOLEAN AS $$
			BEGIN
				EXECUTE format('
					ALTER TABLE %s
					ADD COLUMN IF NOT EXISTS
						created_at timestamp without time zone NOT NULL DEFAULT now();
				', tablename);
			RETURN true;
			END;
			$$ LANGUAGE plpgsql;

			CREATE OR REPLACE FUNCTION add_updated_at_col(tablename regclass)
			RETURNS BOOLEAN AS $$
			BEGIN
				EXECUTE format('
					ALTER TABLE %s
					ADD COLUMN IF NOT EXISTS
						updated_at timestamp without time zone NOT NULL DEFAULT now();
				', tablename);
			RETURN true;
			END;
			$$ LANGUAGE plpgsql;

			CREATE OR REPLACE FUNCTION update_tstamp()
			RETURNS TRIGGER AS $$
				BEGIN
					NEW.updated_at = now();
					RETURN NEW;
				END;
			$$ LANGUAGE plpgsql;

			CREATE OR REPLACE FUNCTION alter_all_tables()
			RETURNS BOOLEAN AS $$
			DECLARE
				table_name_record RECORD;
				table_name_regclass regclass;
			BEGIN
				FOR table_name_record IN
					SELECT tablename
					FROM pg_tables
					WHERE tableowner = 'starr4ever'
				LOOP
					table_name_regclass := table_name_record.tablename::regclass;
					PERFORM add_created_at_col(table_name_regclass);
					PERFORM add_updated_at_col(table_name_regclass);

					EXECUTE format('
						CREATE OR REPLACE TRIGGER tg_updated_at
						AFTER UPDATE ON %I
						FOR EACH ROW
						EXECUTE FUNCTION update_tstamp();
					', table_name_regclass);
				END LOOP;
			RETURN true;
			END;
			$$ LANGUAGE plpgsql;

			SELECT alter_all_tables();
		""");
	}
}
