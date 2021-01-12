package Process;

import Database.DBConnect;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomCapacityProcess {
    public DBConnect conn = new DBConnect();
    public ResultSet getRoomCapacityList() throws SQLException {
        conn.connectSQL();
        String sql = "SELECT * FROM SUCCHUA";
        return conn.LoadData(sql);
    }
    public void deleteRoomCapacity(String id) throws SQLException {
        conn.connectSQL();
        String sql = "DELETE FROM SUCCHUA WHERE MASUCCHUA = '"+id+"'";
        conn.UpdateData(sql);
    }
    public void insertRoomCapacity(String roomCapacity, String roomCapacityPrice) throws SQLException {
        conn.connectSQL();
        String sql = "INSERT INTO SUCCHUA(SOGIUONG, GIASUCCHUA) VALUES('"+roomCapacity+"', '"+roomCapacityPrice+"')";
        conn.UpdateData(sql);
    }
    public void updateRoomCapacity(String roomCapacity, String roomCapacityPrice, String id) throws SQLException {
        conn.connectSQL();
        String sql = "UPDATE SUCCHUA SET  SOGIUONG = N'"+roomCapacity+"', GIASUCCHUA = '"+roomCapacityPrice+"' where MASUCCHUA = '"+id+"'";
        conn.UpdateData(sql);
    }
}
