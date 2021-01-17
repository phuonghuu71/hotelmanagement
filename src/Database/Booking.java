package Database;

public class Booking {
    String id;
    String customerName;
    String roomName;
    String reservationDate;
    String checkInDate;
    String checkOutDate;
    String totalCash;
    String billID;

    public Booking(String id, String customerName, String roomName, String reservationDate, String checkInDate, String checkOutDate, String totalCash, String billID) {
        this.id = id;
        this.customerName = customerName;
        this.roomName = roomName;
        this.reservationDate = reservationDate;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalCash = totalCash;
        this.billID = billID;
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

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getTotalCash() {
        return totalCash;
    }

    public void setTotalCash(String totalCash) {
        this.totalCash = totalCash;
    }

    public String getBillID() {
        return billID;
    }

    public void setBillID(String billID) {
        this.billID = billID;
    }
}
