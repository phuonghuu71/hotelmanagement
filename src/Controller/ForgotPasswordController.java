package Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ForgotPasswordController {
    public JFXTextField txtEmail;
    public JFXButton btnResetPassword;
    public JFXButton btnCancelResetPassword;
    public StackPane stackpaneResetPassword;

    private LoginController loginController;

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void closeStage() {
        Stage stage = (Stage) btnResetPassword.getScene().getWindow();
        stage.close();
    }

}
