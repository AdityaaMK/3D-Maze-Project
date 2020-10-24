import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.awt.Polygon;
import java.awt.GradientPaint;

public class MazeProject extends JPanel implements KeyListener {

    JFrame frame;
    int numRows = 18;
    int numCols = 41;
    int size = 20;
    char[][] maze = new char[numRows][numCols];
    Hero hero;
    ArrayList<Wall> walls;
    boolean draw3D = false;
    int shrink = 50;
    Image wallImg;
    int r = 1, c = 0;

    public MazeProject() {

        hero = new Hero(new Location(r, c), 1, size, Color.RED);
        setBoard();
        frame = new JFrame("Adityaa's Maze");
        frame.add(this);
        frame.setSize(1200, 800);
        frame.addKeyListener(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // for (int c = 0; c < maze[0].length; c++) {
        // for (int rr = 0; rr < maze.length; rr++) {
        // System.out.println(maze[rr][c]);
        // }
        // }
    }

    public void setBoard() {
        File name = new File("maze.txt");

        try {
            BufferedReader input = new BufferedReader(new FileReader(name));

            String text;
            int r = 0;
            while ((text = input.readLine()) != null) {
                for (int c = 0; c < text.length(); c++)
                    maze[r][c] = text.charAt(c);
                r++;
            }

        } catch (IOException io) {
            System.out.println("File error");
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g); // giant eraser
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, frame.getWidth(), frame.getHeight()); // fill background black

        if (!draw3D) {
            g2.setColor(Color.GRAY);
            for (int c = 0; c < maze[0].length; c++)
                for (int r = 0; r < maze.length; r++)
                    if (maze[r][c] == ' ')
                        g2.fillRect(c * size + size, r * size + size, size, size);
                    else
                        g2.drawRect(c * size + size, r * size + size, size, size);

            g2.setColor(hero.getColor());
            g2.fill(hero.getRect());
        } else {
            for (Wall w : walls) {
                GradientPaint paint = new GradientPaint(0, 0, w.getColor(), 0, 0, Color.BLACK);
                g2.setPaint(paint);
                Polygon temp = new Polygon(w.getCols(), w.getRows(), w.getNumSides());
                g2.fillPolygon(temp);
                g2.setPaint(Color.BLACK);
                g2.drawPolygon(temp);
            }
        }

    }

