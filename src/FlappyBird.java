import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 800;
    int boardHeight = 600;

    Image backgroundImage;
    Image birdImage1;
    Image birdImage2;
    Image topPipeImage;
    Image bottomPipeImage;

    int bird_X = boardWidth / 8;
    int bird_Y = boardHeight / 2;
    int birdHeight = 24;
    int birdWidth = 34;

    class Bird {
        int x = bird_X;
        int y = bird_Y;
        int height = birdHeight;
        int width = birdWidth;
        Image img;

        Bird(Image img) {
            this.img = img;
        }
    }

    int pipe_X = boardWidth;
    int pipe_Y = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    class Pipe {
        int x = pipe_X;
        int y = pipe_Y;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        Pipe(Image img) {
            this.img = img;
        }
    }

    Bird bird;
    int velocityY = 0;
    int velocityX = -4;
    int gravity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameLoop;
    Timer placePipesTimer;
    Timer birdAnimationTimer;

    boolean gameOver = false;
    double score = 0;

    boolean birdFlap = true;

    public FlappyBird() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.blue);

        setFocusable(true);
        addKeyListener(this);

        backgroundImage = new ImageIcon(getClass().getResource("assets/background.png")).getImage();
        birdImage1 = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        birdImage2 = new ImageIcon(getClass().getResource("assets/bird2.png")).getImage();
        topPipeImage = new ImageIcon(getClass().getResource("assets/top_pipe.png")).getImage();
        bottomPipeImage = new ImageIcon(getClass().getResource("assets/bottom_pipe.png")).getImage();

        bird = new Bird(birdImage1);
        pipes = new ArrayList<>();

        placePipesTimer = new Timer(1500, (ActionEvent e) -> {
            placePipes();
        });
        placePipesTimer.start();

        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();

        birdAnimationTimer = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameOver) {
                    bird.img = birdFlap ? birdImage1 : birdImage2;
                    birdFlap = !birdFlap;
                }
            }
        });
        birdAnimationTimer.start();
    }

    public void placePipes() {
        int randomPipeY = (int) (pipe_Y - pipeHeight / 4 - Math.random() * (pipeHeight / 2));
        int openSpace = boardHeight / 4;

        Pipe topPipe = new Pipe(topPipeImage);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImage);
        bottomPipe.y = topPipe.y + pipeHeight + openSpace;
        pipes.add(bottomPipe);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, boardWidth, boardHeight, null);
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        g.setColor(Color.BLUE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Score: " + (int) score, 10, 30);
        g.drawString("Use Space key to control", 310, 30);

        if (gameOver) {
            g.setColor(Color.RED);
            g.drawString("Game Over.", 320, boardHeight / 2);
            g.drawString("Press ENTER To Restart", 225, boardHeight / 2 + 30);
        }
    }

    public void move() {
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                pipe.passed = true;
                score += 0.5;
            }
            if (collision(bird, pipe)) {
                gameOver = true;
            }
        }
        if (bird.y > boardHeight) {
            gameOver = true;
        }
    }

    public boolean collision(Bird bird, Pipe pipe) {
        return bird.x < pipe.x + pipe.width &&
                bird.x + bird.width > pipe.x &&
                bird.y < pipe.y + pipe.height &&
                bird.y + bird.height > pipe.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            placePipesTimer.stop();
            gameLoop.stop();
            birdAnimationTimer.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -9;
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (gameOver) {
                bird.y = bird_Y;
                velocityY = 0;
                pipes.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                placePipesTimer.start();
                birdAnimationTimer.start();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 300, Short.MAX_VALUE)
        );
    }
}
