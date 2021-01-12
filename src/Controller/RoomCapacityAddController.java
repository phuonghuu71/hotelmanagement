package Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class RoomCapacityAddController implements Initializable {
    public JFXTextField txtRoomCapacity;
    public JFXTextField txtRoomCapacityPrice;
    public JFXButton btnConfirmAddRoomCapacity;
    public JFXButton btnCancelAddRoomCapacity;
    public StackPane stackpaneAddRoomCapacity;

    private RoomController roomController;

    public void setRoomController(RoomController roomController) {
        this.roomController = roomController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void closeStage() {
        Stage stage = (Stage) btnConfirmAddRoomCapacity.getScene().getWindow();
        stage.close();
    }

}
