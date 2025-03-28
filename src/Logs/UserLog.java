package Logs;

public class UserLog {
    int LogID;
    int userID;
    int day;
    String action;

    /**
     * Constructor for the UserLog class.
     * @param userID
     * @param day
     * @param action
     */
    public UserLog(int LogID, int userID, int day, String action) {
        this.userID = userID;
        this.day = day;
        this.action = action;
    }

    /**
     * Override the toString method to return a string representation of the UserLog object.
     */
    @Override
    public String toString() {
        return "UserLog { userID=" + userID + ", day=" + day + ", action=" + action + " }";
    }
    
}
