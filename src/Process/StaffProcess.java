package Process;

import Database.DBConnect;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StaffProcess {
    public DBConnect conn = new DBConnect();
    public ResultSet Login(String username, String password) throws SQLException {
        conn.connectSQL();
        String sql = "SELECT COUNT(*) FROM NHANVIEN WHERE TENDANGNHAP = '"+username+"' AND MATKHAU = HASHBYTES('MD5', '"+password+"')";
        return conn.LoadData(sql);
    }

    public ResultSet LoginByEmail(String email, String password) throws SQLException {
        conn.connectSQL();
        String sql = "SELECT COUNT(*) FROM NHANVIEN WHERE EMAIL = '"+email+"' AND MATKHAU = HASHBYTES('MD5', '"+password+"')";
        return conn.LoadData(sql);
    }

    public ResultSet checkUsernameExist(String username) throws SQLException {
        conn.connectSQL();
        String sql = "SELECT COUNT(*) FROM NHANVIEN WHERE TENDANGNHAP = '"+username+"'";
        return conn.LoadData(sql);
    }

    public ResultSet checkEmailExist(String email) throws SQLException {
        conn.connectSQL();
        String sql = "SELECT COUNT(*) FROM NHANVIEN WHERE EMAIL = '"+email+"'";
        return conn.LoadData(sql);
    }

    public ResultSet getStaffInfoByUsername(String username) throws SQLException {
        conn.connectSQL();
        String sql = "SELECT TOP 1 TENNV, EMAIL, TENCHUCVU FROM NHANVIEN A, CHUCVU B WHERE TENDANGNHAP = '"+username+"' AND A.MACHUCVU = B.MACHUCVU";
        return conn.LoadData(sql);
    }

    public ResultSet getStaffInfoByFullname(String fullname) throws SQLException {
        conn.connectSQL();
        String sql = "SELECT TOP 1 A.MANV, TENNV, EMAIL, TENCHUCVU FROM NHANVIEN A, CHUCVU B WHERE TENNV = N'"+fullname+"' AND A.MACHUCVU = B.MACHUCVU";
        return conn.LoadData(sql);
    }

    public ResultSet getStaffInfoByEmail(String email) throws SQLException {
        conn.connectSQL();
        String sql = "SELECT TOP 1 TENNV, EMAIL, TENCHUCVU FROM NHANVIEN A, CHUCVU B WHERE EMAIL = '"+email+"' AND A.MACHUCVU = B.MACHUCVU";
        return conn.LoadData(sql);
    }

    public ResultSet getStaffList() throws SQLException {
        conn.connectSQL();
//        String sql = "SELECT MANV, TENNV, TENDANGNHAP, TENCHUCVU, LUONG FROM NHANVIEN A, CHUCVU B WHERE A.MACHUCVU = B.MACHUCVU";
        String sql = "SELECT * FROM NHANVIEN A, CHUCVU B WHERE A.MACHUCVU = B.MACHUCVU";
        return conn.LoadData(sql);
    }

    public void insertStaff(String username, String password, String email, String fullName) throws SQLException {
        conn.connectSQL();
        String sql = "INSERT INTO NHANVIEN(TENDANGNHAP,MATKHAU,EMAIL,TENNV,MACHUCVU) VALUES" +
                     "('"+username+"',HASHBYTES('MD5','"+password+"'),'"+email+"',N'"+fullName+"','1')";
        conn.UpdateData(sql);
    }

    public void updateStaff(String staffName, String typeId, String id) throws SQLException {
        conn.connectSQL();
        String sql = "UPDATE NHANVIEN SET TENNV = N'" + staffName + "', MACHUCVU = '" + typeId + "' WHERE MANV = '" + id + "'";
        conn.UpdateData(sql);
    }

    public void deleteStaff(String id) throws SQLException {
        conn.connectSQL();
        String sql = "DELETE FROM NHANVIEN WHERE MANV = '"+id+"'";
        conn.UpdateData(sql);
    }

    public void changePasswordByEmail(String password, String email) throws SQLException {
        conn.connectSQL();
        String sql = "UPDATE NHANVIEN SET MATKHAU = HASHBYTES('MD5','"+password+"') WHERE EMAIL = '"+email+"'";
        conn.UpdateData(sql);
    }

}
