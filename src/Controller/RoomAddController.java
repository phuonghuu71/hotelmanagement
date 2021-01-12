package Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class RoomAddController implements Initializable {
    public JFXTextField txtRoomName;
    public JFXTextField txtRoomStatus;
    public JFXComboBox cbRoomType;
    public JFXComboBox cbRoomCapacity;
    public JFXButton btnConfirmAddRoom;
    public JFXButton btnCancelAddRoom;
    public StackPane stackpaneAddRoom;
    private RoomController roomController;

    public void setRoomController(RoomController roomController) {
        this.roomController = roomController;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void closeStage() {
        Stage stage = (Stage) btnConfirmAddRoom.getScene().getWindow();
        stage.close();
    }
}
