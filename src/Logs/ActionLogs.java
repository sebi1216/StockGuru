package Logs;
import java.util.ArrayList;

public class ActionLogs {
    static ArrayList<BuySellLog> BuySellLogs = new ArrayList<BuySellLog>();
    static ArrayList<UserLog> UserLogs = new ArrayList<UserLog>();
    private static int currentBSID = 0;
    private static int currentUID = 0;

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
    public void addBuySellLog(int userID, int day, int stockID, int amount, double course) {
        int ID = currentBSID++;
        BuySellLog log = new BuySellLog(ID, userID, day, stockID, amount, course);
        if (log != null) {
            BuySellLogs.add(log);
        }
    }

    /**
     * Gets the list of BuySellLogs.
     * @return ArrayList of BuySellLogs
     */
    public static ArrayList<BuySellLog> getBuySellLogs() {
        return BuySellLogs;
    }

    /**
     * Adds a UserLog to the list of UserLogs.
     * @param userID
     * @param day
     * @param action
     */
    public static void addUserLog(int userID, int day, String action) {
        int ID = currentUID++;
        UserLog log = new UserLog(ID, userID, day, action);
        if (log != null) {
            UserLogs.add(log);
        }
    }

    /**
     * Gets the list of UserLogs.
     * @return ArrayList of UserLogs
     */
    public static ArrayList<UserLog> getUserLogs() {
        return UserLogs;
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

    /**
     * Override the toString method to return a string representation of the ActionLogs object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Buy/Sell Logs:\n");
        for (BuySellLog log : BuySellLogs) {
            sb.append(log.toString()).append("\n");
        }

        sb.append("User Logs:\n");
        for (UserLog log : UserLogs) {
            sb.append(log.toString()).append("\n");
        }

        return sb.toString();
    }
}