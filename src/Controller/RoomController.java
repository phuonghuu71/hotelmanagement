package Controller;

import Database.Room;
import Database.RoomCapacity;
import Database.RoomType;
import Process.RoomTypeProcess;
import Process.RoomCapacityProcess;
import Process.RoomProcess;
import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import resources.AlertMaker;

import javafx.fxml.FXML;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class RoomController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadRoomType();
            loadRoomCapacity();
            loadRoom();
            showCb();
            setEditable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public StackPane stackpaneRoom;

    //set Editable
    private void setEditable() {
        this.txtRoomID.setEditable(false);
        this.txtRoomPrice.setEditable(false);
        this.txtRoomCapacityID.setEditable(false);
        this.txtRoomTypeID.setEditable(false);
    }
    //end of set Editable

    //room type
    public TableView<RoomType> tableRoomType;
    public TableColumn<RoomType, String> colRoomTypeID;
    public TableColumn<RoomType, String> colRoomTypeName;
    public TableColumn<RoomType, String> colRoomTypePrice;
    public JFXTextField txtRoomTypeID;
    public JFXTextField txtRoomTypeName;
    public JFXTextField txtRoomTypePrice;
    public JFXButton btnRoomTypeAddOpen;

    ArrayList<Integer> roomTypeIDList = new ArrayList<>();

    ObservableList<RoomType> roomTypeList = FXCollections.observableArrayList();
    RoomTypeProcess roomTypeProcess = new RoomTypeProcess();
    private RoomTypeAddController roomTypeAddController;
    //end of room type

    //room capacity
    public TableView<RoomCapacity> tableRoomCapacity;
    public TableColumn<RoomCapacity, String> colRoomCapacityID;
    public TableColumn<RoomCapacity, String> colRoomCapacity;
    public TableColumn<RoomCapacity, String> colRoomCapacityPrice;
    public JFXButton btnRoomCapacityAddOpen;
    public JFXTextField txtRoomCapacityID;
    public JFXTextField txtRoomCapacity;
    public JFXTextField txtRoomCapacityPrice;

    ArrayList<Integer> roomCapacityIDList = new ArrayList<>();

    ObservableList<RoomCapacity> roomCapacityList = FXCollections.observableArrayList();
    private RoomCapacityProcess roomCapacityProcess = new RoomCapacityProcess();
    private RoomCapacityAddController roomCapacityAddController;

    //end of room capacity

    //room
    public TableView<Room> tableRoom;
    public TableColumn<Room, String> colRoomID;
    public TableColumn<Room, String> colRoomName;
    public TableColumn<Room, String> colRoomStatus;
    public TableColumn<Room, String> colRoomType_Room;
    public TableColumn<Room, String> colRoomCapacity_Room;
    public TableColumn<Room, String> colRoomPrice;
    public JFXTextField txtRoomID;
    public JFXTextField txtRoomName;
    public JFXTextField txtRoomStatus;
    public JFXComboBox cbRoomType;
    public JFXComboBox cbRoomCapacity;
    public JFXTextField txtRoomPrice;

    ObservableList<Room> roomList = FXCollections.observableArrayList();
    private RoomProcess roomProcess = new RoomProcess();
    private RoomAddController roomAddController;

    //end of room

    //room
    void loadRoom() throws SQLException {
        if(tableRoom != null) {
            tableRoom.getItems().clear();
        }
        ResultSet result = roomProcess.getRoomList();
        while (result.next()) {
            double price = Double.parseDouble(result.getString("GIALOAIPHONG")) + Double.parseDouble(result.getString("GIASUCCHUA"));
            roomList.add(new Room(result.getString("MAPHONG"), result.getString("TENPHONG"), result.getString("TINHTRANG"), result.getString("TENLOAIPHONG"), result.getString("SOGIUONG"), currencyChange(price) ));
        }

        colRoomID.setCellValueFactory(new PropertyValueFactory<Room, String>("id"));
        colRoomName.setCellValueFactory(new PropertyValueFactory<Room, String>("roomName"));
        colRoomStatus.setCellValueFactory(new PropertyValueFactory<Room, String>("roomStatus"));
        colRoomType_Room.setCellValueFactory(new PropertyValueFactory<Room, String>("roomTypeName"));
        colRoomCapacity_Room.setCellValueFactory(new PropertyValueFactory<Room, String>("roomCapacity"));
        colRoomPrice.setCellValueFactory(new PropertyValueFactory<Room, String>("roomPrice"));

        tableRoom.setItems(roomList);
    }

    private void showCbRoomType() throws SQLException {
        ResultSet resultSet = null;

        resultSet = roomTypeProcess.getRoomTypeList();
        while(resultSet.next()) {
            String roomTypeID = resultSet.getString("MALOAIPHONG").trim();
            roomTypeIDList.add(Integer.parseInt(roomTypeID));
            cbRoomType.getItems().add(resultSet.getString("TENLOAIPHONG"));
        }
    }

    private void showCbRoomCapacity() throws SQLException {
        ResultSet resultSet = null;

        resultSet = roomCapacityProcess.getRoomCapacityList();
        while(resultSet.next()) {
            String roomCapacityID = resultSet.getString("MASUCCHUA").trim();
            roomCapacityIDList.add(Integer.parseInt(roomCapacityID));
            cbRoomCapacity.getItems().add(resultSet.getString("SOGIUONG"));
        }
    }

    void clearRoom() {
        this.txtRoomID.setText("");
        this.txtRoomName.setText("");
        this.txtRoomStatus.setText("");
        this.cbRoomType.getSelectionModel().selectFirst();
        this.cbRoomCapacity.getSelectionModel().selectFirst();
        this.txtRoomPrice.setText("");
    }

    private void showCb() throws SQLException {
        showCbRoomType();
        showCbRoomCapacity();
    }

    public void handleRoomTable(MouseEvent mouseEvent) {
        Room room = tableRoom.getSelectionModel().getSelectedItem();
        txtRoomID.setText(room.getId());
        txtRoomName.setText(room.getRoomName());
        txtRoomStatus.setText(room.getRoomStatus());
        cbRoomType.getSelectionModel().select(room.getRoomTypeName());
        cbRoomCapacity.getSelectionModel().select(room.getRoomCapacity());
        priceProperty(txtRoomPrice);
        txtRoomPrice.setText(room.getRoomPrice());

    }

    public void handleRoomOpen(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/AddRoom.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Quản lý Khách Sạn");
        stage.setScene(scene);
        new FadeIn(root).play();
        stage.show();

        roomAddController = loader.getController();
        roomAddController.setRoomController(this);
        roomAddController.cbRoomType.getItems().addAll(cbRoomType.getItems());
        roomAddController.cbRoomCapacity.getItems().addAll(cbRoomCapacity.getItems());
        roomAddController.cbRoomType.getSelectionModel().selectFirst();
        roomAddController.cbRoomCapacity.getSelectionModel().selectFirst();
        roomAddController.btnConfirmAddRoom.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                JFXButton conf = new AlertMaker().customBtn("Xác Nhận");
                String roomName = roomAddController.txtRoomName.getText();
                String roomStatus = roomAddController.txtRoomStatus.getText();

                if(roomName == null || roomName.length() <= 0) {
                    AlertMaker.showMaterialDialog(roomAddController.stackpaneAddRoom, Arrays.asList(conf), "Thất Bại", "Chưa nhập tên phòng");
                    return;
                }

                if(roomStatus == null || roomStatus.length() <= 0) {
                    AlertMaker.showMaterialDialog(roomAddController.stackpaneAddRoom, Arrays.asList(conf), "Thất Bại", "Chưa nhập tình trạng phòng");
                    return;
                }

                String roomTypeID = Integer.toString(roomTypeIDList.get(roomAddController.cbRoomType.getSelectionModel().getSelectedIndex()));
                String roomCapacityID = Integer.toString(roomCapacityIDList.get(roomAddController.cbRoomCapacity.getSelectionModel().getSelectedIndex()));

                try {
                    roomProcess.insertRoom(roomName, roomStatus, roomTypeID, roomCapacityID);
                    loadRoom();
                    clearRoom();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                JFXButton agree = new AlertMaker().customBtn("Đồng Ý");
                agree.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        roomAddController.closeStage();
                    }
                });
                AlertMaker.showMaterialDialog(roomAddController.stackpaneAddRoom, Arrays.asList(agree), "Thành Công", "Thêm phòng thành công");

            }
        });

        roomAddController.btnCancelAddRoom.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                roomAddController.closeStage();
            }
        });
    }

    public void handleRoomUpdate(MouseEvent mouseEvent) throws SQLException {
        JFXButton conf = new AlertMaker().customBtn("Xác Nhận");
        String roomID = txtRoomID.getText();
        String roomName = txtRoomName.getText();
        String roomStatus = txtRoomStatus.getText();
        String roomType = Integer.toString(roomTypeIDList.get(cbRoomType.getSelectionModel().getSelectedIndex()));
        String roomCapacity = Integer.toString(roomCapacityIDList.get(cbRoomCapacity.getSelectionModel().getSelectedIndex()));

        if(roomID == null || roomID.length() <= 0) {
            AlertMaker.showMaterialDialog(stackpaneRoom, Arrays.asList(conf), "Thất Bại", "Chưa chọn phòng");
            return;
        }

        if(roomName == null || roomName.length() <= 0) {
            AlertMaker.showMaterialDialog(stackpaneRoom, Arrays.asList(conf), "Thất Bại", "Chưa nhập tên phòng");
            return;
        }

        if(roomStatus == null || roomStatus.length() <= 0) {
            AlertMaker.showMaterialDialog(stackpaneRoom, Arrays.asList(conf), "Thất Bại", "Chưa nhập tình trạng");
            return;
        }

        roomProcess.updateRoom(roomName, roomStatus, roomType, roomCapacity, roomID);
        loadRoom();
        clearRoom();
        AlertMaker.showMaterialDialog(stackpaneRoom, Arrays.asList(conf), "Thành công", "Sửa phòng thành công");

    }

    public void handleRoomDelete(MouseEvent mouseEvent) {
        JFXButton yes = new AlertMaker().customBtn("Có");
        yes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                JFXButton ok = new AlertMaker().customBtn("Đồng Ý");
                String header = "Thành công";
                String body = "Xóa phòng thành công";

                if(txtRoomID.getText().isEmpty()) {
                    header = "Thất bại";
                    body = "Chưa chọn phòng";
                    AlertMaker.showMaterialDialog(stackpaneRoom, Arrays.asList(ok), header, body);
                    return;
                }

                String roomID = txtRoomID.getText();
                try {
                    roomProcess.deleteRoom(roomID);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                clearRoom();
                try {
                    loadRoom();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                AlertMaker.showMaterialDialog(stackpaneRoom, Arrays.asList(ok), header, body);

            }
        });

        JFXButton no = new AlertMaker().customBtn("Không");
        no.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                return;
            }
        });
        AlertMaker.showMaterialDialog(stackpaneRoom, Arrays.asList(yes, no), "Xác Nhận", "Bạn thật sự muốn xóa");
    }

    public void handleRoomStatus(MouseEvent mouseEvent) {
        this.txtRoomStatus.setText("");
    }

    public void handleRoomName(MouseEvent mouseEvent) {
        this.txtRoomName.setText("");
    }
    //end of room

    //currency
    private String currencyChange(double curr) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(curr)+ " VNĐ";
    }

    private void priceProperty(JFXTextField text) {
        text.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.matches("\\d*"));
                text.setText(t1.replaceAll("[^\\d]", ""));
            }
        });

    }
    //end of currency

    //room type
    void loadRoomType() throws SQLException {
        if(tableRoomType != null) {
            tableRoomType.getItems().clear();
        }
        ResultSet result = roomTypeProcess.getRoomTypeList();
        while (result.next()) {
            roomTypeList.add(new RoomType(result.getString("MALOAIPHONG"), result.getString("TENLOAIPHONG"), currencyChange(Double.parseDouble(result.getString("GIALOAIPHONG")))));
        }

        colRoomTypeID.setCellValueFactory(new PropertyValueFactory<RoomType, String>("id"));
        colRoomTypeName.setCellValueFactory(new PropertyValueFactory<RoomType, String>("roomTypeName"));
        colRoomTypePrice.setCellValueFactory(new PropertyValueFactory<RoomType, String>("roomPrice"));

        tableRoomType.setItems(roomTypeList);
    }

    void clearRoomType() {
        this.txtRoomTypeID.setText("");
        this.txtRoomTypeName.setText("");
        this.txtRoomTypePrice.setText("");
    }

    public void handleRoomTypeTable(MouseEvent mouseEvent) {
        RoomType roomType = tableRoomType.getSelectionModel().getSelectedItem();
        txtRoomTypeID.setText(roomType.getId());
        txtRoomTypeName.setText(roomType.getRoomTypeName());
        priceProperty(txtRoomTypePrice);
        txtRoomTypePrice.setText(roomType.getRoomPrice());
    }

    public void handleRoomTypeUpdate(MouseEvent mouseEvent) throws SQLException {
        JFXButton conf = new AlertMaker().customBtn("Xác Nhận");
        String roomTypeId = txtRoomTypeID.getText();
        String roomTypeName = txtRoomTypeName.getText();
        String roomTypePrice = txtRoomTypePrice.getText().trim();

        if(roomTypeId == null || roomTypeId.length() <= 0) {
            AlertMaker.showMaterialDialog(stackpaneRoom, Arrays.asList(conf), "Thất Bại", "Chưa chọn loại phòng");
            return;
        }

        if(roomTypeName == null || roomTypeName.length() <= 0) {
            AlertMaker.showMaterialDialog(stackpaneRoom, Arrays.asList(conf), "Thất Bại", "Chưa nhập tên loại phòng");
            return;
        }

        if(roomTypePrice == null || roomTypePrice.length() <= 0) {
            AlertMaker.showMaterialDialog(stackpaneRoom, Arrays.asList(conf), "Thất Bại", "Chưa nhập giá loại phòng");
            return;
        }

        roomTypeProcess.updateRoomType(roomTypeName, roomTypePrice, roomTypeId);
        loadRoomType();
        clearRoomType();
        AlertMaker.showMaterialDialog(stackpaneRoom, Arrays.asList(conf), "Thành công", "Sửa loại phòng thành công");
    }

    public void handleRoomTypeDelete(MouseEvent mouseEvent) throws SQLException {
        JFXButton yes = new AlertMaker().customBtn("Có");
        yes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                JFXButton ok = new AlertMaker().customBtn("Đồng Ý");
                String header = "Thành công";
                String body = "Xóa loại phòng thành công";

                if(txtRoomTypeID.getText().isEmpty()) {
                    header = "Thất bại";
                    body = "Chưa chọn loại phòng";
                    AlertMaker.showMaterialDialog(stackpaneRoom, Arrays.asList(ok), header, body);
                    return;
                }

                String roomtypeID = txtRoomTypeID.getText();
                try {
                    roomTypeProcess.deleteRoomType(roomtypeID);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                clearRoomType();
                try {
                    loadRoomType();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                AlertMaker.showMaterialDialog(stackpaneRoom, Arrays.asList(ok), header, body);

            }
        });

        JFXButton no = new AlertMaker().customBtn("Không");
        no.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                return;
            }
        });
        AlertMaker.showMaterialDialog(stackpaneRoom, Arrays.asList(yes, no), "Xác Nhận", "Bạn thật sự muốn xóa");
    }

    public void handleRoomTypeOpen(MouseEvent mouseEvent) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/AddRoomType.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Quản lý Khách Sạn");
        stage.setScene(scene);
        new FadeIn(root).play();
        stage.show();

        RoomTypeAddController roomTypeAddController = loader.getController();
        roomTypeAddController.setRoomController(this);
        roomTypeAddController.btnConfirmAddRoomType.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                JFXButton conf = new AlertMaker().customBtn("Xác Nhận");
                String roomtypeName = roomTypeAddController.txtRoomTypeName.getText();
                String roomtypePrice = roomTypeAddController.txtRoomTypePrice.getText();

                if(roomtypeName == null || roomtypeName.length() <= 0) {
                    AlertMaker.showMaterialDialog(roomTypeAddController.stackpaneAddRoomType, Arrays.asList(conf), "Thất Bại", "Chưa nhập tên loại phòng");
                    return;
                }

                if(roomtypePrice == null || roomtypePrice.length() <= 0) {
                    AlertMaker.showMaterialDialog(roomTypeAddController.stackpaneAddRoomType, Arrays.asList(conf), "Thất Bại", "Chưa nhập giá loại phòng");
                    return;
                }

                try {
                    roomTypeProcess.insertRoomType(roomtypeName, roomtypePrice);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    loadRoomType();
                    clearRoomType();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                JFXButton agree = new AlertMaker().customBtn("Đồng Ý");
                agree.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        roomTypeAddController.closeStage();
                    }
                });
                AlertMaker.showMaterialDialog(roomTypeAddController.stackpaneAddRoomType, Arrays.asList(agree), "Thành Công", "Thêm loại phòng thành công");

            }
        });

        roomTypeAddController.btnCancelAddRoomType.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                roomTypeAddController.closeStage();
            }
        });
    }

    public void handleRoomTypeName(MouseEvent mouseEvent) {
        txtRoomTypeName.setText("");
    }

    public void handleRoomTypePrice(MouseEvent mouseEvent) {
        txtRoomTypePrice.setText("");
        priceProperty(txtRoomTypePrice);
    }
    //end of room type

    //capacity
    void loadRoomCapacity() throws SQLException {
        if(tableRoomCapacity != null) {
            tableRoomCapacity.getItems().clear();
        }
        ResultSet result = roomCapacityProcess.getRoomCapacityList();
        while (result.next()) {
            roomCapacityList.add(new RoomCapacity(result.getString("MASUCCHUA"), result.getString("SOGIUONG"), currencyChange(Double.parseDouble(result.getString("GIASUCCHUA")))));
        }

        colRoomCapacityID.setCellValueFactory(new PropertyValueFactory<RoomCapacity, String>("id"));
        colRoomCapacity.setCellValueFactory(new PropertyValueFactory<RoomCapacity, String>("roomCapacity"));
        colRoomCapacityPrice.setCellValueFactory(new PropertyValueFactory<RoomCapacity, String>("roomCapacityPrice"));

        tableRoomCapacity.setItems(roomCapacityList);
    }

    void clearRoomCapacity() {
        this.txtRoomCapacityID.setText("");
        this.txtRoomCapacity.setText("");
        this.txtRoomCapacityPrice.setText("");
    }

    public void handleRoomCapacityTable(MouseEvent mouseEvent) {
        RoomCapacity roomCapacity = tableRoomCapacity.getSelectionModel().getSelectedItem();
        txtRoomCapacityID.setText(roomCapacity.getId());
        txtRoomCapacity.setText(roomCapacity.getRoomCapacity());
        priceProperty(txtRoomCapacityPrice);
        txtRoomCapacityPrice.setText(roomCapacity.getRoomCapacityPrice());
    }

    public void handleRoomCapacityDelete(MouseEvent mouseEvent) {
        JFXButton yes = new AlertMaker().customBtn("Có");
        yes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                JFXButton ok = new AlertMaker().customBtn("Đồng Ý");
                String header = "Thành công";
                String body = "Xóa sức chứa thành công";

                if(txtRoomCapacityID.getText().isEmpty()) {
                    header = "Thất bại";
                    body = "Chưa chọn sức chứa";
                    AlertMaker.showMaterialDialog(stackpaneRoom, Arrays.asList(ok), header, body);
                    return;
                }

                String roomCapacityID = txtRoomCapacityID.getText();
                try {
                    roomCapacityProcess.deleteRoomCapacity(roomCapacityID);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                clearRoomCapacity();
                try {
                    loadRoomCapacity();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                AlertMaker.showMaterialDialog(stackpaneRoom, Arrays.asList(ok), header, body);

            }
        });

        JFXButton no = new AlertMaker().customBtn("Không");
        no.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                return;
            }
        });
        AlertMaker.showMaterialDialog(stackpaneRoom, Arrays.asList(yes, no), "Xác Nhận", "Bạn thật sự muốn xóa");
    }

    public void handleRoomCapacityUpdate(MouseEvent mouseEvent) throws SQLException {
        JFXButton conf = new AlertMaker().customBtn("Xác Nhận");
        String roomCapacityID = txtRoomCapacityID.getText();
        String roomCapacity = txtRoomCapacity.getText();
        String roomCapacityPrice = txtRoomCapacityPrice.getText().trim();

        if(roomCapacityID == null || roomCapacityID.length() <= 0) {
            AlertMaker.showMaterialDialog(stackpaneRoom, Arrays.asList(conf), "Thất Bại", "Chưa chọn sức chứa");
            return;
        }

        if(roomCapacity == null || roomCapacity.length() <= 0) {
            AlertMaker.showMaterialDialog(stackpaneRoom, Arrays.asList(conf), "Thất Bại", "Chưa nhập số giường");
            return;
        }

        if(roomCapacityPrice == null || roomCapacityPrice.length() <= 0) {
            AlertMaker.showMaterialDialog(stackpaneRoom, Arrays.asList(conf), "Thất Bại", "Chưa nhập giá sức chứa");
            return;
        }

        roomCapacityProcess.updateRoomCapacity(roomCapacity, roomCapacityPrice, roomCapacityID);
        loadRoomCapacity();
        clearRoomCapacity();
        AlertMaker.showMaterialDialog(stackpaneRoom, Arrays.asList(conf), "Thành công", "Sửa sức chứa thành công");
    }

    public void handleRoomCapacityOpen(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/AddRoomCapacity.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Quản lý Khách Sạn");
        stage.setScene(scene);
        new FadeIn(root).play();
        stage.show();

        roomCapacityAddController = loader.getController();
        roomCapacityAddController.setRoomController(this);
        roomCapacityAddController.btnConfirmAddRoomCapacity.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                JFXButton conf = new AlertMaker().customBtn("Xác Nhận");
                String roomCapacity = roomCapacityAddController.txtRoomCapacity.getText();
                String roomCapacityPrice = roomCapacityAddController.txtRoomCapacityPrice.getText();

                if(roomCapacity == null || roomCapacity.length() <= 0) {
                    AlertMaker.showMaterialDialog(roomCapacityAddController.stackpaneAddRoomCapacity, Arrays.asList(conf), "Thất Bại", "Chưa nhập số giường");
                    return;
                }

                if(roomCapacityPrice == null || roomCapacityPrice.length() <= 0) {
                    AlertMaker.showMaterialDialog(roomCapacityAddController.stackpaneAddRoomCapacity, Arrays.asList(conf), "Thất Bại", "Chưa nhập giá sức chứa");
                    return;
                }

                try {
                    roomCapacityProcess.insertRoomCapacity(roomCapacity, roomCapacityPrice);
                    loadRoomCapacity();
                    clearRoomCapacity();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                JFXButton agree = new AlertMaker().customBtn("Đồng Ý");
                agree.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        roomCapacityAddController.closeStage();
                    }
                });
                AlertMaker.showMaterialDialog(roomCapacityAddController.stackpaneAddRoomCapacity, Arrays.asList(agree), "Thành Công", "Thêm sức chứa thành công");
            }
        });

        roomCapacityAddController.btnCancelAddRoomCapacity.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                roomCapacityAddController.closeStage();
            }
        });
    }

    public void handleRoomCapacity(MouseEvent mouseEvent) {
        txtRoomCapacity.setText("");
    }

    public void handleRoomCapacityPrice(MouseEvent mouseEvent) {
        txtRoomCapacityPrice.setText("");
    }
    //end of capacity


}
