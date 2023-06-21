package Finalproject;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.*;


public class ShootingGame extends JFrame {
    private List<Target> targets;
    private int score;
    private int shotsFired;

    public ShootingGame() {
        targets = new ArrayList<>();
        score = 0;
        shotsFired = 0;

        setSize(500, 500);
        setTitle("Shooting Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel gamePanel = new JPanel() {
            public void paintComponent(Graphics gc) {
                super.paintComponent(gc);
                gc.setColor(Color.WHITE);
                gc.fillRect(0, 0, getWidth(), getHeight());

                for (Target target : targets) {
                    target.draw(gc);
                }

                gc.setColor(Color.BLUE);
                int bulletRadius = 5;
                for (Target target : targets) {
                    target.draw(gc);
                }
                gc.fillOval(bulletX - bulletRadius, bulletY - bulletRadius, 2 * bulletRadius, 2 * bulletRadius);

                gc.setColor(Color.BLACK);
                gc.drawString("Score: " + score, 20, 40);
                gc.drawString("Shots Fired: " + shotsFired, 20, 60);
            }
        };

        gamePanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                bulletX = e.getX();
                bulletY = e.getY();
                shotsFired++;
                checkHitTargets();
                gamePanel.repaint();
            }
        });

        getContentPane().add(gamePanel);

        Thread gameThread = new Thread(() -> {
            while (true) {
                generateNewTargets();
                updateTargets();
                removeExpiredTargets();
                checkGameStatus();
                gamePanel.repaint();

                try {
                    Thread.sleep(10);
                } catch (InterruptedException sleep) {
                    sleep.printStackTrace();
                }
            }
        });

        gameThread.start();
    }

    private void generateNewTargets() {
        if (targets.size() < 8) {
            int x = (int) (Math.random() * (getWidth() - 40));
            int y = (int) (Math.random() * (getHeight() - 40));
            Target target = new Target(x, y);
            targets.add(target);
        }
    }

    private void updateTargets() {
        for (Target target : targets) {
            target.move();
        }
    }

    private void removeExpiredTargets() {
        Iterator<Target> iter = targets.iterator();
        while (iter.hasNext()) {
            Target target = iter.next();
            if (target.isExpired()) {
                iter.remove();
            }
        }
    }

    public void checkHitTargets() {
        Iterator<Target> iterator = targets.iterator();
        while (iterator.hasNext()) {
            Target target = iterator.next();
            if (target.isHit(bulletX, bulletY)) {
                score++;
                iterator.remove();
                break;
            }
        }
    }

    private void checkGameStatus() {
        if (score >= 10) {
            JOptionPane.showMessageDialog(this, "Congratulations! You win!");
            System.exit(0);
        } else if (shotsFired >= 20) {
            JOptionPane.showMessageDialog(this, "Game over! You lose!");
            System.exit(0);
        }
    }

    private static int bulletX;
    private static int bulletY;

    class Target {
        private int x;
        private int y;
        private int speedX;
        private int speedY;
        private int size;
        private long createTime;
        private static final int MAX_SPEED = 3;
        private static final int MIN_SIZE = 20;
        private static final int MAX_SIZE = 40;
        private static final int MAX_LIFETIME = 5000;

        public Target(int x, int y) {
            this.x = x;
            this.y = y;
            this.size = (int) (Math.random() * (MAX_SIZE - MIN_SIZE) + MIN_SIZE);
            this.createTime = System.currentTimeMillis();

            int directionX = Math.random() < 0.5 ? -1 : 1;
            int directionY = Math.random() < 0.5 ? -1 : 1;
            this.speedX = (int) (Math.random() * MAX_SPEED) * directionX;
            this.speedY = (int) (Math.random() * MAX_SPEED) * directionY;
        }

        public void move() {
            x += speedX;
            y += speedY;
            checkBoundary();
        }

        public void checkBoundary() {
            if (x <= 0 || x >= getWidth() - size) {
                speedX = -speedX;
            }
            if (y <= 0 || y >= getHeight() - size) {
                speedY = -speedY;
            }
        }

        public boolean isHit(int bulletX, int bulletY) {
            int centerX = x + size / 2;
            int centerY = y + size / 2;
            return Math.sqrt(Math.pow(bulletX - centerX, 2) + Math.pow(bulletY - centerY, 2)) <= size / 2;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() - createTime >= MAX_LIFETIME;
        }

        public void draw(Graphics gc) {
            gc.setColor(Color.PINK);
            gc.fillOval(x, y, size, size);
        }
    }

    public static void main(String[] args) {
        new ShootingGame().setVisible(true);
    }
}



