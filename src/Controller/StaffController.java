package Controller;

import Database.RoomType;
import Database.Staff;
import Database.StaffDuty;
import Process.StaffProcess;
import Process.StaffDutyProcess;
import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import resources.AlertMaker;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.ResourceBundle;

public class StaffController implements Initializable {

    public StackPane stackpaneStaff;

    //staff
    public TableView<Staff> tableStaff;
    public TableColumn<Staff, String> colStaffID;
    public TableColumn<Staff, String> colStaffName;
    public TableColumn<Staff, String> colUsername;
    public TableColumn<Staff, String> colType;
    public TableColumn<Staff, String> colSalary;
    public JFXComboBox cbType;
    public JFXTextField txtId;
    public JFXTextField txtUsername;
    public JFXTextField txtStaffName;
    public JFXTextField txtSalary;
    public JFXButton btnUpdateStaff;
    public JFXButton btnDeleteStaff;

    StaffProcess staffProcess = new StaffProcess();
    ObservableList<Staff> staffList = FXCollections.observableArrayList();
    //end of staff

    //staff duty
    public TableView<StaffDuty> tableDuty;
    public TableColumn<StaffDuty, String> colDutyID;
    public TableColumn<StaffDuty, String> colDutyName;
    public TableColumn<StaffDuty, String> colDutySalary;
    public JFXTextField txtDutyID;
    public JFXTextField txtDutyName;
    public JFXTextField txtDutySalary;
    StaffDutyAddController staffDutyAddController = new StaffDutyAddController();

    StaffDutyProcess staffDutyProcess = new StaffDutyProcess();
    ObservableList<StaffDuty> staffDutyList = FXCollections.observableArrayList();
    //end of staff duty

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadStaff();
            loadStaffDuty();
            showCb();
            this.txtId.setEditable(false);
            this.txtUsername.setEditable(false);
            this.txtDutyID.setEditable(false);
            this.txtSalary.setEditable(false);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //staff
    private void showCb() throws SQLException {
        ResultSet resultSet = null;

        resultSet = staffDutyProcess.getTypeList();
        while(resultSet.next()) {
            cbType.getItems().add(resultSet.getString("TENCHUCVU"));
        }

    }

