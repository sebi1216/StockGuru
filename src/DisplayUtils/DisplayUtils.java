package DisplayUtils;

import java.util.HashMap;
import java.util.List;

import Logs.BuySellLog;
import Logs.UserLog;
import Stocks.Stock;
import Stocks.StockDay;
import User.User;

public class DisplayUtils {
    private static OutputHandler outputHandler = new ConsoleOutputHandler();
    private static int languageIdx = 0; // 0 = English, 1 = Deutsch

    /**
     * Sets the output handler to be used for displaying messages.
     * @param handler
     */
    public static void setOutputHandler(OutputHandler handler) {
        outputHandler = handler;
    }

    public static void setLanguage(int languageIdx) {
        DisplayUtils.languageIdx = languageIdx;
    }
    
    /**
     * Displays a message indicating that the user should select their language.
     */
    public static void askLanguage() {
            outputHandler.println("Please select your language: (English/German)");
    }
    
    /**
     * Displays a welcome message to the user.
     */
    public static void welcomeMessage() {
        if (languageIdx == 0) {
            outputHandler.println("Welcome to StockGuru!");
        } else {
            outputHandler.println("Willkommen bei StockGuru!");
        }
    }
    
    /**
     * Asks the user for their name.
     */
    public static void askUserName() {
        if (languageIdx == 0) {
            outputHandler.println("What is your name?");
        } else {
            outputHandler.println("Wie heißen Sie?");
        }
    }
    
    /**
     * Displays a greeting message to the user.
     * @param username
     */
    public static void greetUser(String username) {
        if (languageIdx == 0) {
            outputHandler.println("Hello " + username + "!");
        } else {
            outputHandler.println("Hallo " + username + "!");
        }
    }
    
    /**
     * Asks the user if they want to be a trader or a bot.
     */
    public static void askUserType() {
        if (languageIdx == 0) {
            outputHandler.println("Do you want to be a Trader or a Bot? (Trader/Bot)");
        } else {
            outputHandler.println("Möchten Sie ein Trader oder ein Bot sein? (Trader/Bot)");
        }
    }
    
    /**
     * Asks the user for the Bot Name.
     */
    public static void askBotName() {
        if (languageIdx == 0) {
            outputHandler.println("What is your Bot's name?");
        } else {
            outputHandler.println("Wie heißt Ihr Bot?");
        }
    }
    
    /**
     * Asks the user for the maximum percentage of shares per stock for their Bot.
     */
    public static void askMaxSharesPercentage() {
        if (languageIdx == 0) {
            outputHandler.println("Enter the maximum percentage of shares per stock for your Bot:");
        } else {
            outputHandler.println("Geben Sie den maximalen Prozentsatz der Aktien pro Aktie für Ihren Bot ein:");
        }
    }
    
    /**
     * End of game message.
     */
    public static void endOfGameMessage() {
        if (languageIdx == 0) {
            outputHandler.println("END OF GAME!");
        } else {
            outputHandler.println("SPIELENDE!");
        }
    }
    
    /**
     * Displays the best performing stock.
     * @param stockName
     * @param absoluteIncrease
     * @param percentageIncrease
     */
    public static void displayBestStock(String stockName, double absoluteIncrease, double percentageIncrease) {
        if (languageIdx == 0) {
            outputHandler.printf("The best performing stock is: %s with a total increase of Value by: %.2f$ (%.2f%%)!%n", stockName, absoluteIncrease, percentageIncrease);
        } else {
            outputHandler.printf("Die beste Aktie ist: %s mit einer Wertsteigerung von: %.2f$ (%.2f%%)!%n", stockName, absoluteIncrease, percentageIncrease);
        }
    }
    
    /**
     * Displays total profit.
     * @param totalProfit
     */
    public static void displayTotalProfit(double totalProfit) {
        if (languageIdx == 0) {
            outputHandler.printf("The Total Profit is: %.2f$!%n", totalProfit);
        } else {
            outputHandler.printf("Der Gesamtgewinn beträgt: %.2f$!%n", totalProfit);
        }
    }
    
