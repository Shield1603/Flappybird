import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class flappybird extends JPanel implements ActionListener, KeyListener {
    int boardwidth= 360;
    int boardheihgt= 640;


    Image backgroundImg;
    Image birdimg;
    Image topPipeimg;
    Image BottomPipeimg;


    int birdx = boardwidth/8;
    int birdy = boardheihgt/2;
    int birdWidth = 34;
    int birdHeight = 24;



    class Bird{
    int x = birdx;
    int y = birdy;
    int width= birdWidth;
    int height= birdHeight;
    Image img;

    Bird(Image img){
        this.img=img;
    }
}

//pipes
    int pipex= boardwidth;
    int pipey = 0;
    int pipwidth = 64;
    int pipeheight = 512;

    class Pipe{
        int x= pipex;
        int y = pipey;
        int width = pipwidth;
        int height = pipeheight;
        Image img;
        boolean passed = false;

    Pipe(Image img){
        this.img= img;
    }

    }

//game logic
Bird bird;
int VelocityX = -4;
int velocityY = 0;
int gravity = 1;

ArrayList<Pipe>pipes;
Random random = new Random();

Timer gameloop;
Timer Placepipestimer;
boolean gameOver = false;
double score = 0;

    flappybird(){
        setPreferredSize(new Dimension(boardwidth,boardheihgt));
        //setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);
        //load images
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdimg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeimg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        BottomPipeimg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();




        //bird
        bird = new Bird(birdimg);
        pipes = new ArrayList<Pipe>();

        //place piper timer
        Placepipestimer  = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placepipes();
            }
        });
        Placepipestimer.start();

        //game timer

        gameloop= new Timer(1000/60,this);
        gameloop.start();

    }

    public void placepipes(){
        int randomPipeY = (int)(pipey - pipeheight/4 - Math.random()*(pipeheight/2));
        int openingspace = boardheihgt/4;

        Pipe topPipe = new Pipe (topPipeimg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(BottomPipeimg);
        bottomPipe.y = topPipe.y + pipeheight + openingspace;
        pipes.add(bottomPipe);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){

        //background
        g.drawImage(backgroundImg,0,0,boardwidth,boardheihgt,null);

        //bird
        g.drawImage(bird.img,bird.x,bird.y,bird.width,bird.height,null);

        for(int i = 0; i<pipes.size();i++){
            Pipe Pipe = pipes.get(i);
            g.drawImage(Pipe.img,Pipe.x,Pipe.y,Pipe.width,Pipe.height,null);
        }

        //score

        g.setColor(Color.white);
        g.setFont(new Font("Arial",Font.PLAIN,32));
        if(gameOver){
            g.drawString("Game Over:" + String.valueOf((int)score),10,35);
        }
        else{
            g.drawString(String.valueOf((int)score),10,35);
        }
    }

    public void move(){
        //bird
        velocityY+=gravity;
        bird.y += velocityY;
        bird.y= Math.max(bird.y,0);

        //pipes
        for(int i = 0; i < pipes.size();i++){
            Pipe pipe = pipes.get(i);
            pipe.x += VelocityX;


        if(!pipe.passed && bird.x > pipe.x + pipe.width){

            pipe.passed = true;
            score += 0.5;

        }
            if(collision(bird, pipe)){
                gameOver = true;
            }
        }

        if (bird.y>boardheihgt){
            gameOver= true;
        }
    }

    public boolean collision(Bird a, Pipe b){
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y  < b.y + b.height &&
                a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            Placepipestimer.stop();
            gameloop.stop();
        }
    }




    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE){
            velocityY=-9;
            if(gameOver){
                //restart
                bird.y=birdy;
                velocityY=0;
                pipes.clear();
                score=0;
                gameOver=false;
                gameloop.start();
                Placepipestimer.start();
            }
        }

    }


    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}


}
