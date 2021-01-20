package Controller;

import Database.BillService;
import Database.Booking;
import Process.BookingProcess;
import Process.RoomProcess;
import Process.ServiceProcess;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.jfoenix.controls.JFXButton;
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
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import resources.AlertMaker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
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
    public StackPane stackpaneCheckout;

    ObservableList<Booking> checkOutList = FXCollections.observableArrayList();
    ObservableList<BillService> billServiceList = FXCollections.observableArrayList();
    BookingProcess bookingProcess = new BookingProcess();
    RoomProcess roomProcess = new RoomProcess();
    ServiceProcess serviceProcess = new ServiceProcess();


    private String billID = "";
    private double getRoomPrice = 0.0;
    private double money = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LocalDate date_now = LocalDate.now();
        LocalTime hour_now = LocalTime.now();
        dtpDateNow.setValue(date_now);
        dtpHourNow.setValue(hour_now);
        dtpReverse.setValue(date_now);
        dtpCheckIn.setValue(date_now);
        dtpCheckOut.setValue(date_now);
        try {
            loadCheckOutList();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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

    private void clearInfo() {
        dtpReverse.setValue(LocalDate.now());
        dtpCheckIn.setValue(LocalDate.now());
        dtpCheckOut.setValue(LocalDate.now());
        txtSurcharge.setText("");
        txtServicePrice.setText("");
        txtTotalCash.setText("");
        tableCheckOut.getSelectionModel().clearAndSelect(-1);
    }

    private String currencyChange(double curr) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(curr)+ " VNĐ";
    }


    public void handleTableCheckOut(MouseEvent mouseEvent) throws ParseException, SQLException {
        if(billServiceList != null) {
            billServiceList.clear();
        }

        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter hour_formatter = DateTimeFormatter.ofPattern("kk:mm");
        DateTimeFormatter date_hour_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String reservationDate_string = tableCheckOut.getSelectionModel().getSelectedItem().getReservationDate().substring(0,10);
        String checkInDate_string = tableCheckOut.getSelectionModel().getSelectedItem().getCheckInDate().substring(0,10);
        String checkOutDate_string = tableCheckOut.getSelectionModel().getSelectedItem().getCheckOutDate().substring(0,10);

        String billID = tableCheckOut.getSelectionModel().getSelectedItem().getBillID();

        double totalCash = Double.parseDouble(tableCheckOut.getSelectionModel().getSelectedItem().getTotalCash());
        getRoomPrice = totalCash;

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
            billServiceList.add(new BillService(getService.getString("MADV"), getService.getString("TENDV"), getService.getString("SOLUONG"), getService.getString("GIADV") ));
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
        txtServicePrice.setText(currencyChange(service));
        txtTotalCash.setText(currencyChange(totalCash+roomPrice*surcharge+service));
        totalCash += roomPrice*surcharge + service;
        money = totalCash;
    }

    public void handleCheckOut(MouseEvent mouseEvent) throws Exception {
        if(txtTotalCash.getText().equals("")) {
            JFXButton conf_fail = new AlertMaker().customBtn("Xác nhận");
            AlertMaker.showMaterialDialog(stackpaneCheckout, Arrays.asList(conf_fail), "Thất bại", "Chưa chọn khách");

            return;
        }

        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter hour_formatter = DateTimeFormatter.ofPattern("kk:mm");

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

        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF File","*.pdf"));
        fc.setTitle("Save to PDF");
        fc.setInitialFileName("Untitled.pdf");

        Stage getStage = (Stage) txtTotalCash.getScene().getWindow();

        File file = fc.showSaveDialog(getStage);
        if(file != null) {
            String str = file.getAbsolutePath();
            FileOutputStream fos = new FileOutputStream(str);
            Document document = new Document();
            PdfWriter.getInstance(document, fos);

            File fontFile = new File("src/resources/font/NotoMono-Regular.ttf");
            BaseFont bf = BaseFont.createFont(fontFile.getAbsolutePath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(bf,15);


            document.open();
            Paragraph welcome = new Paragraph("Khách sạn Green Leaf hân hạnh được phục vụ quý khách", font);
            document.add(welcome);

            Paragraph underscope = new Paragraph("----------------------------------------------------------", font);
            document.add(underscope);



            Paragraph title = new Paragraph("Hóa đơn của khách hàng: "+tableCheckOut.getSelectionModel().getSelectedItem().getCustomerName(), font);
            document.add(title);

            Paragraph billID_ = new Paragraph("Mã hóa đơn: "+tableCheckOut.getSelectionModel().getSelectedItem().getBillID(), font);
            document.add(billID_);

            Paragraph date_bill = new Paragraph("Ngày lập hóa đơn: "+date_formatter.format(dtpDateNow.getValue()).toString(), font);
            document.add(date_bill);

            Paragraph hour_bill = new Paragraph("Giờ lập hóa đơn: "+hour_formatter.format(dtpHourNow.getValue()).toString(), font);
            document.add(hour_bill);


            Paragraph reserved_day = new Paragraph("Ngày đặt phòng: "+date_formatter.format(dtpReverse.getValue()).toString(), font);
            document.add(reserved_day);

            Paragraph checkIn_day = new Paragraph("Ngày nhận phòng: "+date_formatter.format(dtpCheckIn.getValue()).toString(), font);
            document.add(checkIn_day);

            Paragraph checkOut_day = new Paragraph("Ngày trả phòng: "+date_formatter.format(dtpCheckOut.getValue()).toString(), font);
            document.add(checkOut_day);

            Paragraph bill = new Paragraph("Tiền phòng: "+currencyChange(getRoomPrice), font);
            document.add(bill);


            for(int i=0; i<billServiceList.size(); i++) {
                Paragraph service = new Paragraph("Tên dịch vụ: "+billServiceList.get(i).getServiceName()+" Giá dịch vụ: "+billServiceList.get(i).getServicePrice()+" Số lượng: "+billServiceList.get(i).getQuantity(), font);
                document.add(service);
            }

            Paragraph serviceTotal = new Paragraph("Tổng dịch vụ: "+txtServicePrice.getText(), font);
            document.add(serviceTotal);

            Paragraph surcharge = new Paragraph("Phụ thu trả phòng trễ: "+txtSurcharge.getText(), font);
            document.add(surcharge);

            document.add(underscope);


            Paragraph total = new Paragraph("Tổng tiền: "+currencyChange(money), font);
            document.add(total);

            document.close();
        }

        serviceProcess.deleteDetailServiceByServiceBillId(serviceBillId);

        loadCheckOutList();
        clearInfo();

        JFXButton conf = new AlertMaker().customBtn("Đồng Ý");
        AlertMaker.showMaterialDialog(stackpaneCheckout, Arrays.asList(conf), "Thành Công", "Trả phòng thành công");

    }

    public void handleCancelCheckOut(MouseEvent mouseEvent) {
        clearInfo();
    }

}
