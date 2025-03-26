import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class StockGuru {
    static ActionLogs actionLogs = new ActionLogs();        // Logs of all actions ==> Buy, Sell
    static StockDaysAll stockDaysAll = new StockDaysAll();  // All Stock Data of all Days
    static Users users = new Users();                       // All Users
    static HashMap<Integer, Object[]> stocksMap = new HashMap<>(); // ID, [StockAbbr, StockName]
    static HashMap<String, Integer> abbrMap = new HashMap<>(); // Reverse lookup for stock abbreviation

    final static int days = 10;
    static int day = 0;

    // File paths
    private static final String NAMINGS_FILE = "lib/Namings.csv";
    private static final String STOCKS_DIR = "lib/Stocks/";

    public static void main(String[] args) {
        System.out.println("Welcome to StockGuru!");
        initial();
        System.out.println("What is your name?");
        String username = getInput(0, null);
        System.out.println("Hello " + username + "!");
        User user = new User(0, username);
        days(user);
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

    public static void days(User user) {
        while (day <= days) {
            beginOfDay(user);
            chooseStock(user);
            day++;
        }
        System.out.println("There is no more data available!");
        // displayFinalInfo(user);
    }

    public static void beginOfDay(User user) {
        System.out.println("Data is being loaded...");
        updateStockData(user);
        System.out.println("Data loaded!");
        System.out.printf("%nYour stock portfolio:%n");
        System.out.printf("%-67s | %-65s %n", "Available Stocks    |    Day: " + day, "Portfolio of " + user.username + "  |  Balance: " + user.money + "$");
        System.out.printf("%-5s %-5s %-30s %-13s %-10s | %-7s %-7s %-7s%n", "ID", "Abbr.", "Name", "Course", "Volume", "Amount", "Value", "Avg. Entry");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------");
    
        StockDay stockDay = stockDaysAll.getStockDay(day);
        for (Stock stock : stockDay.stocks) {
            int stockID = stock.ID;
            int userStockAmount = user.stockPortfolio.getOrDefault(stockID, 0);
    
            String stockAbbr = stocksMap.get(stockID)[0].toString();
            String stockName = stocksMap.get(stockID)[1].toString();
            double stockCourse = stock.course;
            long stockVolume = stock.volume;
    
            // If the user doesn't own the stock, display empty values
            String userStockAmountStr = userStockAmount == 0 ? "" : String.valueOf(userStockAmount);
            String userStockValueStr = userStockAmount == 0 ? "" : String.format("%.2f", userStockAmount * stockCourse);
            double avgEntryPrice = actionLogs.getAvgEntryPrice(user.ID, stockID);
            String userStockAvgEntryPrice = userStockAmount == 0 ? "" : String.format("%.2f", avgEntryPrice);
            String userStockDiffPercentStr = userStockAmount == 0 || avgEntryPrice == 0 ? "" : String.format("%.2f%%", ((stockCourse - avgEntryPrice) / avgEntryPrice) * 100);
            
            System.out.printf("%-5s %-5s %-30s %-13.2f %-10d | %-7s %-7s %-7s %-7s%n",
                stockID, stockAbbr, stockName, stockCourse, stockVolume,
                userStockAmountStr, userStockValueStr, userStockAvgEntryPrice, userStockDiffPercentStr);
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
    }

    public static void chooseStock(User user) {
        System.out.println("What would you like to do?");
        System.out.println("1. Buy Stock");
        System.out.println("2. Sell Stock");
        System.out.println("3. End Day");
        int choice = Integer.parseInt(getInput(1, List.of("1", "2", "3")));
        switch (choice) {
            case 1 -> buyStock(user);
            case 2 -> sellStock(user);
        }
    }

    public static void buyStock(User user) {
        System.out.println("Which Stock would you like to buy? (Enter ID)");
        int stockID = Integer.parseInt(getInput(1, null));
        System.out.println("How many stocks would you like to buy?");
        int stockAmount = getInputStockBuy(user, stockID);
        double stockCourse = stockDaysAll.getStockDay(day).getStock(stockID).course;
        user.buyStock(stockID, stockAmount, stockCourse, actionLogs, day);
    }

    public static void sellStock(User user) {
        System.out.println("Which Stock would you like to sell?");
        int stockID = Integer.parseInt(getInput(1, null));
        System.out.println("How many stocks would you like to sell?");
        int stockAmount = getInputStockSell(user, stockID);
        double stockCourse = stockDaysAll.getStockDay(day).getStock(stockID).course;
        user.sellStock(stockID, stockAmount, stockCourse, actionLogs, day);
    }

    public static int getInputStockSell(User user, int stockID) {
        int stockAmount = user.stockPortfolio.getOrDefault(stockID, 0);
        while (true) {
            int sellAmount = Integer.parseInt(getInput(1, null));
            if (sellAmount > stockAmount) {
                System.out.println("You don't have that amount of stock in your portfolio!");
            } else {
                return sellAmount;
            }
        }
    }

    public static int getInputStockBuy(User user, int stockID) {
        while (true) {
            int buyAmount = Integer.parseInt(getInput(1, null));
            double stockCourse = stockDaysAll.getStockDay(day).getStock(stockID).course;
            if (buyAmount * stockCourse > user.money) {
                System.out.println("You don't have enough money to buy that amount of stocks!");
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