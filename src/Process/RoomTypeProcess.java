package Process;

import Database.DBConnect;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomTypeProcess {
    public DBConnect conn = new DBConnect();
    public ResultSet getRoomTypeList() throws SQLException {
        conn.connectSQL();
        String sql = "SELECT * FROM LOAIPHONG";
        return conn.LoadData(sql);
    }

    public void deleteRoomType(String id) throws SQLException {
        conn.connectSQL();
        String sql = "DELETE FROM LOAIPHONG WHERE MALOAIPHONG = '"+id+"'";
        conn.UpdateData(sql);
    }

    public void insertRoomType(String roomTypeName, String roomTypePrice) throws SQLException {
        conn.connectSQL();
        String sql = "INSERT INTO LOAIPHONG(TENLOAIPHONG, GIALOAIPHONG) VALUES('"+roomTypeName+"', '"+roomTypePrice+"')";
        conn.UpdateData(sql);
    }

    public void updateRoomType(String roomTypeName, String roomTypePrice, String roomTypeId) throws SQLException {
        conn.connectSQL();
        String sql = "UPDATE LOAIPHONG SET  TENLOAIPHONG = N'"+roomTypeName+"', GIALOAIPHONG = '"+roomTypePrice+"' where MALOAIPHONG = '"+roomTypeId+"'";
        conn.UpdateData(sql);
    }

}
