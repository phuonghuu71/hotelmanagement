package Database;

public class Report {
    String id;
    String roomName;
    String billDate;
    String checkInDate;
    String checkOutDate;
    String totalCash;

    public Report(String id, String roomName, String billDate, String checkInDate, String checkOutDate, String totalCash) {
        this.id = id;
        this.roomName = roomName;
        this.billDate = billDate;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalCash = totalCash;
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

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
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
}
