package Controller;

import Database.Customer;
import Database.RoomType;
import Process.*;

import com.jfoenix.controls.*;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;


import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BookingController implements Initializable {
    public JFXToggleButton toggleReverse;
    public JFXDatePicker dtpReverse;
    public JFXDatePicker dtpCheckIn;
    public JFXDatePicker dtpCheckOut;
    public JFXComboBox cbRoomType;
    public JFXComboBox cbRoomCapacity;
    public JFXComboBox cbRoomName;
    public JFXButton btnBooking;
    public JFXButton btnCancel;
//    public JFXTimePicker dtpReverseTime;
//    public JFXTimePicker dtpCheckInTime;

    public TableView<Customer> tableCustomer;
    public TableColumn<Customer, String> colCustomerID;
    public TableColumn<Customer, String> colCustomerName;
    public TableColumn<Customer, String> colCustomerPhoneNumber;

    public JFXTextField txtCustomerNameSearch;
    public JFXTextField txtTotalCash;
    public JFXTextField txtCustomerName;
    ObservableList<Customer> customerList = FXCollections.observableArrayList();
    CustomerProcess customerProcess = new CustomerProcess();

    RoomTypeProcess roomTypeProcess = new RoomTypeProcess();
    RoomCapacityProcess roomCapacityProcess = new RoomCapacityProcess();
    RoomProcess roomProcess = new RoomProcess();

    ArrayList<Integer> roomTypeIDList = new ArrayList<>();
    ArrayList<Integer> roomCapacityIDList = new ArrayList<>();

    ArrayList<Double> roomTypePriceList = new ArrayList<>();
    ArrayList<Double> roomCapacityPriceList = new ArrayList<>();

    long date_diff = 0;
    double money = 0;
    double roomTypePrice = 0;
    double roomCapacityPrice = 0;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            showCb();
            loadCustomerList();
            searchCustomer();
            setDate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void setDate() {
        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter hour_formatter = DateTimeFormatter.ofPattern("kk:mm");



        LocalDate date_now = LocalDate.now();
        dtpReverse.setValue(date_now);
        dtpCheckIn.setValue(date_now);
        dtpCheckOut.setValue(date_now);

        date_diff = ChronoUnit.DAYS.between(dtpCheckIn.getValue(), dtpCheckOut.getValue())+1;
        txtTotalCash.setText(Long.toString(date_diff));

        dtpCheckIn.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observableValue, LocalDate localDate, LocalDate t1) {
                date_diff = ChronoUnit.DAYS.between(dtpCheckIn.getValue(), dtpCheckOut.getValue())+1;
                money = (roomTypePrice+roomCapacityPrice)*date_diff;
                txtTotalCash.setText(Double.toString(money));
            }
        });

        dtpCheckOut.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observableValue, LocalDate localDate, LocalDate t1) {
                date_diff = ChronoUnit.DAYS.between(dtpCheckIn.getValue(), dtpCheckOut.getValue())+1;
                money = (roomTypePrice+roomCapacityPrice)*date_diff;
                txtTotalCash.setText(Double.toString(money));
            }
        });

    }

    private void loadCbRoomType() throws SQLException {
        ResultSet getRoomTypeList = roomTypeProcess.getRoomTypeList();
        while(getRoomTypeList.next()) {
            String roomTypeID = getRoomTypeList.getString("MALOAIPHONG").trim();
            roomTypeIDList.add(Integer.parseInt(roomTypeID));

            String roomTypePrice = getRoomTypeList.getString("GIALOAIPHONG").trim();
            roomTypePriceList.add(Double.parseDouble(roomTypePrice));

            cbRoomType.getItems().add(getRoomTypeList.getString("TENLOAIPHONG"));
        }
    }

    private void loadCbRoomCapacity() throws SQLException {
        ResultSet getRoomCapacityList = roomCapacityProcess.getRoomCapacityList();
        while(getRoomCapacityList.next()) {
            String roomCapacityID = getRoomCapacityList.getString("MASUCCHUA").trim();
            roomCapacityIDList.add(Integer.parseInt(roomCapacityID));

            String roomCapacityPrice = getRoomCapacityList.getString("GIASUCCHUA").trim();
            roomCapacityPriceList.add(Double.parseDouble(roomCapacityPrice));

            cbRoomCapacity.getItems().add(getRoomCapacityList.getString("SOGIUONG"));
        }
    }

    private void showCb() throws SQLException {
        loadCbRoomType();
        loadCbRoomCapacity();
    }

    public void handleRoomName(MouseEvent mouseEvent) throws SQLException {
        cbRoomName.getItems().clear();
        int roomCapacityID = roomCapacityIDList.get(cbRoomCapacity.getSelectionModel().getSelectedIndex());
        int roomTypeID = roomTypeIDList.get(cbRoomType.getSelectionModel().getSelectedIndex());
        ResultSet getRoomlist = roomProcess.getRoomEmptyListByRoomTypeIDAndRoomCapacityID(Integer.toString(roomTypeID), Integer.toString(roomCapacityID));
        while(getRoomlist.next()) {
            cbRoomName.getItems().add(getRoomlist.getString(1));
        }

        roomTypePrice = roomTypePriceList.get(cbRoomType.getSelectionModel().getSelectedIndex());
        roomCapacityPrice = roomCapacityPriceList.get(cbRoomCapacity.getSelectionModel().getSelectedIndex());
        money = (roomTypePrice+roomCapacityPrice)*date_diff;
        this.txtTotalCash.setText(Double.toString(money));

    }

    void loadCustomerList() throws SQLException {
        if(tableCustomer != null) {
            tableCustomer.getItems().clear();
        }
        ResultSet result = customerProcess.getCustomerList();
        while (result.next()) {
            customerList.add(new Customer(result.getString("MAKH"), result.getString("TENKH"), result.getString("SOCMND"), result.getString("SODT")));
        }

        colCustomerID.setCellValueFactory(new PropertyValueFactory<Customer, String>("id"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
        colCustomerPhoneNumber.setCellValueFactory(new PropertyValueFactory<Customer, String>("phoneNumber"));

        tableCustomer.setItems(customerList);
    }

    private void searchCustomer() {
        FilteredList<Customer> filteredList = new FilteredList<>(customerList, b->true);
        txtCustomerNameSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(customer -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (customer.getCustomerName().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true; // Filter matches name.
                else if (customer.getPhoneNumber().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;
                else
                    return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Customer> sortedData = new SortedList<>(filteredList);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(tableCustomer.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tableCustomer.setItems(sortedData);

    }

    public void handleCustomer(MouseEvent mouseEvent) {
        String customerName = tableCustomer.getSelectionModel().getSelectedItem().getCustomerName();
        this.txtCustomerName.setText(customerName);
    }

}
