import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import DisplayUtils.DisplayUtils;
import Logs.ActionLogs;
import Stocks.Stock;
import Stocks.StockDay;
import Stocks.StockDaysAll;
import User.Trader;
import User.Bot;
import User.User;
import User.Users;

public class StockGuru {
    static ActionLogs actionLogs = new ActionLogs();
    static StockDaysAll stockDaysAll = new StockDaysAll();
    static HashMap<Integer, Object[]> stocksMap = new HashMap<>(); // stockID, [abbr, name]
    static HashMap<String, Integer> abbrMap = new HashMap<>();     // abbr, stockID
    static Users users = new Users();
    static Scanner scan = new Scanner(System.in);

    final static int days = 10;
    static int day = 0;
    static StockDay today = stockDaysAll.getStockDay(day);

    private static final String NAMINGS_FILE = "lib/Namings.csv";
    private static final String STOCKS_DIR = "lib/Stocks/";


    /**
     * Main method to start the StockGuru application.
     * It initializes the application, prompts for user input, and creates the User.
     * Starts the trading simulation and displays the end information.
     * If the user chooses to restart, it resets the application and starts again.
     */
    public static void main( String[] args) {
        DisplayUtils.welcomeMessage();
        initial();

        DisplayUtils.askUserName();
        String username = getInput(0, null);
        DisplayUtils.greetUser(username);
        Trader trader = users.createTrader(username);
        ActionLogs.addUserLog(trader.getID(), day, "Trader created");

        DisplayUtils.askUserType();
        String userType = getInput(0, List.of("Trader", "Bot"));

        if (userType.equals("Bot")) {
            DisplayUtils.askBotName();
            username = getInput(0, null);
            DisplayUtils.askMaxSharesPercentage();
            int maxSharesPercentage = Integer.parseInt(getInput(1, null));
            Bot bot = users.createBot(username, maxSharesPercentage);
            ActionLogs.addUserLog(bot.getID(), day, "Bot created");
            start(bot);
            endInfo(bot);
        } else {
            start(trader);
            endInfo(trader);
        }

        DisplayUtils.askRestart();
        String restart = getInput(0, List.of("Yes", "No"));
        if (restart.equals("Yes")) {
            reset();
            main(args);
        } else {
            DisplayUtils.goodbyeMessage();
        }
    }

    /**
     * Resets the application state by reinitializing variables and clearing maps.
     * This method is called when the user chooses to restart the application.
     */
    public static void reset() {
        actionLogs = new ActionLogs();
        stockDaysAll = new StockDaysAll();
        stocksMap.clear();
        abbrMap.clear();
        users = new Users();
        day = 0;
        today = stockDaysAll.getStockDay(day);
    }

