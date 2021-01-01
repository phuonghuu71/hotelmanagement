package Process;

import Database.DBConnect;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Staff {
    public DBConnect conn = new DBConnect();
    public ResultSet Login(String username, String password) throws SQLException {
        conn.connectSQL();
        String sql = "SELECT COUNT(*) FROM NHANVIEN WHERE TENDANGNHAP = '"+username+"' AND MATKHAU = HASHBYTES('MD5', '"+password+"')";
        return conn.LoadData(sql);
    }
    public ResultSet getStaffList() throws SQLException {
        conn.connectSQL();
        String sql = "SELECT MANV, TENNV, TENDANGNHAP, TENCHUCVU, LUONG FROM NHANVIEN A, CHUCVU B WHERE A.MACHUCVU = B.MACHUCVU";
        return conn.LoadData(sql);
    }
}