    /**
     * Displays the result grade along with total profit and percentage profit.
     * The grade changes color based on its value (1 is green, 6 is red, with a gradient in between).
     * @param totalProfit The total profit made by the user.
     * @param totalPercentageProfit The total percentage profit made by the user.
     * @param grade The grade awarded to the user.
     */
    public static void displayResultGrade(double totalProfit, double totalPercentageProfit, double grade) {
        if (languageIdx == 0) {
            outputHandler.printf("Total Profit: %.2f$\n", totalProfit);
            outputHandler.printf("Total Percentage Profit: %.2f%%\n", totalPercentageProfit);
            outputHandler.printf("Your Grade: ");
        } else {
            outputHandler.printf("Gesamtgewinn: %.2f$\n", totalProfit);
            outputHandler.printf("Gesamtprozentualer Gewinn: %.2f%%\n", totalPercentageProfit);
            outputHandler.printf("Ihre Note: ");
        }
    
        // Calculate the color based on the grade (1 is green, 6 is red)
        int red = (int) ((grade - 1) / 5 * 255);
        int green = (int) ((6 - grade) / 5 * 255);
    
        String gradeColor = String.format("\u001B[38;2;%d;%d;0m", red, green);
    
        outputHandler.printf("%s%.1f/6\u001B[0m\n", gradeColor, grade);
    }
    
    /**
     * Displays the portfolio header which is displayed at the beginning of the day.
     * @param user The user whose portfolio is being displayed.
     * @param day The current day in the simulation.
     * @param today The current stock market day data.
     */
    public static void displayStocksHeader(User user, int day, StockDay today) {
        displaySeparator();
    
        // Display the header title based on the selected language
        if (languageIdx == 0) { // English
            outputHandler.println("Today's Stock Market:");
        } else { // Deutsch
            outputHandler.println("Heutiger Aktienmarkt:");
        }
    
        // Display the portfolio summary
        String availableStocksLabel = (languageIdx == 0) ? "Available Stocks    |    Day: " : "Verfügbare Aktien    |    Tag: ";
        String portfolioLabel = (languageIdx == 0) ? "Portfolio of " : "Portfolio von ";
        String balanceLabel = (languageIdx == 0) ? "Balance: " : "Kontostand: ";
        String totalValueLabel = (languageIdx == 0) ? "Total Value: " : "Gesamtwert: ";
    
        outputHandler.printf("%-77s | %-65s %n",
            availableStocksLabel + day,
            portfolioLabel + user.getUsername() + " | " + balanceLabel + user.getMoney() + "$" + " | " + totalValueLabel + String.format("%.2f", user.getInvestedValue(today)) + "$"
        );
    
        if (languageIdx == 0) { // English
            outputHandler.printf("%-5s %-5s %-30s %-9s %-13s %-10s | %-7s %-10s %-11s %-14s %-10s%n",
                "ID", "Abbr.", "Name", "Course", "Volume", "Diff", "Amount", "Value", "Avg. Entry", "Profit", "Notes");
            outputHandler.printf("%-5s %-5s %-30s %-9s %-13s %-10s | %-7s %-10s %-11s %-7s %-10s%n",
                "", "", "", "", "Total", "Yesterday", "", "", "", "Total", "");
        } else { // Deutsch
            outputHandler.printf("%-5s %-5s %-30s %-9s %-13s %-10s | %-7s %-10s %-11s %-7s %-10s%n",
                "ID", "Abk.", "Name", "Kurs", "Volumen", "Diff", "Menge", "Wert", "Durchschn. Einstieg", "Gewinn", "Notiz");
            outputHandler.printf("%-5s %-5s %-30s %-9s %-13s %-10s | %-7s %-10s %-11s %-7s %-10s%n",
                "", "", "", "", "Gesamt", "Gestern", "", "", "", "Gesamt", "");
        }
    
        displaySeparator();
    }
    
    /**
     * Displays a message indicating that the portfolio is empty.
     */
    public static void portfolioEmptyMessage() {
        if (languageIdx == 0) {
            outputHandler.println("Your portfolio is empty! You should first buy some Stocks.");
        } else {
            outputHandler.println("Ihr Portfolio ist leer! Sie sollten zuerst einige Aktien kaufen.");
        }
    }

