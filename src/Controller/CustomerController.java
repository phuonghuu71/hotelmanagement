package Controller;

import Database.Customer;
import Database.Service;
import Process.CustomerProcess;
import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.Arrays;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {

    public TableView<Customer> tableCustomer;
    public TableColumn<Customer, String> colCustomerID;
    public TableColumn<Customer, String> colCustomerName;
    public TableColumn<Customer, String> colCustomerIdentityID;
    public TableColumn<Customer, String> colPhone;
    ObservableList<Customer> customerList = FXCollections.observableArrayList();
    CustomerProcess customerProcess = new CustomerProcess();

    public StackPane stackpaneCustomer;
    public JFXTextField txtCustomerID;
    public JFXTextField txtIdentityID;
    public JFXTextField txtCustomerName;
    public JFXTextField txtPhone;

    private CustomerAddController customerAddController;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadCustomer();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    void loadCustomer() throws SQLException {
        tableCustomer.getItems().clear();
        ResultSet result = customerProcess.getCustomerList();
        while (result.next()) {
            customerList.add(new Customer(result.getString("MAKH"), result.getString("TENKH"), result.getString("SOCMND"), result.getString("SODT")));
        }

        colCustomerID.setCellValueFactory(new PropertyValueFactory<Customer, String>("id"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
        colCustomerIdentityID.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerIdentityCard"));
        colPhone.setCellValueFactory(new PropertyValueFactory<Customer, String>("phoneNumber"));

        tableCustomer.setItems(customerList);
    }

    private void clearCustomer() {
        this.txtCustomerID.setText("");
        this.txtCustomerName.setText("");
        this.txtIdentityID.setText("");
        this.txtPhone.setText("");
    }

    public void handleCustomerAddOpen(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/AddCustomer.fxml"));
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

        customerAddController = loader.getController();
        customerAddController.setCustomerController(this);
        customerAddController.btnConfirmAddCustomer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                JFXButton conf = new AlertMaker().customBtn("Xác Nhận");
                String customerName = customerAddController.txtCustomerName.getText();
                String customerIdentityID = customerAddController.txtCustomerIdentityID.getText();
                String customerPhone = customerAddController.txtCustomerPhone.getText();

                if(customerName == null || customerName.length() <= 0) {
                    AlertMaker.showMaterialDialog(customerAddController.stackpaneCustomer, Arrays.asList(conf), "Thất Bại", "Chưa nhập tên khách");
                    return;
                }

                if(customerIdentityID == null || customerIdentityID.length() <= 0) {
                    AlertMaker.showMaterialDialog(customerAddController.stackpaneCustomer, Arrays.asList(conf), "Thất Bại", "Chưa nhập CMND");
                    return;
                }

                if(customerPhone == null || customerPhone.length() <= 0) {
                    AlertMaker.showMaterialDialog(customerAddController.stackpaneCustomer, Arrays.asList(conf), "Thất Bại", "Chưa nhập SĐT");
                    return;
                }

                try {
                    customerProcess.insertCustomer(customerName, customerIdentityID, customerPhone);
                    loadCustomer();
                    clearCustomer();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                JFXButton agree = new AlertMaker().customBtn("Đồng Ý");
                agree.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        customerAddController.closeStage();
                    }
                });
                AlertMaker.showMaterialDialog(customerAddController.stackpaneCustomer, Arrays.asList(agree), "Thành Công", "Thêm khách thành công");
            }
        });

        customerAddController.btnCancelAddCustomer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                customerAddController.closeStage();
            }
        });

    }

    public void handleCustomerUpdate(MouseEvent mouseEvent) throws SQLException {
        JFXButton ok = new AlertMaker().customBtn("Đồng Ý");
        String header = "Thành công";
        String body = "Sửa khách thành công";

        if(this.txtCustomerID.getText().isEmpty()) {
            header = "Thất bại";
            body = "Chưa chọn khách";
            AlertMaker.showMaterialDialog(stackpaneCustomer, Arrays.asList(ok), header, body);
            return;
        }

        String customerId = this.txtCustomerID.getText();
        String customerName = this.txtCustomerName.getText();
        String customerIdentityID = this.txtIdentityID.getText();
        String customerPhone = this.txtPhone.getText();
        customerProcess.updateCustomer(customerName, customerIdentityID, customerPhone, customerId);
        clearCustomer();
        loadCustomer();

        AlertMaker.showMaterialDialog(stackpaneCustomer, Arrays.asList(ok), header, body);

    }

    public void handleCustomerDelete(MouseEvent mouseEvent) throws SQLException {

        JFXButton ok = new AlertMaker().customBtn("Đồng Ý");
        String header = "Thành công";
        String body = "Xóa khách thành công";

        String customerId = this.txtCustomerID.getText();

        if(this.txtCustomerID.getText().isEmpty()) {
            header = "Thất bại";
            body = "Chưa chọn khách";
            AlertMaker.showMaterialDialog(stackpaneCustomer, Arrays.asList(ok), header, body);
            return;
        }

        try {
            customerProcess.deleteCustomer(customerId);
        }
        catch (SQLServerException e) {
            header = "Thất bại";
            body = "Xóa khách thất bại";
            AlertMaker.showMaterialDialog(stackpaneCustomer, Arrays.asList(ok), header, body);
            return;
        }
        clearCustomer();
        loadCustomer();

        AlertMaker.showMaterialDialog(stackpaneCustomer, Arrays.asList(ok), header, body);




    }


    public void handleCustomerTable(MouseEvent mouseEvent) {
        Customer customer = tableCustomer.getSelectionModel().getSelectedItem();
        this.txtCustomerID.setText(customer.getId());
        this.txtCustomerName.setText(customer.getCustomerName());
        this.txtIdentityID.setText(customer.getCustomerIdentityCard());
        this.txtPhone.setText(customer.getPhoneNumber());
    }


}
