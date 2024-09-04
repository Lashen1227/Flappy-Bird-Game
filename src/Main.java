import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) throws Exception{
        int frameWidth = 800;
        int frameHeight = 600;

        JFrame frame = new JFrame("Flappy Bird Game");

        frame.setSize(frameWidth,frameHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FlappyBird  flappybird = new FlappyBird();
        frame.add(flappybird);
        frame.pack();

        flappybird.requestFocus();
        frame.setVisible(true);
    }
}
