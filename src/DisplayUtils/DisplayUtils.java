package DisplayUtils;

import Stocks.StockDay;
import User.User;

public class DisplayUtils {
    private static OutputHandler outputHandler = new ConsoleOutputHandler();

    /**
     * Sets the output handler to be used for displaying messages.
     * @param handler
     */
    public static void setOutputHandler(OutputHandler handler) {
        outputHandler = handler;
    }

    /**
     * Displays a welcome message to the user.
     */
    public static void welcomeMessage() {
        outputHandler.println("Welcome to StockGuru!");
    }

    /**
     * Asks the user for their name.
     */
    public static void askUserName() {
        outputHandler.println("What is your name?");
    }

    /**
     * Displays a greeting message to the user.
     * @param username
     */
    public static void greetUser(String username) {
        outputHandler.println("Hello " + username + "!");
    }

    /**
     * Asks the user if they want to be a trader or a bot.
     */
    public static void askUserType() {
        outputHandler.println("Do you want to be a Trader or a Bot? (Trader/Bot)");
    }

    /**
     * Asks the user for the Bot Name.
     */
    public static void askBotName() {
        outputHandler.println("What is your Bot's name?");
    }

    /**
     * Asks the user for the maximum percentage of shares per stock for their Bot.
     */
    public static void askMaxSharesPercentage() {
        outputHandler.println("Enter the maximum percentage of shares per stock for your Bot:");
    }

    /**
     * End of game message.
     */
    public static void endOfGameMessage() {
        outputHandler.println("END OF GAME!");
    }

    /**
     * Displays the best performing stock.
     * @param stockName
     * @param absoluteIncrease
     * @param percentageIncrease
     */
    public static void displayBestStock(String stockName, double absoluteIncrease, double percentageIncrease) {
        outputHandler.printf("The best performing stock is: %s with a total increase of Value by: %.2f$ (%.2f%%)!%n", stockName, absoluteIncrease, percentageIncrease);
    }

    /**
     * Displays total profit.
     * @param totalProfit
     */
    public static void displayTotalProfit(double totalProfit) {
        outputHandler.printf("The Total Profit is: %.2f$!%n", totalProfit);
    }

    /**
     * Displays the portfolio header which is displayed at the beginning of the day
     * @param user
     * @param day
     * @param today
     */
    public static void displayStocksHeader(User user, int day, StockDay today) {
        displaySeparator();
        outputHandler.println("Todays Stock Market:");
        outputHandler.printf("%-77s | %-65s %n", "Available Stocks    |    Day: " + day, "Portfolio of " + user.getUsername() + " | Balance: " + user.getMoney() + "$" + " | Total Value: " + String.format("%.2f", user.getInvestedValue(today)) + "$");
        outputHandler.printf("%-5s %-5s %-30s %-9s %-13s %-10s | %-7s %-10s %-11s %-7s%n", "ID", "Abbr.", "Name", "Course", "Volume", "Diff"     , "Amount", "Value", "Avg. Entry", "Profit");
        outputHandler.printf("%-5s %-5s %-30s %-9s %-13s %-10s | %-7s %-10s %-11s %-7s%n", ""  , ""     , ""    , ""      , "Total" , "Yesterday", ""      , ""     , ""          , "Total");
        displaySeparator();
    }

    /**
     * Displays the portfolio header.
     * @param user
     * @param day
     * @param today
     */
    public static void displayPortfolioHeader(User user, int day, StockDay today) {
        displaySeparator();
        outputHandler.println("Your Stocks:");
        outputHandler.printf("%-77s | %-65s %n", "Available Stocks    |    Day: " + day, "Portfolio of " + user.getUsername() + " | Balance: " + user.getMoney() + "$" + " | Total Value: " + String.format("%.2f", user.getInvestedValue(today)) + "$");
        outputHandler.printf("%-5s %-5s %-30s %-9s %-13s %-10s | %-7s %-10s %-11s %-7s%n", "ID", "Abbr.", "Name", "Course", "Volume", "Diff"     , "Amount", "Value", "Avg. Entry", "Profit");
        outputHandler.printf("%-5s %-5s %-30s %-9s %-13s %-10s | %-7s %-10s %-11s %-7s%n", ""  , ""     , ""    , ""      , "Total" , "Yesterday", ""      , ""     , ""          , "Total");
        displaySeparator();
    }

    /**
     * Displays a message indicating that the portfolio is empty.
     */
    public static void portfolioEmptyMessage() {
        outputHandler.println("Your portfolio is empty! You should first buy some Stocks.");
    }

