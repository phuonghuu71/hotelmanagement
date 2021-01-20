package Process;

import Database.DBConnect;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceProcess {
    public DBConnect conn = new DBConnect();

    public ResultSet getServiceListByBillID(String billID) throws SQLException {
        conn.connectSQL();
        String sql =    "SELECT D.MADV, TENDV, SOLUONG, GIADV " +
                        "FROM PHIEUSDDV A, HOADON B, CHITIETSDDV C, DICHVU D " +
                        "WHERE A.MAPDV = B.MAPDV AND B.MAPDV = C.MAPDV  AND C.MADV = D.MADV AND MAHD = '"+billID+"'";
        return conn.LoadData(sql);
    }

    public ResultSet getCallService() throws SQLException {
        conn.connectSQL();
        String sql =    "SELECT MACHITIETSDDV, TENPHONG, TENDV, SOLUONG, GIADV " +
                        "FROM HOADON A, PHIEUSDDV B, CHITIETSDDV C, DICHVU D, PHONG E " +
                        "WHERE A.MAPDV = B.MAPDV AND B.MAPDV = C.MAPDV AND C.MADV = D.MADV AND B.MAPHONG = E.MAPHONG";
        return conn.LoadData(sql);
    }

    public ResultSet getService() throws  SQLException {
        conn.connectSQL();
        String sql = "SELECT * FROM DICHVU";
        return  conn.LoadData(sql);
    }

    public void insertService(String serviceName, String servicePrice) throws SQLException {
        conn.connectSQL();
        String sql = "INSERT INTO DICHVU(TENDV, GIADV) VALUES('"+serviceName+"', '"+servicePrice+"')";
        conn.UpdateData(sql);
    }

    public void updateService(String serviceName, String servicePrice, String serviceId) throws SQLException {
        conn.connectSQL();
        String sql = "UPDATE DICHVU SET TENDV = N'"+serviceName+"', GIADV = '"+servicePrice+"' WHERE MADV = '"+serviceId+"'";
        conn.UpdateData(sql);
    }

    public void deleteService(String serviceId) throws SQLException {
        conn.connectSQL();
        String sql = "DELETE FROM DICHVU WHERE MADV = '"+serviceId+"'";
        conn.UpdateData(sql);
    }

    public ResultSet getServiceByServiceName(String serviceName) throws SQLException {
        conn.connectSQL();
        String sql = "SELECT * FROM DICHVU WHERE TENDV = N'"+serviceName+"'";
        return conn.LoadData(sql);
    }

    public ResultSet getServiceBillByRoomId(String roomId) throws SQLException {
        conn.connectSQL();
        String sql = "SELECT TOP 1 * FROM PHIEUSDDV WHERE MAPHONG = '"+roomId+"' ORDER BY MAPDV DESC";
        return conn.LoadData(sql);
    }

    public void insertBookedServiceBill(String serviceBillId, String serviceId, String quantity) throws SQLException {
        conn.connectSQL();
        String sql = "INSERT INTO CHITIETSDDV(MAPDV, MADV, SOLUONG) VALUES('"+serviceBillId+"','"+serviceId+"','"+quantity+"')";
        conn.UpdateData(sql);
    }

    public void deleteBookedServiceBill(String serviceBillId) throws SQLException {
        conn.connectSQL();
        String sql = "DELETE FROM CHITIETSDDV WHERE MACHITIETSDDV = '"+serviceBillId+"'";
        conn.UpdateData(sql);
    }

    public void deleteDetailServiceByServiceBillId(String serviceBilId) throws SQLException {
        conn.connectSQL();
        String sql = "DELETE FROM CHITIETSDDV WHERE MAPDV = '"+serviceBilId+"'";
        conn.UpdateData(sql);
    }

    public ResultSet getServiceBillIdByBillId(String billId) throws SQLException {
        conn.connectSQL();
        String sql = "SELECT MAPDV FROM HOADON WHERE MAHD = '"+billId+"'";
        return conn.LoadData(sql);
    }

}
