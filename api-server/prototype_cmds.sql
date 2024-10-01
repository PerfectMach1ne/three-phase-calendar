CREATE ROLE starr4ever WITH LOGIN PASSWORD 'hahahano';
GRANT ALL PRIVILEGES ON DATABASE tpc_testing TO starr4ever;
ALTER DATABASE tpc_testing OWNER TO starr4ever;
REVOKE ALL ON table_name FROM starr4ever;
DROP ROLE starr4ever;
ALTER DATABASE tpc_testing OWNER TO postgres;
