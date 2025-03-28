package Stocks;
import java.util.ArrayList;
import java.util.HashMap;

import DisplayUtils.DisplayUtils;

public class StockDay {
    int day;
    ArrayList<Stock> stocks = new ArrayList<Stock>();

    /**
     * * Constructor for the StockDay class.
     * @param day
     */
    public StockDay(int day) {
        this.day = day;
    }

    /**
     * * Returns alll the stocks for this StockDay.
     * @return ArrayList<Stock>
     */
    public ArrayList<Stock> getStocks() {
        return stocks;
    }

    /**
     * Returns the stock for the given ID.
     * @param ID
     * @return Stock
     */
    public Stock getStock (int ID) {
        for (Stock stock : stocks) {
            if (stock.ID == ID) {
                return stock;
            }
        }
        return null;
    }

    /**
     * * Adds a stock to the list of stocks for this StockDay.
     * @param stock
     */
    public void addStock(Stock stock) {
        if (stock != null) {
            stocks.add(stock);
        }
    }

    /**
     * * Shows the user which stocks they can buy with the given amount of money.
     * @param money
     * @param stocksMap
     */
    public void userCanBuy(double money, HashMap<Integer, Object[]> stocksMap) {
        DisplayUtils.printUserCanBuyHeader(money);
        for (Stock stock : stocks) {
            if (stock.course <= money) {
                int maxQuantity = (int) (money / stock.course);
                String stockAbbr = stocksMap.get(stock.ID)[0].toString();
                String stockName = stocksMap.get(stock.ID)[1].toString();
                DisplayUtils.displayUserCanBuyStock(stock.ID, stockAbbr, stockName, maxQuantity, stock.course);
            }
        }
    }

    /**
     * Overrides the toString method to provide a string representation of the StockDay object.
     */
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
