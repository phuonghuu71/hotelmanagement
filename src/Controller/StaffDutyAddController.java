package Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class StaffDutyAddController implements Initializable {

    public JFXTextField txtDutyName;
    public JFXTextField txtDutySalary;
    public JFXButton btnConfirmAddDuty;
    public JFXButton btnCancelAddDuty;
    public StackPane stackpaneAddStaffDuty;

    private StaffController staffController;

    public void setStaffController(StaffController staffController) {
        this.staffController = staffController;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
    }

    public void closeStage() {
        Stage stage = (Stage) btnConfirmAddDuty.getScene().getWindow();
        stage.close();
    }
    
    
    
}
