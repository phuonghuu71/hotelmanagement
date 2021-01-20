package Database;

public class BillService {
    String id;
    String serviceName;
    String quantity;
    String servicePrice;

    public BillService(String id, String serviceName, String quantity, String servicePrice) {
        this.id = id;
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

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }
}
