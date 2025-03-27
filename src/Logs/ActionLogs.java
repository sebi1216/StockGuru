package Logs;
import java.util.ArrayList;

public class ActionLogs {
    ArrayList<ActionLog> Logs = new ArrayList<ActionLog>();

    /**
     * Constructor for the ActionLogs class.
     * Initializes an empty list of ActionLogs.
     */
    public ActionLogs() {
        this.Logs = new ArrayList<>();
    }

    /**
     * Adds an ActionLog to the list of ActionLogs.
     * @param log
     */
    public void addLog(int ID, int day, int stockID, int amount, double course) {
        ActionLog log = new ActionLog(ID, day, stockID, amount, course);
        if (log != null) {
            Logs.add(log);
        }
    }

    /**
     * Gets the Avgerage Entry Price for a given user and stock.
     * @param userID
     * @param stockID
     * @param day
     * @return Average Entry Price
     */
    public double getAvgEntryPrice(int userID, int stockID, int day) {
        double sum = 0;
        int count = 0;
        for (ActionLog log : Logs) {
            if (log.userID == userID && log.stockID == stockID && log.day <= day) {
                sum += (log.course * log.amount);
                count+= log.amount;
            }
        }
        return sum / count;
    }

}
