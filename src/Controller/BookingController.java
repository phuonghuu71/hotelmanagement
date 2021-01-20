package Controller;

import Database.Customer;
import Database.Booking;
import Process.*;

import com.jfoenix.controls.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import resources.AlertMaker;


import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class BookingController implements Initializable {
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

    public TableView<Booking> tableReservation;
    public TableColumn<Booking, String> colReservationID;
    public TableColumn<Booking, String> colCustomerName_Reservation;
    public TableColumn<Booking, String> colRoomName;
    public TableColumn<Booking, String> colReservationDate;
    public TableColumn<Booking, String> colCheckInDate;
    public TableColumn<Booking, String> colCheckOutDate;
    public TableColumn<Booking, String> colTotalCash;

    public TableView<Booking> tableCheckIn;
    public TableColumn<Booking, String> colCheckInID;
    public TableColumn<Booking, String> colCustomerName_CheckIn;
    public TableColumn<Booking, String> colRoomName_CheckIn;
    public TableColumn<Booking, String> colReservationDate_CheckIn;
    public TableColumn<Booking, String> colCheckInDate_CheckIn;
    public TableColumn<Booking, String> colCheckOutDate_CheckIn;
    public TableColumn<Booking, String> colTotalCash_CheckIn;

    public JFXTextField txtCustomerNameSearch;
    public JFXTextField txtTotalCash;
    public JFXTextField txtCustomerName;
    public JFXTextField txtStaffName;
    public JFXTextField txtCustomerName_list;
    public StackPane stackpaneBooking;
    public JFXToggleButton toggleReserve;
    public JFXTextField txtCustomerNameSearch_reserved;
    public JFXTextField txtCustomerNameSearch_checkin;
    public Label lblReserved;
    public Label lblCheckIn;
    public JFXTextField txtDiscount;

    ObservableList<Customer> customerList = FXCollections.observableArrayList();
    CustomerProcess customerProcess = new CustomerProcess();

    ObservableList<Booking> bookingList = FXCollections.observableArrayList();
    ObservableList<Booking> checkInList = FXCollections.observableArrayList();

    RoomTypeProcess roomTypeProcess = new RoomTypeProcess();
    RoomCapacityProcess roomCapacityProcess = new RoomCapacityProcess();
    RoomProcess roomProcess = new RoomProcess();
    StaffProcess staffProcess = new StaffProcess();
    BookingProcess bookingProcess = new BookingProcess();

    ArrayList<Integer> roomTypeIDList = new ArrayList<>();
    ArrayList<Integer> roomCapacityIDList = new ArrayList<>();

    ArrayList<Double> roomTypePriceList = new ArrayList<>();
    ArrayList<Double> roomCapacityPriceList = new ArrayList<>();

    long date_diff = 0;
    double money = 0;
    double roomTypePrice = 0;
    double roomCapacityPrice = 0;

    private LoginController loginController;

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void setStaffName(String text) {
        txtStaffName.setText(text);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            showCb();
            loadCustomerList();
            loadReservationList();
            loadCheckInList();
            setDate();
            setCbProperty();
            priceProperty(txtDiscount);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void setDate() {
        LocalDate date_now = LocalDate.now();
        dtpReverse.setValue(date_now);
        dtpReverse.setDisable(true);
        dtpCheckIn.setValue(date_now);
        dtpCheckOut.setValue(date_now);

        date_diff = ChronoUnit.DAYS.between(dtpCheckIn.getValue(), dtpCheckOut.getValue());
        txtTotalCash.setText(Long.toString(date_diff));


        dtpCheckIn.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observableValue, LocalDate localDate, LocalDate t1) {
                date_diff = ChronoUnit.DAYS.between(dtpCheckIn.getValue(), dtpCheckOut.getValue());
                money = (roomTypePrice+roomCapacityPrice)*date_diff;
                txtTotalCash.setText(currencyChange(money));
            }
        });

        dtpCheckOut.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observableValue, LocalDate localDate, LocalDate t1) {
                date_diff = ChronoUnit.DAYS.between(dtpCheckIn.getValue(), dtpCheckOut.getValue());
                money = (roomTypePrice+roomCapacityPrice)*date_diff;
                txtTotalCash.setText(currencyChange(money));
            }
        });

    }

    private void setCbProperty() {
        cbRoomType.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                cbRoomName.getSelectionModel().select(-1);
            }
        });
        cbRoomCapacity.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                cbRoomName.getSelectionModel().select(-1);
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

    void loadCustomerList() throws SQLException {
        if(tableCustomer != null) {
            customerList.clear();
        }
        ResultSet result = customerProcess.getCustomerList();
        while (result.next()) {
            customerList.add(new Customer(result.getString("MAKH"), result.getString("TENKH"), result.getString("SOCMND"), result.getString("SODT")));
        }

        colCustomerID.setCellValueFactory(new PropertyValueFactory<Customer, String>("id"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
        colCustomerPhoneNumber.setCellValueFactory(new PropertyValueFactory<Customer, String>("phoneNumber"));

        tableCustomer.setItems(customerList);

        searchCustomer();
    }

    void loadReservationList() throws SQLException {
        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if(tableReservation != null) {
            bookingList.clear();
        }
        ResultSet result = bookingProcess.getReserveInfo();
        while (result.next()) {
            bookingList.add(new Booking(result.getString("SOPHIEUDP"), result.getString("TENKH"), result.getString("TENPHONG"), date_formatter.format(LocalDate.parse(result.getString("NGAYDATPHONG").substring(0, 10))), date_formatter.format(LocalDate.parse(result.getString("NGAYNHANPHONG").substring(0, 10))), date_formatter.format(LocalDate.parse(result.getString("NGAYTRAPHONG").substring(0, 10))), currencyChange(Double.parseDouble(result.getString("TONGTIEN"))), result.getString("MAHD")));
        }

        colReservationID.setCellValueFactory(new PropertyValueFactory<Booking, String>("id"));
        colCustomerName_Reservation.setCellValueFactory(new PropertyValueFactory<Booking, String>("customerName"));
        colRoomName.setCellValueFactory(new PropertyValueFactory<Booking, String>("roomName"));
        colReservationDate.setCellValueFactory(new PropertyValueFactory<Booking, String>("reservationDate"));
        colCheckInDate.setCellValueFactory(new PropertyValueFactory<Booking, String>("checkInDate"));
        colCheckOutDate.setCellValueFactory(new PropertyValueFactory<Booking, String>("checkOutDate"));
        colTotalCash.setCellValueFactory(new PropertyValueFactory<Booking, String>("totalCash"));

        tableReservation.setItems(bookingList);

        searchCustomer();
    }

    void loadCheckInList() throws SQLException {
        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if(tableCheckIn != null) {
            checkInList.clear();
        }
        ResultSet result = bookingProcess.getCheckInInfo();
        while (result.next()) {
            checkInList.add(new Booking(result.getString("SOPHIEUDP"), result.getString("TENKH"), result.getString("TENPHONG"), date_formatter.format(LocalDate.parse(result.getString("NGAYDATPHONG").substring(0, 10))), result.getString("NGAYNHANPHONG"), date_formatter.format(LocalDate.parse(result.getString("NGAYTRAPHONG").substring(0, 10))), currencyChange(Double.parseDouble(result.getString("TONGTIEN"))), result.getString("MAHD")));
        }

        colCheckInID.setCellValueFactory(new PropertyValueFactory<Booking, String>("id"));
        colCustomerName_CheckIn.setCellValueFactory(new PropertyValueFactory<Booking, String>("customerName"));
        colRoomName_CheckIn.setCellValueFactory(new PropertyValueFactory<Booking, String>("roomName"));
        colReservationDate_CheckIn.setCellValueFactory(new PropertyValueFactory<Booking, String>("reservationDate"));
        colCheckInDate_CheckIn.setCellValueFactory(new PropertyValueFactory<Booking, String>("checkInDate"));
        colCheckOutDate_CheckIn.setCellValueFactory(new PropertyValueFactory<Booking, String>("checkOutDate"));
        colTotalCash_CheckIn.setCellValueFactory(new PropertyValueFactory<Booking, String>("totalCash"));

        tableCheckIn.setItems(checkInList);

        searchCustomer();
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


        FilteredList<Booking> filteredList_reserved = new FilteredList<>(bookingList, b->true);
        txtCustomerNameSearch_reserved.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList_reserved.setPredicate(customer -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (customer.getCustomerName().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true; // Filter matches name.
                else if (customer.getRoomName().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;
                else
                    return false; // Does not match.
            });
        });
        SortedList<Booking> sortedData_reserved = new SortedList<>(filteredList_reserved);
        sortedData_reserved.comparatorProperty().bind(tableReservation.comparatorProperty());
        tableReservation.setItems(sortedData_reserved);

        FilteredList<Booking> filteredList_checkin = new FilteredList<>(checkInList, b->true);
        txtCustomerNameSearch_checkin.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList_checkin.setPredicate(customer -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (customer.getCustomerName().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true; // Filter matches name.
                else if (customer.getRoomName().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;
                else
                    return false; // Does not match.
            });
        });
        SortedList<Booking> sortedData_checkin = new SortedList<>(filteredList_checkin);
        sortedData_checkin.comparatorProperty().bind(tableCheckIn.comparatorProperty());
        tableCheckIn.setItems(sortedData_checkin);

    }

    private String getStaffIdByName(String fullname) throws SQLException {
        ResultSet getStaffId = staffProcess.getStaffInfoByFullname(fullname);
        String staffId = null;
        while(getStaffId.next()) {
            staffId = getStaffId.getString("MANV");
        }
        return staffId;
    }

    private String getRoomIdByName(String roomName) throws SQLException {
        ResultSet getRoomId = roomProcess.getRoomIdByRoomName(roomName);
        String roomId = null;
        while(getRoomId.next()) {
            roomId = getRoomId.getString("MAPHONG");
        }
        return roomId;
    }

    private String currencyChange(double curr) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(curr)+ " VNĐ";
    }

    private void clearInfo() {
        txtCustomerName.setText("");
        cbRoomName.setValue("");
        cbRoomCapacity.setValue("");
        cbRoomType.setValue("");
        dtpReverse.setValue(LocalDate.now());
        dtpCheckIn.setValue(LocalDate.now());
        dtpCheckOut.setValue(LocalDate.now());
    }

    private boolean dateCheck() {
        JFXButton conf_false = new AlertMaker().customBtn("Xác Nhận");
        if(dtpReverse.getValue().isBefore(LocalDate.now())) {
            AlertMaker.showMaterialDialog(stackpaneBooking, Arrays.asList(conf_false), "Thất bại", "Ngày đặt phòng không hợp lệ");
            return false;
        }
        if(dtpCheckIn.getValue().isBefore(LocalDate.now())) {
            AlertMaker.showMaterialDialog(stackpaneBooking, Arrays.asList(conf_false), "Thất bại", "Ngày nhận phòng không hợp lệ");
            return false;
        }
        if(dtpCheckOut.getValue().isBefore(LocalDate.now()) || dtpCheckOut.getValue().isEqual(LocalDate.now())) {
            AlertMaker.showMaterialDialog(stackpaneBooking, Arrays.asList(conf_false), "Thất bại", "Ngày trả phòng không hợp lệ");
            return false;
        }

        if(dtpCheckIn.getValue().isAfter(dtpCheckOut.getValue())) {
            AlertMaker.showMaterialDialog(stackpaneBooking, Arrays.asList(conf_false), "Thất bại", "Ngày nhận phòng phải lớn hơn ngày trả phòng");
            return false;
        }

        return true;
    }

    private boolean checkInput() {
        if(txtCustomerName.getText().equals("") || txtCustomerName.getText().length() <= 0) {
            return false;
        }
        if(cbRoomName.getSelectionModel().isEmpty()) {
            return false;
        }
        if(cbRoomCapacity.getSelectionModel().isEmpty()) {
            return false;
        }
        if(cbRoomType.getSelectionModel().isEmpty()) {
            return false;
        }
        return true;
    }

    private void reserveAction() throws SQLException {

        if(!checkInput()) {
            JFXButton conf_false = new AlertMaker().customBtn("Xác Nhận");
            AlertMaker.showMaterialDialog(stackpaneBooking, Arrays.asList(conf_false), "Thất bại", "Chưa nhập đủ thông tin");
            return;
        }

        if(!dateCheck()) {
            return;
        }

        if(Double.parseDouble(txtDiscount.getText()) >= 100) {
            JFXButton conf_false = new AlertMaker().customBtn("Xác Nhận");
            AlertMaker.showMaterialDialog(stackpaneBooking, Arrays.asList(conf_false), "Thất bại", "Giảm giá quá cao");
            return;
        }

        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter hour_formatter = DateTimeFormatter.ofPattern("kk:mm");

        String roomId = getRoomIdByName(cbRoomName.getSelectionModel().getSelectedItem().toString());
        String customerId = tableCustomer.getSelectionModel().getSelectedItem().getId();
        String staffId = getStaffIdByName(txtStaffName.getText());

        String reservationDate = date_formatter.format(dtpReverse.getValue());
        String checkInDate = date_formatter.format(dtpCheckIn.getValue());
        String checkOutDate = date_formatter.format(dtpCheckOut.getValue());

        bookingProcess.insertBooking(reservationDate,checkInDate,checkOutDate,customerId,staffId);
        bookingProcess.insertBookingService(roomId);

        String bookingId = null;

        ResultSet getLastBooking = bookingProcess.getLastBooking();
        while(getLastBooking.next()) {
            bookingId = getLastBooking.getString("SOPHIEUDP");
        }

        bookingProcess.insertBookingDetail(roomId,bookingId);

        String bookingServiceId = null;

        ResultSet getLastBookingService = bookingProcess.getLastBookingService();
        while(getLastBookingService.next()) {
            bookingServiceId = getLastBookingService.getString("MAPDV");
        }

        bookingProcess.insertBookingBill(reservationDate,bookingId,bookingServiceId, Double.toString(money-money*Double.parseDouble(txtDiscount.getText())/100));

        bookingProcess.updateBookingReservation(roomId);

        loadCustomerList();
        loadReservationList();
        loadCheckInList();
        clearInfo();

        JFXButton conf = new AlertMaker().customBtn("Đồng Ý");
        AlertMaker.showMaterialDialog(stackpaneBooking, Arrays.asList(conf), "Thành Công", "Đặt trước thành công");

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


    private void checkInAction() throws SQLException {

        if(!checkInput()) {
            JFXButton conf_false = new AlertMaker().customBtn("Xác Nhận");
            AlertMaker.showMaterialDialog(stackpaneBooking, Arrays.asList(conf_false), "Thất bại", "Chưa nhập đủ thông tin");
            return;
        }

        if(!dateCheck()) {
            return;
        }

        if(Double.parseDouble(txtDiscount.getText()) >= 100) {
            JFXButton conf_false = new AlertMaker().customBtn("Xác Nhận");
            AlertMaker.showMaterialDialog(stackpaneBooking, Arrays.asList(conf_false), "Thất bại", "Giảm giá quá cao");
            return;
        }

        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter hour_formatter = DateTimeFormatter.ofPattern("kk:mm");

        String info = "Nhận phòng thành công. ";

        double discount = 0;

        LocalTime hour_now = LocalTime.now();
        LocalDate date_now = LocalDate.now();
        String hour_now_string = hour_formatter.format(hour_now);
        String date_now_string = date_formatter.format(date_now);
        String checkInDate = date_now_string+" "+hour_now_string;
        String checkOutDate = date_formatter.format(dtpCheckOut.getValue());

        String customerId = tableCustomer.getSelectionModel().getSelectedItem().getId();
        String staffId = getStaffIdByName(txtStaffName.getText());


        if(hour_now.getHour() >= 5 && hour_now.getHour() < 9) {
            discount = 0.5;
            info += "Giảm 50% (5h - 9h)";
        }
        else if(hour_now.getHour() >=9 && hour_now.getHour() < 14) {
            discount = 0.3;
            info += "Giảm 30% (9h - 14h)";
        }

        if(Double.parseDouble(txtDiscount.getText()) > 0) {
            info += "Đã giảm giá thêm "+txtDiscount.getText()+"%";
        }


        String roomName = cbRoomName.getSelectionModel().getSelectedItem().toString();

        ResultSet getRoomPriceByName = bookingProcess.getRoomPriceByRoomName(roomName);
        double roomTypePrice = 0;
        double roomCapacityPrice = 0;
        while(getRoomPriceByName.next()) {
            roomTypePrice = Double.parseDouble(getRoomPriceByName.getString("GIALOAIPHONG"));
            roomCapacityPrice = Double.parseDouble(getRoomPriceByName.getString("GIASUCCHUA"));
        }

//        date_diff = ChronoUnit.DAYS.between(date_now, dtpCheckOut.getValue());

        double roomPrice = roomTypePrice + roomCapacityPrice;
        double roomPrice_tmp = roomPrice;
        roomPrice_tmp = roomPrice_tmp*date_diff - roomPrice*discount - roomPrice*Double.parseDouble(txtDiscount.getText())/100;
        roomPrice = roomPrice_tmp;

        String roomId = getRoomIdByName(roomName);

        bookingProcess.insertBooking(date_formatter.format(date_now),checkInDate,checkOutDate,customerId,staffId);
        bookingProcess.insertBookingService(roomId);

        String bookingId = null;

        ResultSet getLastBooking = bookingProcess.getLastBooking();
        while(getLastBooking.next()) {
            bookingId = getLastBooking.getString("SOPHIEUDP");
        }

        bookingProcess.insertBookingDetail(roomId,bookingId);

        String bookingServiceId = null;

        ResultSet getLastBookingService = bookingProcess.getLastBookingService();
        while(getLastBookingService.next()) {
            bookingServiceId = getLastBookingService.getString("MAPDV");
        }


        bookingProcess.insertBookingBill(date_formatter.format(date_now),bookingId,bookingServiceId,Double.toString(roomPrice));

        bookingProcess.updateBookingChecIn(roomId);

        loadCustomerList();
        loadReservationList();
        loadCheckInList();
        clearInfo();

        JFXButton conf = new AlertMaker().customBtn("Đồng Ý");
        AlertMaker.showMaterialDialog(stackpaneBooking, Arrays.asList(conf), "Thành Công", info);
    }

    public void handleBooking(MouseEvent mouseEvent) throws SQLException {
        if(toggleReserve.isSelected()) {
            checkInAction();
        }
        else {
            reserveAction();
        }
    }

    public void handleCheckIn(MouseEvent mouseEvent) throws SQLException {
        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter hour_formatter = DateTimeFormatter.ofPattern("kk:mm");

        String info = "Nhận phòng thành công. ";

        double discount = 0;

        LocalTime hour_now = LocalTime.now();
        String hour_now_string = hour_formatter.format(hour_now);

        if(hour_now.getHour() >= 5 && hour_now.getHour() < 9) {
            discount = 0.5;
            info += "Giảm 50% (5h - 9h)";
        }
        else if(hour_now.getHour() >=9 && hour_now.getHour() < 14) {
            discount = 0.3;
            info += "Giảm 30% (9h - 14h)";
        }

        String roomName = tableReservation.getSelectionModel().getSelectedItem().getRoomName();
        String bookingId = tableReservation.getSelectionModel().getSelectedItem().getId();

        ResultSet getRoomPriceByName = bookingProcess.getRoomPriceByRoomName(roomName);
        double roomTypePrice = 0;
        double roomCapacityPrice = 0;
        while(getRoomPriceByName.next()) {
            roomTypePrice = Double.parseDouble(getRoomPriceByName.getString("GIALOAIPHONG"));
            roomCapacityPrice = Double.parseDouble(getRoomPriceByName.getString("GIASUCCHUA"));
        }

        double roomPrice = roomTypePrice + roomCapacityPrice;
        double roomPrice_tmp = roomPrice;

        LocalDate date_now = LocalDate.now();
        LocalDate checkOutDate = LocalDate.parse(tableReservation.getSelectionModel().getSelectedItem().getCheckOutDate().substring(0, 10), date_formatter);

        double date_diff = ChronoUnit.DAYS.between(date_now, checkOutDate);

        roomPrice_tmp = roomPrice_tmp*date_diff - roomPrice*discount;
        roomPrice = roomPrice_tmp;

        String roomId = getRoomIdByName(roomName);

        bookingProcess.updateBookingChecIn(roomId);

        bookingProcess.updateBillTotalCashByBookingId(Double.toString(roomPrice), bookingId);

        bookingProcess.updateCheckInDateByBookingId(date_formatter.format(date_now)+" "+hour_now_string, bookingId);

        loadCustomerList();
        loadReservationList();
        loadCheckInList();

        JFXButton conf = new AlertMaker().customBtn("Đồng Ý");
        AlertMaker.showMaterialDialog(stackpaneBooking, Arrays.asList(conf), "Thành Công", info);

    }

    public void handleTableReservation(MouseEvent mouseEvent) {
        String customerName = tableReservation.getSelectionModel().getSelectedItem().getCustomerName();
        txtCustomerName_list.setText(customerName);
    }

    public void handleReserve(MouseEvent mouseEvent) {
        if(toggleReserve.isSelected()) {
            dtpCheckIn.setValue(LocalDate.now());
            toggleReserve.setText("Nhận phòng");
            btnBooking.setText("NHẬN PHÒNG");
            dtpReverse.setVisible(false);
            dtpCheckIn.setVisible(false);
            lblReserved.setVisible(false);
            lblCheckIn.setVisible(false);
        }
        else {
            toggleReserve.setText("Đặt trước phòng");
            btnBooking.setText("ĐẶT TRƯỚC PHÒNG");
            dtpReverse.setVisible(true);
            dtpCheckIn.setVisible(true);
            lblReserved.setVisible(true);
            lblCheckIn.setVisible(true);
        }
    }

    public void handleCancelReservation(MouseEvent mouseEvent) throws SQLException {
        String bookingID = tableReservation.getSelectionModel().getSelectedItem().getId();
        String roomID = getRoomIdByName(tableReservation.getSelectionModel().getSelectedItem().getRoomName());
        bookingProcess.deleteBookingDetailByBookingID(bookingID);
        bookingProcess.deleteBookingBillByBookingID(bookingID);
        bookingProcess.deleteBookingByBookingID(bookingID);
        bookingProcess.deleteBookingServiceByRoomID(roomID);
        roomProcess.updateRoomStatusCheckOut(roomID);
        loadReservationList();
    }

    public void handleCustomer(MouseEvent mouseEvent) {
        String customerName = tableCustomer.getSelectionModel().getSelectedItem().getCustomerName();
        this.txtCustomerName.setText(customerName);
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
        this.txtTotalCash.setText(currencyChange(money));

    }

    public void handleCancleBooking(MouseEvent mouseEvent) {
        clearInfo();
    }
}



