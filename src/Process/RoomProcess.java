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

    public ResultSet getRoomEmptyListByRoomTypeIDAndRoomCapacityID(String roomTypeID, String roomCapacityID) throws SQLException {
        conn.connectSQL();
        String sql = "SELECT TENPHONG FROM PHONG WHERE MALOAIPHONG = '"+roomTypeID+"' AND MASUCCHUA = '"+roomCapacityID+"' AND TINHTRANG = N'Còn Phòng'";
        return conn.LoadData(sql);
    }

    public ResultSet getRoomIdByRoomName(String roomName) throws SQLException {
        conn.connectSQL();
        String sql = "SELECT MAPHONG FROM PHONG WHERE TENPHONG = N'"+roomName+"'";
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
        String sql = "UPDATE PHONG SET  TENPHONG = N'"+roomName+"', TINHTRANG = N'"+roomStatus+"', MALOAIPHONG = '"+roomTypeID+"', MASUCCHUA = '"+roomCapacityID+"' WHERE MAPHONG = '"+roomID+"'";
        conn.UpdateData(sql);
    }

    public void updateRoomStatusCheckOut(String roomID) throws SQLException {
        conn.connectSQL();
        String sql = "UPDATE PHONG SET TINHTRANG = N'Còn Phòng' WHERE MAPHONG = '"+roomID+"'";
        conn.UpdateData(sql);
    }

    public ResultSet getRoomCapacityByRoomName(String roomName) throws SQLException {
        conn.connectSQL();
        String sql = "SELECT * FROM SUCCHUA A, PHONG B WHERE A.MASUCCHUA = B.MASUCCHUA AND TENPHONG = N'"+roomName+"'";
        return conn.LoadData(sql);
    }

    public ResultSet getRoomTypeByRoomName(String roomName) throws SQLException {
        conn.connectSQL();
        String sql = "SELECT * FROM LOAIPHONG A, PHONG B WHERE A.MALOAIPHONG = B.MALOAIPHONG AND TENPHONG = N'"+roomName+"'";
        return conn.LoadData(sql);
    }

    public ResultSet getRoomBooked() throws  SQLException {
        conn.connectSQL();
        String sql = "SELECT * FROM PHONG WHERE TINHTRANG = N'Được đặt'";
        return  conn.LoadData(sql);
    }
}
