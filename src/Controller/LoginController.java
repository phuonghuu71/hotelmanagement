package Controller;

import Process.StaffProcess;
import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.paint.ImagePattern;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import resources.AlertMaker;
import javafx.scene.image.Image;
import resources.MailConfig;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.prefs.Preferences;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class LoginController implements Initializable {

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

    public Label lbForgotPassword;
    public CheckBox chkRememberMe;
    public Label lblExit;
    public StackPane spConfirm;
    public Label lbRegister;
    Preferences preferences;
    StaffProcess getStaffProcess = new StaffProcess();
    public Button btnLogin;
    public TextField txtUsername;
    public PasswordField txtPassword;

    //login
    public void handleLogin(ActionEvent actionEvent) throws SQLException, IOException {

        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String email = txtUsername.getText();
        ResultSet login = getStaffProcess.Login(username, password);
        ResultSet loginEmail = getStaffProcess.LoginByEmail(email, password);
        int valid = 0;
        int validEmail = 0;
        while(login.next()) {
            valid = Integer.parseInt(login.getString(1));
        }
        while(loginEmail.next()) {
            validEmail = Integer.parseInt(loginEmail.getString(1));
        }

        if(chkRememberMe.isSelected()) {
            preferences.put("rememberme", "checked");
        }
        else {
            preferences.put("rememberme", "unchecked");
        }

        if((valid > 0 || validEmail > 0) && chkRememberMe.isSelected()) {
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
                    } catch (IOException | SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        else if((valid > 0 || validEmail > 0) && !chkRememberMe.isSelected()) {
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
                    } catch (IOException | SQLException e) {
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

    private void openDashboard() throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/Dashboard.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Quản lý Khách Sạn");

        stage.setScene(scene);

        ResultSet getStaffInfoByUsername = getStaffProcess.getStaffInfoByUsername(txtUsername.getText());
        ResultSet getStaffInfoByEmail = getStaffProcess.getStaffInfoByEmail(txtUsername.getText());
        String fullname = "";
        String email = "";
        String duty = "";

        while(getStaffInfoByUsername.next()) {
            fullname = getStaffInfoByUsername.getString("TENNV");
            email = getStaffInfoByUsername.getString("EMAIL");
            duty = getStaffInfoByUsername.getString("TENCHUCVU");
        }

        while(getStaffInfoByEmail.next()) {
            fullname = getStaffInfoByEmail.getString("TENNV");
            email = getStaffInfoByEmail.getString("EMAIL");
            duty = getStaffInfoByEmail.getString("TENCHUCVU");
        }

        DashboardController dashboardController = loader.getController();
        dashboardController.setUsername(fullname);


        if(!duty.equals("admin")) {
            dashboardController.setAdminControl();
        }

        new FadeIn(root).play();
        stage.show();
    }
    //end of login

    //exit
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
    //end of exit

    //register
    public void handleRegister(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/CreateAccount.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Tạo Tài Khoản");
        stage.setScene(scene);
        new FadeIn(root).play();
        stage.show();

        CreateAccountController createAccountController = loader.getController();
        createAccountController.setLoginController(this);

        createAccountController.btnCreateAccount.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                JFXButton conf = new AlertMaker().customBtn("Xác Nhận");
                String username = createAccountController.txtUsername.getText();
                String password = createAccountController.txtPassword.getText();
                String email = createAccountController.txtEmail.getText();
                String fullName = createAccountController.txtFullName.getText();


                try {
                    int validUsername = 0;
                    int validEmail = 0;

                    if(username.length()<=0 || password.length()<=0 || email.length()<=0 || fullName.length()<=0)
                    {
                        AlertMaker.showMaterialDialog(createAccountController.stackpaneCreateAccount, Arrays.asList(conf),"Thất bại", "Nhập thiếu thông tin");
                        return;
                    }
                    ResultSet checkUsername = getStaffProcess.checkUsernameExist(username);
                    while(checkUsername.next()) {
                        validUsername = Integer.parseInt(checkUsername.getString(1));
                    }

                    ResultSet checkEmail = getStaffProcess.checkEmailExist(email);
                    while(checkEmail.next()) {
                        validEmail = Integer.parseInt(checkEmail.getString(1));
                    }

                    if(validUsername >= 1 || validEmail >= 1) {
                        AlertMaker.showMaterialDialog(createAccountController.stackpaneCreateAccount, Arrays.asList(conf),"Thất bại", "Tên đăng nhập hoặc email đã được sử dụng");
                        return;
                    }

                    getStaffProcess.insertStaff(username,password,email,fullName);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                JFXButton agree = new AlertMaker().customBtn("Đồng Ý");

                agree.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        createAccountController.closeStage();
                    }
                });

                AlertMaker.showMaterialDialog(createAccountController.stackpaneCreateAccount, Arrays.asList(agree), "Thành Công", "Tạo tài khoản thành công");

            }
        });

        createAccountController.btnCancelCreateAccount.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                createAccountController.closeStage();
            }
        });

    }

    public void handleRegisterEnter(MouseEvent mouseEvent) {
        lbRegister.setTextFill(Color.web("#a8cf45"));
    }

    public void handleRegisterExit(MouseEvent mouseEvent) {
        lbRegister.setTextFill(Color.web("BLACK"));
    }
    //end of register

    //forgot password

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    public void handleForgotPassword(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/ForgotPassword.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Quên mật khẩu");
        stage.setScene(scene);
        new FadeIn(root).play();
        stage.show();

        ForgotPasswordController forgotPasswordController = loader.getController();
        forgotPasswordController.setLoginController(this);

        forgotPasswordController.btnResetPassword.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                JFXButton conf = new AlertMaker().customBtn("Xác Nhận");

                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.host", MailConfig.HOST_NAME);
                props.put("mail.smtp.socketFactory.port", MailConfig.SSL_PORT);
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.port", MailConfig.SSL_PORT);

                // get Session
                Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(MailConfig.APP_EMAIL, MailConfig.APP_PASSWORD);
                    }
                });


                // compose message
                try {
                    int validEmail = 0;
                    String email = forgotPasswordController.txtEmail.getText();
                    ResultSet checkEmail = getStaffProcess.checkEmailExist(email);
                    while(checkEmail.next()) {
                        validEmail = Integer.parseInt(checkEmail.getString(1));
                    }

                    if(validEmail<=0) {
                        AlertMaker.showMaterialDialog(forgotPasswordController.stackpaneResetPassword, Arrays.asList(conf), "Thất bại", "Email chưa được đăng ký");
                        return;
                    }

                    String generatedString = getSaltString();
                    getStaffProcess.changePasswordByEmail(generatedString, email);

                    MimeMessage message = new MimeMessage(session);
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                    message.setSubject("Đặt lại mật khẩu");
                    message.setText("Mật khẩu được đặt lại là: "+generatedString);

                    // send message
                    Transport.send(message);

                    System.out.println("Message sent successfully");
                } catch (MessagingException | SQLException e) {
                    throw new RuntimeException(e);
                }

                AlertMaker.showMaterialDialog(forgotPasswordController.stackpaneResetPassword, Arrays.asList(conf), "Thành công", "Vui lòng kiểm tra email");

            }

        });

        forgotPasswordController.btnCancelResetPassword.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                forgotPasswordController.closeStage();
            }
        });

    }

    public void handleForgotPasswordEnter(MouseEvent mouseEvent) {
        lbForgotPassword.setTextFill(Color.web("#a8cf45"));
    }

    public void handleForgotPasswordExit(MouseEvent mouseEvent) {
        lbForgotPassword.setTextFill(Color.web("BLACK"));
    }
    //end of forgot password


}
