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
    static HashMap<String, Integer> abbrMap = new HashMap<>(); // abbr, stockID
    static Users users = new Users();
    static Scanner scan = new Scanner(System.in);

    final static int days = 10;
    static int day = 0;
    static StockDay today = stockDaysAll.getStockDay(day);

    private static final String NAMINGS_FILE = "lib/Namings.csv";
    private static final String STOCKS_DIR = "lib/Stocks/";

    public static void main( String[] args) {
        DisplayUtils.welcomeMessage();
        initial();

        DisplayUtils.askUserName();
        String username = getInput(0, null);
        DisplayUtils.greetUser(username);
        User user = new Trader(0, username);

        DisplayUtils.askUserType();
        String userType = getInput(0, List.of("Trader", "Bot"));

        if (userType.equals("Bot")) {
            DisplayUtils.askBotName();
            username = getInput(0, null);
            DisplayUtils.askMaxSharesPercentage();
            int maxSharesPercentage = Integer.parseInt(getInput(1, null));
            Bot bot = new Bot(1, username, maxSharesPercentage);
            start(bot);
            endInfo(bot);
        } else {
            start(user);
            endInfo(user);
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

    public static void reset() {
        actionLogs = new ActionLogs();
        stockDaysAll = new StockDaysAll();
        stocksMap.clear();
        abbrMap.clear();
        users = new Users();
        day = 0;
        today = stockDaysAll.getStockDay(day);
    }

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

    public static void start(User user) {
        while (day <= days) {
            DisplayUtils.displayDay(day);
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
    }

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

    public static Stock getBestPerformingStock() {
        return today.getStocks().stream()
                .max(Comparator.comparingDouble(stock -> {
                    double firstDayPrice = stockDaysAll.getStockDay(0).getStock(stock.getID()).getCourse();
                    return stock.getCourse() / firstDayPrice;
                }))
                .orElse(null);
    }

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
            double avgEntryPriceToday = actionLogs.getAvgEntryPrice(user.getID(), stockID, day);
            double profitAmount = (stockCourse - avgEntryPriceToday) * userStockAmount;
        
            double perStockChange = 0;
            if (stocksYesterday != null) {
                Stock stockY = stocksYesterday.getStock(stockID);
                perStockChange = stockY == null ? 0 : ((stockCourse - stockY.getCourse()) / stockY.getCourse()) * 100;
            }
        
            double profitPercentage = userStockAmount == 0 || avgEntryPriceToday == 0 ? 0 : ((stockCourse - avgEntryPriceToday) / avgEntryPriceToday) * 100;
            DisplayUtils.displayStockDetails(stockID, stockAbbr, stockName, stockCourse, (int) stockVolume, perStockChange, userStockAmount, avgEntryPriceToday, profitAmount, profitPercentage);
        }
        DisplayUtils.displaySeparator();
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
                    System.err.println("Error reading stock file " + stockFile + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error listing stock files: " + e.getMessage());
        }
        stockDaysAll.addStockDay(stockDay);
        today = stockDay;
    }

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

    public static void buyStock(User user) {
        System.out.println("Which Stock would you like to buy? (Enter ID)");
        int stockID = Integer.parseInt(getInput(1, null));
        double stockCourse = today.getStock(stockID).getCourse();
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

        user.buyStock(stockID, stockName, stockAmount, stockCourse, actionLogs, day);
    }

    public static void sellStockOptions(User user) {
        DisplayUtils.displaySellOptions();

        int choice = Integer.parseInt(getInput(1, List.of("1", "2")));
        switch (choice) {
            case 1 -> sellStock(user);
            case 2 -> user.sellAllStocks(actionLogs, day, today);
        }
    }

    public static void sellStock(User user) {
        DisplayUtils.askStockToSell();
        int stockID = Integer.parseInt(getInput(1, null));
        DisplayUtils.askStockAmountToSell();
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

    public static String getInput(int typeGet, List<String> validInputs) {
        while (true) {
            String input = scan.nextLine();
            if (isValidInput(input, typeGet, validInputs)) {
                return input;
            } else {
                DisplayUtils.displayInvalidInputMessage();
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