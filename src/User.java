import java.util.HashMap;

public class User {
    int ID;
    String username;
    int money = 1000;
    HashMap<Integer, Integer> stockPortfolio = new HashMap<Integer, Integer>(); // stockID, amount

    public User(int ID, String username) {
        this.username = username;
    }

    public void buyStock (int stockID,String name, int amount, double course, ActionLogs actionLogs, int day) {
        if (money >= amount * course) {
            money -= amount * course;
            if (stockPortfolio.containsKey(stockID)) {
                stockPortfolio.put(stockID, stockPortfolio.get(stockID) + amount);
            } else {
                stockPortfolio.put(stockID, amount);
            }
            actionLogs.addLog(new ActionLog(ID, day, stockID, amount, course, (amount * course) * -1));
            System.out.println("Bought " + amount + " stocks of " + name + " for " + ((double) amount) * course + "$");
            System.out.println("New Account Balance: " + money + "$");
        }
    }

    public void sellStock (int stockID, int amount, double course, ActionLogs actionLogs, int day) {
        if (stockPortfolio.containsKey(stockID) && stockPortfolio.get(stockID) >= amount) {
            money += amount * course;
            stockPortfolio.put(stockID, stockPortfolio.get(stockID) - amount);
            actionLogs.addLog(new ActionLog(ID, day, stockID, amount, course, amount * course));
            System.out.println("Sold " + amount + " stocks of " + stockID + " for " + ((double) amount) * course + "$");
        }
    }

    public void sellAllStocks (ActionLogs actionLogs, int day) {
        for (int stockID : stockPortfolio.keySet()) {
            Stock stock = new Stock(stockID, 0, 0);
            sellStock(stockID, stockPortfolio.get(stockID), stock.course, actionLogs, day);
        }
        System.out.println("All stocks sold new Balance: " + money + "$");

    }

    public void evaluateSellOptions(StockDay today, ActionLogs actionLogs, HashMap<Integer, Object[]> stocksMap, int day) {
        for (int stockID : stockPortfolio.keySet()) {
            Stock stock = today.getStock(stockID);
            if (stock != null) {
                double currentPrice = stock.course;
                double avgEntryPrice = actionLogs.getAvgEntryPrice(ID, stockID, day);
                int stockAmount = stockPortfolio.get(stockID);
                String stockName = stocksMap.get(stockID)[1].toString();
    
                System.out.println("You can sell " + stockAmount + " stocks of " + stockName + " for " + currentPrice + "$ each.");
    
                if (currentPrice < avgEntryPrice) {
                    double loss = stockAmount * (avgEntryPrice - currentPrice);
                    System.out.println("You will lose " + loss + "$");
                } else if (currentPrice > avgEntryPrice) {
                    double profit = stockAmount * (currentPrice - avgEntryPrice);
                    System.out.println("You will profit " + profit + "$");
                }
            }
        }
    }
}
