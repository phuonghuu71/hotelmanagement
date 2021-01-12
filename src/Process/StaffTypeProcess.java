package Process;

import Database.DBConnect;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StaffTypeProcess {
    public DBConnect conn = new DBConnect();

    public ResultSet getTypeList() throws SQLException {
        conn.connectSQL();
        String sql = "SELECT * FROM CHUCVU";
        return conn.LoadData(sql);
    }

    public ResultSet getTypeId(String typeName) throws SQLException {
        conn.connectSQL();
        String sql = "SELECT MACHUCVU FROM CHUCVU WHERE TENCHUCVU = N'" + typeName + "'";
        return conn.LoadData(sql);
    }


}
