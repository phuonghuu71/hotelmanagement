package Process;

import Database.DBConnect;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookingProcess {
    DBConnect conn = new DBConnect();

    public void insertBooking(String reservationDate, String checkInDate, String checkOutDate, String customerId, String staffId) throws SQLException {
        conn.connectSQL();
        String sql =    "INSERT INTO PHIEUDATPHONG(NGAYDATPHONG,NGAYNHANPHONG,NGAYTRAPHONG,MAKH,MANV) " +
                        "VALUES('"+reservationDate+"','"+checkInDate+"','"+checkOutDate+"','"+customerId+"','"+staffId+"')";
        conn.UpdateData(sql);
    }

    public ResultSet getLastBooking() throws SQLException {
        conn.connectSQL();
        String sql = "SELECT TOP 1 * FROM PHIEUDATPHONG ORDER BY SOPHIEUDP DESC";
        return conn.LoadData(sql);
    }

    public void insertBookingDetail(String roomId, String bookingId) throws SQLException {
        conn.connectSQL();
        String sql = "INSERT INTO CHITIETPDP(MAPHONG,SOPHIEUDP) VALUES('"+roomId+"','"+bookingId+"')";
        conn.UpdateData(sql);
    }

    public void insertBookingService(String roomId) throws SQLException {
        conn.connectSQL();
        String sql = "INSERT INTO PHIEUSDDV(MAPHONG) VALUES('"+roomId+"')";
        conn.UpdateData(sql);
    }

    public ResultSet getLastBookingService() throws  SQLException {
        conn.connectSQL();
        String sql = "SELECT TOP 1 * FROM PHIEUSDDV ORDER BY MAPDV DESC";
        return conn.LoadData(sql);
    }

    //Update status for reservation
    public void updateBookingReservation(String roomId) throws SQLException {
        conn.connectSQL();
        String sql ="UPDATE PHONG SET TINHTRANG = N'Được đặt trước' WHERE MAPHONG = '"+roomId+"'";
        conn.UpdateData(sql);
    }

    public void updateBookingChecIn(String roomId) throws SQLException {
        conn.connectSQL();
        String sql ="UPDATE PHONG SET TINHTRANG = N'Được đặt' WHERE MAPHONG = '"+roomId+"'";
        conn.UpdateData(sql);
    }

    public void updateCheckInDateByBookingId(String checkInDate, String bookingId) throws   SQLException {
        conn.connectSQL();
        String sql = "UPDATE PHIEUDATPHONG SET NGAYNHANPHONG = '"+checkInDate+"' WHERE SOPHIEUDP = '"+bookingId+"'";
        conn.UpdateData(sql);
    }

    public void insertBookingBill(String billCreatedDate, String bookingId, String bookingServiceId, String totalCash) throws SQLException {
        conn.connectSQL();
        String sql = "  INSERT INTO HOADON(NGAYLAPHD,SOPHIEUDP,MAPDV,TONGTIEN,TINHTRANGHD) " +
                        "VALUES('"+billCreatedDate+"','"+bookingId+"','"+bookingServiceId+"','"+totalCash+"', N'Chưa thanh toán')";
        conn.UpdateData(sql);
    }

    public ResultSet getReserveInfo() throws SQLException {
        conn.connectSQL();
//        String sql =    "SELECT A.SOPHIEUDP, TENKH, TENPHONG, NGAYDATPHONG, NGAYNHANPHONG, NGAYTRAPHONG, TONGTIEN, MAHD " +
//                        "FROM PHIEUDATPHONG A, CHITIETPDP B, PHONG C, HOADON D, KHACH E " +
//                        "WHERE A.SOPHIEUDP = B.SOPHIEUDP AND B.MAPHONG = C.MAPHONG AND A.SOPHIEUDP = D.SOPHIEUDP AND A.MAKH = E.MAKH AND TINHTRANG = N'Được đặt trước'";
        String sql =    "SELECT x.SOPHIEUDP, x.TENKH, x.TENPHONG, x.NGAYDATPHONG, x.NGAYNHANPHONG, x.NGAYTRAPHONG, x.TONGTIEN, x.MAHD " +
                        "FROM " +
                        "(" +
                        "SELECT A.SOPHIEUDP, E.TENKH, TENPHONG, NGAYDATPHONG, NGAYNHANPHONG, NGAYTRAPHONG, TONGTIEN, MAHD, " +
                        "ROW_NUMBER() OVER (PARTITION BY TENPHONG ORDER BY A.SOPHIEUDP DESC) AS RowNo " +
                        "FROM PHIEUDATPHONG A, CHITIETPDP B, PHONG C, HOADON D, KHACH E " +
                        "WHERE A.SOPHIEUDP = B.SOPHIEUDP AND B.MAPHONG = C.MAPHONG AND A.SOPHIEUDP = D.SOPHIEUDP AND A.MAKH = E.MAKH AND TINHTRANG = N'Được đặt trước' " +
                        ") as x " +
                        "WHERE x.RowNo = 1";

        return conn.LoadData(sql);
    }

    public ResultSet getCheckInInfo() throws SQLException {
        conn.connectSQL();
//        String sql =    "SELECT A.SOPHIEUDP, TENKH, TENPHONG, NGAYDATPHONG, NGAYNHANPHONG, NGAYTRAPHONG, TONGTIEN, MAHD " +
//                        "FROM PHIEUDATPHONG A, CHITIETPDP B, PHONG C, HOADON D, KHACH E " +
//                        "WHERE A.SOPHIEUDP = B.SOPHIEUDP AND B.MAPHONG = C.MAPHONG AND A.SOPHIEUDP = D.SOPHIEUDP AND A.MAKH = E.MAKH AND TINHTRANG = N'Được đặt'";
        String sql =    "SELECT x.SOPHIEUDP, x.TENKH, x.TENPHONG, x.NGAYDATPHONG, x.NGAYNHANPHONG, x.NGAYTRAPHONG, x.TONGTIEN, x.MAHD " +
                        "FROM " +
                        "(" +
                        "SELECT A.SOPHIEUDP, E.TENKH, TENPHONG, NGAYDATPHONG, NGAYNHANPHONG, NGAYTRAPHONG, TONGTIEN, MAHD, " +
                        "ROW_NUMBER() OVER (PARTITION BY TENPHONG ORDER BY A.SOPHIEUDP DESC) AS RowNo " +
                        "FROM PHIEUDATPHONG A, CHITIETPDP B, PHONG C, HOADON D, KHACH E " +
                        "WHERE A.SOPHIEUDP = B.SOPHIEUDP AND B.MAPHONG = C.MAPHONG AND A.SOPHIEUDP = D.SOPHIEUDP AND A.MAKH = E.MAKH AND TINHTRANG = N'Được đặt' " +
                        ") as x " +
                        "WHERE x.RowNo = 1";
        return conn.LoadData(sql);
    }

    public ResultSet getRoomPriceByRoomName(String roomName) throws  SQLException {
        conn.connectSQL();
        String sql =    "SELECT GIALOAIPHONG, GIASUCCHUA " +
                        "FROM PHONG A, LOAIPHONG B, SUCCHUA C " +
                    "WHERE A.MALOAIPHONG = B.MALOAIPHONG AND A.MASUCCHUA = C.MASUCCHUA AND TENPHONG = N'"+roomName+"'";
        return conn.LoadData(sql);
    }

    public void updateBillTotalCashByBookingId(String totalCash, String bookingId) throws SQLException {
        conn.connectSQL();
        String sql = "UPDATE HOADON SET TONGTIEN = '"+totalCash+"', TINHTRANGHD = N'Đã thanh toán' WHERE SOPHIEUDP = '"+bookingId+"'";
        conn.UpdateData(sql);
    }

    public void deleteBookingDetailByBookingID(String bookingID) throws SQLException {
        conn.connectSQL();
        String sql = "DELETE FROM CHITIETPDP WHERE SOPHIEUDP = '"+bookingID+"'";
        conn.UpdateData(sql);
    }

    public void deleteBookingBillByBookingID(String bookingID) throws SQLException {
        conn.connectSQL();
        String sql = "DELETE FROM HOADON WHERE SOPHIEUDP = '"+bookingID+"'";
        conn.UpdateData(sql);
    }

    public void deleteBookingByBookingID(String bookingID) throws SQLException {
        conn.connectSQL();
        String sql = "DELETE FROM PHIEUDATPHONG WHERE SOPHIEUDP = '"+bookingID+"'";
        conn.UpdateData(sql);
    }

    public void deleteBookingServiceByRoomID(String roomID) throws SQLException {
        conn.connectSQL();
        String sql = "DELETE FROM PHIEUSDDV WHERE MAPHONG = '"+roomID+"'";
        conn.UpdateData(sql);
    }


}
