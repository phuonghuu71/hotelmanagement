package Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ServiceAddController {
    public JFXTextField txtServiceName;
    public JFXTextField txtServicePrice;
    public JFXButton btnConfirmAddService;
    public JFXButton btnCancelAddService;
    public StackPane stackpaneAddService;

    private ServiceController serviceController;

    public void setServiceController(ServiceController serviceController) {
        this.serviceController = serviceController;
    }

    public void closeStage() {
        Stage stage = (Stage) btnConfirmAddService.getScene().getWindow();
        stage.close();
    }

}