    /**
     * Initializes the application by reading stock data from files and populating maps.
     * It reads the namings file to get stock abbreviations and names, and stores them in maps.
     */
    public static void initial() {

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
            System.err.println("Error reading the namings file: " + e.getMessage());
        }
    }

    /**
     * Starts the trading simulation for the specified user.
     * It iterates through the days, updates stock data, and allows the user to choose stocks to trade.
     * @param user The user object representing the current user.
     */
    public static void start(User user ) {
        while (day <= days) {
            DisplayUtils.displayDay(day);
            updateStockData();
            beginOfDay(user);

            if (user instanceof Bot) {
                Bot bot = (Bot) user;
                bot.autoTrade(stockDaysAll, actionLogs, day, stocksMap);
            } else {
                user.evaluateSellOptions(today, actionLogs, stocksMap, day);
                chooseStock(user);
            }
            day++;
        }
        System.out.println("There is no more data available!");
    }

    /**
     * Displays end information after the trading simulation is complete.
     * It shows the best performing stock and its performance metrics.
     * @param user The user object representing the current user.
     */
    public static void endInfo(User user) {
        DisplayUtils.endOfGameMessage();
        Stock bestStock = getBestPerformingStock();

        if (bestStock != null) {
            double firstDayPrice = stockDaysAll.getStockDay(0).getStock(bestStock.getID()).getCourse();
            double currentPrice = bestStock.getCourse();
            double absoluteIncrease = currentPrice - firstDayPrice;
            double percentageIncrease = (absoluteIncrease / firstDayPrice) * 100;
            String stockName = stocksMap.get(bestStock.getID())[1].toString();

            DisplayUtils.displayBestStock(stockName, absoluteIncrease, percentageIncrease);
        }
    }

    /**
     * Gets the best performing stock based on the percentage increase from the first day to the current day.
     * @return The best performing stock object.
     */
    public static Stock getBestPerformingStock() {
        return today.getStocks().stream()
                .max(Comparator.comparingDouble(stock -> {
                    double firstDayPrice = stockDaysAll.getStockDay(0).getStock(stock.getID()).getCourse();
                    return stock.getCourse() / firstDayPrice;
                }))
                .orElse(null);
    }

    /**
     * Displays the beginning of the day data for the user.
     * It shows the stock details, including course, volume, and performance metrics.
     * @param user The user object representing the current user.
     */
    public static void beginOfDay(User user) {
        System.out.println("New Day Data is being loaded...");
        System.out.println("Data loaded!");
        DisplayUtils.displayPortfolioHeader(user, day, today);

        StockDay stocksYesterday = stockDaysAll.getStockDay(day - 1);
        for (Stock stock : today.getStocks()) {
            int stockID = stock.getID();
            int userStockAmount = user.getStockPortfolio().getOrDefault(stockID, 0);
            String stockAbbr = stocksMap.get(stockID)[0].toString();
            String stockName = stocksMap.get(stockID)[1].toString();
            double stockCourse = stock.getCourse();
            long stockVolume = stock.getVolume();
        
            // Calculations
            double avgEntryPrice = actionLogs.getAvgEntryPrice(user.getID(), stockID, day);
            double profitAmount = (stockCourse - avgEntryPrice) * userStockAmount;
        
            double perStockChange = 0;
            if (stocksYesterday != null) {
                Stock stockY = stocksYesterday.getStock(stockID);
                perStockChange = stockY == null ? 0 : ((stockCourse - stockY.getCourse()) / stockY.getCourse()) * 100;
            }
        
            double profitPercentage = userStockAmount == 0 || avgEntryPrice == 0 ? 0 : ((stockCourse - avgEntryPrice) / avgEntryPrice) * 100;
            DisplayUtils.displayStockDetails(stockID, stockAbbr, stockName, stockCourse, (int) stockVolume, perStockChange, userStockAmount, avgEntryPrice, profitAmount, profitPercentage);
        }
        DisplayUtils.displaySeparator();
    }

    /**
     * Updates the stock data for the current day by reading from CSV files.
     * It populates the stock data into the StockDay object and adds it to the StockDaysAll object.
     */
    public static void updateStockData() {
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
                    System.err.println("Error reading stock file " + stockFile + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error listing stock files: " + e.getMessage());
        }
        stockDaysAll.addStockDay(stockDay);
        today = stockDay;
    }

    /**
     * Displays the user action options at the beginning of the day.
     * It allows the user to choose between buying stocks, showing available stocks, selling stock options, or ending the day.
     * @param user The user object representing the current user.
     */
    public static void chooseStock(User user) {
        ArrayList<Object[]> validInputs = new ArrayList<>();
        DisplayUtils.askUserAction();

        if (user.getMoney() > 0) {
            validInputs.add(new Object[]{"Buy Stock", (Runnable) () -> buyStock(user)});
        }
        validInputs.add(new Object[]{"Show all Stocks I could buy", (Runnable) () -> today.userCanBuy(user.getMoney(), stocksMap)});
        if (!user.getStockPortfolio().isEmpty()) {
            validInputs.add(new Object[]{"Sell Stock Options", (Runnable) () -> sellStockOptions(user)});
        }
        validInputs.add(new Object[]{"End Day", (Runnable) DisplayUtils::endingDayMessage});

        List<String> validChoices = new ArrayList<>();
        for (int i = 0; i < validInputs.size(); i++) {
            DisplayUtils.displayMenuOption(i + 1, validInputs.get(i)[0].toString());
            validChoices.add(String.valueOf(i + 1));
        }

        int choice = Integer.parseInt(getInput(1, validChoices));

        Runnable action = (Runnable) validInputs.get(choice - 1)[1];
        action.run();
        if (choice != validInputs.size()) {
            chooseStock(user);
        }
    }

    /**
     * Handles the Buying of a Stock by the User
     * Asks the user which stock he wants to buy
     * @param user The user object representing the current user.
     * @throws NumberFormatException if the input is not a valid integer.
     */
    public static void buyStock(User user) {
        System.out.println("Which Stock would you like to buy? (Enter ID)");
        int stockID = Integer.parseInt(getInput(1, stocksMap.keySet().stream().map(String::valueOf).collect(Collectors.toList())));
        Stock stockToBuy = today.getStock(stockID);
        double stockCourse = stockToBuy.getCourse();
        double userMoney = user.getMoney();
        int maxAmount = (int) (userMoney / stockCourse);
        String stockName = stocksMap.get(stockID)[1].toString();

        if (maxAmount == 0) {
            DisplayUtils.notEnoughMoneyToBuy(stockName, stockID);
            return;
        }

        DisplayUtils.displayBuyOptions(userMoney, maxAmount, maxAmount * stockCourse);
        int stockAmount = getInputStockBuy(user, stockID, maxAmount);

        if (stockAmount == 0) {
            return;
        }

        user.buyStock(stockToBuy, stockName, stockAmount, actionLogs, day);
    }

    /**
     * Displays the options for selling stocks to the user.
     * It allows the user to choose between selling a specific stock or selling all stocks.
     * @param user The user object representing the current user.
     */
    public static void sellStockOptions(User user) {
        DisplayUtils.displaySellOptions();

        int choice = Integer.parseInt(getInput(1, List.of("1", "2")));
        switch (choice) {
            case 1 -> sellStock(user);
            case 2 -> user.sellAllStocks(actionLogs, day, today, stocksMap);
        }
    }

    /**
     * Handles the Selling of a Stock by the User
     * Asks the user which stock he wants to sell and how much of it.
     * @param user The user object representing the current user.
     */
    public static void sellStock(User user) {
        DisplayUtils.askStockToSell();

        // Get the list of stock IDs the user owns
        List<String> ownedStockIDs = user.getStockPortfolio().keySet().stream().map(String::valueOf).collect(Collectors.toList());

        // Validate input to ensure the user can only select stocks they own
        int stockID = Integer.parseInt(getInput(1, ownedStockIDs));

        DisplayUtils.askStockAmountToSell();
        int stockAmount = getInputStockSell(user, stockID);

        String stockName = stocksMap.get(stockID)[1].toString();
        if (stockAmount == 0) {
            return;
        }

        Stock stockToSell = today.getStock(stockID);
        user.sellStock(stockToSell, stockName, stockAmount, actionLogs, day);
    }

    /**
     * Method to get user input for stock selling amount.
     * It validates the input and ensures the amount is not greater than the user's stock amount.
     * If the input is invalid, it displays an error message and prompts again.
     * @param user    The user object representing the current user.
     * @param stockID The ID of the stock to be sold.
     * @return The validated stock amount to be sold.
     * @throws NumberFormatException if the input is not a valid integer. (Shoudtn't happen or the getInput method doesnt work correctly)
     */
    public static int getInputStockSell(User user, int stockID) {
        int stockAmount = user.getStockPortfolio().getOrDefault(stockID, 0);
        while (true) {
            int sellAmount = Integer.parseInt(getInput(1, null));
            if (sellAmount > stockAmount) {
                DisplayUtils.invalidSellAmount();
            } else if (sellAmount < 0) {
                DisplayUtils.negativeStockAmount();
            } else if (sellAmount == 0) {
                DisplayUtils.noStocksSold();
                return 0;
            } else {
                return sellAmount;
            }
        }
    }

    /**
     * Gets user input for the stock buying amount.
     * Validates the input to ensure it does not exceed the maximum amount the user can afford.
     * Displays error messages for invalid inputs and prompts again.
     *
     * @param user      The user object representing the current user.
     * @param stockID   The ID of the stock to be bought.
     * @param maxAmount The maximum number of stocks the user can afford.
     * @return The validated stock amount to be bought.
     * @throws NumberFormatException if the input is not a valid integer. (Shoudtn't happen or the getInput method doesnt work correctly)
     */
    public static int getInputStockBuy(User user, int stockID, int maxAmount) {
        String stockName = stocksMap.get(stockID)[1].toString();

        while (true) {
            int buyAmount = Integer.parseInt(getInput(1, null));
            if (buyAmount > maxAmount) {
                DisplayUtils.notEnoughMoneyToBuy(stockName, stockID);
            } else if (buyAmount < 0) {
                DisplayUtils.negativeStockAmount();
            } else if (buyAmount == 0) {
                DisplayUtils.noStocksBought();
                return 0;
            } else {
                return buyAmount;
            }
        }
    }

    /**
     * Gets user input as a string and validates it based on the specified type.
     * Re-prompts the user if the input is invalid.
     *
     * @param typeGet     The type of input to validate (0: letters, 1: integers, 2: decimals).
     * @param validInputs A list of valid inputs (optional).
     * @return The validated user input as a string.
     */
    public static String getInput(int typeGet, List<String> validInputs) {
        while (true) {
            String input = scan.nextLine();
            input = input.trim(); // Remove leading and trailing whitespace
            input = input.replaceAll("\\s+", " "); // Replace multiple spaces with a single space
            if (isValidInput(input, typeGet, validInputs)) {
                return input;
            } else {
                DisplayUtils.displayInvalidInputMessage();
            }
        }
        
    }

    /**
     * Validates user input based on the specified type and optional valid inputs.
     *
     * @param input       The user input to validate.
     * @param typeGet     The type of input to validate (0: letters, 1: integers, 2: decimals).
     * @param validInputs A list of valid inputs (optional).
     * @return True if the input is valid, false otherwise.
     */
    private static boolean isValidInput(String input, int typeGet, List<String> validInputs) {
        switch (typeGet) {
            case 0:
                return input.matches("^[a-zA-Z]+$") && 
                       (validInputs == null || validInputs.stream().map(String::toLowerCase).anyMatch(input.toLowerCase()::equals));
            case 1:
                return input.matches("^[0-9]+$") && 
                       (validInputs == null || validInputs.stream().map(String::toLowerCase).anyMatch(input.toLowerCase()::equals));
            case 2:
                return input.matches("^[0-9]*\\.?[0-9]+$") && 
                       (validInputs == null || validInputs.stream().map(String::toLowerCase).anyMatch(input.toLowerCase()::equals));
            default:
                return false;
        }
    }
}