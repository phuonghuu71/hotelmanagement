package Controller;

import Database.Statis;
import Process.ReportProcess;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    public CategoryAxis categoryAxis;
    public NumberAxis numberAxis;
    public javafx.scene.chart.LineChart LineChart;

    ReportProcess reportProcess = new ReportProcess();
    ObservableList<Statis> statisList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadStatis();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    void loadStatis() throws SQLException {
        ResultSet getStatis = reportProcess.getStatisByMonth();

        double[] arr = new double[]{0,0,0,0,0,0,0,0,0,0,0,0};

        while (getStatis.next()) {
            statisList.add(new Statis(getStatis.getString("THANG"), getStatis.getString("TONGTIEN")));
        }
        for(int i=0; i<statisList.size(); i++) {
            arr[Integer.parseInt(statisList.get(i).getMonth())-1] = Double.parseDouble(statisList.get(i).getMoney());
        }

        XYChart.Series series = new XYChart.Series();


        series.getData().add(new XYChart.Data("1",arr[0]));
        series.getData().add(new XYChart.Data("2",arr[1]));
        series.getData().add(new XYChart.Data("3",arr[2]));
        series.getData().add(new XYChart.Data("4",arr[3]));
        series.getData().add(new XYChart.Data("5",arr[4]));
        series.getData().add(new XYChart.Data("6",arr[5]));
        series.getData().add(new XYChart.Data("7",arr[6]));
        series.getData().add(new XYChart.Data("8",arr[7]));
        series.getData().add(new XYChart.Data("9",arr[8]));
        series.getData().add(new XYChart.Data("10",arr[9]));
        series.getData().add(new XYChart.Data("11",arr[10]));
        series.getData().add(new XYChart.Data("12",arr[11]));

        LineChart.getData().addAll(series);

    }
}
