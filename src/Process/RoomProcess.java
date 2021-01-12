package Process;

import Database.DBConnect;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomProcess {
    public DBConnect conn = new DBConnect();
    public ResultSet getRoomList() throws SQLException {
        conn.connectSQL();
        String sql = "SELECT MAPHONG, TENPHONG, TINHTRANG, TENLOAIPHONG, SOGIUONG, GIALOAIPHONG, GIASUCCHUA FROM PHONG A, LOAIPHONG B, SUCCHUA C WHERE A.MALOAIPHONG = B.MALOAIPHONG AND A.MASUCCHUA = C.MASUCCHUA";
        return conn.LoadData(sql);
    }
    public void deleteRoom(String id) throws SQLException {
        conn.connectSQL();
        String sql = "DELETE FROM PHONG WHERE MAPHONG = '"+id+"'";
        conn.UpdateData(sql);
    }
    public void insertRoom(String roomName, String roomStatus, String roomTypeID, String roomCapacityID) throws SQLException {
        conn.connectSQL();
        String sql = "INSERT INTO PHONG(TENPHONG, TINHTRANG, MALOAIPHONG, MASUCCHUA) VALUES('"+roomName+"', '"+roomStatus+"', '"+roomTypeID+"', '"+roomCapacityID+"')";
        conn.UpdateData(sql);
    }

    public void updateRoom(String roomName, String roomStatus, String roomTypeID, String roomCapacityID, String roomID) throws SQLException {
        conn.connectSQL();
        String sql = "UPDATE PHONG SET  TENPHONG = N'"+roomName+"', TINHTRANG = N'"+roomStatus+"', MALOAIPHONG = '"+roomTypeID+"', MASUCCHUA = '"+roomCapacityID+"' where MAPHONG = '"+roomID+"'";
        conn.UpdateData(sql);
    }
}
