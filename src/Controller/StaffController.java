package Controller;

import Database.Staff;
import Process.StaffProcess;
import Process.StaffTypeProcess;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import resources.AlertMaker;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;

public class StaffController implements Initializable {

    public TableView<Database.Staff> tableStaff;
    public TableColumn<Database.Staff, String> colStaffID;
    public TableColumn<Database.Staff, String> colStaffName;
    public TableColumn<Database.Staff, String> colUsername;
    public TableColumn<Database.Staff, String> colType;
    public TableColumn<Database.Staff, String> colSalary;
    public JFXComboBox cbType;
    public JFXTextField txtId;
    public JFXTextField txtUsername;
    public JFXTextField txtStaffName;
    public JFXTextField txtSalary;
    public JFXButton btnUpdateStaff;
    public StackPane spConfirm;
    public JFXButton btnDeleteStaff;
    public StackPane stackpaneStaff;

    StaffProcess staffProcess = new StaffProcess();
    StaffTypeProcess staffTypeProcess = new StaffTypeProcess();
    ObservableList<Database.Staff> staffList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadStaff();
            showCb();
            txtId.setEditable(false);
            txtUsername.setEditable(false);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void showCb() throws SQLException {
        ResultSet resultSet = null;

        resultSet = staffTypeProcess.getTypeList();
        while(resultSet.next()) {
            cbType.getItems().add(resultSet.getString("TENCHUCVU"));
        }

    }

    private void loadStaff() throws SQLException {
        tableStaff.getItems().clear();
        ResultSet result = staffProcess.getStaffList();
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

    private void clearStaff() {
        this.txtId.setText("");
        this.txtStaffName.setText("");
        this.txtUsername.setText("");
        this.txtSalary.setText("");
    }

    public void handleStaffTable(MouseEvent mouseEvent) {
        Staff staff = tableStaff.getSelectionModel().getSelectedItem();
        this.txtId.setText(staff.getId());
        this.txtStaffName.setText(staff.getStaffName());
        this.txtUsername.setText(staff.getUserName());
        this.txtSalary.setText(staff.getSalary());
        cbType.getSelectionModel().select(staff.getType());
    }

    public void handleStaffUpdate(MouseEvent mouseEvent) throws SQLException {
        JFXButton ok = new AlertMaker().customBtn("Đồng Ý");
        String header = "Thành công";
        String body = "Sửa nhân viên thành công";

        if(this.txtId.getText().isEmpty()) {
            header = "Thất bại";
            body = "Chưa chọn nhân viên";
            AlertMaker.showMaterialDialog(stackpaneStaff, Arrays.asList(ok), header, body);
            return;
        }

        String staffName = this.txtStaffName.getText();
        String typeId = null;
        String staffId = this.txtId.getText();
        ResultSet getTypeId = null;
        getTypeId = staffTypeProcess.getTypeId(this.cbType.getSelectionModel().getSelectedItem().toString());
        while(getTypeId.next()) {
            typeId = getTypeId.getString("MACHUCVU");
        }
        staffProcess.updateStaff(staffName, typeId, staffId);
        clearStaff();
        loadStaff();

        AlertMaker.showMaterialDialog(stackpaneStaff, Arrays.asList(ok), header, body);

    }


    public void handleStaffDelete(MouseEvent mouseEvent) throws SQLException {
        JFXButton yes = new AlertMaker().customBtn("Có");
        yes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                JFXButton ok = new AlertMaker().customBtn("Đồng Ý");
                String header = "Thành công";
                String body = "Xóa nhân viên thành công";

                if(txtId.getText().isEmpty()) {
                    header = "Thất bại";
                    body = "Chưa chọn nhân viên";
                    AlertMaker.showMaterialDialog(stackpaneStaff, Arrays.asList(ok), header, body);
                    return;
                }

                String staffId = txtId.getText();
                try {
                    staffProcess.deleteStaff(staffId);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                clearStaff();
                try {
                    loadStaff();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                AlertMaker.showMaterialDialog(stackpaneStaff, Arrays.asList(ok), header, body);
            }
        });

        JFXButton no = new AlertMaker().customBtn("Không");
        no.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                return;
            }
        });

        AlertMaker.showMaterialDialog(stackpaneStaff, Arrays.asList(yes, no), "Xác Nhận", "Bạn thật sự muốn xóa");


    }
}
