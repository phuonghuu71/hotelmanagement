package Database;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBConnect {
    public Connection conn = null;
    public void connectSQL() throws SQLException {
        try {
            String username = "phuong";
            String password = "phuong";
            String url = "jdbc:sqlserver://DESKTOP-HPMN3NS\\SQLEXPRESS;databaseName=hotel;";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = java.sql.DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
//            JOptionPane.showMessageDialog(null, "Kết nối CSDL thất bại", "Thông báo", 1);
        }
    }

    public ResultSet LoadData(String sql) {
        ResultSet result = null;
        try {
            Statement statement = conn.createStatement();
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void UpdateData(String sql) {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
