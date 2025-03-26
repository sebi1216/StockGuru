package Stocks;
import java.util.ArrayList;
import java.util.HashMap;

public class StockDay {
    int day;
    ArrayList<Stock> stocks = new ArrayList<Stock>();

    public StockDay(int day) {
        this.day = day;
    }

    public ArrayList<Stock> getStocks() {
        return stocks;
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

    public void userCanBuy(double money, HashMap<Integer, Object[]> stocksMap) {
        for (Stock stock : stocks) {
            if (stock.course <= money) {
                int maxQuantity = (int) (money / stock.course);
                System.out.println("You can buy " + stocksMap.get(stock.ID)[1].toString() + " for " + stock.course + "$ each. Maximum quantity: " + maxQuantity);
            }
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
