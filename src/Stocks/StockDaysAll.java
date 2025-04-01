package Stocks;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StockDaysAll {
    HashMap<Integer, StockDay> stockDays = new HashMap<Integer, StockDay>();

    /**
     * * Constructor for the StockDaysAll class.
     * * Initializes an empty list of StockDays.
     */
    public StockDaysAll() {
        this.stockDays = new HashMap<Integer, StockDay>();
    }

    /**
     * * Adds a StockDay to the list of StockDays.
     * @param stockDay
     */
    public void addStockDay(StockDay stockDay) {
        if (stockDay != null) {
            stockDays.put(stockDay.day, stockDay);
        } else {
            System.out.println("StockDay is null. Cannot add to list.");
        }
    }

    /**
     * * Returns the StockDay for the given day.
     * @param day
     * @return StockDay or null if not found
     */
    public StockDay getStockDay(int day) {
        if (stockDays.containsKey(day)) {
            return stockDays.get(day);
        } else {
            System.out.println("StockDay not found for day: " + day);
        }
        return null;
    }

    /**
     * Returns the best performing stocks and undervalued stocks for a given day.
     * @param day
     * @param money
     * @return
     */
    public HashMap<String, List<Stock>> getBestStocks(int day, int money) {
        HashMap<String, List<Stock>> result = new HashMap<>();
        if (day <= 0) {return result;}
        List<Stock> bestPerformingStocks = new ArrayList<>();
        List<Stock> undervaluedStocks = new ArrayList<>();
    
        StockDay previousDay = getStockDay(day - 1);
        StockDay today = getStockDay(day);
        if (previousDay == null || today == null) {
            return result;
        }
    
        for (Stock stock : today.getStocks()) {
            if (stock.getCourse() > money) {
                continue;
            }
    
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
    
        // Sort best-performing stocks by highest percentage change
        bestPerformingStocks.sort((s1, s2) -> {
            double change1 = (s1.getCourse() - previousDay.getStock(s1.getID()).getCourse()) / previousDay.getStock(s1.getID()).getCourse();
            double change2 = (s2.getCourse() - previousDay.getStock(s2.getID()).getCourse()) / previousDay.getStock(s2.getID()).getCourse();
            return Double.compare(change2, change1);
        });
    
        // Sort undervalued stocks by most negative percentage change
        undervaluedStocks.sort((s1, s2) -> {
            double change1 = (s1.getCourse() - previousDay.getStock(s1.getID()).getCourse()) / previousDay.getStock(s1.getID()).getCourse();
            double change2 = (s2.getCourse() - previousDay.getStock(s2.getID()).getCourse()) / previousDay.getStock(s2.getID()).getCourse();
            return Double.compare(change1, change2);
        });
    
        // Limit the results to 2 stocks each
        result.put("bestPerformingStocks", bestPerformingStocks.stream().limit(2).toList());
        result.put("undervaluedStocks", undervaluedStocks.stream().limit(2).toList());
        return result;
    }

    /**
     * Overrides the toString method to display all StockDays.
     * @return String representation of StockDaysAll
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("StockDaysAll { ");
        for (StockDay stockDay : stockDays.values()) {
            sb.append(stockDay.toString()).append(", ");
        }
        sb.append("}");
        return sb.toString();
    }
}
