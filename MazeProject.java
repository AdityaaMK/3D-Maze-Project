import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.awt.Polygon;

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
    int r = 1, c = 0;
    int initialDir = 1;
    int moves = 0;
    int sensors = 15;
    boolean sensorDropped;
    char[][] sensorWallLocations = new char[numRows][numCols];
    int mapSize = 3;

    public MazeProject() {

        hero = new Hero(new Location(r, c), initialDir, size, Color.RED, moves, 3, 100, false);
        setBoard();
        frame = new JFrame("Adityaa's Maze");
        frame.add(this);
        frame.setSize(1400, 800);
        frame.addKeyListener(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
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

        if (!draw3D && !gameWon()) {
            g2.setColor(Color.GRAY);
            for (int c = 0; c < maze[0].length; c++)
                for (int r = 0; r < maze.length; r++)
                    if (maze[r][c] == ' ')
                        g2.fillRect(c * size + size, r * size + size, size, size);
                    else
                        g2.drawRect(c * size + size, r * size + size, size, size);

            g2.setColor(hero.getColor());
            g2.fill(hero.getRect());
        } else if (!gameWon()) {
            boolean sensorRightBelow = true;
            for (int w = 0; w < walls.size(); w++) {
                for (int c = 0; c < sensorWallLocations[0].length; c++)
                    for (int r = 0; r < sensorWallLocations.length; r++)
                        if (walls.get(w).getType().equals("FloorMain") && sensorWallLocations[r][c] == '#'
                                && hero.getLoc().getR() == r && hero.getLoc().getC() == c && sensorRightBelow) {
                            walls.get(w).setColor(0, 255, 0);
                            sensorRightBelow = false;
                        }
                g2.setPaint(walls.get(w).getPaint());
                Polygon temp = new Polygon(walls.get(w).getCols(), walls.get(w).getRows(), walls.get(w).getNumSides());
                g2.fillPolygon(temp);
                g2.setPaint(Color.BLACK);
                g2.drawPolygon(temp);
            }
            g2.setColor(Color.WHITE);
            for (int c = 0; c < maze[0].length; c++)
                for (int r = 0; r < maze.length; r++)
                    if (maze[r][c] == '#' && hero.getLoc().getDistance(new Location(r, c)) < mapSize)
                        g2.fillRect(c * 10 + 770, r * 10 + 200, 10, 10);
            g2.setColor(hero.getColor());
            g2.fillOval(hero.getLoc().getC() * 10 + 770, hero.getLoc().getR() * 10 + 200, 10, 10);

            g.setColor(Color.WHITE);
            g.setFont(new Font("TimesRoman", Font.BOLD, 12));
            g.drawString("* Press Delete to Quit", 1200, 35);
            g.drawString("* Press A for Flashlight", 1200, 50);
            g.drawString("* Press Down Arrow to Drop Sensor", 1200, 65);

            g.drawString("Moves: " + hero.getMoves(), 1200, 640);

            if (hero.getFlash() && hero.getBattery() != 0)
                g.drawString("Flashlight: On", 1200, 670);
            else
                g.drawString("Flashlight: Off", 1200, 670);

            g.drawString("Battery: " + hero.getBattery() + "%", 1200, 700);

            g.drawString("Visiblity: " + hero.getVisibleDistance() + " Spaces", 1200, 730);

            g.drawString("Sensors Remaining: " + sensors, 1200, 760);
        } else if (gameWon()) {
            g.setColor(Color.WHITE);
            Font font = new Font("TimesRoman", Font.BOLD, 40);
            g.setFont(font);
            g.drawString("CONGRATS! YOU BEAT THE MAZE!", 175, 400);
            g.drawString("FINISHED WITH " + hero.getMoves() + " MOVES!", 175, 500);
        }

    }

    public boolean gameWon() {
        return (hero.getLoc().getR() == 16 && hero.getLoc().getC() == 40);
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

    public void setFloorsandCeilings() {
        for (int i = 0; i < 5; i++) {
            if (sensorDropped) {
                sensorWallLocations[hero.getLoc().getR()][hero.getLoc().getC()] = '#';
                sensorDropped = false;
                sensors--;
            }
            int[] rLocs = { 700 - (shrink * i), 650 - (shrink * i), 650 - (shrink * i), 700 - (shrink * i) };
            int[] cLocs = { 750 - (shrink * i), 700 - (shrink * i), 100 + (shrink * i), 50 + (shrink * i) };
            walls.add(new Wall(rLocs, cLocs, 255 - shrink * (i), 255 - shrink * (i), 255 - shrink * (i), size,
                    "FloorMain"));
        }
        for (int i = 0; i < 5; i++) {
            int[] rLocs = { 150 + (shrink * i), 100 + (shrink * i), 100 + (shrink * i), 150 + (shrink * i) };
            int[] cLocs = { 750 - (shrink * i), 700 - (shrink * i), 100 + (shrink * i), 50 + (shrink * i) };
            walls.add(new Wall(rLocs, cLocs, 255 - shrink * (i), 255 - shrink * (i), 255 - shrink * (i), size,
                    "CeilMain"));
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

    public void keyReleased(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        if (gameWon())
            return;

        hero.move(e.getKeyCode(), maze);
        // System.out.println(hero.getDir());

        if (e.getKeyCode() == 32)
            draw3D = !draw3D;
        if (draw3D)
            createWalls();

        if (e.getKeyCode() == 40 && sensors != 0)
            sensorDropped = true;

        if (e.getKeyCode() == 65 && hero.getBattery() > 0) {
            hero.toggleFlash();
            mapSize = hero.getVisibleDistance();
        }

        if (e.getKeyCode() == 8) {
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    int option = JOptionPane.showConfirmDialog(frame, "Too hard? Better Luck Next Time", "Game Over",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (option == JOptionPane.DEFAULT_OPTION)
                        System.exit(0);
                }
            });
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }

        repaint();
    }

    public void keyTyped(KeyEvent e) {

    }

    public static void main(String[] args) {
        MazeProject maze = new MazeProject();
    }
}
