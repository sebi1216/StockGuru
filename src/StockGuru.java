import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import Logs.ActionLogs;
import Stocks.Stock;
import Stocks.StockDay;
import Stocks.StockDaysAll;
import User.Trader;
import User.Bot;
import User.User;
import User.Users;

public class StockGuru {
    static ActionLogs actionLogs = new ActionLogs();        // Logs of all actions ==> Buy, Sell
    static StockDaysAll stockDaysAll = new StockDaysAll();  // All Stock Data of all Days
    static Users users = new Users();                       // All Users
    static HashMap<Integer, Object[]> stocksMap = new HashMap<>(); // ID, [StockAbbr, StockName]
    static HashMap<String, Integer> abbrMap = new HashMap<>(); // Reverse lookup for stock abbreviation
    

    final static int days = 10;
    static int day = 0;
    static StockDay today = stockDaysAll.getStockDay(day);

    // File paths
    private static final String NAMINGS_FILE = "lib/Namings.csv";
    private static final String STOCKS_DIR = "lib/Stocks/";

    public static void main(String[] args) {
        System.out.println("Welcome to StockGuru!");
        initial();
        System.out.println("What is your name?");
        String username = getInput(0, null);
        System.out.println("Hello " + username + "!");
        User user = new Trader(0, username);
        System.out.println("Do you want to be a Trader or a Bot? (Trader/Bot)");
        String userType = getInput(0, List.of("Trader", "Bot"));

        // different user types (Trader/Bot)
        if (userType.equals("Bot")) {
            System.out.println("What is your Bot's name?");
            username = getInput(0, null);
            System.out.println("Enter the maximum percentage of shares per stock for your Bot:");
            int max_shares_per_stock_percentage = Integer.parseInt(getInput(1, null));
            Bot bot = new Bot(1, username, max_shares_per_stock_percentage);
            start(bot);
        } else {
            start(user);
        }

        System.out.println("Do yo want to restart StockGuru? (Yes/No)");
        String restart = getInput(0, List.of("Yes", "No"));
        if (restart.equals("Yes")) {
            reset(); 
            main(args);
        } else {
            System.out.println("Goodbye! Thanks for using StockGuru!");
        }
    }

    public static void reset() {
        actionLogs = new ActionLogs();
        stockDaysAll = new StockDaysAll();
        users = new Users();
        stocksMap = new HashMap<>();
        abbrMap = new HashMap<>();
        day = 0;
        today = stockDaysAll.getStockDay(day);
    }

