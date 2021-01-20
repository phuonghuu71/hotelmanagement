package Controller;

import Process.StaffProcess;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import resources.AlertMaker;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;

public class StaffOptions implements Initializable {

    public JFXTextField txtUsername;
    public JFXPasswordField txtOldPassword;
    public JFXPasswordField txtNewPassword;
    public StackPane stackpaneChangePassword;

    StaffProcess staffProcess = new StaffProcess();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void handleChangePassword(MouseEvent mouseEvent) throws SQLException {
        if(!checkInput()) {
            return;
        }
        String username = txtUsername.getText();
        String oldPassword = txtOldPassword.getText();
        String newPassword = txtNewPassword.getText();

        ResultSet getLogin = staffProcess.Login(username, oldPassword);

        double validate = 0;

        while (getLogin.next()) {
            validate = Double.parseDouble(getLogin.getString(1));
        }

        if(validate == 0) {
            JFXButton conf = new AlertMaker().customBtn("Đồng Ý");
            AlertMaker.showMaterialDialog(stackpaneChangePassword, Arrays.asList(conf), "Thất bại", "Mật khẩu cũ không đúng");
            return;
        }

        staffProcess.changePasswordByUsername(newPassword, username);

        JFXButton conf = new AlertMaker().customBtn("Đồng Ý");
        AlertMaker.showMaterialDialog(stackpaneChangePassword, Arrays.asList(conf), "Thành công", "Đổi mật khẩu thành công");

    }

    private boolean checkInput() {
        if(txtUsername.getText().isEmpty() || txtOldPassword.getText().isEmpty() || txtNewPassword.getText().isEmpty()) {
            JFXButton conf = new AlertMaker().customBtn("Đồng Ý");
            AlertMaker.showMaterialDialog(stackpaneChangePassword, Arrays.asList(conf), "Thất bại", "Xin nhập đủ thông tin");
            return false;
        }
        return true;
    }

}
