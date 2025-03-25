import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class StockGuru {
    static ArrayList<StockDay> stockDaysAll = new ArrayList<StockDay>();

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Welcome to StockGuru!");
        System.out.println("What is your name?");
        String username = scan.nextLine();
        System.out.println("Hello " + username + "!");
        User user = new User(username);
        beginOfDay(user);
        scan.close();
    }

    public static void beginOfDay(User user) {
        System.out.println("Data is being loaded...");
        updateStockData(user);
        System.out.println("Data loaded!");
        System.out.println("Day " + user.day);
        System.out.println("You have " + user.money + "$ in your account.");
        System.out.println("Your stock portfolio:");
        for (Stock stock : stockDaysAll.get(user.day).stocks) {
            System.out.println(stock.name + " - " + stock.course + " - " + stock.volume);
        }
    }

    // We read the CSV Data of all our Stocks and get the Data of the current Day
    public static void updateStockData(User user) {
        int day = user.day;
        StockDay stockDay = new StockDay(day);
        String[] stockFiles = {
            "^GDAXI.csv", "ADS.csv", "AIR.csv", "ALV.csv", "BAS.csv", "BAYN.csv", "BEI.csv", "BMW.csv", "BNR.csv", "CBK.csv", "CON.csv",
            "DB1.csv", "DBK.csv", "DHL.csv", "DTE.csv", "DTG.csv", "ENR.csv", "EOAN.csv", "FME.csv", "FRE.csv", "HEI.csv",
            "HEN3.csv", "HNR1.csv", "IFX.csv", "MBG.csv", "MRK.csv", "MTX.csv", "MUV2.csv", "P911.csv", "PAH3.csv", "QIA.csv",
            "RHM.csv", "RWE.csv", "SAP.csv", "SHL.csv", "SIE.csv", "SRT3.csv", "SY1.csv", "VNA.csv", "VOW3.csv", "ZAL.csv"
        };

        for (String stockFile : stockFiles) {
            try (BufferedReader br = new BufferedReader(new FileReader("Stocks/" + stockFile))) {
                String line;
                br.readLine(); // Skip header
                for (int i = 0; i <= day; i++) {
                    line = br.readLine();
                    if (line == null) break;
                    if (i == day) {
                        String[] data = line.split(";");
                        String name = stockFile.replace(".csv", "");
                        double course = Double.parseDouble(data[1].replace(",", "."));
                        int volume = Integer.parseInt(data[2].replace(".", "").replace(",", ""));
                        Stock stock = new Stock(name, course, volume);
                        stockDay.stocks.add(stock);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        stockDaysAll.add(stockDay);
    }
}