    /**
     * Displays the portfolio header.
     * @param user
     * @param day
     * @param today
     */
    public static void displayPortfolioHeader(User user, int day, StockDay today) {
        displaySeparator();
        
        if (languageIdx == 0) { // English
            outputHandler.println("Your Stocks:");
            outputHandler.printf("%-77s | %-65s %n", 
                "Available Stocks    |    Day: " + day, 
                "Portfolio of " + user.getUsername() + 
                " | Balance: " + user.getMoney() + "$" + 
                " | Total Value: " + String.format("%.2f", user.getInvestedValue(today)) + "$"
            );
            outputHandler.printf("%-5s %-5s %-30s %-9s %-13s %-10s | %-7s %-10s %-11s %-14s %-10s%n", 
                "ID", "Abbr.", "Name", "Course", "Volume", "Diff", "Amount", "Value", "Avg. Entry", "Profit", "Note"
            );
            outputHandler.printf("%-5s %-5s %-30s %-9s %-13s %-10s | %-7s %-10s %-11s %-7s %-10s%n", 
                "", "", "", "", "Total", "Yesterday", "", "", "", "Total", ""
            );
        } else { // Deutsch
            outputHandler.println("Ihre Aktien:");
            outputHandler.printf("%-77s | %-65s %n", 
                "Verfügbare Aktien    |    Tag: " + day, 
                "Portfolio von " + user.getUsername() + " | Kontostand: " + user.getMoney() + "$" + " | Gesamtwert: " + String.format("%.2f", user.getInvestedValue(today)) + "$"
            );
            outputHandler.printf("%-5s %-5s %-30s %-9s %-13s %-10s | %-7s %-10s %-11s %-14s %-10s%n", 
                "ID", "Abk.", "Name", "Kurs", "Volumen", "Diff", "Menge", "Wert", "D. Einstieg", "Gewinn", "Notiz"
            );
            outputHandler.printf("%-5s %-5s %-30s %-9s %-13s %-10s | %-7s %-10s %-11s %-7s %-10s%n", 
                "", "", "", "", "Gesamt", "Gestern", "", "", "", "Gesamt", ""
            );
        }
        
        displaySeparator();
    }

    /**
     * Displays the stock details of the portfolio.
     */
    public static void displayStockDetails(int stockID, String stockAbbr, String stockName, double stockCourse, int stockVolume, double perStockChange, int userStockAmount, double avgEntryPrice, double profitAmount, double profitPercentage, String noteText) {
        // String Formatting
        String stockCourseString = String.format("%.2f$", stockCourse);
        String perStockChangeStr = perStockChange >= 0 ? "\u001B[32m+" + String.format("%.2f%%", perStockChange) + "\u001B[0m" : "\u001B[31m" + String.format("%.2f%%", perStockChange) + "\u001B[0m";
        String userStockAmountStr = userStockAmount == 0 ? "" : String.valueOf(userStockAmount);
        String userStockValueStr = userStockAmount == 0 ? "" : String.format("%.2f$", userStockAmount * stockCourse);
        String userStockAvgEntryPrice = userStockAmount == 0 ? "" : String.format("%.2f$", avgEntryPrice);
        String userProfitAmount = userStockAmount == 0 ? "" : (profitAmount >= 0 ? "\u001B[32m+" + String.format("%.2f$", profitAmount) + "\u001B[0m" : "\u001B[31m" + String.format("%.2f$", profitAmount) + "\u001B[0m");
        String userProfitAmountPer = userStockAmount == 0 || avgEntryPrice == 0 ? "" : (profitPercentage >= 0 ? "\u001B[32m" + String.format("%.2f%%", profitPercentage) + "\u001B[0m" : "\u001B[31m" + String.format("%.2f%%", profitPercentage) + "\u001B[0m");
    
        // Output
        outputHandler.printf(
            "%-5s %-5s %-30s %-10s %-13d %-18s | %-7s %-10s %-11s %-9s %-12s %-10s%n",
            stockID,
            stockAbbr,
            stockName,
            stockCourseString,
            stockVolume,
            perStockChangeStr,
            userStockAmountStr,
            userStockValueStr,
            userStockAvgEntryPrice,
            userProfitAmount,
            userProfitAmountPer,
            noteText
        );
    }
    
