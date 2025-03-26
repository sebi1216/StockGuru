package Stocks;
public class Stock {
    int ID;
    double course;
    long volume; 

    public Stock(int ID, double course, long volume) {
        this.ID = ID;
        this.course = course;
        this.volume = volume;
    }

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public double getCourse() {
        return course;
    }

    public void setCourse(double course) {
        this.course = course;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "Stock { ID=" + ID + "', course=" + course + ", volume=" + volume + " }";
    }
}
