package User;

import Logs.ActionLogs;
import Stocks.Stock;
import Stocks.StockDay;
import Stocks.StockDaysAll;

import java.util.*;

public class Bot extends User {
    private int maxSharesPerStockPercentage = 100;

    public Bot(int ID, String username, int maxSharesPerStockPercentage) {
        super(ID, username);
        this.maxSharesPerStockPercentage = maxSharesPerStockPercentage;
    }
    
    public void autoTrade(StockDaysAll stockDaysAll, ActionLogs actionLogs, int currentDay) {
        StockDay today = stockDaysAll.getStockDay(currentDay);

        if (currentDay == 0) {
            // Initial Investment Logic
            initialInvestment(today, actionLogs, currentDay);
            return;
        }
    
        if (currentDay == 10) {
            // Sell all stocks on day 10
            sellAllStocks(actionLogs, currentDay, today);
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
        optimizePortfolio(today, bestPerformingStocks, actionLogs, currentDay);
    
        // Allocate funds
        double totalFunds = getMoney();
        double growthFunds = totalFunds * 0.75;
        double riskFunds = totalFunds * 0.25;
    
        allocateFunds(today, bestPerformingStocks, growthFunds, actionLogs, currentDay);
        allocateFunds(today, undervaluedStocks, riskFunds, actionLogs, currentDay);
    }
    
    private void initialInvestment(StockDay today, ActionLogs actionLogs, int currentDay) {
        Random random = new Random();
        List<Stock> stocks = today.getStocks();
        Collections.shuffle(stocks); // Randomize stock selection
    
        double funds = getMoney();
        for (Stock stock : stocks) {
            double stockPrice = stock.getCourse();
            int maxShares = (int) (funds / stockPrice);
    
            if (maxShares > 0) {
                int sharesToBuy = random.nextInt(maxShares) + 1; // Randomize quantity
                buyStock(stock.getID(), "Stock " + stock.getID(), sharesToBuy, stockPrice, actionLogs, currentDay);
                funds -= sharesToBuy * stockPrice;
    
                if (funds <= 0) break; // Stop if funds are exhausted
            }
        }
    }
    
    private void optimizePortfolio(StockDay today, List<Stock> bestPerformingStocks, ActionLogs actionLogs, int currentDay) {
        List<Map.Entry<Integer, Integer>> portfolioEntries = new ArrayList<>(getStockPortfolio().entrySet());
    
        for (Map.Entry<Integer, Integer> entry : portfolioEntries) {
            int stockID = entry.getKey();
            int stockAmount = entry.getValue();
            Stock stock = today.getStock(stockID);
    
            if (stock != null) {
                double currentPrice = stock.getCourse();
                double avgEntryPrice = actionLogs.getAvgEntryPrice(getID(), stockID, currentDay);
                double profit = (currentPrice - avgEntryPrice) * stockAmount;
    
                if (profit > 0) {
                    for (Stock potentialStock : bestPerformingStocks) {
                        if (potentialStock.getCourse() > currentPrice) {
                            sellStock(stockID, stockAmount, currentPrice, actionLogs, currentDay);
                            double funds = stockAmount * currentPrice;
                            allocateFunds(today, Collections.singletonList(potentialStock), funds, actionLogs, currentDay);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void allocateFunds(StockDay today, List<Stock> stocks, double funds, ActionLogs actionLogs, int currentDay) {
        double totalPortfolioValue = funds + getInvestedValue(today); // Total funds including invested value
        double maxInvestment = totalPortfolioValue * (maxSharesPerStockPercentage / 100.0); // Max investment per stock
    
        for (Stock stock : stocks) {
            double stockPrice = stock.getCourse();
            int maxShares = (int) (maxInvestment / stockPrice);
    
            if (maxShares > 0 && stockPrice <= funds) {
                int sharesToBuy = Math.min(maxShares, (int) (funds / stockPrice));
                buyStock(stock.getID(), "Stock " + stock.getID(), sharesToBuy, stockPrice, actionLogs, currentDay);
                funds -= sharesToBuy * stockPrice;
    
                if (funds <= 0) break;
            }
        }
    }
}