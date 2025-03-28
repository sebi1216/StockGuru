package Logs;
import java.util.ArrayList;

public class ActionLogs {
    ArrayList<BuySellLog> BuySellLogs = new ArrayList<BuySellLog>();
    private static int currentID = 0;

    /**
     * Constructor for the ActionLogs class.
     * Initializes an empty list of ActionLogs.
     */
    public ActionLogs() {
        this.BuySellLogs = new ArrayList<>();
    }

    /**
     * Adds an ActionLog to the list of ActionLogs.
     * @param day
     * @param stockID
     * @param amount
     * @param course
     */
    public void addLog(int day, int stockID, int amount, double course) {
        int ID = currentID++;
        BuySellLog log = new BuySellLog(ID, day, stockID, amount, course);
        if (log != null) {
            BuySellLogs.add(log);
        }
    }

    /**
     * Gets the Average Entry Price for a given user and stock.
     * @param userID
     * @param stockID
     * @param day
     * @return Average Entry Price
     */
    public double getAvgEntryPrice(int userID, int stockID, int day) {
        double sum = 0;
        int count = 0;
        for (BuySellLog log : BuySellLogs) {
            if (log.userID == userID && log.stockID == stockID && log.day <= day) {
                sum += (log.course * log.amount);
                count += log.amount;
            }
        }
        return sum / count;
    }
}