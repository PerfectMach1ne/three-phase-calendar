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


CREATE FUNCTION add_updated_at_col()
RETURNS trigger AS $$
	BEGIN
		-- a
	END;
$$ LANGUAGE plpgsql;

CREATE FUNCTION add_created_at_col()
RETURNS trigger AS $$
	BEGIN
		-- a
	END;
$$ LANGUAGE plpgsql;
