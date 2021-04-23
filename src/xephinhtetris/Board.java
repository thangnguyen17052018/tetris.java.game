/*
 Board ~~ Game State
 */
package xephinhtetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;


/**
 *
 * @author Nguyen Minh Thang B1805816
 *  //    Image background = Toolkit.getDefaultToolkit().createImage("image\\background.jpg");
//        g.drawImage(background, 0, 0, null);

 */
public class Board extends JPanel implements KeyListener, MouseListener, MouseMotionListener{
    public static int STATE_GAME_PLAY = 0;
    public static int STATE_GAME_PAUSE = 1;
    public static int STATE_GAME_OVER = 2;
    
    private BufferedImage refresh, pause;
    private int score = 0;
    private int state = STATE_GAME_PLAY;
    
    private static int FPS = 60;
    private static int delayTime =  1000 / FPS;
    
    public static final int Board_WIDTH = 10;
    public static final int Board_HEIGHT = 20;
    public static final int BLOCK_SIZE = 30;
 
    private Timer looper;
    private Color[][] board = new Color[Board_HEIGHT][Board_WIDTH]; //chieu dai board phai la so row cua coord
    private Color[] colors = {Color.decode("#f31111"), Color.decode("#0e267d"), Color.decode("#196a47"), 
        Color.decode("#f7347a"), Color.decode("#ccff00"), Color.decode("#b452cd"), Color.decode("#ff8c00")};
  
    /*
        Mang shape nghia la: 
        -Do dai cua mang chinh la so BLOCK
        -To mang tai moi BLOCK de tao ra SHAPE tuong ung
    */
    private Timer gameloop;
    
    private Shape[] shapes = new Shape[7];
    private Shape currentShape, nextShape;
    private Random random;
    
    private int mouseX, mouseY;
    private boolean leftClick = false;
    private Rectangle stopBounds, refreshBounds;
    
