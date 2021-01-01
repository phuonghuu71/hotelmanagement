package Controller;

import Process.Staff;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StaffController implements Initializable {

    public TableView<Database.Staff> tableStaff;
    public TableColumn<Database.Staff, String> colStaffID;
    public TableColumn<Database.Staff, String> colStaffName;
    public TableColumn<Database.Staff, String> colUsername;
    public TableColumn<Database.Staff, String> colType;
    public TableColumn<Database.Staff, String> colSalary;

    Staff staff = new Staff();
    ObservableList<Database.Staff> staffList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadStaff();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void loadStaff() throws SQLException {
        ResultSet result = staff.getStaffList();
        while (result.next()) {
            staffList.add(new Database.Staff(result.getString("MANV"), result.getString("TENNV"), result.getString("TENDANGNHAP"), result.getString("TENCHUCVU"), result.getString("LUONG")));
        }

        colStaffID.setCellValueFactory(new PropertyValueFactory<Database.Staff, String>("id"));
        colStaffName.setCellValueFactory(new PropertyValueFactory<Database.Staff, String>("staffName"));
        colUsername.setCellValueFactory(new PropertyValueFactory<Database.Staff, String>("userName"));
        colType.setCellValueFactory(new PropertyValueFactory<Database.Staff, String>("type"));
        colSalary.setCellValueFactory(new PropertyValueFactory<Database.Staff, String>("salary"));

        tableStaff.setItems(staffList);
    }

}