    /**
     * Prints the header for the sell options.
     */
    public static void printSellOptionsHeader() {
        displaySeparator();
        if (languageIdx == 0) {
            outputHandler.println("Sell options for your stocks:");
            outputHandler.printf("%-5s %-5s %-30s %-10s %-10s %-15s %-10s %n", "ID", "Abbr.", "Name", "Amount", "Price", "Profit", "Note");
        } else {
            outputHandler.println("Verkaufsoptionen für Ihre Aktien:");
            outputHandler.printf("%-5s %-5s %-30s %-10s %-10s %-15s %-10s %n", "ID", "Abk.", "Name", "Menge", "Preis", "Gewinn", "Notiz");
        }
        displaySeparator();
    }
    
    /**
     * Displays the stock details of the available stocks.
     */
    public static void displaySellDetailsOptions(int stockID, String stockAbbr, String stockName, int userStockAmount, double stockCourse, double profitAmount, double profitPercentage, String noteText) {
        // String Formatting
        String stockCourseString = String.format("%.2f$", stockCourse);
        String userStockAmountStr = userStockAmount == 0 ? "" : String.valueOf(userStockAmount);
        String userProfitAmount = userStockAmount == 0 ? "" : (profitAmount >= 0 ? "\u001B[32m+" + String.format("%.2f$", profitAmount) + "\u001B[0m" : "\u001B[31m" + String.format("%.2f$", profitAmount) + "\u001B[0m");
        String userProfitAmountPer = (profitPercentage >= 0 ? "\u001B[32m" + String.format("%.2f%%", profitPercentage) + "\u001B[0m" : "\u001B[31m" + String.format("%.2f%%", profitPercentage) + "\u001B[0m");
    
        // Output
        outputHandler.printf(
            "%-5s %-5s %-30s %-10s %-10s %-7s %-7s%n",
            stockID,
            stockAbbr,
            stockName,
            userStockAmountStr,
            stockCourseString,
            userProfitAmount,
            userProfitAmountPer,
            noteText
        );
    }
    
    /**
     * Prints the header for the User can buy.
     * @param money
     */
    public static void printUserCanBuyHeader(double money) {
        displaySeparator();
        if (languageIdx == 0) {
            outputHandler.println("With your Money: " + money + "$ you can buy:");
            outputHandler.printf("%-5s %-5s %-30s %-7s %-10s %-10s %n", "ID", "Abbr.", "Name", "Max", "Price", "Note");
        } else {
            outputHandler.println("Mit Ihrem Geld: " + money + "$ können Sie kaufen:");
            outputHandler.printf("%-5s %-5s %-30s %-7s %-10s %-10s %n", "ID", "Abk.", "Name", "Max", "Preis", "Notiz");
        }
        displaySeparator();
    }
    
    /**
     * Displays the stock details of the available stocks that the user can buy.
     * @param stockID
     * @param stockAbbr
     * @param stockName
     * @param maxAmount
     * @param stockCourse
     * @param stockNote
     */
    public static void displayUserCanBuyStock(int stockID, String stockAbbr, String stockName, int maxAmount, double stockCourse, String stockNote) {
        // String Formatting
        String stockCourseString = String.format("%.2f$", stockCourse);
    
        // Output
        outputHandler.printf(
            "%-5s %-5s %-30s %-7s %-10s %-10s%n",
            stockID,
            stockAbbr,
            stockName,
            maxAmount,
            stockCourseString,
            stockNote
        );
    }

