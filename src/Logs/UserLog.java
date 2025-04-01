package Logs;

public class UserLog {
    int logID;
    int userID;
    int day;
    String action;

    /**
     * Constructor for the UserLog class.
     * @param logID
     * @param userID
     * @param day
     * @param action
     */
    public UserLog(int logID, int userID, int day, String action) {
        this.logID = logID;
        this.userID = userID;
        this.day = day;
        this.action = action;
    }

    /**
     * Returns the ID of the log.
     * @return
     */
    public int getLogID() {
        return logID;
    }

    /**
     * Returns the ID of the user who made the action.
     * @return
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Returns the day of the action.
     * @return
     */
    public int getDay() {
        return day;
    }

    /**
     * Returns the action performed by the user.
     * @return
     */
    public String getAction() {
        return action;
    }

    /**
     * Override the toString method to return a string representation of the UserLog object.
     */
    @Override
    public String toString() {
        return "UserLog { userID=" + userID + ", day=" + day + ", action=" + action + " }";
    }
    
}
