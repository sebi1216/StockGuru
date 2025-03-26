import java.util.ArrayList;

public class Users {
    private ArrayList<User> users;

    public Users() {
        this.users = new ArrayList<>();
    }

    public User getUser(int ID) {
        for (User user : users) {
            if (user.ID == ID) {
                return user;
            }
        }
        return null;
    }

    public void addUser(User user) {
        if (user != null) {
            users.add(user);
        }
    }
}