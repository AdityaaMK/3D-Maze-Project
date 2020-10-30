public class Location {
    private int r = 0;
    private int c = 0;

    public Location(int r, int c) {
        this.r = r;
        this.c = c;
    }

    public void setR(int a) {
        r += a;
    }

    public void setC(int b) {
        c += b;
    }

    public int getR() {
        return r;
    }

    public int getC() {
        return c;
    }

    public double getDistance(Location loc) {
        int dRow = r - loc.getR();
        int dCol = c - loc.getC();
        return Math.sqrt((dRow * dRow) + (dCol * dCol));
    }
}
