package Database;

public class RoomType {
    String id;
    String roomTypeName;
    String roomPrice;

    public RoomType(String id, String roomTypeName, String roomPrice) {
        this.id = id;
        this.roomTypeName = roomTypeName;
        this.roomPrice = roomPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public String getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(String roomPrice) {
        this.roomPrice = roomPrice;
    }

}
