package Database;

public class CallService {
    String id;
    String roomName;
    String serviceName;
    String quantity;
    String servicePrice;

    public CallService(String id, String roomName, String serviceName, String quantity, String servicePrice) {
        this.id = id;
        this.roomName = roomName;
        this.serviceName = serviceName;
        this.quantity = quantity;
        this.servicePrice = servicePrice;
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

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
