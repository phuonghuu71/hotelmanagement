package Database;

public class Service {
    String id;
    String serviceName;
    String servicePrice;

    public Service(String id, String serviceName, String servicePrice) {
        this.id = id;
        this.serviceName = serviceName;
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

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }
}
