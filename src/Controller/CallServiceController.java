package Controller;

import Database.Booking;
import Database.CallService;
import Database.Room;
import Database.Service;
import Process.ServiceProcess;
import  Process.RoomProcess;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import resources.AlertMaker;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.ResourceBundle;

public class CallServiceController implements Initializable {


    public TableView<CallService> tableService;
    public TableColumn<CallService, String> colServiceID;
    public TableColumn<CallService, String> colRoomName;
    public TableColumn<CallService, String> colServiceName_Customer;
    public  TableColumn<CallService, String> colQuantity;
    public TableColumn<CallService, String> colServicePrice_Customer;
    public JFXComboBox cbServiceName;
    public JFXComboBox cbRoomName;
    public JFXTextField txtServicePrice;
    public Spinner numNumber;

    ObservableList<CallService> callserviceList = FXCollections.observableArrayList();
    ObservableList<Service> serviceList = FXCollections.observableArrayList();

    ServiceProcess serviceProcess = new ServiceProcess();
    RoomProcess roomProcess = new RoomProcess();

    String bookedID = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadService();
            loadCb();
            loadnum();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    void loadCb() throws SQLException {
        loadServiceCb();
        loadBookedRoomCb();
    }

    void loadServiceCb() throws SQLException {
        ResultSet getServiceList = serviceProcess.getService();
        while (getServiceList.next()) {
            cbServiceName.getItems().add(getServiceList.getString("TENDV"));
            serviceList.add(new Service(getServiceList.getString("MADV"), getServiceList.getString("TENDV"), getServiceList.getString("GIADV")));
        }

        cbServiceName.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                String servicePrice = serviceList.get(cbServiceName.getSelectionModel().getSelectedIndex()).getServicePrice();
                txtServicePrice.setText(servicePrice);
            }
        });


    }

    void loadBookedRoomCb() throws SQLException {
        ResultSet getBookedRoom = roomProcess.getRoomBooked();
        while (getBookedRoom.next()) {
            cbRoomName.getItems().add(getBookedRoom.getString("TENPHONG"));
        }
    }

    void loadnum() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000, 1);
        numNumber.setValueFactory(valueFactory);
    }

    void loadService() throws SQLException {

        if(tableService != null) {
            callserviceList.clear();
        }
        ResultSet result = serviceProcess.getCallService();
        while (result.next()) {
            callserviceList.add(new CallService(result.getString("MACHITIETSDDV"), result.getString("TENPHONG"), result.getString("TENDV"), result.getString("SOLUONG"), currencyChange(Double.parseDouble(result.getString("GIADV")))));
        }

        colServiceID.setCellValueFactory(new PropertyValueFactory<CallService, String>("id"));
        colRoomName.setCellValueFactory(new PropertyValueFactory<CallService, String>("roomName"));
        colServiceName_Customer.setCellValueFactory(new PropertyValueFactory<CallService, String>("serviceName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<CallService, String>("quantity"));
        colServicePrice_Customer.setCellValueFactory(new PropertyValueFactory<CallService, String>("servicePrice"));

        tableService.setItems(callserviceList);
    }

    private String currencyChange(double curr) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(curr)+ " VNĐ";
    }


    public void handleServiceTable(MouseEvent mouseEvent) {
        bookedID = tableService.getSelectionModel().getSelectedItem().getId();
    }

    public void handleAddService(MouseEvent mouseEvent) throws SQLException {
        ResultSet getRoomId = roomProcess.getRoomIdByRoomName(cbRoomName.getSelectionModel().getSelectedItem().toString());
        String roomId = "";
        while (getRoomId.next()) {
            roomId = getRoomId.getString(1);
        }
        String serviceId = "";
        ResultSet getServiceId = serviceProcess.getServiceByServiceName(cbServiceName.getSelectionModel().getSelectedItem().toString());
        while (getServiceId.next()) {
            serviceId = getServiceId.getString("MADV");
        }

        String serviceBookedId = "";

        ResultSet getServiceBookedId = serviceProcess.getServiceBillByRoomId(roomId);
        while (getServiceBookedId.next()) {
            serviceBookedId = getServiceBookedId.getString(1);
        }

        serviceProcess.insertBookedServiceBill(serviceBookedId, serviceId, numNumber.getValue().toString());

        loadService();

    }


    public void handleServiceDelete(MouseEvent mouseEvent) throws SQLException {
        serviceProcess.deleteBookedServiceBill(bookedID);
        loadService();

    }
}