    /**
     * Displays the stock details of the portfolio.
     * @param stockID
     * @param stockAbbr
     * @param stockName
     * @param stockCourse
     * @param stockVolume
     * @param perStockChange
     * @param userStockAmount
     * @param avgEntryPrice
     * @param profitAmount
     * @param profitPercentage
     */
    public static void displayStockDetails(int stockID, String stockAbbr, String stockName, double stockCourse, int stockVolume, double perStockChange, int userStockAmount, double avgEntryPrice, double profitAmount, double profitPercentage) {
        // String Formatting
        String stockCourseString = String.format("%.2f$", stockCourse);
        String perStockChangeStr = perStockChange >= 0 ? "\u001B[32m+" + String.format("%.2f%%", perStockChange) + "\u001B[0m" : "\u001B[31m" + String.format("%.2f%%", perStockChange) + "\u001B[0m";
        String userStockAmountStr = userStockAmount == 0 ? "" : String.valueOf(userStockAmount);
        String userStockValueStr = userStockAmount == 0 ? "" : String.format("%.2f$", userStockAmount * stockCourse);
        String userStockAvgEntryPrice = userStockAmount == 0 ? "" : String.format("%.2f$", avgEntryPrice);
        String userProfitAmount = userStockAmount == 0 ? "" : (profitAmount >= 0 ? "\u001B[32m+" + String.format("%.2f$", profitAmount) + "\u001B[0m" : "\u001B[31m" + String.format("%.2f$", profitAmount) + "\u001B[0m");
        String userProfitAmountPer = userStockAmount == 0 || avgEntryPrice == 0 ? "" : (profitPercentage >= 0 ? "\u001B[32m" + String.format("%.2f%%", profitPercentage) + "\u001B[0m" : "\u001B[31m" + String.format("%.2f%%", profitPercentage) + "\u001B[0m");
    
        // Output
        outputHandler.printf( "%-5s %-5s %-30s %-10s %-13d %-18s | %-7s %-10s %-11s %-7s %-7s%n", 
            stockID, stockAbbr, stockName, stockCourseString, stockVolume, perStockChangeStr, userStockAmountStr, userStockValueStr, userStockAvgEntryPrice, userProfitAmount, userProfitAmountPer
        );
    }

    /**
     * Prints the header for the sell options.
     */
    public static void printSellOptionsHeader(){
        displaySeparator();
        outputHandler.println("Sell options for your stocks:");
        outputHandler.printf("%-5s %-5s %-30s %-10s %-10s %-7s%n", "ID", "Abbr.", "Name", "Amount", "Price", "Profit");
        displaySeparator();
    }

    /**
     * Displays the stock details of the available stocks.
     * @param stockID
     * @param stockAbbr
     * @param stockName
     * @param userStockAmount
     * @param stockCourse
     * @param perStockChange
     */
    public static void displaySellDetailsOptions(int stockID, String stockAbbr, String stockName, int userStockAmount, double stockCourse, double profitAmount, double profitPercentage) {
        // String Formatting
        String stockCourseString = String.format("%.2f$", stockCourse);
        String userStockAmountStr = userStockAmount == 0 ? "" : String.valueOf(userStockAmount);
        String userProfitAmount = userStockAmount == 0 ? "" : (profitAmount >= 0 ? "\u001B[32m+" + String.format("%.2f$", profitAmount) + "\u001B[0m" : "\u001B[31m" + String.format("%.2f$", profitAmount) + "\u001B[0m");
        String userProfitAmountPer = (profitPercentage >= 0 ? "\u001B[32m" + String.format("%.2f%%", profitPercentage) + "\u001B[0m" : "\u001B[31m" + String.format("%.2f%%", profitPercentage) + "\u001B[0m");
        // Output
        outputHandler.printf( "%-5s %-5s %-30s %-10s %-10s %-7s %-7s%n", 
            stockID, stockAbbr, stockName, userStockAmountStr, stockCourseString, userProfitAmount, userProfitAmountPer
        );
    }

    /**
     * Prints the header for the User can buy.
     */
    public static void printUserCanBuyHeader(double money) {
        displaySeparator();
        outputHandler.println("With your Money: " + money + "$ you can buy:");
        outputHandler.printf("%-5s %-5s %-30s %-7s %-10s %n", "ID", "Abbr.", "Name", "Max", "Price");
        displaySeparator();
    }

    /**
     * Displays the stock details of the available stocks that the user can buy.
     * @param stockID
     * @param stockAbbr
     * @param stockName
     * @param maxAmount
     * @param stockCourse
     */
    public static void displayUserCanBuyStock (int stockID, String stockAbbr, String stockName, int maxAmount, double stockCourse) {
        // String Formatting
        String stockCourseString = String.format("%.2f$", stockCourse);
        // Output
        outputHandler.printf( "%-5s %-5s %-30s %-7s %-10s %n", 
            stockID, stockAbbr, stockName, maxAmount, stockCourseString
        );
    }

