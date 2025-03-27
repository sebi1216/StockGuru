package Stocks;
public class Stock {
    int ID;
    double course;
    long volume; 

    /**
     * * Constructor for the Stock class.
     * @param ID
     * @param course
     * @param volume
     */
    public Stock(int ID, double course, long volume) {
        this.ID = ID;
        this.course = course;
        this.volume = volume;
    }

    /**
     * * Returns the ID of the stock.
     * @return int
     */
    public int getID() {
        return ID;
    }

    /**
     * * Returns the course of the stock.
     * @return double
     */
    public double getCourse() {
        return course;
    }

    /**
     * * Returns the volume of the stock.
     * @return
     */
    public long getVolume() {
        return volume;
    }

    /**
     * * Sets the volume of the stock.
     * @param volume
     */
    public void setVolume(long volume) {
        this.volume = volume;
    }

    /**
     * * Override toString method to display stock information.
     * * @return String representation of the stock.
     */
    @Override
    public String toString() {
        return "Stock { ID=" + ID + "', course=" + course + ", volume=" + volume + " }";
    }
}
