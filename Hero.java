import java.awt.Color;
import java.awt.Rectangle;

public class Hero {

    private Location loc;
    private int dir;
    private int size;
    private Color color;

    public Hero(Location loc, int dir, int size, Color color) {
        this.loc = loc;
        this.dir = dir;
        this.size = size;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public Location getLoc() {
        return loc;
    }

    public int getDir() {
        return dir;
    }

    public void move(int key, char[][] maze) {
        int r = getLoc().getR();
        int c = getLoc().getC();
        if (key == 38) { // forward
            if (dir == 0) { // up
                if (r > 0 && maze[r - 1][c] == ' ')
                    getLoc().setR(-1);
            }
            if (dir == 1) { // right
                if (c < maze[0].length - 1 && maze[r][c + 1] == ' ')
                    getLoc().setC(+1);
            }
            if (dir == 2) { // down
                if (r < maze.length - 1 && maze[r + 1][c] == ' ')
                    getLoc().setR(+1);
            }
            if (dir == 3) { // left
                if (c > 0 && maze[r][c - 1] == ' ')
                    getLoc().setC(-1);
            }
        }
        if (key == 37) { // rotate left
            dir--;
            if (dir < 0)
                dir = 3;
        }
        if (key == 39) { // rotate right
            dir++;
            if (dir > 3)
                dir = 0;
            // dir %= 4;
        }
    }

    public Rectangle getRect() {
        int r = loc.getR();
        int c = loc.getC();
        return new Rectangle(c * size + size, r * size + size, size, size); // r,c,size,size
    }
}
