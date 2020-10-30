import java.awt.Color;
import java.awt.Rectangle;

public class Hero {

    private Location loc;
    private int dir;
    private int size;
    private int moves;
    private Color color;
    private int visibleDistance;
    private int battery;
    private boolean flashStatus;

    public Hero(Location loc, int dir, int size, Color color, int moves, int visibleDistance, int battery,
            boolean flashStatus) {
        this.loc = loc;
        this.dir = dir;
        this.size = size;
        this.color = color;
        this.moves = moves;
        this.visibleDistance = visibleDistance;
        this.battery = battery;
        this.flashStatus = flashStatus;
    }

    public int getVisibleDistance() {
        return visibleDistance;
    }

    public int getBattery() {
        return battery;
    }

    public boolean getFlash() {
        return flashStatus;
    }

    public void toggleFlash() {
        if (flashStatus) {
            flashStatus = false;
        } else if (battery > 0) {
            flashStatus = true;
            useBattery(2);
        }

        if (visibleDistance == 3 && battery > 50) {
            visibleDistance = 5;
        } else if (visibleDistance == 3 && battery > 0) {
            visibleDistance = 4;
        } else if (visibleDistance == 4 || visibleDistance == 5) {
            visibleDistance = 3;
        }
    }

    public void useBattery(int usage) {
        battery -= usage;
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

    public int getMoves() {
        return moves;
    }

    public void move(int key, char[][] maze) {
        int r = getLoc().getR();
        int c = getLoc().getC();
        if (key == 38) { // forward
            moves++;
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
            moves++;
            dir--;
            if (dir < 0)
                dir = 3;
        }
        if (key == 39) { // rotate right
            moves++;
            dir++;
            if (dir > 3)
                dir = 0;
            // dir %= 4;
        }
    }

    public Rectangle getRect() {
        int r = loc.getR();
        int c = loc.getC();
        return new Rectangle(c * size + size, r * size + size, size, size);
    }
}
