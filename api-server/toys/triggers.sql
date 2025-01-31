CREATE OR REPLACE TRIGGER tg_created_at
AFTER INSERT ON taskevents
FOR EACH ROW
EXECUTE FUNCTION add_created_at_col();

CREATE OR REPLACE TRIGGER tg_updated_at
AFTER INSERT ON taskevents
FOR EACH ROW
EXECUTE FUNCTION add_updated_at_col();

CREATE OR REPLACE TRIGGER tg_updated_at
AFTER UPDATE ON taskevents
FOR EACH ROW
EXECUTE FUNCTION add_updated_at_col();

-- I am doing it wrong.
-- There need to be 2 separate categories of functions:
-- First group of functions is delegated towards building the schema (add the 
-- columns to each table).
-- Second group of functions is to be delegated towards updating the rows
-- via triggeers on INSERT or UPDATE.
CREATE FUNCTION add_updated_at_col()
RETURNS trigger AS $$
	BEGIN
		ALTER TABLE taskevents
		ADD COLUMN IF NOT EXISTS updated_at timestamp without time zone;
	END;
$$ LANGUAGE plpgsql;

CREATE FUNCTION add_created_at_col()
RETURNS trigger AS $$
	BEGIN
		ALTER TABLE taskevents 
		ADD COLUMN IF NOT EXISTS created_at timestamp without time zone;
	END;
$$ LANGUAGE plpgsql;
