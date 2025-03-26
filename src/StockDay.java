import java.util.ArrayList;

public class StockDay {
    int day;
    ArrayList<Stock> stocks = new ArrayList<Stock>();

    public StockDay(int day) {
        this.day = day;
    }

    public Stock getStock (int ID) {
        for (Stock stock : stocks) {
            if (stock.ID == ID) {
                return stock;
            }
        }
        return null;
    }

    public void addStock(Stock stock) {
        if (stock != null) {
            stocks.add(stock);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("StockDay { day=").append(day).append(", stocks=[");
        for (Stock stock : stocks) {
            sb.append("\n  ").append(stock.toString());
        }
        sb.append("\n]}");
        return sb.toString();
    }
}
