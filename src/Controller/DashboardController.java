package Controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {


    public VBox adminVbox;
    public HBox adminHbox;
    public BorderPane borderpaneDashboard;
    public HBox hboxHome;
    public HBox staffHbox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        adminVbox.setVisible(false);
        adminVbox.setManaged(false);
    }

    private void hideSubMenu() {
        if(adminVbox.isVisible() == true && adminVbox.isManaged() == true) {
            adminVbox.setVisible(false);
            adminVbox.setManaged(false);
        }
    }

    private void showSubMenu() {
        if(adminVbox.isVisible() == false && adminVbox.isManaged() == false) {
            hideSubMenu();
            adminVbox.setVisible(true);
            adminVbox.setManaged(true);
        }
        else {
            adminVbox.setVisible(false);
            adminVbox.setManaged(false);
        }
    }

    public void handleAdminHbox(MouseEvent mouseEvent) {
        showSubMenu();
    }

    private void loadUI(String ui) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/Interface/"+ui+".fxml"));
            borderpaneDashboard.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleHome(MouseEvent mouseEvent) {
        loadUI("Login");
    }

    public void handleStaff(MouseEvent mouseEvent) {
        loadUI("Staff");
    }
}
