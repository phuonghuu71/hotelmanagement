package Controller;

import Process.StaffProcess;
import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import resources.AlertMaker;


import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.prefs.Preferences;

public class LoginController implements Initializable {

    public CheckBox chkRememberMe;
    public Label lblExit;
    public StackPane spConfirm;
    Preferences preferences;
    StaffProcess getStaffProcess = new StaffProcess();
    public Button btnLogin;
    public TextField txtUsername;
    public PasswordField txtPassword;

    public void handleLogin(ActionEvent actionEvent) throws SQLException, IOException {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        ResultSet login = getStaffProcess.Login(username, password);
        int valid = 0;
        while(login.next()) {
            valid = Integer.parseInt(login.getString(1));
        }

        if(chkRememberMe.isSelected()) {
            preferences.put("rememberme", "checked");
        }
        else {
            preferences.put("rememberme", "unchecked");
        }

        if(valid > 0 && chkRememberMe.isSelected()) {
            preferences.put("username", txtUsername.getText());
            preferences.put("password", txtPassword.getText());
            JFXButton succ = new AlertMaker().customBtn("OK");
            AlertMaker.showMaterialDialog(spConfirm, Arrays.asList(succ), "Thành Công", "Đăng Nhập Thành Công");
            succ.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    try {
                        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        stage.hide();
                        openDashboard();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        else if(valid > 0 && !chkRememberMe.isSelected()) {
            preferences.put("username", "");
            preferences.put("password", "");
            JFXButton succ = new AlertMaker().customBtn("OK");
            AlertMaker.showMaterialDialog(spConfirm, Arrays.asList(succ), "Thành Công", "Đăng Nhập Thành Công");
            succ.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    try {
                        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        stage.hide();
                        openDashboard();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        else {
            preferences.put("username", "");
            preferences.put("password", "");
            JFXButton fail = new AlertMaker().customBtn("OK");
            AlertMaker.showMaterialDialog(spConfirm, Arrays.asList(fail), "Thất Bại", "Đăng Nhập Thất Bại");
        }
    }

    private void openDashboard() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Interface/Dashboard.fxml"));
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Quản lý Khách Sạn");
        stage.setScene(scene);
        new FadeIn(root).play();
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        preferences = Preferences.userNodeForPackage(LoginController.class);
        if(preferences != null) {
            if(preferences.get("username", null) != null && !preferences.get("username", null).isEmpty()) {
                txtUsername.setText(preferences.get("username", null));
                txtPassword.setText(preferences.get("password", null));
            }
            if(preferences.get("rememberme", null) != null && preferences.get("rememberme", null).equals("checked")) {
                chkRememberMe.setSelected(true);
            }
        }
    }

    public void handleExitEnter(MouseEvent mouseEvent) {
        lblExit.setTextFill(Color.web("#a8cf45"));
    }

    public void handleExitExit(MouseEvent mouseEvent) {
        lblExit.setTextFill(Color.web("BLACK"));
    }

    public void handleExit(MouseEvent mouseEvent) {
        JFXButton yes = new AlertMaker().customBtn("Có");
        yes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        JFXButton no = new AlertMaker().customBtn("Không");
        no.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                return;
            }
        });

        AlertMaker.showMaterialDialog(spConfirm, Arrays.asList(yes, no), "Xác Nhận", "Bạn thật sự muốn thoát");
    }
}
