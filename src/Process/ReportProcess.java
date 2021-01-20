package Process;

import Database.DBConnect;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportProcess {
    DBConnect conn = new DBConnect();
    public ResultSet getReport(String startDay, String endDay) throws SQLException {
        conn.connectSQL();
        String sql =        "SELECT MAHD, TENPHONG, NGAYLAPHD, NGAYNHANPHONG, NGAYTRAPHONG, TONGTIEN " +
                            "FROM HOADON A, PHIEUDATPHONG B, CHITIETPDP C, PHONG D " +
                            "WHERE A.SOPHIEUDP = B.SOPHIEUDP AND B.SOPHIEUDP = C.SOPHIEUDP AND C.MAPHONG = D.MAPHONG AND TINHTRANGHD = N'Đã thanh toán' AND NGAYLAPHD >= '"+startDay+"' AND NGAYLAPHD <= '"+endDay+"'";
        return conn.LoadData(sql);
    }

    public ResultSet getStatisByMonth() throws SQLException {
        conn.connectSQL();
        String sql =    "SELECT DATEPART(MONTH, NGAYLAPHD)AS THANG, SUM(TONGTIEN) AS TONGTIEN " +
                        "FROM HOADON A, PHIEUDATPHONG B, CHITIETPDP C, PHONG D " +
                        "WHERE A.SOPHIEUDP = B.SOPHIEUDP AND B.SOPHIEUDP = C.SOPHIEUDP AND C.MAPHONG = D.MAPHONG AND TINHTRANGHD = N'Đã thanh toán' AND DATEPART(YEAR, NGAYLAPHD) = DATEPART(YEAR, GETDATE()) " +
                        "GROUP BY DATEPART(MONTH, NGAYLAPHD)";
        return conn.LoadData(sql);
    }
}
