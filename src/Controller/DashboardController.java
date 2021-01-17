package Controller;

import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import resources.AlertMaker;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {


    public VBox adminVbox;
    public HBox adminHbox;
    public AnchorPane anchorpaneDashboard;
    public BorderPane borderpaneDashboard;
    public HBox hboxHome;
    public HBox staffHbox;
    public Circle circleOff;
    public BorderPane borderpaneRoot;
    public StackPane stackpaneDashboard;
    public Label lbName;
    public HBox bookingHbox;
    public HBox serviceHbox;


    private LoginController loginController;

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        adminVbox.setVisible(false);
        adminVbox.setManaged(false);
        customPowerOff();
    }

    void setUsername(String text) {
        lbName.setText(text);
    }

    void setAdminControl() {
        adminHbox.setDisable(true);
    }

    //hide sub menu & show sub menu
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

    void loadUI(String ui) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/Interface/"+ui+".fxml"));
            borderpaneDashboard.setCenter(root);
            new FadeIn(root).play();
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

    //Power Off Button
    private void customPowerOff() {
        Image off = new Image("/resources/images/power-off-solid.png");
        circleOff.setFill(new ImagePattern(off));
    }
    public void handleOffEnter(MouseEvent mouseEvent) {
        Image off = new Image("/resources/images/power-off-solid_green.png");
        circleOff.setFill(new ImagePattern(off));
    }

    public void handleOffExit(MouseEvent mouseEvent) {
        Image off = new Image("/resources/images/power-off-solid.png");
        circleOff.setFill(new ImagePattern(off));
    }

    public void handleOffClick(MouseEvent mouseEvent) {
        JFXButton yes = new AlertMaker().customBtn("Có");
        yes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        JFXButton no = new AlertMaker().customBtn("Không");
        no.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                return;
            }
        });

        AlertMaker.showMaterialDialog(stackpaneDashboard, Arrays.asList(yes, no), "Xác Nhận", "Bạn thật sự muốn thoát");
    }

    public void handleRoom(MouseEvent mouseEvent) {
        loadUI("Room");
    }

    public void handleBookingHbox(MouseEvent mouseEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/Booking.fxml"));
        Parent root = loader.load();
        borderpaneDashboard.setCenter(root);
        new FadeIn(root).play();

        String fullname = lbName.getText();

        BookingController bookingController = loader.getController();
        bookingController.setStaffName(fullname);

    }

    public void handleCallServiceHbox(MouseEvent mouseEvent) {
        loadUI("CallService");
    }

    public void handleCheckoutHbox(MouseEvent mouseEvent) {
        loadUI("CheckOut");
    }

    public void handleCustomerHbox(MouseEvent mouseEvent) {
        loadUI("Customer");
    }

    public void handleServiceHbox(MouseEvent mouseEvent) {
        loadUI("Service");
    }

    public void handleReportHbox(MouseEvent mouseEvent) {
        loadUI("Report");
    }
}
