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
    public void createTrader(String username) {
        int ID = currentID++;
        Trader trader = new Trader(ID, username);
        users.add(trader);
    }

    /**
     * Creates a new bot with a unique ID and username.
     * Add the bot to the list of users.
     * @param username
     * @param maxSharesPercentage
     */
    public void createBot(String username, int maxSharesPercentage) {
        int ID = currentID++;
        Bot bot = new Bot(ID, username, maxSharesPercentage);
        users.add(bot);
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

    /**
     * Adds a user to the list of users.
     * @param user
     */
    public void addUser(User user) {
        if (user != null) {
            users.add(user);
        }
    }
}