    public static void initial() {
        // Load Names into HashMap
        try (BufferedReader br = new BufferedReader(new FileReader(NAMINGS_FILE))) {
            String line;
            int ID = 0;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 2) {
                    stocksMap.put(ID, new Object[]{parts[0], parts[1]});
                    abbrMap.put(parts[0], ID);
                    ID++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void start(User user) {
        while (day <= days) {
            System.out.println("Day: " + day);
            updateStockData(user);
            beginOfDay(user);
            if (user instanceof Bot) {
                Bot bot = (Bot) user;
                bot.autoTrade(stockDaysAll, actionLogs, day);
            } else {
                chooseStock(user);
            }
            day++;
        }
        System.out.println("There is no more data available!");
        // displayFinalInfo(user);
    }

    // Begin of Day Message will be displayed once
    public static void beginOfDay(User user) {
        System.out.println("New Day Data is being loaded...");
        System.out.println("Data loaded!");
        System.out.printf("%nYour stock portfolio:%n");
        System.out.printf("%-77s | %-65s %n", "Available Stocks    |    Day: " + day, "Portfolio of " + user.getUsername() + " | Balance: " + user.getMoney() + "$" + " | Total Value: " + String.format("%.2f", user.getInvestedValue(today)) + "$");
        System.out.printf("%-5s %-5s %-30s %-9s %-13s %-10s | %-7s %-10s %-11s %-7s%n", "ID", "Abbr.", "Name", "Course", "Volume", "Diff"     , "Amount", "Value", "Avg. Entry", "Profit");
        System.out.printf("%-5s %-5s %-30s %-9s %-13s %-10s | %-7s %-10s %-11s %-7s%n", ""  , ""     , ""    , ""      , "Total" , "Yesterday", ""      , ""     , ""          , "Total");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------");

        StockDay stocksYesterday = stockDaysAll.getStockDay(day - 1);
        for (Stock stock : today.getStocks()) {
            int stockID = stock.getID();
            int userStockAmount = user.getStockPortfolio().getOrDefault(stockID, 0);
        
            // Get stock data
            String stockAbbr = stocksMap.get(stockID)[0].toString();
            String stockName = stocksMap.get(stockID)[1].toString();
            double stockCourse = stock.getCourse();
            String stockCourseString = String.format("%.2f$", stockCourse);
            long stockVolume = stock.getVolume();
        
            // Handle case where stocksYesterday is null
            double perStockChange = 0;
            if (stocksYesterday != null) {
                Stock stockY = stocksYesterday.getStock(stockID);
                perStockChange = stockY == null ? 0 : ((stockCourse - stockY.getCourse()) / stockY.getCourse()) * 100;
            }
            String perStockChangeStr = perStockChange >= 0 ? "\u001B[32m+" + String.format("%.2f%%", perStockChange) + "\u001B[0m" : "\u001B[31m" + String.format("%.2f%%", perStockChange) + "\u001B[0m";
        
            // Get user stock data and calculate
            String userStockAmountStr = userStockAmount == 0 ? "" : String.valueOf(userStockAmount);
            String userStockValueStr = userStockAmount == 0 ? "" : String.format("%.2f$", userStockAmount * stockCourse);
            double avgEntryPriceToday = actionLogs.getAvgEntryPrice(user.getID(), stockID, day);            
            String userStockAvgEntryPrice = userStockAmount == 0 ? "" : String.format("%.2f$", avgEntryPriceToday);
            double profitAmount = (stockCourse - avgEntryPriceToday) * userStockAmount;
            String userProfitAmount = userStockAmount == 0 ? "" : 
                (profitAmount >= 0 ? "\u001B[32m+" + String.format("%.2f$", profitAmount) + "\u001B[0m" : "\u001B[31m" + String.format("%.2f$", profitAmount) + "\u001B[0m");
            
            String userProfitAmountPer = userStockAmount == 0 || avgEntryPriceToday == 0 ? "" : 
                (((stockCourse - avgEntryPriceToday) / avgEntryPriceToday) * 100 >= 0 ? 
                    "\u001B[32m" + String.format("%.2f%%", ((stockCourse - avgEntryPriceToday) / avgEntryPriceToday) * 100) + "\u001B[0m" : 
                    "\u001B[31m" + String.format("%.2f%%", ((stockCourse - avgEntryPriceToday) / avgEntryPriceToday) * 100) + "\u001B[0m");
            
            System.out.printf("%-5s %-5s %-30s %-10s %-13d %-18s | %-7s %-10s %-11s %-7s %-7s%n",
                stockID, stockAbbr, stockName, stockCourseString, stockVolume, perStockChangeStr, userStockAmountStr, userStockValueStr, userStockAvgEntryPrice, userProfitAmount, userProfitAmountPer);
        }
        System.out.println("--------------------------------------------------------------------------------------------------------------------------");
    }

    public static void updateStockData(User user) {
        StockDay stockDay = new StockDay(day);

        try {
            List<String> stockFiles = Files.list(Paths.get(STOCKS_DIR))
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toList());

            for (String stockFile : stockFiles) {
                try (BufferedReader br = new BufferedReader(new FileReader(STOCKS_DIR + stockFile))) {
                    String line;
                    br.readLine(); // Skip header
                    for (int i = 0; i <= day; i++) {
                        line = br.readLine();
                        if (line == null) break;
                        if (i == day) {
                            String[] data = line.split(";");
                            String abbr = stockFile.replace(".csv", "");
                            double course = Double.parseDouble(data[1].replace(",", "."));
                            long volume = Long.parseLong(data[2].replace(".", "").replace(",", ""));
                            int stockID = abbrMap.getOrDefault(abbr, -1);
                            if (stockID != -1) {
                                Stock stock = new Stock(stockID, course, volume);
                                stockDay.addStock(stock);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        stockDaysAll.addStockDay(stockDay);
        today = stockDay;
    }

    public static void chooseStock(User user) {
        ArrayList<Object[]> validInputs = new ArrayList<>();
        System.out.println("What would you like to do?");
        
        // Dynamically populate validInputs based on conditions
        if (user.getMoney() > 0) {
            validInputs.add(new Object[]{"Buy Stock", (Runnable) () -> buyStock(user)});
        }
        validInputs.add(new Object[]{"Show all Stocks I could buy", (Runnable) () -> today.userCanBuy(user.getMoney(), stocksMap)});
        if (!user.getStockPortfolio().isEmpty()) {
            validInputs.add(new Object[]{"Sell Stock Options", (Runnable) () -> sellStockOptions(user)});
        }
        validInputs.add(new Object[]{"End Day", (Runnable) () -> System.out.println("Ending the day...")});
    
        // Display menu options dynamically
        List<String> validChoices = new ArrayList<>();
        for (int i = 0; i < validInputs.size(); i++) {
            System.out.println((i + 1) + ". " + validInputs.get(i)[0]);
            validChoices.add(String.valueOf(i + 1));
        }

        int choice = Integer.parseInt(getInput(1, validChoices));
    
        // Execute the corresponding action
        Runnable action = (Runnable) validInputs.get(choice - 1)[1];
        action.run();
        if (choice != validInputs.size()) {
            chooseStock(user);
        }
    }

    public static void sellStockOptions(User user) {
        user.evaluateSellOptions(today, actionLogs, stocksMap, day);
        System.out.println("There are Multiple Options select what you want to do:");
        System.out.println("1. Sell Specific Stock");
        System.out.println("2. Sell All Stocks");

        int choice = Integer.parseInt(getInput(1, List.of("1", "2", "3","4")));
        switch (choice) {
            case 1 -> sellStock(user);
            case 2 -> user.sellAllStocks(actionLogs, day, today);
        }
    }

    public static void buyStock(User user) {
        System.out.println("Which Stock would you like to buy? (Enter ID)");
        int stockID = Integer.parseInt(getInput(1, null));
        double stockCourse = today.getStock(stockID).getCourse();
        double userMoney = user.getMoney();
        String name = stocksMap.get(stockID)[1].toString();
        int maxAmount = (int) (userMoney / stockCourse);
    
        if (maxAmount == 0) {
            System.out.println("You don't have enough money to buy even 1 stock of " + name + "!");
            return;
        }
    
        System.out.println("You have " + userMoney + "$ available and can buy a Maximum amount of " + maxAmount + " stocks for a Total of " + maxAmount * stockCourse + "$.");
        System.out.println("How many stocks would you like to buy? (0 to cancel)");
        int stockAmount = getInputStockBuy(user, stockID, maxAmount);
    
        if (stockAmount == 0) {
            return;
        }
    
        user.buyStock(stockID, name, stockAmount, stockCourse, actionLogs, day);
    }

    public static void sellStock(User user) {
        System.out.println("Which Stock would you like to sell?");
        int stockID = Integer.parseInt(getInput(1, null));
        System.out.println("How many stocks would you like to sell? (0 to cancel)");
        int stockAmount = getInputStockSell(user, stockID);
        if (stockAmount == 0) {
            return;
        }
        double stockCourse = stockDaysAll.getStockDay(day).getStock(stockID).getCourse();
        user.sellStock(stockID, stockAmount, stockCourse, actionLogs, day);
    }

    public static int getInputStockSell(User user, int stockID) {
        int stockAmount = user.getStockPortfolio().getOrDefault(stockID, 0);
        while (true) {
            int sellAmount = Integer.parseInt(getInput(1, null));
            if (sellAmount > stockAmount) {
                System.out.println("You don't have that amount of stock in your portfolio!");
            } else if ( sellAmount < 0) {
                System.out.println("The amount of stocks must be positive!");
            } else if (sellAmount == 0) {
                System.out.println("We will not sell any stocks!");
                return 0;
            }else  {
                return sellAmount;
            }
        }
    }

    public static int getInputStockBuy(User user, int stockID, int maxAmount) {
        while (true) {
            int buyAmount = Integer.parseInt(getInput(1, null));
            if (buyAmount > maxAmount) {
                System.out.println("You don't have enough money to buy that amount of stocks!");
            } else if (buyAmount < 0){ 
                System.out.println("The amount of stocks must be positive!");
            } else if (buyAmount == 0){
                System.out.println("We will not buy any stocks!");
                return 0;
            } else {
                return buyAmount;
            }
        }
    }

    public static String getInput(int typeGet, List<String> validInputs) {
        Scanner scan = new Scanner(System.in);
        while (true) {
            String input = scan.nextLine();
            if (isValidInput(input, typeGet, validInputs)) {
                return input;
            } else {
                System.out.println("Please enter a valid input!");
            }
        }
    }

    private static boolean isValidInput(String input, int typeGet, List<String> validInputs) {
        switch (typeGet) {
            case 0:
                return input.matches("^[a-zA-Z]+$") && (validInputs == null || validInputs.contains(input));
            case 1:
                return input.matches("^[0-9]+$") && (validInputs == null || validInputs.contains(input));
            case 2:
                return input.matches("^[0-9]*\\.?[0-9]+$") && (validInputs == null || validInputs.contains(input));
            default:
                return false;
        }
    }
}