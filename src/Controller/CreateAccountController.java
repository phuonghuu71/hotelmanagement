package Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class CreateAccountController {

    public JFXTextField txtUsername;
    public JFXTextField txtFullName;
    public JFXPasswordField txtPassword;
    public JFXButton btnCreateAccount;
    public JFXButton btnCancelCreateAccount;
    public StackPane stackpaneCreateAccount;
    public JFXTextField txtEmail;
    LoginController loginController = new LoginController();
    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void closeStage() {
        Stage stage = (Stage) btnCreateAccount.getScene().getWindow();
        stage.close();
    }

}
