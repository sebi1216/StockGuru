public class Stock {
    int ID;
    double course;
    long volume; 

    public Stock(int ID, double course, long volume) {
        this.ID = ID;
        this.course = course;
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "Stock { ID=" + ID + "', course=" + course + ", volume=" + volume + " }";
    }
}
