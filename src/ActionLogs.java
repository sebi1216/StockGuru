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

    public double getAvgEntryPrice(int userID, int stockID, int day) {
        double sum = 0;
        int count = 0;
        for (ActionLog log : Logs) {
            if (log.userID == userID && log.stockID == stockID && log.day <= day) {
                sum += log.course;
                count++;
            }
        }
        return sum / count;

    }

}
