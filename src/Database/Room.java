package Database;

public class Room {
    String id;
    String roomName;
    String roomStatus;
    String roomTypeName;
    String roomCapacity;
    String roomPrice;

    public Room(String id, String roomName, String roomStatus, String roomTypeName, String roomCapacity, String roomPrice) {
        this.id = id;
        this.roomName = roomName;
        this.roomStatus = roomStatus;
        this.roomTypeName = roomTypeName;
        this.roomCapacity = roomCapacity;
        this.roomPrice = roomPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(String roomStatus) {
        this.roomStatus = roomStatus;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public String getRoomCapacity() {
        return roomCapacity;
    }

    public void setRoomCapacity(String roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    public String getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(String roomPrice) {
        this.roomPrice = roomPrice;
    }
}
