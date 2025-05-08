# StockGuru

StockGuru is a stock trading simulation game where users can act as either a Trader or a Bot to buy, sell, and manage stocks over a 10-day period. The game provides insights into stock performance and allows users to optimize their portfolio for maximum profit.

## Features

- **Trader Mode**: Manually buy and sell stocks based on market data.
- **Bot Mode**: Automated trading with strategies for initial investment, portfolio optimization, and fund allocation.
- **Stock Data**: Real-time simulation using stock data from CSV files.
- **Logs**: Detailed logs of user actions and buy/sell transactions.
- **Performance Insights**: Displays the best-performing stock and total profit at the end of the game.

## Project Structure

StockGuru/
├── bin/                # Compiled Java classes
├── lib/                # Stock data and naming files
│   ├── Stocks/         # CSV files with stock data
│   └── Namings.csv     # Stock abbreviations and names
├── src/                # Source code
│   ├── DisplayUtils/   # Utility classes for displaying messages
│   ├── Logs/           # Logging classes for user actions and transactions
│   ├── Stocks/         # Stock-related classes
│   └── User/           # User-related classes (Trader, Bot)
├── .vscode/            # VS Code settings
│   └── settings.json   # VS Code configuration
├── README.md           # Project documentation
└── Individualleistung Programmierung II.pdf # Project description document ==> Requires Extension to Read

## How to Run

1. **Setup**:
   - Ensure you have Java installed on your system.
   - Place the project files in a directory.

2. **Compile**:
   - Navigate to the `src` directory and compile the project:
     ```sh
     javac -d ../bin $(find . -name "*.java")
     ```

3. **Run**:
   - Navigate to the `bin` directory and run the main class:
     ```sh
     java StockGuru
     ```

## How to Insert Custom CSV Data

To use your own stock data in the simulation, follow these steps:

1. **Namings File**:
   - Create a CSV file named `Namings.csv` in the `lib/` directory.
   - This file should contain stock abbreviations and their full names in the following format:
     ```
     Abk;Name
     ADS;adidas AG
     AIR;Airbus SE
     ```
   - Each line represents a stock, where `Abk` is the abbreviation and `Name` is the full name.

2. **Stock Data Files**:
   - Place your stock data CSV files in the `lib/Stocks/` directory.
   - Each file should be named using the stock abbreviation (e.g., `ADS.csv` for adidas AG).
   - The file should contain daily stock data in the following format:
     ```
     Date;Endwert;Volumen
     Tag 0;451.72;948003
     Tag 1;470.58;1535096
     ```
   - `Date` is the day, `Endwert` is the closing price, and `Volumen` is the trading volume.

3. **Ensure Consistency**:
   - The abbreviations in `Namings.csv` must match the filenames of the stock data CSV files.
   - For example, if `Namings.csv` contains `ADS`, there must be a corresponding `ADS.csv` in the `lib/Stocks/` directory.

4. **Restart the Application**:
   - After adding your custom CSV files, restart the application to load the new data.

## Gameplay

1. **Start**:
   - Enter your name and choose whether to play as a Trader or a Bot.
   - If you choose Bot, specify the maximum percentage of shares per stock.

2. **Daily Actions**:
   - View available stocks and their performance.
   - Buy or sell stocks based on your strategy.

3. **End of Game**:
   - After 10 days, view the best-performing stock and your total profit.

## Data Files

- **`lib/Namings.csv`**: Contains stock abbreviations and their full names.
- **`lib/Stocks/*.csv`**: Daily stock data, including price and volume.

## Classes Overview

- **`StockGuru`**: Main class to initialize and run the game.
- **`User`**: Abstract class for users (Trader and Bot).
- **`Bot`**: Automated trading logic.
- **`Trader`**: Manual trading logic.
- **`Stock`**: Represents a stock with ID, price, and volume.
- **`StockDay`**: Represents stock data for a single day.
- **`StockDaysAll`**: Manages stock data across all days.
- **`ActionLogs`**: Logs user actions and transactions.
- **`DisplayUtils`**: Utility methods for displaying messages.

## License

This project is developed as part of the DHBW Programming Project by Sebastian Schierholz. All rights reserved.