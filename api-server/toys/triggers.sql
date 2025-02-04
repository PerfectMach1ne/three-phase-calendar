-- Schema step 2 builders
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
		-- Row create/update triggers
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