    public void createWalls() {

        walls = new ArrayList<Wall>();

        setFloorsandCeilings();

        int rr = hero.getLoc().getR();
        int cc = hero.getLoc().getC();
        int dir = hero.getDir();

        for (int n = 0; n < 5; n++) {
            walls.add(getLeftPath(n));
            walls.add(getRightPath(n));
        }

        boolean oneFront;
        switch (dir) {
            case 0: // up
                oneFront = true;
                for (int n = 0; n < 5; n++) {
                    try {
                        if (oneFront) {
                            if (maze[rr - n][cc - 1] == '#') {
                                walls.add(getLeft(n));
                            } else {
                                walls.add(getFloorLeft(n + 1));
                                walls.add(getCeilingLeft(n + 1));
                            }
                            if (maze[rr - n][cc + 1] == '#') {
                                walls.add(getRight(n));
                            } else {
                                walls.add(getFloorRight(n + 1));
                                walls.add(getCeilingRight(n + 1));
                            }
                            if (maze[rr - n][cc] == '#') {
                                walls.add(getFront(n));
                                oneFront = false;
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {

                    }
                }
                break;
            case 1: // right
                oneFront = true;
                for (int n = 0; n < 5; n++) {
                    try {
                        if (oneFront) {
                            if (maze[rr - 1][cc + n] == '#') {
                                walls.add(getLeft(n));
                            } else {
                                walls.add(getFloorLeft(n + 1));
                                walls.add(getCeilingLeft(n + 1));
                            }
                            if (maze[rr + 1][cc + n] == '#') {
                                walls.add(getRight(n));
                            } else {
                                walls.add(getFloorRight(n + 1));
                                walls.add(getCeilingRight(n + 1));
                            }
                            if (maze[rr][cc + n] == '#') {
                                walls.add(getFront(n));
                                oneFront = false;
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {

                    }
                }
                break;
            case 2: // down
                oneFront = true;
                for (int n = 0; n < 5; n++) {
                    try {
                        if (oneFront) {
                            if (maze[rr + n][cc + 1] == '#') {
                                walls.add(getLeft(n));
                            } else {
                                walls.add(getFloorLeft(n + 1));
                                walls.add(getCeilingLeft(n + 1));
                            }
                            if (maze[rr + n][cc - 1] == '#') {
                                walls.add(getRight(n));
                            } else {
                                walls.add(getFloorRight(n + 1));
                                walls.add(getCeilingRight(n + 1));
                            }
                            if (maze[rr + n][cc] == '#') {
                                walls.add(getFront(n));
                                oneFront = false;
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {

                    }
                }
                break;
            case 3: // left
                oneFront = true;
                for (int n = 0; n < 5; n++) {
                    try {
                        if (oneFront) {
                            if (maze[rr + 1][cc - n] == '#') {
                                walls.add(getLeft(n));
                            } else {
                                walls.add(getFloorLeft(n + 1));
                                walls.add(getCeilingLeft(n + 1));
                            }
                            if (maze[rr - 1][cc - n] == '#') {
                                walls.add(getRight(n));
                            } else {
                                walls.add(getFloorRight(n + 1));
                                walls.add(getCeilingRight(n + 1));
                            }
                            if (maze[rr][cc - n] == '#') {
                                walls.add(getFront(n));
                                oneFront = false;
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {

                    }
                }
                break;
        }
    }

    public Wall getLeft(int n) // trapezoid
    {
        int[] rLocs = { 100 + shrink * n, 150 + shrink * n, 650 - shrink * n, 700 - shrink * n };
        int[] cLocs = { 100 + shrink * n, 150 + shrink * n, 150 + shrink * n, 100 + shrink * n };
        return new Wall(rLocs, cLocs, 255 - shrink * n, 255 - shrink * n, 255 - shrink * n, size, "LeftWall");
    }

    public Wall getRight(int n) // trapezoid
    {
        int[] rLocs = { 100 + shrink * n, 150 + shrink * n, 650 - shrink * n, 700 - shrink * n };
        int[] cLocs = { 700 - shrink * n, 650 - shrink * n, 650 - shrink * n, 700 - shrink * n };
        return new Wall(rLocs, cLocs, 255 - shrink * n, 255 - shrink * n, 255 - shrink * n, size, "RightWall");
    }

    public Wall getFront(int n) // trapezoid
    {
        int[] rLocs = { 100 + 50 * n, 100 + 50 * n, 700 - 50 * n, 700 - 50 * n };
        int[] cLocs = { 100 + 50 * n, 700 - 50 * n, 700 - 50 * n, 100 + 50 * n };
        return new Wall(rLocs, cLocs, 255 - shrink * n, 255 - shrink * n, 255 - shrink * n, size, "FrontWall");
    }

    public Wall getLeftPath(int n) // rectangle
    {
        int[] rLocs = { 100 + shrink * n, 100 + shrink * n, 700 - shrink * n, 700 - shrink * n };
        int[] cLocs = { 50 + shrink * n, 100 + shrink * n, 100 + shrink * n, 50 + shrink * n };
        return new Wall(rLocs, cLocs, 255 - shrink * n, 255 - shrink * n, 255 - shrink * n, size, "LeftPath");
    }

    public Wall getRightPath(int n) // rectangle
    {
        int[] rLocs = { 100 + shrink * n, 100 + shrink * n, 700 - shrink * n, 700 - shrink * n };
        int[] cLocs = { 750 - shrink * n, 700 - shrink * n, 700 - shrink * n, 750 - shrink * n };
        return new Wall(rLocs, cLocs, 255 - shrink * n, 255 - shrink * n, 255 - shrink * n, size, "RightPath");
    }

    public Wall getFloorLeft(int n) // triangle
    {
        int[] rLocs = { 750 - shrink * n, 700 - shrink * n, 700 - shrink * n };
        int[] cLocs = { 50 + shrink * n, 50 + shrink * n, 100 + shrink * n };
        return new Wall(rLocs, cLocs, 255 - shrink * (n - 1), 255 - shrink * (n - 1), 255 - shrink * (n - 1), size,
                "FloorLeft");
    }

    public Wall getCeilingLeft(int n) // triangle
    {
        int[] rLocs = { 50 + shrink * n, 100 + shrink * n, 100 + shrink * n };
        int[] cLocs = { 50 + shrink * n, 50 + shrink * n, 100 + shrink * n };
        return new Wall(rLocs, cLocs, 255 - shrink * (n - 1), 255 - shrink * (n - 1), 255 - shrink * (n - 1), size,
                "CeilLeft");
    }

    public Wall getFloorRight(int n) // triangle
    {
        int[] rLocs = { 750 - shrink * n, 700 - shrink * n, 700 - shrink * n };
        int[] cLocs = { 750 - shrink * n, 750 - shrink * n, 700 - shrink * n };
        return new Wall(rLocs, cLocs, 255 - shrink * (n - 1), 255 - shrink * (n - 1), 255 - shrink * (n - 1), size,
                "FloorRight");
    }

    public Wall getCeilingRight(int n) // triangle
    {
        int[] rLocs = { 50 + shrink * n, 100 + shrink * n, 100 + shrink * n };
        int[] cLocs = { 750 - shrink * n, 750 - shrink * n, 700 - shrink * n };
        return new Wall(rLocs, cLocs, 255 - shrink * (n - 1), 255 - shrink * (n - 1), 255 - shrink * (n - 1), size,
                "CeilRight");
    }

    public void setFloorsandCeilings() {
        for (int i = 0; i < 5; i++) {
            int[] cLocs = { 750 - (shrink * i), 700 - (shrink * i), 100 + (shrink * i), 50 + (shrink * i) };
            int[] rLocs = { 150 + (shrink * i), 100 + (shrink * i), 100 + (shrink * i), 150 + (shrink * i) };
            walls.add(new Wall(rLocs, cLocs, 255 - shrink * (i), 255 - shrink * (i), 255 - shrink * (i), size,
                    "CeilMain"));
        }

        for (int i = 0; i < 5; i++) {
            int[] cLocs = { 750 - (shrink * i), 700 - (shrink * i), 100 + (shrink * i), 50 + (shrink * i) };
            int[] rLocs = { 700 - (shrink * i), 650 - (shrink * i), 650 - (shrink * i), 700 - (shrink * i) };
            walls.add(new Wall(rLocs, cLocs, 255 - shrink * (i), 255 - shrink * (i), 255 - shrink * (i), size,
                    "FloorMain"));
        }
    }

    public void keyReleased(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {

        hero.move(e.getKeyCode(), maze);
        // System.out.println(hero.getDir());

        if (e.getKeyCode() == 32)
            draw3D = !draw3D;
        if (draw3D)
            createWalls();

        repaint();
    }

    public void keyTyped(KeyEvent e) {

    }

    public static void main(String[] args) {
        MazeProject maze = new MazeProject();
    }
}
