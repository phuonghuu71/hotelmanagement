package Process;

import Database.DBConnect;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerProcess {
    DBConnect conn = new DBConnect();
    public ResultSet getCustomerList() throws SQLException {
        conn.connectSQL();
        String sql = "SELECT * FROM KHACH";
        return conn.LoadData(sql);
    }
}
