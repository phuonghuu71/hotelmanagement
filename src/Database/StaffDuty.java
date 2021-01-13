package Database;

public class StaffDuty {
    String id;
    String tenChucvu;
    String luong;

    public StaffDuty(String id, String tenChucvu, String luong) {
        this.id = id;
        this.tenChucvu = tenChucvu;
        this.luong = luong;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenChucvu() {
        return tenChucvu;
    }

    public void setTenChucvu(String tenChucvu) {
        this.tenChucvu = tenChucvu;
    }

    public String getLuong() {
        return luong;
    }

    public void setLuong(String luong) {
        this.luong = luong;
    }
}
