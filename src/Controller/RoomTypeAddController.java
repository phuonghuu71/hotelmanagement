package Controller;

import Database.RoomType;
import animatefx.animation.FadeOut;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;

import  Process.RoomTypeProcess;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import resources.AlertMaker;

public class RoomTypeAddController implements Initializable {
    public JFXTextField txtRoomTypeName;
    public JFXTextField txtRoomTypePrice;
    public JFXButton btnCancelAddRoomType;
    public JFXButton btnConfirmAddRoomType;
    public StackPane stackpaneAddRoomType;

    private RoomController roomController;

    public void setRoomController(RoomController roomController) {
        this.roomController = roomController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void closeStage() {
        Stage stage = (Stage) btnConfirmAddRoomType.getScene().getWindow();
        stage.close();
    }

}
