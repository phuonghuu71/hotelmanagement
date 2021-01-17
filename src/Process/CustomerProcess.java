package Process;

import Database.DBConnect;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerProcess {
    DBConnect conn = new DBConnect();
    public ResultSet getCustomerList() throws SQLException {
        conn.connectSQL();
//        String sql = "SELECT * FROM KHACH WHERE MAKH NOT IN(SELECT A.MAKH FROM KHACH A, PHIEUDATPHONG B WHERE A.MAKH = B.MAKH)";
        String sql = "SELECT * FROM KHACH";
        return conn.LoadData(sql);

    }

    public void updateCustomer(String customerName, String customerIdentityID, String customerPhone, String customerID) throws SQLException {
        conn.connectSQL();
        String sql = "UPDATE KHACH SET TENKH = N'"+customerName+"', SOCMND = '"+customerIdentityID+"', SODT = '"+customerPhone+"' WHERE MAKH = '"+customerID+"'";
        conn.UpdateData(sql);
    }

    public void insertCustomer(String customerName, String customerIdentityID, String customerPhone) throws SQLException {
        conn.connectSQL();
        String sql = "INSERT INTO KHACH(TENKH, SOCMND, SODT) VALUES(N'"+customerName+"', '"+customerIdentityID+"', '"+customerPhone+"')";
        conn.UpdateData(sql);
    }

}
