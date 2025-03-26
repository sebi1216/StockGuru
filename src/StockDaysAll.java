import java.util.ArrayList;

public class StockDaysAll {
    ArrayList<StockDay> stockDays = new ArrayList<StockDay>();

    public StockDaysAll() {
        this.stockDays = new ArrayList<>();
    }

    public void addStockDay(StockDay stockDay) {
        if (stockDay != null) {
            stockDays.add(stockDay);
        }
    }

    public StockDay getStockDay(int day) {
        for (StockDay stockDay : stockDays) {
            if (stockDay.day == day) {
                return stockDay;
            }
        }
        return null;
    }
    
}
