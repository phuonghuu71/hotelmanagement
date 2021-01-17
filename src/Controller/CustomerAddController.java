package Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class CustomerAddController {
    public JFXTextField txtCustomerName;
    public JFXTextField txtCustomerIdentityID;
    public JFXTextField txtCustomerPhone;
    public JFXButton btnConfirmAddCustomer;
    public JFXButton btnCancelAddCustomer;
    public StackPane stackpaneCustomerAdd;

    private CustomerController customerController;

    public void setCustomerController(CustomerController customerController) {
        this.customerController = customerController;
    }

    public void closeStage() {
        Stage stage = (Stage) btnConfirmAddCustomer.getScene().getWindow();
        stage.close();
    }

}
