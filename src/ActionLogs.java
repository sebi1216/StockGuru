import java.util.ArrayList;

public class ActionLogs {
    ArrayList<ActionLog> Logs = new ArrayList<ActionLog>();

    public ActionLogs() {
        this.Logs = new ArrayList<>();
    }

    public void addLog(ActionLog log) {
        if (log != null) {
            Logs.add(log);
        }
    }

    public double getAvgEntryPrice(int userID, int stockID) {
        double sum = 0;
        int count = 0;
        for (ActionLog log : Logs) {
            System.out.println(log);
            System.out.println(userID + " " + stockID);
            if (log.userID == userID && log.stockID == stockID) {
                sum += (log.course * log.amount);
                count+= log.amount;
                System.out.println(sum + " " + count);
            }
        }
        System.out.println(sum);
        System.out.println(count);
        return sum / count;

    }

}
