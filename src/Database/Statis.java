package Database;

public class Statis {
    String month;
    String money;

    public Statis(String month, String money) {
        this.month = month;
        this.money = money;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
