import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Game4 extends Applet implements KeyListener {

 private static final int SIZE = 100;
    private int[][] board = new int[4][4];
    private Font font = new Font("Arial", Font.PLAIN, 20);
    
    private HashMap<Integer, Color> colorLookup = new HashMap<Integer, Color>() {{
        put(0, new Color(0xCCFFFF));
        put(2, new Color(0x99CCFF));
        put(4, new Color(0x3399FF));
        put(8, new Color(0x6666FF));
        put(16, new Color(0xB266FF));
        put(32, new Color(0xFF66B2));
        put(64, new Color(0x404040));
        put(128, new Color(0x003366));
        put(256,  new Color(0x330033));
        put(512,  new Color(0x003319));
        put(1024, new Color(0x003333));
        put(2048, new Color(0x000000));
    }};

    @Override
    public void init() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                board[i][j] = 0;
            }
        }
        resize(board.length * SIZE, board[0].length * SIZE);
        addRandom(2); 
        addKeyListener(this);
    }
	
    private void addRandom(int howMany) {
        ArrayList<Point> available = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == 0) {
                    available.add(new Point(i, j));
                }
            }
        }
        Random r = new Random();
        if (available.isEmpty()) {
            System.out.println("Game Over");
            return;
        }
        for (int i = 0; i < howMany; i++) {

            Point p = available.get(r.nextInt(available.size()));
            // weight the 2 so it shows up more...
            board[(int)p.getX()][(int)p.getY()] = r.nextInt(4) == 1 ? 4 : 2;
            available.remove(p);
        }
    }

    private void move(int dir) {
        if (dir == KeyEvent.VK_UP) {
            for (int i = 0; i < board.length; i++) {
                for (int j = 1; j < board[0].length; j++) {
                    if (board[i][j] == 0 && board[i][j - 1] == 0) {
                        continue;
                    }
                    if (board[i][j] == board[i][j - 1] ) {
                        board[i][j - 1] = 2 * board[i][j - 1];
                        board[i][j] = 0;
                    } else if (board[i][j - 1] == 0) {
                        board[i][j - 1] = board[i][j];
                        board[i][j] = 0;
                        j = 0;
                    }
                }
            }
        } else if (dir == KeyEvent.VK_DOWN) {
            for (int i = 0; i < board.length; i++) {
                for (int j = board[0].length - 2; j >= 0; j--) {
                    if (board[i][j] == 0 && board[i][j + 1] == 0) {
                        continue;
                    }
                    if (board[i][j] == board[i][j + 1] ) {
                        board[i][j + 1] = 2 * board[i][j + 1];
                        board[i][j] = 0;
                    } else if (board[i][j + 1] == 0) {
                        board[i][j + 1] = board[i][j];
                        board[i][j] = 0;
                        j = board[0].length - 1;
                    }
                }
            }
        } else if (dir == KeyEvent.VK_LEFT) {

            for (int j = 0; j < board[0].length; j++) {
                for (int i = 1; i < board.length; i++) {
                    if (board[i][j] == 0 && board[i - 1][j] == 0) {
                        continue;
                    }
                    if (board[i][j] == board[i - 1][j]) {
                        board[i - 1][j] = 2 * board[i - 1][j];
                        board[i][j] = 0;
                    } else if (board[i - 1][j] == 0) {
                        board[i - 1][j] = board[i][j];
                        board[i][j] = 0;
                        i = 0;
                    }
                }
            }
        } else if (dir == KeyEvent.VK_RIGHT) {
            for (int j = 0; j < board[0].length; j++) {
                for (int i = board.length - 2; i >= 0; i--) {
                    if (board[i][j] == 0 && board[i + 1][j] == 0) {
                        continue;
                    }
                    if (board[i][j] == board[i + 1][j]) {
                        board[i + 1][j] = 2 * board[i][j];
                        board[i][j] = 0;
                    } else if (board[i + 1][j] == 0) {
                        board[i + 1][j] = board[i][j];
                        board[i][j] = 0;
                        i = board.length - 1;
                    }
                }
            }
        }

    }

    @Override
    public void paint(Graphics g) {
        g.clearRect(0, 0, board.length, board[0].length);
        FontMetrics fontMetrics = g.getFontMetrics();
        g.setFont(font);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                g.setColor(colorLookup.get(board[i][j]));
                g.fillRoundRect(i * SIZE, j * SIZE, SIZE, SIZE,35,35);
                g.setColor(colorLookup.get(0));
                g.drawRoundRect(i * SIZE, j * SIZE, SIZE, SIZE,35,35);
                g.setColor(new Color(204, 255, 255));
                String num = String.valueOf(board[i][j]);
                int x = (i * SIZE) + (SIZE / 2) - (fontMetrics.stringWidth(num) / 2);
                int y = (j * SIZE) + (SIZE / 2) + (fontMetrics.getAscent() / 2);
                g.drawString(num, x, y);
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP
                || key == KeyEvent.VK_DOWN
                || key == KeyEvent.VK_RIGHT
                || key == KeyEvent.VK_LEFT) {
            move(key);
            addRandom(1);
            repaint();
        }
    }

    public void keyReleased(KeyEvent e) {

    }

    public void keyTyped(KeyEvent e) {

    }
}