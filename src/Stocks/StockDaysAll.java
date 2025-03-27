package Stocks;
import java.util.ArrayList;

public class StockDaysAll {
    ArrayList<StockDay> stockDays = new ArrayList<StockDay>();

    /**
     * * Constructor for the StockDaysAll class.
     * * Initializes an empty list of StockDays.
     */
    public StockDaysAll() {
        this.stockDays = new ArrayList<>();
    }

    /**
     * * Adds a StockDay to the list of StockDays.
     * @param stockDay
     */
    public void addStockDay(StockDay stockDay) {
        if (stockDay != null) {
            stockDays.add(stockDay);
        }
    }

    /**
     * * Returns the StockDay for the given day.
     * @param day
     * @return StockDay or null if not found
     */
    public StockDay getStockDay(int day) {
        for (StockDay stockDay : stockDays) {
            if (stockDay.day == day) {
                return stockDay;
            }
        }
        return null;
    }
    
}
