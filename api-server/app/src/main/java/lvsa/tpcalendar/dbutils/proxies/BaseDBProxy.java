package lvsa.tpcalendar.dbutils.proxies;

import java.sql.Connection;
import java.sql.SQLException;

import lvsa.tpcalendar.dbutils.DBConnProvider;
import lvsa.tpcalendar.http.HTTPStatusCode;

public abstract class BaseDBProxy {
    private DBConnProvider db;
    protected Connection conn;

    public Connection getConn() { return conn; }

    // public void setDb(DBConnProvider db) { this.db = db; }

    public void setConn(Connection conn) { this.conn = conn; }

    protected BaseDBProxy(DBConnProvider dbConnProvider) {
        this.db = dbConnProvider;
        this.conn = db.getDBConnection();
    }

    public HTTPStatusCode create(String json) throws SQLException {
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    public String read(int hashcode) throws SQLException {
        return "";
    }

    public HTTPStatusCode updateWhole(int hashcode, String json) throws SQLException {
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    public HTTPStatusCode updatePartial(int hashcode, String json) throws SQLException {
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }

    public HTTPStatusCode delete(int hashcode) throws SQLException {
        return HTTPStatusCode.HTTP_501_NOT_IMPLEMENTED;
    }
}