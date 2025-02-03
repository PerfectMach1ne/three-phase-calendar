-- Schema step 2 builders
CREATE FUNCTION add_created_at_col(tablename regclass)
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

CREATE FUNCTION add_updated_at_col(tablename regclass)
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

CREATE FUNCTION alter_all_tables() RETURNS BOOLEAN AS $$
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
	END LOOP;
RETURN true;
END;
$$ LANGUAGE plpgsql;
-- Row create/update triggers

CREATE OR REPLACE TRIGGER tg_updated_at
AFTER UPDATE ON taskevents
FOR EACH ROW
EXECUTE FUNCTION add_updated_at_col();

-- FIXME: I am doing it wrong.
-- There need to be 2 separate categories of functions:
-- First group of functions is delegated towards building the schema (add the 
-- columns to each table).
-- Second group of functions is to be delegated towards updating the rows
-- via triggeers on INSERT or UPDATE.
