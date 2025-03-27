package User;
import java.util.ArrayList;

public class Users {
    private ArrayList<User> users;

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
        Trader trader = new Trader(getHighestID(), username);
        users.add(trader);
    }

    /**
     * Creates a new bot with a unique ID and username.
     * Add the bot to the list of users.
     * @param username
     * @param maxSharesPercentage
     */
    public void createBot(String username, int maxSharesPercentage) {
        Bot bot = new Bot(getHighestID(), username, maxSharesPercentage);
        users.add(bot);
    }

    /**
     * Returns a new ID that is one higher than the highest ID in the list of users.
     * @return int
     */
    public int getHighestID() {
        int highestID = 0;
        for (User user : users) {
            if (user.getID() > highestID) {
                highestID = user.getID();
            }
        }
        return highestID + 1; 
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