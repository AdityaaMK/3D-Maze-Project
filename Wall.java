import java.awt.GradientPaint;
import java.awt.Color;

public class Wall {

    private int rows[], cols[], r, g, b, size;
    private String type;

    public Wall(int rows[], int cols[], int r, int g, int b, int size, String type) {
        this.rows = rows;
        this.cols = cols;
        this.r = r;
        this.g = g;
        this.b = b;
        this.size = size;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public int[] getRows() {
        return rows;
    }

    public int[] getCols() {
        return cols;
    }

    public int getNumSides() {
        return rows.length;
    }

    public Color getColor() {
        return new Color(r, g, b);
    }

    public GradientPaint getPaint() {
        return new GradientPaint(0, 0, getColor(), 0, 0, Color.BLACK);
    }

}
