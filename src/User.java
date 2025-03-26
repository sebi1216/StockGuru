import java.util.HashMap;

public class User {
    int ID;
    String username;
    int money = 1000;
    HashMap<Integer, Integer> stockPortfolio = new HashMap<Integer, Integer>();

    public User(int ID, String username) {
        this.username = username;
    }

    public void buyStock (int stockID, int amount, double course, ActionLogs actionLogs, int day) {
        if (money >= amount * course) {
            money -= amount * course;
            if (stockPortfolio.containsKey(stockID)) {
                stockPortfolio.put(stockID, stockPortfolio.get(stockID) + amount);
            } else {
                stockPortfolio.put(stockID, amount);
            }
            actionLogs.addLog(new ActionLog(ID, day, stockID, amount, course, (amount * course) * -1));
        }
    }

    public void sellStock (int stockID, int amount, double course, ActionLogs actionLogs, int day) {
        if (stockPortfolio.containsKey(stockID) && stockPortfolio.get(stockID) >= amount) {
            money += amount * course;
            stockPortfolio.put(stockID, stockPortfolio.get(stockID) - amount);
            actionLogs.addLog(new ActionLog(ID, day, stockID, amount, course, amount * course));
        }
    }
}
