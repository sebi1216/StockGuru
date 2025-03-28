package User;
import java.util.HashMap;
import java.util.Map;

import DisplayUtils.DisplayUtils;
import Logs.ActionLogs;
import Stocks.Stock;
import Stocks.StockDay;

public abstract class User {
    int ID;
    String username;
    int money = 1000;
    HashMap<Integer, Integer> stockPortfolio = new HashMap<Integer, Integer>(); // stockID, amount

    /**
     * * Constructor for the User class.
     * @param ID
     * @param username
     */
    public User(int ID, String username) {
        this.username = username;
    }

    /**
     * * Returns the ID of the user.
     * @return ID
     */
    public int getID() {
        return ID;
    }

    /**
     * * Returns the username of the user.
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * * Returns the current amount of money the user has.
     * @return money
     */
    public int getMoney() {
        return money;
    }

    /**
     * * Returns the stock portfolio of the user.
     * @return stockPortfolio
     */
    public HashMap<Integer, Integer> getStockPortfolio() {
        return stockPortfolio;
    }

    /**
     * * Buys a stock if the user has enough money.
     * If the user already has the stock, it adds the amount to the existing amount.
     * @param stockID
     * @param name
     * @param amount
     * @param course
     * @param actionLogs
     * @param day
     */
    public void buyStock (Stock stock, String name, int amount, ActionLogs actionLogs, int day) {
        double course = stock.getCourse();
        int stockID = stock.getID();
        if (money >= amount * course) {
            money -= amount * course;
            if (stockPortfolio.containsKey(stockID)) {
                stockPortfolio.put(stockID, stockPortfolio.get(stockID) + amount);
            } else {
                stockPortfolio.put(stockID, amount);
            }
            actionLogs.addBuySellLog(ID, day, stockID, amount, course);
            DisplayUtils.buyStockMessage(name, amount, course, money);
            stock.setVolume(stock.getVolume() - amount);
        }
    }

    /**
     * * Sells a stock if the user has enough of it.
     * @param stockID
     * @param name
     * @param amount
     * @param course
     * @param actionLogs
     * @param day
     */
    public void sellStock (Stock stock, String name, int amount, ActionLogs actionLogs, int day) {
        double course = stock.getCourse();
        int stockID = stock.getID();
        if (amount == 0) {return;}
        if (stockPortfolio.containsKey(stockID) && stockPortfolio.get(stockID) >= amount) {
            money += amount * course;
            stockPortfolio.put(stockID, stockPortfolio.get(stockID) - amount);
            if (stockPortfolio.get(stockID) == 0) {
                stockPortfolio.remove(stockID);
            }
            actionLogs.addBuySellLog(ID, day, stockID, amount * -1, course);
            DisplayUtils.sellStockMessage(name, amount, course, money);
            stock.setVolume(stock.getVolume() + amount);
        }
    }

    /**
     * * Sells all stocks in the portfolio.
     * @param actionLogs
     * @param day
     * @param stockDay
     */
    public void sellAllStocks (ActionLogs actionLogs, int day,StockDay stockDay, HashMap<Integer, Object[]> stocksMap) {
        for (int stockID : stockPortfolio.keySet()) {
            Stock stock = stockDay.getStock(stockID);
            String stockName = stocksMap.get(stockID)[1].toString();
            sellStock(stock, stockName, stockPortfolio.get(stockID), actionLogs, day);
        }
        System.out.println("All stocks sold new Balance: " + money + "$");
    }

    /**
     * * Evaluates the stocks in the portfolio and prints the current value of each stock.
     * @param today
     * @param actionLogs
     * @param stocksMap
     * @param day
     */
    public void evaluateSellOptions(StockDay today, ActionLogs actionLogs, HashMap<Integer, Object[]> stocksMap, int day) {
        DisplayUtils.printSellOptionsHeader();
        for (int stockID : stockPortfolio.keySet()) {
            Stock stock = today.getStock(stockID);
            if (stock != null) {
                double currentPrice = stock.getCourse();
                double avgEntryPrice = actionLogs.getAvgEntryPrice(ID, stockID, day);
                int stockAmount = stockPortfolio.get(stockID);
                if (stockAmount == 0) {continue;}
                String stockName = stocksMap.get(stockID)[1].toString();
                String stockAbbr = stocksMap.get(stockID)[0].toString();
        
                double profitPercentage = ((currentPrice - avgEntryPrice) / avgEntryPrice) * 100;
                double profitAmount = (currentPrice - avgEntryPrice) * stockAmount;
                DisplayUtils.displaySellDetailsOptions(stock.getID(), stockAbbr, stockName, stockAmount, currentPrice, profitAmount, profitPercentage);
            }
        }
        DisplayUtils.displaySeparator();
    }

    /**
     * * Returns the current value of the portfolio.
     * @param today
     * @return investedValue
     */
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

    /**
     * * Returns the total value of the portfolio.
     * @param today
     * @return totalValue
     */
    public double getTotalValue(StockDay today) {
        double totalValue = -1000; // Starting with the initial investment of 1000
        totalValue += getInvestedValue(today); // Add the current value of the portfolio
        totalValue += money; // Add the remaining cash in the account
        return totalValue;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User { ID=").append(ID).append(", username=").append(username).append(", money=").append(money).append(", stockPortfolio={");
        for (Map.Entry<Integer, Integer> entry : stockPortfolio.entrySet()) {
            sb.append("StockID: ").append(entry.getKey()).append(", Amount: ").append(entry.getValue()).append("; ");
        }
        sb.append("} }");
        return sb.toString();
    }
}
