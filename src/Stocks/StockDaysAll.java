package Stocks;
import java.util.HashMap;

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
