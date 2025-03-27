package User;
import java.util.HashMap;
import java.util.Map;

import DisplayUtils.DisplayUtils;
import Logs.ActionLog;
import Logs.ActionLogs;
import Stocks.Stock;
import Stocks.StockDay;

public abstract class User {
    int ID;
    String username;
    int money = 1000;
    HashMap<Integer, Integer> stockPortfolio = new HashMap<Integer, Integer>(); // stockID, amount

    public User(int ID, String username) {
        this.username = username;
    }

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public HashMap<Integer, Integer> getStockPortfolio() {
        return stockPortfolio;
    }

    public void setStockPortfolio(HashMap<Integer, Integer> stockPortfolio) {
        this.stockPortfolio = stockPortfolio;
    }

    public void buyStock (int stockID, String name, int amount, double course, ActionLogs actionLogs, int day) {
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
        if (amount == 0) {return;}
        if (stockPortfolio.containsKey(stockID) && stockPortfolio.get(stockID) >= amount) {
            money += amount * course;
            stockPortfolio.put(stockID, stockPortfolio.get(stockID) - amount);
            actionLogs.addLog(new ActionLog(ID, day, stockID, amount, course, amount * course));
            System.out.println("Sold " + amount + " stocks of " + stockID + " for " + ((double) amount) * course + "$");
        }
    }

    public void sellAllStocks (ActionLogs actionLogs, int day,StockDay stockDay) {
        for (int stockID : stockPortfolio.keySet()) {
            Stock stock = stockDay.getStock(stockID);
            sellStock(stockID, stockPortfolio.get(stockID), stock.getCourse(), actionLogs, day);
        }
        System.out.println("All stocks sold new Balance: " + money + "$");
    }

    public void evaluateSellOptions(StockDay today, ActionLogs actionLogs, HashMap<Integer, Object[]> stocksMap, int day) {
        for (int stockID : stockPortfolio.keySet()) {
            Stock stock = today.getStock(stockID);
            if (stock != null) {
                double currentPrice = stock.getCourse();
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

    public double getInvestedValue(StockDay today) {
        double investedValue = 0.0;
    
        for (Map.Entry<Integer, Integer> entry : getStockPortfolio().entrySet()) {
            int stockID = entry.getKey();
            int stockAmount = entry.getValue();
            Stock stock = today.getStock(stockID);
    
            if (stock != null) {
                investedValue += stockAmount * stock.getCourse();
            }
        }
        return investedValue;
    }

    public double getTotalValue(StockDay today) {
        double totalValue = -1000; // Starting with the initial investment of 1000
        totalValue += getInvestedValue(today); // Add the current value of the portfolio
        totalValue += money; // Add the remaining cash in the account
        return totalValue;
    }
}
