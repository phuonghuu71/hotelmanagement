package Process;

import Database.DBConnect;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportProcess {
    DBConnect conn = new DBConnect();
    public ResultSet getReport(String startDay, String endDay) throws SQLException {
        conn.connectSQL();
        String sql =        "SELECT MAHD, TENPHONG, NGAYLAPHD, NGAYDATPHONG, NGAYTRAPHONG, TONGTIEN " +
                            "FROM HOADON A, PHIEUDATPHONG B, CHITIETPDP C, PHONG D " +
                            "WHERE A.SOPHIEUDP = B.SOPHIEUDP AND B.SOPHIEUDP = C.SOPHIEUDP AND C.MAPHONG = D.MAPHONG AND TINHTRANGHD = N'Đã thanh toán' AND NGAYLAPHD >= '"+startDay+"' AND NGAYLAPHD <= '"+endDay+"'";
        return conn.LoadData(sql);
    }
}
