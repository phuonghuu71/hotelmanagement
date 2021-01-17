package Controller;

import Database.Customer;
import Database.Report;
import Process.ReportProcess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ReportController implements Initializable {

    ReportProcess reportProcess = new ReportProcess();

    public TableView<Report> tableReport;
    public TableColumn<Report, String> colReportID;
    public TableColumn<Report, String> colRoomName;
    public TableColumn<Report, String> colBillDate;
    public TableColumn<Report, String> colCheckInDate;
    public TableColumn<Report, String> colCheckOutDate;
    public TableColumn<Report, String> colTotalCash;

    ObservableList<Report> reportList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateNow = LocalDate.now();
            String first_day_of_week = date_formatter.format(dateNow.with(DayOfWeek.MONDAY));
            String last_day_of_week = date_formatter.format(dateNow.with(DayOfWeek.SUNDAY));
            loadReport(first_day_of_week, last_day_of_week);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    void loadReport(String first, String last) throws SQLException {
        if(tableReport != null) {
            reportList.clear();
        }
        ResultSet getReport = reportProcess.getReport(first, last);
        while (getReport.next()) {
            reportList.add(new Report(getReport.getString("MAHD"),getReport.getString("TENPHONG"),getReport.getString("NGAYLAPHD"),getReport.getString("NGAYDATPHONG"),getReport.getString("NGAYTRAPHONG"),getReport.getString("TONGTIEN")));
        }


        colReportID.setCellValueFactory(new PropertyValueFactory<Report, String>("id"));
        colRoomName.setCellValueFactory(new PropertyValueFactory<Report, String>("roomName"));
        colBillDate.setCellValueFactory(new PropertyValueFactory<Report, String>("billDate"));
        colCheckInDate.setCellValueFactory(new PropertyValueFactory<Report, String>("checkInDate"));
        colCheckOutDate.setCellValueFactory(new PropertyValueFactory<Report, String>("checkOutDate"));
        colTotalCash.setCellValueFactory(new PropertyValueFactory<Report, String>("totalCash"));

        tableReport.setItems(reportList);
    }

    public void handleReportByWeek(MouseEvent mouseEvent) throws SQLException {
        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateNow = LocalDate.now();
        String first_day_of_week = date_formatter.format(dateNow.with(DayOfWeek.MONDAY));
        String last_day_of_week = date_formatter.format(dateNow.with(DayOfWeek.SUNDAY));
        loadReport(first_day_of_week, last_day_of_week);
    }

    public void handleReportByMonth(MouseEvent mouseEvent) throws SQLException {
        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateNow = LocalDate.now();
        String first_day_of_month = date_formatter.format(dateNow.withDayOfMonth(1));
        String last_day_of_month = date_formatter.format(dateNow.withDayOfMonth(dateNow.lengthOfMonth()));
        loadReport(first_day_of_month, last_day_of_month);
    }

    public void handleReportByYear(MouseEvent mouseEvent) throws SQLException {
        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateNow = LocalDate.now();
        String first_day_of_year = date_formatter.format(dateNow.withDayOfYear(1));
        String last_day_of_year = date_formatter.format(dateNow.withDayOfYear(dateNow.lengthOfYear()));
        loadReport(first_day_of_year, last_day_of_year);
    }
}
