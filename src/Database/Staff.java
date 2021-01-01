package Database;

public class Staff {
    String id;
    String staffName;
    String userName;
    String type;
    String salary;

    public Staff(String id, String staffName, String userName, String type, String salary) {
        this.id = id;
        this.staffName = staffName;
        this.userName = userName;
        this.type = type;
        this.salary = salary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }
}
