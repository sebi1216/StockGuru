package User;
import java.util.ArrayList;

public class Users {
    private ArrayList<User> users;
    private static int currentID = 0;

    /**
     * * Constructor for the Users class.
     * Initializes an empty list of users.
     */
    public Users() {
        this.users = new ArrayList<>();
    }

    /**
     * * Creates a new trader with a unique ID and username.
     * Add the trader to the list of users.
     * @param username
     */
    public Trader createTrader(String username) {
        int ID = currentID++;
        Trader trader = new Trader(ID, username);
        users.add(trader);
        return trader;
    }

    /**
     * Creates a new bot with a unique ID and username.
     * Add the bot to the list of users.
     * @param username
     * @param maxSharesPercentage
     */
    public Bot createBot(String username, int maxSharesPercentage) {
        int ID = currentID++;
        Bot bot = new Bot(ID, username, maxSharesPercentage);
        users.add(bot);
        return bot;
    }

    /**
     * Returns the user with the given ID.
     * @param ID
     * @return User or null if not found
     */
    public User getUser(int ID) {
        for (User user : users) {
            if (user.ID == ID) {
                return user;
            }
        }
        return null;
    }
}