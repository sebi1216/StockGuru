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


## Fragen

Tag 0 ist dies der Start Tag / Soll dieser angezeigt werden
Course: Ist der Course immer der Endkurs, bedeutet wir kaufen zu dem Course vom Vortag?

