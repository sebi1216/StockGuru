package User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import DisplayUtils.DisplayUtils;
import Logs.ActionLogs;
import Notes.Notes;
import Stocks.Stock;
import Stocks.StockDay;
import Stocks.StockDaysAll;

public abstract class User {
    int ID;
    String username;
    int money = 1000;
    HashMap<Integer, Integer> stockPortfolio = new HashMap<Integer, Integer>(); // stockID, amount
    Notes notes = new Notes();

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
        HashMap<Integer, Integer> stockPortfolioCopy = new HashMap<Integer, Integer>();
        stockPortfolioCopy.putAll(stockPortfolio);
        for (int stockID : stockPortfolioCopy.keySet()) {
            if (stockPortfolio.getOrDefault(stockID, -1) == -1) {continue;}
            Stock stock = stockDay.getStock(stockID);
            String stockName = stocksMap.get(stockID)[1].toString();
            sellStock(stock, stockName, stockPortfolio.get(stockID), actionLogs, day);
        }
        DisplayUtils.allStocksSold(money);
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
                String noteText = notes.getNote(stockID);
                if (noteText == null) {noteText = "";}
                DisplayUtils.displaySellDetailsOptions(stock.getID(), stockAbbr, stockName, stockAmount, currentPrice, profitAmount, profitPercentage, noteText);
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

    /**
     * Adds a note to the stock.
     * @param noteText
     * @param day
     * @param stockID
     */
    public void addNote (String noteText, int day, int stockID) {
        notes.addNote(noteText, day, stockID);
    }

    /**
     * Get the Note for a StockID
     * @param StockID
     */
    public String getNote(int StockID) {
        String note = notes.getNote(StockID);
        return note != null ? note : "";
    }

    /**
     * Trades for the User.
     * This method analyzes the stock market and makes trading decisions
     * based on the user's portfolio and available funds.
     * @param stockDaysAll The stock data for all days.
     * @param actionLogs The logs to record buy/sell actions.
     * @param currentDay The current day in the simulation.
     * @param stocksMap A map of stock IDs to their abbreviations and names.
     */
    public void autoTrade(StockDaysAll stockDaysAll, ActionLogs actionLogs, int currentDay, HashMap<Integer, Object[]> stocksMap) {
        StockDay today = stockDaysAll.getStockDay(currentDay);
    
        if (currentDay == 0) {
            // Initial Investment Logic
            initialInvestment(today, actionLogs, currentDay, stocksMap);
            return;
        }
    
        if (currentDay == 10) {
            return;
        }
    
        StockDay previousDay = stockDaysAll.getStockDay(currentDay - 1);
    
        if (today == null || previousDay == null) return;
    
        // Analyze percentage changes
        List<Stock> bestPerformingStocks = new ArrayList<>();
        List<Stock> undervaluedStocks = new ArrayList<>();
    
        for (Stock stock : today.getStocks()) {
            Stock previousStock = previousDay.getStock(stock.getID());
            if (previousStock != null) {
                double percentageChange = ((stock.getCourse() - previousStock.getCourse()) / previousStock.getCourse()) * 100;
                if (percentageChange > 0) {
                    bestPerformingStocks.add(stock);
                } else {
                    undervaluedStocks.add(stock);
                }
            }
        }
    
        // Sort stocks by percentage change
        bestPerformingStocks.sort((s1, s2) -> Double.compare(
            (s2.getCourse() - previousDay.getStock(s2.getID()).getCourse()) / previousDay.getStock(s2.getID()).getCourse(),
            (s1.getCourse() - previousDay.getStock(s1.getID()).getCourse()) / previousDay.getStock(s1.getID()).getCourse()
        ));
        undervaluedStocks.sort((s1, s2) -> Double.compare(
            (s1.getCourse() - previousDay.getStock(s1.getID()).getCourse()) / previousDay.getStock(s1.getID()).getCourse(),
            (s2.getCourse() - previousDay.getStock(s2.getID()).getCourse()) / previousDay.getStock(s2.getID()).getCourse()
        ));
    
        // Selling Strategy
        optimizePortfolio(today, bestPerformingStocks, actionLogs, currentDay, stocksMap);
    
        // Allocate funds
        double totalFunds = getMoney();
        double growthFunds = totalFunds * 0.75;
        double riskFunds = totalFunds * 0.25;
    
        allocateFunds(today, bestPerformingStocks, growthFunds, actionLogs, currentDay, stocksMap);
        allocateFunds(today, undervaluedStocks, riskFunds, actionLogs, currentDay, stocksMap);
    }
    
