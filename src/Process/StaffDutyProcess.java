package Process;

import Database.DBConnect;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StaffDutyProcess {
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

    public void deleteType(String id) throws SQLException {
        conn.connectSQL();
        String sql = "DELETE FROM CHUCVU WHERE MACHUCVU = '"+id+"'";
        conn.UpdateData(sql);
    }

    public void updateType(String typeName, String typeSalary, String typeID) throws SQLException {
        conn.connectSQL();
        String sql = "UPDATE CHUCVU SET TENCHUCVU = N'"+typeName+"', LUONG = '"+typeSalary+"' WHERE MACHUCVU = '"+typeID+"'";
        conn.UpdateData(sql);
    }

    public void insertType(String typeName, String typeSalary) throws SQLException {
        conn.connectSQL();
        String sql = "INSERT INTO CHUCVU(TENCHUCVU, LUONG) VALUES(N'"+typeName+"', '"+typeSalary+"')";
        conn.UpdateData(sql);
    }

}
