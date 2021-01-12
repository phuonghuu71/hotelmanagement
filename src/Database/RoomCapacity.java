package Database;

public class RoomCapacity {
    String id;
    String roomCapacity;
    String roomCapacityPrice;

    public RoomCapacity(String id, String roomCapacity, String roomCapacityPrice) {
        this.id = id;
        this.roomCapacity = roomCapacity;
        this.roomCapacityPrice = roomCapacityPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomCapacity() {
        return roomCapacity;
    }

    public void setRoomCapacity(String roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    public String getRoomCapacityPrice() {
        return roomCapacityPrice;
    }

    public void setRoomCapacityPrice(String roomCapacityPrice) {
        this.roomCapacityPrice = roomCapacityPrice;
    }
}
