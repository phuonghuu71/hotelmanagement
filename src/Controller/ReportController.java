package Controller;

import Database.Customer;
import Database.DBConnect;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.*;
import net.sf.jasperreports.swing.JRViewer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
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

    String first;
    String last;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateNow = LocalDate.now();
            String first_day_of_week = date_formatter.format(dateNow.with(DayOfWeek.MONDAY));
            String last_day_of_week = date_formatter.format(dateNow.with(DayOfWeek.SUNDAY));
            first = first_day_of_week;
            last = last_day_of_week;
            loadReport(first_day_of_week, last_day_of_week);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private String currencyChange(double curr) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(curr)+ " VNĐ";
    }

    void loadReport(String first, String last) throws SQLException {
        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if(tableReport != null) {
            reportList.clear();
        }
        ResultSet getReport = reportProcess.getReport(first, last);
        while (getReport.next()) {
            reportList.add(new Report(getReport.getString("MAHD"),getReport.getString("TENPHONG"),date_formatter.format(LocalDate.parse(getReport.getString("NGAYLAPHD").substring(0, 10))),getReport.getString("NGAYNHANPHONG"),date_formatter.format(LocalDate.parse(getReport.getString("NGAYTRAPHONG").substring(0, 10))), currencyChange(Double.parseDouble(getReport.getString("TONGTIEN")))));
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
        first = first_day_of_week;
        last = last_day_of_week;
        loadReport(first_day_of_week, last_day_of_week);
    }

    public void handleReportByMonth(MouseEvent mouseEvent) throws SQLException {
        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateNow = LocalDate.now();
        String first_day_of_month = date_formatter.format(dateNow.withDayOfMonth(1));
        String last_day_of_month = date_formatter.format(dateNow.withDayOfMonth(dateNow.lengthOfMonth()));
        first = first_day_of_month;
        last = last_day_of_month;
        loadReport(first_day_of_month, last_day_of_month);
    }

    public void handleReportByYear(MouseEvent mouseEvent) throws SQLException {
        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateNow = LocalDate.now();
        String first_day_of_year = date_formatter.format(dateNow.withDayOfYear(1));
        String last_day_of_year = date_formatter.format(dateNow.withDayOfYear(dateNow.lengthOfYear()));
        first = first_day_of_year;
        last = last_day_of_year;
        loadReport(first_day_of_year, last_day_of_year);
    }

    public void handlePrintReport(MouseEvent mouseEvent) throws JRException, SQLException, FileNotFoundException {
        String reportSrc = "src/resources/report/hotel.jrxml";

        JasperDesign jasperDesign = JRXmlLoader.load(reportSrc);

        String sql =    "SELECT MAHD, TENPHONG, NGAYLAPHD, NGAYNHANPHONG, NGAYTRAPHONG, TONGTIEN " +
                        "FROM HOADON A, PHIEUDATPHONG B, CHITIETPDP C, PHONG D " +
                        "WHERE A.SOPHIEUDP = B.SOPHIEUDP AND B.SOPHIEUDP = C.SOPHIEUDP AND C.MAPHONG = D.MAPHONG AND TINHTRANGHD = N'Đã thanh toán' AND NGAYLAPHD >= '"+first+"' AND NGAYLAPHD <= '"+last+"'";

        DBConnect database = new DBConnect();
        database.connectSQL();

        Connection conn = database.getConn();


        JRDesignQuery jrDesignQuery = new JRDesignQuery();

        jrDesignQuery.setText(sql);

        jasperDesign.setQuery(jrDesignQuery);

        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, conn);

        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF File","*.pdf"));
        fc.setTitle("Save to PDF");
        fc.setInitialFileName("Untitled.pdf");

        Stage getStage = (Stage) tableReport.getScene().getWindow();

        File file = fc.showSaveDialog(getStage);
        if(file!=null) {
            String str = file.getAbsolutePath();
            FileOutputStream fos = new FileOutputStream(str);

            JRPdfExporter exporter = new JRPdfExporter();

            ExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
            exporter.setExporterInput(exporterInput);

            OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(fos);
            exporter.setExporterOutput(exporterOutput);

            SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
            exporter.setConfiguration(configuration);
            exporter.exportReport();


        }

//
//        File outDir = new File("C:/Users/UwU/IdeaProjects/HotelManagement/src/resources/report/report");
//        outDir.mkdirs();
//
//        JRPdfExporter exporter = new JRPdfExporter();
//
//        ExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
//        exporter.setExporterInput(exporterInput);
//
//        OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
//                "C:/Users/UwU/IdeaProjects/HotelManagement/src/resources/report/hotel.pdf");
//        exporter.setExporterOutput(exporterOutput);
//
//        SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
//        exporter.setConfiguration(configuration);
//        exporter.exportReport();

    }
}