    /**
     * Initial investment strategy for the user.
     * This method is called on the first day to invest in stocks.
     * @param today The stock data for the current day.
     * @param actionLogs The logs to record buy/sell actions.
     * @param currentDay The current day in the simulation.
     * @param stocksMap A map of stock IDs to their abbreviations and names.
     */
    private void initialInvestment(StockDay today, ActionLogs actionLogs, int currentDay, HashMap<Integer, Object[]> stocksMap) {
        Random random = new Random();
        List<Stock> stocks = today.getStocks();
        Collections.shuffle(stocks); // Randomize stock selection
    
        double funds = getMoney();
        for (Stock stock : stocks) {
            double stockPrice = stock.getCourse();
            int maxShares = (int) (funds / stockPrice);
    
            if (maxShares > 0) {
                int sharesToBuy = random.nextInt(maxShares) + 1; // Randomize quantity
                String stockName = stocksMap.get(stock.getID())[1].toString();
                buyStock(stock, stockName, sharesToBuy, actionLogs, currentDay);
                funds -= sharesToBuy * stockPrice;
    
                if (funds <= 0) break; // Stop if funds are exhausted
            }
        }
    }
    
    /**
     * Optimizes the portfolio by selling stocks that are performing well and reallocating funds to better-performing stocks.
     * @param today The stock data for the current day.
     * @param bestPerformingStocks The list of best-performing stocks.
     * @param actionLogs The logs to record buy/sell actions.
     * @param currentDay The current day in the simulation.
     * @param stocksMap A map of stock IDs to their abbreviations and names.
     */
    private void optimizePortfolio(StockDay today, List<Stock> bestPerformingStocks, ActionLogs actionLogs, int currentDay, HashMap<Integer, Object[]> stocksMap) {
        List<Map.Entry<Integer, Integer>> portfolioEntries = new ArrayList<>(getStockPortfolio().entrySet());
    
        for (Map.Entry<Integer, Integer> entry : portfolioEntries) {
            int stockID = entry.getKey();
            int stockAmount = entry.getValue();
            Stock stock = today.getStock(stockID);
            String stockName = stocksMap.get(stockID)[1].toString();
    
            if (stock != null) {
                double currentPrice = stock.getCourse();
                double avgEntryPrice = actionLogs.getAvgEntryPrice(getID(), stockID, currentDay);
                double profit = (currentPrice - avgEntryPrice) * stockAmount;
    
                if (profit > 0) {
                    for (Stock potentialStock : bestPerformingStocks) {
                        if (potentialStock.getCourse() > currentPrice) {
                            sellStock(potentialStock, stockName, stockAmount, actionLogs, currentDay);
                            double funds = stockAmount * currentPrice;
                            allocateFunds(today, Collections.singletonList(potentialStock), funds, actionLogs, currentDay, stocksMap);
                            break;
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Allocates funds to buy stocks based on the available funds and the maximum investment percentage per stock.
     * @param today The stock data for the current day.
     * @param stocks The list of stocks to consider for investment.
     * @param funds The amount of funds available for investment.
     * @param actionLogs The logs to record buy/sell actions.
     * @param currentDay The current day in the simulation.
     * @param stocksMap A map of stock IDs to their abbreviations and names.
     */
    private void allocateFunds(StockDay today, List<Stock> stocks, double funds, ActionLogs actionLogs, int currentDay, HashMap<Integer, Object[]> stocksMap) {
        for (Stock stock : stocks) {
            double stockPrice = stock.getCourse();
            int maxShares = (int) (funds / stockPrice);
    
            if (maxShares > 0 && stockPrice <= funds) {
                int sharesToBuy = Math.min(maxShares, (int) (funds / stockPrice));
                String stockName = stocksMap.get(stock.getID())[1].toString();
                buyStock(stock, stockName, sharesToBuy, actionLogs, currentDay);
                funds -= sharesToBuy * stockPrice;
    
                if (funds <= 0) break;
            }
        }
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
