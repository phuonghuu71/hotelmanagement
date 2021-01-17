package Controller;

import Database.Booking;
import Process.BookingProcess;
import Process.RoomProcess;
import Process.ServiceProcess;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class CheckOutController implements Initializable {

    public JFXTimePicker dtpHourNow;
    public JFXDatePicker dtpDateNow;

    public TableView<Booking> tableCheckOut;
    public TableColumn<Booking, String> colCheckOutID;
    public TableColumn<Booking, String> colCustomerName;
    public TableColumn<Booking, String> colRoomName;

    public JFXDatePicker dtpReverse;
    public JFXDatePicker dtpCheckIn;
    public JFXDatePicker dtpCheckOut;
    public JFXTextField txtTotalCash;
    public JFXTextField txtServicePrice;
    public JFXTextField txtSurcharge;

    ObservableList<Booking> checkOutList = FXCollections.observableArrayList();
    BookingProcess bookingProcess = new BookingProcess();
    RoomProcess roomProcess = new RoomProcess();
    ServiceProcess serviceProcess = new ServiceProcess();

    private String billID = "";
    private double money = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LocalDate date_now = LocalDate.now();
        LocalTime hour_now = LocalTime.now();
        dtpDateNow.setValue(date_now);
        dtpHourNow.setValue(hour_now);
        try {
            loadCheckOutList();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void handleTableCheckOut(MouseEvent mouseEvent) throws ParseException, SQLException {
        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter hour_formatter = DateTimeFormatter.ofPattern("kk:mm");
        DateTimeFormatter date_hour_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String reservationDate_string = tableCheckOut.getSelectionModel().getSelectedItem().getReservationDate().substring(0,10);
        String checkInDate_string = tableCheckOut.getSelectionModel().getSelectedItem().getCheckInDate().substring(0,10);
        String checkOutDate_string = tableCheckOut.getSelectionModel().getSelectedItem().getCheckOutDate().substring(0,10);

        String billID = tableCheckOut.getSelectionModel().getSelectedItem().getBillID();

        double totalCash = Double.parseDouble(tableCheckOut.getSelectionModel().getSelectedItem().getTotalCash());

        LocalDate reservationDate = LocalDate.parse(reservationDate_string, date_formatter);
        LocalDate checkInDate = LocalDate.parse(checkInDate_string, date_formatter);
        LocalDate checkOutDate = LocalDate.parse(checkOutDate_string, date_formatter);

        LocalDate dateNow = dtpDateNow.getValue();
        LocalTime hourNow = dtpHourNow.getValue();

        long date_diff = ChronoUnit.DAYS.between(checkOutDate, dateNow);

        ResultSet getRoomType = roomProcess.getRoomTypeByRoomName(tableCheckOut.getSelectionModel().getSelectedItem().getRoomName());
        ResultSet getRoomCapacity = roomProcess.getRoomCapacityByRoomName(tableCheckOut.getSelectionModel().getSelectedItem().getRoomName());
        ResultSet getService = serviceProcess.getServiceListByBillID(billID);


        double roomTypePrice = 0;
        double roomCapacityPrice = 0;
        double roomPrice = 0;
        double service = 0;

        while(getRoomType.next()) {
            roomTypePrice = Double.parseDouble(getRoomType.getString("GIALOAIPHONG"));
        }

        while(getRoomCapacity.next()) {
            roomCapacityPrice = Double.parseDouble(getRoomCapacity.getString("GIASUCCHUA"));
        }

        while(getService.next()) {
            service += Double.parseDouble(getService.getString("SOLUONG"))*Double.parseDouble(getService.getString("GIADV"));
        }

        roomPrice = roomTypePrice + roomCapacityPrice;

        double surcharge = 0;
        String surcharge_text = "0%";

        if(date_diff == 0) {
            if (hourNow.getHour() >= 12 && hourNow.getHour() < 15) {
                surcharge = 0.3;
                surcharge_text = "30%";
            }
            else if (hourNow.getHour() >= 15 && hourNow.getHour() < 18) {
                surcharge = 0.5;
                surcharge_text = "50%";
            }
            else if (hourNow.getHour() >= 18) {
                surcharge = 1;
                surcharge_text = "100%";
            }
        }
        else if(date_diff > 0) {
            surcharge_text = "Trả phòng trễ "+date_diff+" ngày";
            roomPrice *= date_diff;
            surcharge = 1;
        }

        dtpReverse.setValue(reservationDate);
        dtpCheckIn.setValue(checkInDate);
        dtpCheckOut.setValue(checkOutDate);


        txtSurcharge.setText(surcharge_text);
        txtServicePrice.setText(Double.toString(service));
        txtTotalCash.setText(Double.toString(totalCash)+" + "+roomPrice*surcharge+" + "+Double.toString(service));
        totalCash += roomPrice*surcharge + service;
        money = totalCash;
    }

    public void handleCheckOut(MouseEvent mouseEvent) throws SQLException {
        String bookingID = tableCheckOut.getSelectionModel().getSelectedItem().getId();
        ResultSet getRoomID = roomProcess.getRoomIdByRoomName(tableCheckOut.getSelectionModel().getSelectedItem().getRoomName());
        String roomID = "";
        while (getRoomID.next()) {
            roomID = getRoomID.getString("MAPHONG");
        }
        bookingProcess.updateBillTotalCashByBookingId(Double.toString(money), bookingID);
        roomProcess.updateRoomStatusCheckOut(roomID);

        String billID = tableCheckOut.getSelectionModel().getSelectedItem().getBillID();

        ResultSet getServiceBillId = serviceProcess.getServiceBillIdByBillId(billID);
        String serviceBillId = "";
        while(getServiceBillId.next()) {
            serviceBillId = getServiceBillId.getString("MAPDV");
        }

        serviceProcess.deleteDetailServiceByServiceBillId(serviceBillId);

        loadCheckOutList();

    }

    void loadCheckOutList() throws SQLException {
        if(tableCheckOut != null) {
            checkOutList.clear();
        }
        ResultSet result = bookingProcess.getCheckInInfo();
        while (result.next()) {
            checkOutList.add(new Booking(result.getString("SOPHIEUDP"), result.getString("TENKH"), result.getString("TENPHONG"), result.getString("NGAYDATPHONG"), result.getString("NGAYNHANPHONG"), result.getString("NGAYTRAPHONG"), result.getString("TONGTIEN"), result.getString("MAHD")));
        }

        colCheckOutID.setCellValueFactory(new PropertyValueFactory<Booking, String>("billID"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<Booking, String>("customerName"));
        colRoomName.setCellValueFactory(new PropertyValueFactory<Booking, String>("roomName"));

        tableCheckOut.setItems(checkOutList);


    }




}