    private Timer buttonLapse = new Timer(300, new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            buttonLapse.stop();
        }
    });



    
    public Board() {
        pause = ImageLoader.loadImage("/pause.png");
        refresh = ImageLoader.loadImage("/refresh.png");
        
        mouseX = 0;
        mouseY = 0;

        stopBounds = new Rectangle(350, 500, pause.getWidth(), pause.getHeight() + pause.getHeight() / 2);
        refreshBounds = new Rectangle(350, 470 - refresh.getHeight() - 20, refresh.getWidth(), refresh.getHeight() + refresh.getHeight() / 2);
        
        random = new Random();
        
 
        
        shapes[0] = new Shape(new int[][]{ 
            {1, 1, 1, 1} // I shape;
        }, this, colors[0]); // public Shape(int[][] coords, Board board, Color color){

        shapes[1] = new Shape(new int[][]{
            {1, 1, 1},
            {0, 1, 0}, // T shape;
        }, this, colors[1]);

        shapes[2] = new Shape(new int[][]{
            {1, 1, 1},
            {1, 0, 0}, // L shape;
        }, this, colors[2]);

        shapes[3] = new Shape(new int[][]{
            {1, 1, 1},
            {0, 0, 1}, // J shape;
        }, this, colors[3]);

        shapes[4] = new Shape(new int[][]{
            {0, 1, 1},
            {1, 1, 0}, // S shape;
        }, this, colors[4]);

        shapes[5] = new Shape(new int[][]{
            {1, 1, 0},
            {0, 1, 1}, // Z shape;
        }, this, colors[5]);

        shapes[6] = new Shape(new int[][]{
            {1, 1},
            {1, 1}, // O shape;
        }, this, colors[6]);
        
        currentShape = shapes[random.nextInt(6)];
        
        //FPS ~ Frame Per Second
        looper = new Timer(delayTime, new ActionListener() { //500 = 0,5 seconds
            //int n = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
                repaint();//paintComponent se recall va screen se duoc refresh,thay đổi một thuộc tính sẽ ảnh hưởng đến sự xuất hiện của chúng
            }
        });
        
        //looper.start();
    }
    
    private void update(){        
        if (state == STATE_GAME_PLAY){
            currentShape.update();
        }
        if (stopBounds.contains(mouseX, mouseY) && leftClick && !buttonLapse.isRunning() && state == STATE_GAME_PLAY) {
            buttonLapse.start();
            state = STATE_GAME_PAUSE; 
        }
        
        if (stopBounds.contains(mouseX, mouseY) && leftClick && !buttonLapse.isRunning() && state == STATE_GAME_PAUSE) {
            buttonLapse.start();
            state = STATE_GAME_PLAY; 
        }
        
        if (refreshBounds.contains(mouseX, mouseY) && leftClick) {
            //renew board
            startGame();
        }
        
          

        if (state==STATE_GAME_PAUSE || state==STATE_GAME_OVER) {
            return;
        }
        currentShape.update();
    }
    
    public void setCurrentShape(){
        currentShape = nextShape;
        setNextShape();
        currentShape.reset();//Khoi tao lai vi tri cua shape and gan va cham bang false
        checkOverGame();
    }
    
    private void checkOverGame(){
        int coords[][] = currentShape.getCoords();
        for (int row = 0; row < coords.length; row++){
            for (int col = 0; col < coords[0].length; col++){
                if (coords[row][col] != 0){
                    if (board[row + currentShape.getY()][col + currentShape.getX()] != null){
                        state = STATE_GAME_OVER;
                    }
                }
            }
        } 
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
//        Color customColor = new Color(140, 99, 164);
//        g.setColor(customColor);
//        g.fillRect(0, 0, getWidth(), getHeight());
        BufferedImage background;        
        background = ImageLoader.loadImage("/oktrang1.png");
		
		
	g.drawImage(background, 0, 0, null);
        
        g.setColor(nextShape.getColor());
        for (int row = 0; row < nextShape.getCoords().length; row++) {
            for (int col = 0; col < nextShape.getCoords()[0].length; col++) {
                if (nextShape.getCoords()[row][col] != 0) {
                    g.fillRect(col * 30 + 320, row * 30 + 50, Board.BLOCK_SIZE, Board.BLOCK_SIZE);
                }
            }
        }
        currentShape.render(g);
        
        currentShape.render(g); //ket xuat Shape
       
        for (int row = 0; row < board.length; row++){
                for (int col = 0; col < board[0].length; col++){
                    if (board[row][col] != null){     
                        g.setColor(board[row][col]);
                        g.fillRect(col * BLOCK_SIZE,row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                    }
                }
            }
         
        // Ve board
        g.setColor(Color.decode("#999999"));     
        for (int row = 0; row < Board_HEIGHT; row++){           
            g.drawLine(0, BLOCK_SIZE * row, BLOCK_SIZE * Board_WIDTH, BLOCK_SIZE * row);
        }
        
        for (int col = 0; col <= Board_WIDTH; col++){
            g.drawLine(col * BLOCK_SIZE, 0, col * BLOCK_SIZE, BLOCK_SIZE * Board_HEIGHT);
        }
        
        if (state == STATE_GAME_OVER){
            g.setColor(Color.decode("#111111"));
            g.setFont(new Font("Consolas", Font.BOLD, 30));
            g.drawString("THUA CUỘC",50, MainForm.HEIGHT / 2);
        }
        
        if (state == STATE_GAME_PAUSE){
            g.setColor(Color.decode("#111111"));         
            g.setFont(new Font("Consolas", Font.BOLD, 30));
            g.drawString("TẠM DỪNG", 70, MainForm.HEIGHT / 2);
        }
        
        //tao stop button
        if (stopBounds.contains(mouseX, mouseY)) {
            g.drawImage(pause.getScaledInstance(pause.getWidth() + 3, pause.getHeight() + 3, BufferedImage.SCALE_DEFAULT), stopBounds.x + 3, stopBounds.y + 3, null);
        } else {
            g.drawImage(pause, stopBounds.x, stopBounds.y, null);
        }
         
        //Tao refresh button
        if (refreshBounds.contains(mouseX, mouseY)) {
            g.drawImage(refresh.getScaledInstance(refresh.getWidth() + 3, refresh.getHeight() + 3,
                    BufferedImage.SCALE_DEFAULT), refreshBounds.x + 3, refreshBounds.y + 3, null);
        } else {
            g.drawImage(refresh, refreshBounds.x, refreshBounds.y, null);
        }

        
        //DIEM
         g.setColor(Color.decode("#111111")); 
        g.setFont(new Font("Consolas", Font.BOLD, 20));
        g.drawString("ĐIỂM SỐ", MainForm.WIDTH - 125, MainForm.HEIGHT / 2);
        g.drawString(score + "", MainForm.WIDTH - 100, MainForm.HEIGHT / 2 + 30);

        
    }
    
    public void setNextShape() {
        int index = random.nextInt(shapes.length);
        int colorIndex = random.nextInt(colors.length);
        nextShape = new Shape(shapes[index].getCoords(), this, colors[colorIndex]);
    }
    
    public Color[][] getBoard(){
    return this.board;
    }
    
    public void startGame() {
        score = 0;
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                board[row][col] = null;
            }
        }
        looper.stop();
        setNextShape();
        setCurrentShape();
        state = STATE_GAME_PLAY;
        looper.start();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()== KeyEvent.VK_DOWN || e.getKeyCode()== KeyEvent.VK_S){
            currentShape.speedUp();
        } else if (e.getKeyCode()== KeyEvent.VK_RIGHT || e.getKeyCode()== KeyEvent.VK_D){
                currentShape.moveRight();
        } else if (e.getKeyCode()== KeyEvent.VK_LEFT || e.getKeyCode()== KeyEvent.VK_A){
                currentShape.moveLeft();
        } else if (e.getKeyCode()== KeyEvent.VK_UP || e.getKeyCode()== KeyEvent.VK_W){
                currentShape.rotateShape();
        }
        
        if (state == STATE_GAME_OVER){
            if (e.getKeyCode()== KeyEvent.VK_SPACE){
                //renew board
                for (int row = 0; row < board.length; row++){
                    for (int col = 0; col < board[row].length; col++){
                        board[row][col] = null;
                    }
                }
                score = 0;
                setCurrentShape();
                state = STATE_GAME_PLAY;
            }
        }
        
        
        if (e.getKeyCode()== KeyEvent.VK_SPACE){  
            if (state == STATE_GAME_PAUSE){
                state = STATE_GAME_PLAY;
            } else if (state == STATE_GAME_PLAY){
                state = STATE_GAME_PAUSE;
            }   
           
        }

    }
    

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S){
            currentShape.speedDown();
        }
    }
    
    
    
    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftClick = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftClick = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void addScore() {
        score++;
    }
}
