import java.util.ArrayList;

public class User {
    String username;
    int money = 1000;
    int day = 0;
    ArrayList<Stock> stockPortfolio = new ArrayList<Stock>();
    ArrayList<Stock> stocksToBuy = new ArrayList<Stock>();
    ArrayList<Stock> stocksToSell = new ArrayList<Stock>();

    public User(String username) {
        this.username = username;
    }
}
