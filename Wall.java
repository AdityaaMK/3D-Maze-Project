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

    public void setColor(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public GradientPaint getPaint() {
        int rr = getColor().getRed(), gg = getColor().getGreen(), bb = getColor().getBlue();
        int newR, newG, newB;

        if (rr >= 50)
            newR = rr - 50;
        else
            newR = 0;
        if (gg >= 50)
            newG = gg - 50;
        else
            newG = 0;
        if (bb >= 50)
            newB = bb - 50;
        else
            newB = 0;

        if (getType().equals("LeftWall")) {
            return new GradientPaint(cols[0], 0, getColor(), cols[1], 0, new Color(newR, newG, newB));
        } else if (getType().equals("RightWall")) {
            return new GradientPaint(cols[0], 0, getColor(), cols[1], 0, new Color(newR, newG, newB));
        } else if (getType().equals("FloorMain") || getType().equals("CeilMain") || getType().equals("CeilLeft")
                || getType().equals("CeilRight") || getType().equals("FloorLeft") || getType().equals("FloorRight")) {
            return new GradientPaint(0, rows[0], getColor(), 0, rows[1], new Color(newR, newG, newB));
        } else if (getType().equals("LeftPath")) {
            return new GradientPaint(cols[0], 0, getColor(), cols[1], 0, new Color(newR, newG, newB));
        } else if (getType().equals("RightPath")) {
            return new GradientPaint(cols[0], 0, getColor(), cols[1], 0, new Color(newR, newG, newB));
        }
        return new GradientPaint(rows[0], cols[0], new Color(newR, newG, newB), rows[0], cols[0], getColor());
    }

}
