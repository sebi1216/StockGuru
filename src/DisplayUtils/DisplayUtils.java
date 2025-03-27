package DisplayUtils;

import Stocks.StockDay;
import User.User;

public class DisplayUtils {
    private static OutputHandler outputHandler = new ConsoleOutputHandler();

    public static void setOutputHandler(OutputHandler handler) {
        outputHandler = handler;
    }

    public static void welcomeMessage() {
        outputHandler.println("Welcome to StockGuru!");
    }

    public static void askUserName() {
        outputHandler.println("What is your name?");
    }

    public static void greetUser(String username) {
        outputHandler.println("Hello " + username + "!");
    }

    public static void askUserType() {
        outputHandler.println("Do you want to be a Trader or a Bot? (Trader/Bot)");
    }

    public static void askBotName() {
        outputHandler.println("What is your Bot's name?");
    }

    public static void askMaxSharesPercentage() {
        outputHandler.println("Enter the maximum percentage of shares per stock for your Bot:");
    }

    public static void endOfGameMessage() {
        outputHandler.println("END OF GAME!");
    }

    public static void displayBestStock(String stockName, double absoluteIncrease, double percentageIncrease) {
        outputHandler.printf("The best performing stock is: %s with a total increase of Value by: %.2f$ (%.2f%%)!%n", stockName, absoluteIncrease, percentageIncrease);
    }

    public static void displayTotalProfit(double totalProfit) {
        outputHandler.printf("The Total Profit is: %.2f$!%n", totalProfit);
    }

    public static void displayPortfolioHeader(User user, int day, StockDay today) {
        outputHandler.printf("%nYour stock portfolio:%n");
        outputHandler.printf("%-77s | %-65s %n", "Available Stocks    |    Day: " + day, "Portfolio of " + user.getUsername() + " | Balance: " + user.getMoney() + "$" + " | Total Value: " + String.format("%.2f", user.getInvestedValue(today)) + "$");
        outputHandler.printf("%-5s %-5s %-30s %-9s %-13s %-10s | %-7s %-10s %-11s %-7s%n", "ID", "Abbr.", "Name", "Course", "Volume", "Diff"     , "Amount", "Value", "Avg. Entry", "Profit");
        outputHandler.printf("%-5s %-5s %-30s %-9s %-13s %-10s | %-7s %-10s %-11s %-7s%n", ""  , ""     , ""    , ""      , "Total" , "Yesterday", ""      , ""     , ""          , "Total");
        displaySeparator();
    }

    public static void displayStockDetails(int stockID, String stockAbbr, String stockName, double stockCourse, int stockVolume, double perStockChange, int userStockAmount, double avgEntryPriceToday, double profitAmount, double profitPercentage) {
        // String Formatting
        String stockCourseString = String.format("%.2f$", stockCourse);
        String perStockChangeStr = perStockChange >= 0 ? "\u001B[32m+" + String.format("%.2f%%", perStockChange) + "\u001B[0m" : "\u001B[31m" + String.format("%.2f%%", perStockChange) + "\u001B[0m";
        String userStockAmountStr = userStockAmount == 0 ? "" : String.valueOf(userStockAmount);
        String userStockValueStr = userStockAmount == 0 ? "" : String.format("%.2f$", userStockAmount * stockCourse);
        String userStockAvgEntryPrice = userStockAmount == 0 ? "" : String.format("%.2f$", avgEntryPriceToday);
        String userProfitAmount = userStockAmount == 0 ? "" : (profitAmount >= 0 ? "\u001B[32m+" + String.format("%.2f$", profitAmount) + "\u001B[0m" : "\u001B[31m" + String.format("%.2f$", profitAmount) + "\u001B[0m");
        String userProfitAmountPer = userStockAmount == 0 || avgEntryPriceToday == 0 ? "" : (profitPercentage >= 0 ? "\u001B[32m" + String.format("%.2f%%", profitPercentage) + "\u001B[0m" : "\u001B[31m" + String.format("%.2f%%", profitPercentage) + "\u001B[0m");
    
        // Output
        outputHandler.printf( "%-5s %-5s %-30s %-10s %-13d %-18s | %-7s %-10s %-11s %-7s %-7s%n", 
            stockID, stockAbbr, stockName, stockCourseString, stockVolume, perStockChangeStr, userStockAmountStr, userStockValueStr, userStockAvgEntryPrice, userProfitAmount, userProfitAmountPer
        );
    }

    public static void displaySeparator() {
        outputHandler.println("--------------------------------------------------------------------------------------------------------------------------");
    }

    public static void askRestart() {
        outputHandler.println("Do you want to restart StockGuru? (Yes/No)");
    }

    public static void goodbyeMessage() {
        outputHandler.println("Goodbye! Thanks for using StockGuru!");
    }

    public static void displayDay(int day) {
        outputHandler.println("Day: " + day);
    }

    public static void endingDayMessage() {
        outputHandler.println("Ending the day...");
    }

    public static void notEnoughMoneyToBuy(String name, int stockID) {
        outputHandler.println("You don't have enough money to buy even 1 stock of " + name + "!");
    }

    public static void displayBuyOptions(double userMoney, int maxAmount, double totalCost) {
        outputHandler.println("You have " + userMoney + "$ available and can buy a Maximum amount of " + maxAmount + " stocks for a Total of " + totalCost + "$.");
        outputHandler.println("How many stocks would you like to buy? (0 to cancel)");
    }

    public static void displaySellOptions() {
        outputHandler.println("There are Multiple Options select what you want to do:");
        outputHandler.println("1. Sell Specific Stock");
        outputHandler.println("2. Sell All Stocks");
    }

    public static void invalidSellAmount() {
        outputHandler.println("Invalid sell amount! You cannot sell more stocks than you own.");
    }

    public static void negativeStockAmount() {
        outputHandler.println("Invalid input! Stock amount cannot be negative.");
    }

    public static void noStocksSold() {
        outputHandler.println("No stocks were sold.");
    }

    public static void noStocksBought() {
        outputHandler.println("No stocks were bought.");
    }

    public static void askStockToSell() {
        outputHandler.println("Enter the ID of the stock you want to sell:");
    }

    public static void askStockAmountToSell() {
        outputHandler.println("Enter the amount of stocks you want to sell:");
    }

    public static void askUserAction() {
        outputHandler.println("What would you like to do?");
    }

    public static void displayMenuOption(int optionNumber, String optionDescription) {
        outputHandler.printf("%d. %s%n", optionNumber, optionDescription);
    }

    public static void displayInvalidInputMessage() {
        outputHandler.println("Please enter a valid input!");
    }
}