    private void loadStaff() throws SQLException {
        tableStaff.getItems().clear();
        ResultSet result = staffProcess.getStaffList();
        while (result.next()) {
            staffList.add(new Database.Staff(result.getString("MANV"), result.getString("TENNV"), result.getString("TENDANGNHAP"), result.getString("TENCHUCVU"), currencyChange(Double.parseDouble(result.getString("LUONG")))));
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
        getTypeId = staffDutyProcess.getTypeId(this.cbType.getSelectionModel().getSelectedItem().toString());
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

    public void handleStaffName(MouseEvent mouseEvent) {
        this.txtStaffName.setText("");
    }

    //end of staff

    //currency
    private String currencyChange(double curr) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(curr)+ " VNĐ";
    }

    private void priceProperty(JFXTextField text) {
        text.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.matches("\\d*"));
                text.setText(t1.replaceAll("[^\\d]", ""));
            }
        });

    }
    //end of currency

    //staff duty
    private void loadStaffDuty() throws SQLException {
        if(tableDuty != null) {
            tableDuty.getItems().clear();
        }
        ResultSet result = staffDutyProcess.getTypeList();
        while (result.next()) {
            staffDutyList.add(new StaffDuty(result.getString("MACHUCVU"), result.getString("TENCHUCVU"), currencyChange(Double.parseDouble(result.getString("LUONG")))));
        }

        colDutyID.setCellValueFactory(new PropertyValueFactory<StaffDuty, String>("id"));
        colDutyName.setCellValueFactory(new PropertyValueFactory<StaffDuty, String>("tenChucvu"));
        colDutySalary.setCellValueFactory(new PropertyValueFactory<StaffDuty, String>("luong"));

        tableDuty.setItems(staffDutyList);
    }

    private void clearStaffDuty() {
        this.txtDutyID.setText("");
        this.txtDutyName.setText("");
        this.txtDutySalary.setText("");
    }


    public void handleStaffDutyTable(MouseEvent mouseEvent) {
        StaffDuty staffDuty = tableDuty.getSelectionModel().getSelectedItem();
        txtDutyID.setText(staffDuty.getId());
        txtDutyName.setText(staffDuty.getTenChucvu());
        priceProperty(txtDutySalary);
        txtDutySalary.setText(staffDuty.getLuong());
    }

    public void handleStaffDutyUpdate(MouseEvent mouseEvent) throws SQLException {
        JFXButton conf = new AlertMaker().customBtn("Xác Nhận");
        String dutyId = txtDutyID.getText();
        String dutyName = txtDutyName.getText();
        String dutySalary = txtDutySalary.getText().trim();

        if(dutyId == null || dutyId.length() <= 0) {
            AlertMaker.showMaterialDialog(stackpaneStaff, Arrays.asList(conf), "Thất Bại", "Chưa chọn chức vụ");
            return;
        }

        if(dutyName == null || dutyName.length() <= 0) {
            AlertMaker.showMaterialDialog(stackpaneStaff, Arrays.asList(conf), "Thất Bại", "Chưa nhập tên chức vụ");
            return;
        }

        if(dutySalary == null || dutySalary.length() <= 0) {
            AlertMaker.showMaterialDialog(stackpaneStaff, Arrays.asList(conf), "Thất Bại", "Chưa nhập lương");
            return;
        }

        staffDutyProcess.updateType(dutyName, dutySalary, dutyId);
        loadStaffDuty();
        clearStaffDuty();
        AlertMaker.showMaterialDialog(stackpaneStaff, Arrays.asList(conf), "Thành công", "Sửa chức vụ thành công");
    }

    public void handleStaffDutyDelete(MouseEvent mouseEvent) throws SQLException {
        JFXButton yes = new AlertMaker().customBtn("Có");
        yes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                JFXButton ok = new AlertMaker().customBtn("Đồng Ý");
                String header = "Thành công";
                String body = "Xóa chức vụ thành công";

                if(txtDutyID.getText().isEmpty()) {
                    header = "Thất bại";
                    body = "Chưa chọn chức vụ";
                    AlertMaker.showMaterialDialog(stackpaneStaff, Arrays.asList(ok), header, body);
                    return;
                }

                if(txtDutyID.getText().equals("1")) {
                    header = "Thất bại";
                    body = "Không thể xóa chức vụ mặc định";
                    AlertMaker.showMaterialDialog(stackpaneStaff, Arrays.asList(ok), header, body);
                    return;
                }

                String dutyID = txtDutyID.getText();
                try {
                    staffDutyProcess.deleteType(dutyID);
                    clearStaffDuty();
                    loadStaffDuty();
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

    public void handleStaffDutyOpen(MouseEvent mouseEvent) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/AddStaffDuty.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Quản lý Khách Sạn");
        stage.setScene(scene);
        new FadeIn(root).play();
        stage.show();

        staffDutyAddController = loader.getController();
        staffDutyAddController.setStaffController(this);
        staffDutyAddController.btnConfirmAddDuty.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                JFXButton conf = new AlertMaker().customBtn("Xác Nhận");
                String dutyName = staffDutyAddController.txtDutyName.getText();
                String dutySalary = staffDutyAddController.txtDutySalary.getText();

                if(dutyName == null || dutyName.length() <= 0) {
                    AlertMaker.showMaterialDialog(staffDutyAddController.stackpaneAddStaffDuty, Arrays.asList(conf), "Thất Bại", "Chưa nhập tên chức vụ");
                    return;
                }

                if(dutySalary == null || dutySalary.length() <= 0) {
                    AlertMaker.showMaterialDialog(staffDutyAddController.stackpaneAddStaffDuty, Arrays.asList(conf), "Thất Bại", "Chưa nhập lương");
                    return;
                }

                try {
                    staffDutyProcess.insertType(dutyName, dutySalary);
                    loadStaffDuty();
                    clearStaffDuty();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                JFXButton agree = new AlertMaker().customBtn("Đồng Ý");

                agree.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        staffDutyAddController.closeStage();
                    }
                });

                AlertMaker.showMaterialDialog(staffDutyAddController.stackpaneAddStaffDuty, Arrays.asList(agree), "Thành Công", "Thêm chức vụ thành công");

            }
        });

        staffDutyAddController.btnCancelAddDuty.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                staffDutyAddController.closeStage();
            }
        });
    }

    public void handleStaffDuty(MouseEvent mouseEvent) {
        this.txtDutyName.setText("");
    }

    public void handleStaffDutySalary(MouseEvent mouseEvent) {
        this.txtDutySalary.setText("");
        priceProperty(txtDutySalary);
    }
    //end of staff duty

}