    /**
     * Seperator Line
     */
    public static void displaySeparator() {
        outputHandler.println("--------------------------------------------------------------------------------------------------------------------------");
    }

    /**
     * Asks the user if they want to restart the game.
     */
    public static void askRestart() {
        outputHandler.println("Do you want to restart StockGuru? (Yes/No)");
    }

    /**
     * Goodbye message.
     */
    public static void goodbyeMessage() {
        outputHandler.println("Goodbye! Thanks for using StockGuru!");
    }

    /**
     * Displays day.
     * @param day
     */
    public static void displayDay(int day) {
        outputHandler.println("Day: " + day);
    }

    /**
     * Ending day message.
     */
    public static void endingDayMessage() {
        outputHandler.println("Ending the day...");
    }
    
    /**
     * Tells the user that they don't have enough money to buy that stock.
     * @param name
     * @param stockID
     */
    public static void notEnoughMoneyToBuy(String name, int stockID) {
        outputHandler.println("You don't have enough money to buy even 1 stock of " + name + "!");
    }

    /**
     * Asks the user how many stocks they want to buy.
     * @param userMoney
     * @param maxAmount
     * @param totalCost
     */
    public static void displayBuyOptions(double userMoney, int maxAmount, double totalCost) {
        outputHandler.println("You have " + userMoney + "$ available and can buy a Maximum amount of " + maxAmount + " stocks for a Total of " + totalCost + "$.");
        outputHandler.println("How many stocks would you like to buy? (0 to cancel)");
    }

    /**
     * Asks the user if they want to sell a specific stock or all stocks.
     */
    public static void displaySellOptions() {
        outputHandler.println("There are Multiple Options select what you want to do:");
        outputHandler.println("1. Sell Specific Stock");
        outputHandler.println("2. Sell All Stocks");
    }

    /**
     * Displays a message indicating an invalid sell amount.
     * This occurs when the user tries to sell more stocks than they own.
     */
    public static void invalidSellAmount() {
        outputHandler.println("Invalid sell amount! You cannot sell more stocks than you own.");
    }
    
    /**
     * Displays a message indicating that the stock amount cannot be negative.
     * This is used to handle invalid input scenarios.
     */
    public static void negativeStockAmount() {
        outputHandler.println("Invalid input! Stock amount cannot be negative.");
    }
    
    /**
     * Displays a message indicating that no stocks were sold.
     * This is used when the user chooses not to sell any stocks.
     */
    public static void noStocksSold() {
        outputHandler.println("No stocks were sold.");
    }
    
    /**
     * Displays a message indicating that no stocks were bought.
     * This is used when the user chooses not to buy any stocks.
     */
    public static void noStocksBought() {
        outputHandler.println("No stocks were bought.");
    }
    
    /**
     * Prompts the user to enter the ID of the stock they want to sell.
     */
    public static void askStockToSell() {
        outputHandler.println("Enter the ID of the stock you want to sell:");
    }
    
    /**
     * Prompts the user to enter the amount of stocks they want to sell.
     */
    public static void askStockAmountToSell() {
        outputHandler.println("Enter the amount of stocks you want to sell:");
    }
    
    /**
     * Asks the user what action they would like to perform.
     * This is typically used to display a menu of options.
     */
    public static void askUserAction() {
        outputHandler.println("What would you like to do?");
    }
    
    /**
     * Displays a menu option with its number and description.
     * @param optionNumber The number of the menu option.
     * @param optionDescription The description of the menu option.
     */
    public static void displayMenuOption(int optionNumber, String optionDescription) {
        outputHandler.printf("%d. %s%n", optionNumber, optionDescription);
    }
    
    /**
     * Displays a message indicating that the user input is invalid.
     * This is used to prompt the user to enter a valid input.
     */
    public static void displayInvalidInputMessage() {
        outputHandler.println("Please enter a valid input!");
    }
    
    /**
     * Displays a message indicating that stocks were successfully bought.
     * @param name The name of the stock.
     * @param amount The number of stocks bought.
     * @param course The price per stock.
     * @param money The user's new account balance after the purchase.
     */
    public static void buyStockMessage(String name, int amount, double course, double money) {
        outputHandler.println("Bought " + amount + " stocks of " + name + " for " + ((double) amount) * course + "$");
        outputHandler.println("New Account Balance: " + money + "$");
    }
    
    /**
     * Displays a message indicating that stocks were successfully sold.
     * @param name The name of the stock.
     * @param amount The number of stocks sold.
     * @param course The price per stock.
     * @param money The user's new account balance after the sale.
     */
    public static void sellStockMessage(String name, int amount, double course, double money) {
        outputHandler.println("Sold " + amount + " stocks of " + name + " for " + ((double) amount) * course + "$");
    }
}