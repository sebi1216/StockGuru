package Logs;
public class BuySellLog {
    int LogID;
    int userID;
    int day;
    int stockID;
    int amount;
    double course;
    double money;

    /**
     * Constructor for the BuySellLog class.
     * @param userID
     * @param day
     * @param stockID
     * @param amount
     * @param course
     */
    public BuySellLog(int LogID, int userID, int day, int stockID, int amount, double course) {
        this.LogID = LogID;
        this.userID = userID;
        this.day = day;
        this.stockID = stockID;
        this.amount = amount;
        this.course = course;
    }

    /**
     * Override the toString method to return a string representation of the BuySellLog object.
     */
    @Override
    public String toString() {
        return "ActionLog { LogID=" + LogID + ", userID=" + userID + ", day=" + day + ", stockID=" + stockID + ", amount=" + amount + ", course=" + course + " }";
    }
}