    /**
     * Seperator Line
     */
    public static void displaySeparator() {
        outputHandler.println("-------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    /**
     * Asks the user if they want to restart the game.
     */
    public static void askRestart() {
        if (languageIdx == 0) {
            outputHandler.println("Do you want to restart StockGuru? (Yes/No)");
        } else {
            outputHandler.println("Möchten Sie StockGuru neu starten? (Ja/Nein)");
        }
    }
    
    /**
     * Goodbye message.
     */
    public static void goodbyeMessage() {
        if (languageIdx == 0) {
            outputHandler.println("Goodbye! Thanks for using StockGuru!");
        } else {
            outputHandler.println("Auf Wiedersehen! Danke, dass Sie StockGuru verwendet haben!");
        }
    }
    
    /**
     * Displays day.
     * @param day
     */
    public static void displayDay(int day) {
        if (languageIdx == 0) {
            outputHandler.println("Day: " + day);
        } else {
            outputHandler.println("Tag: " + day);
        }
    }
    
    /**
     * Ending day message.
     */
    public static void endingDayMessage() {
        if (languageIdx == 0) {
            outputHandler.println("Ending the day...");
        } else {
            outputHandler.println("Der Tag wird beendet...");
        }
    }
    
    /**
     * Tells the user that they don't have enough money to buy that stock.
     * @param name
     * @param stockID
     */
    public static void notEnoughMoneyToBuy(String name, int stockID) {
        if (languageIdx == 0) {
            outputHandler.println("You don't have enough money to buy even 1 stock of " + name + "!");
        } else {
            outputHandler.println("Sie haben nicht genug Geld, um auch nur 1 Aktie von " + name + " zu kaufen!");
        }
    }
    
    /**
     * Asks the user how many stocks they want to buy.
     * @param userMoney
     * @param maxAmount
     * @param totalCost
     */
    public static void displayBuyOptions(double userMoney, int maxAmount, double totalCost) {
        if (languageIdx == 0) {
            outputHandler.println("You have " + userMoney + "$ available and can buy a Maximum amount of " + maxAmount + " stocks for a Total of " + totalCost + "$.");
            outputHandler.println("How many stocks would you like to buy? (0 to cancel)");
        } else {
            outputHandler.println("Sie haben " + userMoney + "$ verfügbar und können maximal " + maxAmount + " Aktien für insgesamt " + totalCost + "$ kaufen.");
            outputHandler.println("Wie viele Aktien möchten Sie kaufen? (0 zum Abbrechen)");
        }
    }
    
    /**
     * Asks the user if they want to sell a specific stock or all stocks.
     */
    public static void displaySellOptions() {
        if (languageIdx == 0) {
            outputHandler.println("There are Multiple Options select what you want to do:");
            outputHandler.println("1. Sell Specific Stock");
            outputHandler.println("2. Sell All Stocks");
        } else {
            outputHandler.println("Es gibt mehrere Optionen. Wählen Sie, was Sie tun möchten:");
            outputHandler.println("1. Bestimmte Aktie verkaufen");
            outputHandler.println("2. Alle Aktien verkaufen");
        }
    }

        /**
     * Displays a message indicating an invalid sell amount.
     * This occurs when the user tries to sell more stocks than they own.
     */
    public static void invalidSellAmount() {
        if (languageIdx == 0) {
            outputHandler.println("Invalid sell amount! You cannot sell more stocks than you own.");
        } else {
            outputHandler.println("Ungültige Verkaufsmenge! Sie können nicht mehr Aktien verkaufen, als Sie besitzen.");
        }
    }
    
    /**
     * Displays a message indicating that the stock amount cannot be negative.
     * This is used to handle invalid input scenarios.
     */
    public static void negativeStockAmount() {
        if (languageIdx == 0) {
            outputHandler.println("Invalid input! Stock amount cannot be negative.");
        } else {
            outputHandler.println("Ungültige Eingabe! Die Aktienmenge darf nicht negativ sein.");
        }
    }
    
    /**
     * Displays a message indicating that no stocks were sold.
     * This is used when the user chooses not to sell any stocks.
     */
    public static void noStocksSold() {
        if (languageIdx == 0) {
            outputHandler.println("No stocks were sold.");
        } else {
            outputHandler.println("Es wurden keine Aktien verkauft.");
        }
    }
    
    /**
     * Displays a message indicating that no stocks were bought.
     * This is used when the user chooses not to buy any stocks.
     */
    public static void noStocksBought() {
        if (languageIdx == 0) {
            outputHandler.println("No stocks were bought.");
        } else {
            outputHandler.println("Es wurden keine Aktien gekauft.");
        }
    }
    
    /**
     * Prompts the user to enter the ID of the stock they want to sell.
     */
    public static void askStockToSell() {
        if (languageIdx == 0) {
            outputHandler.println("Enter the ID of the stock you want to sell:");
        } else {
            outputHandler.println("Geben Sie die ID der Aktie ein, die Sie verkaufen möchten:");
        }
    }
    
    /**
     * Prompts the user to enter the amount of stocks they want to sell.
     */
    public static void askStockAmountToSell() {
        if (languageIdx == 0) {
            outputHandler.println("Enter the amount of stocks you want to sell:");
        } else {
            outputHandler.println("Geben Sie die Anzahl der Aktien ein, die Sie verkaufen möchten:");
        }
    }
    
    /**
     * Asks the user what action they would like to perform.
     * This is typically used to display a menu of options.
     */
    public static void askUserAction() {
        if (languageIdx == 0) {
            outputHandler.println("What would you like to do?");
        } else {
            outputHandler.println("Was möchten Sie tun?");
        }
    }
    
    /**
     * Displays a menu option with its number and description.
     * @param optionNumber The number of the menu option.
     * @param optionDescription The description of the menu option.
     */
    public static void displayMenuOption(int optionNumber, String optionDescription) {
        if (languageIdx == 0) {
            outputHandler.printf("%d. %s%n", optionNumber, optionDescription);
        } else {
            outputHandler.printf("%d. %s%n", optionNumber, optionDescription); // Assuming the description is already localized
        }
    }
    
    /**
     * Displays a message indicating that the user input is invalid.
     * This is used to prompt the user to enter a valid input.
     */
    public static void displayInvalidInputMessage() {
        if (languageIdx == 0) {
            outputHandler.println("Please enter a valid input!");
        } else {
            outputHandler.println("Bitte geben Sie eine gültige Eingabe ein!");
        }
    }
    
    /**
     * Displays a message indicating that stocks were successfully bought.
     * @param name The name of the stock.
     * @param amount The number of stocks bought.
     * @param course The price per stock.
     * @param money The user's new account balance after the purchase.
     */
    public static void buyStockMessage(String name, int amount, double course, double money) {
        if (languageIdx == 0) {
            outputHandler.println("Bought " + amount + " stocks of " + name + " for " + ((double) amount) * course + "$");
            outputHandler.println("New Account Balance: " + money + "$");
        } else {
            outputHandler.println("Gekauft: " + amount + " Aktien von " + name + " für " + ((double) amount) * course + "$");
            outputHandler.println("Neuer Kontostand: " + money + "$");
        }
    }
    
    /**
     * Displays a message indicating that stocks were successfully sold.
     * @param name The name of the stock.
     * @param amount The number of stocks sold.
     * @param course The price per stock.
     * @param money The user's new account balance after the sale.
     */
    public static void sellStockMessage(String name, int amount, double course, double money) {
        if (languageIdx == 0) {
            outputHandler.println("Sold " + amount + " stocks of " + name + " for " + ((double) amount) * course + "$");
        } else {
            outputHandler.println("Verkauft: " + amount + " Aktien von " + name + " für " + ((double) amount) * course + "$");
        }
    }

    /**
     * Displays a message indicating that the user need to enter a valid ID for the stock where he wants to add a note.
     */
    public static void askStockToAddNote() {
        if (languageIdx == 0) {
            outputHandler.println("Enter the ID of the stock you want to add a note to:");
        } else {
            outputHandler.println("Geben Sie die ID der Aktie ein, zu der Sie eine Notiz hinzufügen möchten:");
        }
    }

    /**
     * Display a message to ask the User what Note he wants to add.
     * @param stockName
     */
    public static void askNoteText(String stockName) {
        if (languageIdx == 0) {
            outputHandler.println("Enter the note text for " + stockName + ":");
        } else {
            outputHandler.println("Geben Sie den Notiztext für " + stockName + " ein:");
        }
    }

    /**
     * Displays the header for BuySellLog history.
     */
    public static void displayBuySellLogHeader() {
        if (languageIdx == 0) {
            outputHandler.printf("%-10s %-5s %-8s %-8s %-10s%n", "UserID", "Day", "StockID", "Amount", "Course");
        } else {
            outputHandler.printf("%-10s %-5s %-8s %-8s %-10s%n", "Benutzer-ID", "Tag", "Aktien-ID", "Menge", "Kurs");
        }
    }
    
    /**
     * Displays the history of BuySellLog with aligned columns.
     * @param log
     */
    public static void displayHistoryBuySellLog(BuySellLog log) {
        displayBuySellLogHeader();
        if (languageIdx == 0) {
            outputHandler.printf("%-10d %-5d %-8d %-8d %-10.2f$%n", 
                log.getUserID(), log.getDay(), log.getStockID(), log.getAmount(), log.getCourse());
        } else {
            outputHandler.printf("%-10d %-5d %-8d %-8d %-10.2f$%n", 
                log.getUserID(), log.getDay(), log.getStockID(), log.getAmount(), log.getCourse());
        }
    }
    
    /**
     * Displays the header for UserLog history.
     */
    public static void displayUserLogHeader() {
        if (languageIdx == 0) {
            outputHandler.printf("%-10s %-5s %-8s %-20s%n", "LogID", "Day", "UserID", "Action");
        } else {
            outputHandler.printf("%-10s %-5s %-8s %-20s%n", "Log-ID", "Tag", "Benutzer-ID", "Aktion");
        }
    }
    
    /**
     * Displays the history of UserLog with aligned columns.
     * @param log
     */
    public static void displayHistoryUserLog(UserLog log) {
        displayUserLogHeader();
        if (languageIdx == 0) {
            outputHandler.printf("%-10d %-5d %-8d %-20s%n", 
                log.getLogID(), log.getDay(), log.getUserID(), log.getAction());
        } else {
            outputHandler.printf("%-10d %-5d %-8d %-20s%n", 
                log.getLogID(), log.getDay(), log.getUserID(), log.getAction());
        }
    }

    /**
     * Displays the best performing stocks and undervalued stocks.
     * @param bestPerforming
     * @param undervalued
     * @param stocksMap
     */
    public static void displayBestPerformingStocks(List<Stock> bestPerforming, List<Stock> undervalued, HashMap<Integer, Object[]> stocksMap) {
        
        if (languageIdx == 0) {
            outputHandler.println ("Stocks of the Day:");
            outputHandler.println("Best Performing Stocks:");
        } else {
            outputHandler.println ("Aktien des Tages:");
            outputHandler.println("Beste Aktien:");
        }

        for (Stock stock : bestPerforming) {
            if (languageIdx == 0) {
                outputHandler.printf("%-20s %-10s%n", 
                    "Stock:" + stocksMap.get(stock.getID())[1].toString(), 
                    String.format("Price: %.2f$", stock.getCourse()));
            } else {
                outputHandler.printf("%-20s %-10s%n", 
                    "Stock:" + stocksMap.get(stock.getID())[1].toString(),  
                    String.format("Preis: %.2f$", stock.getCourse()));
            }
        }

        if (languageIdx == 0) {
            outputHandler.println("Undervalued Stocks:");
        } else {
            outputHandler.println("Unterbewertete Aktien:");
        }
        
        for (Stock stock : undervalued) {
            if (languageIdx == 0) {
                outputHandler.printf("%-20s %-10s%n", 
                    "Stock:" + stocksMap.get(stock.getID())[1].toString(), 
                    String.format("Price: %.2f$", stock.getCourse()));
            } else {
                outputHandler.printf("%-20s %-10s%n", 
                    "Aktie:" + stocksMap.get(stock.getID())[1].toString(), 
                    String.format("Preis: %.2f$", stock.getCourse()));
            }
        }
    }

    /* *
     * Displays a message indicating that all stocks have been sold.
     * @param money The new balance after selling all stocks.
     */
    public static void allStocksSold(double money) {
        if (languageIdx == 0) {
            outputHandler.println("All stocks sold! New Balance: " + money + "$");
        } else {
            outputHandler.println("Alle Aktien verkauft! Neuer Kontostand: " + money + "$");
        }
    }
}