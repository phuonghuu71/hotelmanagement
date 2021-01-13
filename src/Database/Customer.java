package Database;

public class Customer {
    private String id;
    private String customerName;
    private String customerIdentityCard;
    private String phoneNumber;

    public Customer(String id, String customerName, String customerIdentityCard, String phoneNumber) {
        this.id = id;
        this.customerName = customerName;
        this.customerIdentityCard = customerIdentityCard;
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerIdentityCard() {
        return customerIdentityCard;
    }

    public void setCustomerIdentityCard(String customerIdentityCard) {
        this.customerIdentityCard = customerIdentityCard;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
