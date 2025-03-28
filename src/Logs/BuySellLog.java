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
     * Returns the ID of the log.
     * @return
     */
    public int getLogID() {
        return LogID;
    }

    /**
     * Returns the ID of the user who made the transaction.
     * @return
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Returns the day of the transaction.
     * @return
     */
    public int getDay() {
        return day;
    }

    /**
     * Returns the ID of the stock bought or sold in the transaction.
     * @return
     */
    public int getStockID() {
        return stockID;
    }

    /**
     * Returns the amount of stock bought or sold in the transaction.
     * @return
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Returns the course of the stock at the time of the transaction.
     * @return
     */
    public double getCourse() {
        return course;
    }

    /**
     * Returns the amount of money spent or earned in the transaction.
     * @return
     */
    public double getMoney() {
        return money;
    }

    /**
     * Override the toString method to return a string representation of the BuySellLog object.
     */
    @Override
    public String toString() {
        return "ActionLog { LogID=" + LogID + ", userID=" + userID + ", day=" + day + ", stockID=" + stockID + ", amount=" + amount + ", course=" + course + " }";
    }
}
