import javax.swing.*;


public class Main {
    public static void main(String[] args) {

        int boardwidth = 360;
        int boardheight = 640;

        JFrame frame = new JFrame("Flappy Bird");

        frame.setSize(boardwidth,boardheight);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        flappybird flappybird = new flappybird();
        frame.add(flappybird);
        frame.pack();
        flappybird.requestFocus();
        frame.setVisible(true);
        }
    }
