public class ActionLog {
    int userID;
    int day;
    int stockID;
    int amount;
    double course;
    double money;

    public ActionLog(int userID, int day, int stockID, int amount, double course, double money) {
        this.userID = userID;
        this.day = day;
        this.stockID = stockID;
        this.amount = amount;
        this.course = course;
        this.money = money;
    }

    @Override
    public String toString() {
        return "ActionLog { userID=" + userID + ", day=" + day + ", stockID=" + stockID + ", amount=" + amount + ", course=" + course + ", money=" + money + " }";
    }
}
