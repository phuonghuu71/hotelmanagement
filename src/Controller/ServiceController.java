package Controller;

import Database.Service;
import Database.Staff;
import Process.ServiceProcess;
import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
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
import java.util.Arrays;
import java.util.ResourceBundle;

public class ServiceController implements Initializable {

    public TableView<Service> tableService;
    public TableColumn<Service, String> colServiceID;
    public TableColumn<Service, String> colServiceName;
    public TableColumn<Service, String> colServicePrice;
    public StackPane stackpaneService;

    ObservableList<Service> serviceList = FXCollections.observableArrayList();
    ServiceProcess serviceProcess = new ServiceProcess();

    public JFXTextField txtServiceID;
    public JFXTextField txtServiceName;
    public JFXTextField txtServicePrice;

    ServiceAddController serviceAddController = new ServiceAddController();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadService();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    void loadService() throws SQLException {
        tableService.getItems().clear();
        ResultSet result = serviceProcess.getService();
        while (result.next()) {
            serviceList.add(new Service(result.getString("MADV"), result.getString("TENDV"), result.getString("GIADV")));
        }

        colServiceID.setCellValueFactory(new PropertyValueFactory<Service, String>("id"));
        colServiceName.setCellValueFactory(new PropertyValueFactory<Service, String>("serviceName"));
        colServicePrice.setCellValueFactory(new PropertyValueFactory<Service, String>("servicePrice"));

        tableService.setItems(serviceList);
    }

    private void clearService() {
        this.txtServiceID.setText("");
        this.txtServiceName.setText("");
        this.txtServicePrice.setText("");
    }

    public void handleServiceAddOpen(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/AddService.fxml"));
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

        serviceAddController = loader.getController();
        serviceAddController.setServiceController(this);
        serviceAddController.btnConfirmAddService.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                JFXButton conf = new AlertMaker().customBtn("Xác Nhận");
                String serviceName = serviceAddController.txtServiceName.getText();
                String servicePrice = serviceAddController.txtServicePrice.getText();

                if(serviceName == null || serviceName.length() <= 0) {
                    AlertMaker.showMaterialDialog(serviceAddController.stackpaneAddService, Arrays.asList(conf), "Thất Bại", "Chưa nhập tên dịch vụ");
                    return;
                }

                if(servicePrice == null || servicePrice.length() <= 0) {
                    AlertMaker.showMaterialDialog(serviceAddController.stackpaneAddService, Arrays.asList(conf), "Thất Bại", "Chưa nhập giá dịch vụ");
                    return;
                }

                try {
                    serviceProcess.insertService(serviceName, servicePrice);
                    loadService();
                    clearService();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                JFXButton agree = new AlertMaker().customBtn("Đồng Ý");
                agree.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        serviceAddController.closeStage();
                    }
                });
                AlertMaker.showMaterialDialog(serviceAddController.stackpaneAddService, Arrays.asList(agree), "Thành Công", "Thêm dịch vụ thành công");
            }
        });

        serviceAddController.btnCancelAddService.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                serviceAddController.closeStage();
            }
        });

    }

    public void handleServiceUpdate(MouseEvent mouseEvent) throws SQLException {
        JFXButton ok = new AlertMaker().customBtn("Đồng Ý");
        String header = "Thành công";
        String body = "Sửa dịch vụ thành công";

        if(this.txtServiceID.getText().isEmpty()) {
            header = "Thất bại";
            body = "Chưa chọn dịch vụ";
            AlertMaker.showMaterialDialog(stackpaneService, Arrays.asList(ok), header, body);
            return;
        }

        String serviceId = this.txtServiceID.getText();
        String serviceName = this.txtServiceName.getText();
        String servicePrice = this.txtServicePrice.getText();
        serviceProcess.updateService(serviceName, servicePrice, serviceId);
        clearService();
        loadService();

        AlertMaker.showMaterialDialog(stackpaneService, Arrays.asList(ok), header, body);
    }

    public void handleCustomerDelete(MouseEvent mouseEvent) {
        JFXButton yes = new AlertMaker().customBtn("Có");
        yes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                JFXButton ok = new AlertMaker().customBtn("Đồng Ý");
                String header = "Thành công";
                String body = "Xóa dịch vụ thành công";

                if(txtServiceID.getText().isEmpty()) {
                    header = "Thất bại";
                    body = "Chưa chọn dịch vụ";
                    AlertMaker.showMaterialDialog(stackpaneService, Arrays.asList(ok), header, body);
                    return;
                }

                String serviceID = txtServiceID.getText();

                try {
                    serviceProcess.deleteService(serviceID);
                    clearService();
                    loadService();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                AlertMaker.showMaterialDialog(stackpaneService, Arrays.asList(ok), header, body);

            }
        });

        JFXButton no = new AlertMaker().customBtn("Không");
        no.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                return;
            }
        });
        AlertMaker.showMaterialDialog(stackpaneService, Arrays.asList(yes, no), "Xác Nhận", "Bạn thật sự muốn xóa");

    }

    public void handleServiceTable(MouseEvent mouseEvent) {
        Service service = tableService.getSelectionModel().getSelectedItem();
        this.txtServiceID.setText(service.getId());
        this.txtServiceName.setText(service.getServiceName());
        this.txtServicePrice.setText(service.getServicePrice());
    }

}
