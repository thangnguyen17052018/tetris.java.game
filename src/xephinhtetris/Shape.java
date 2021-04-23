package xephinhtetris;

import java.awt.Color;
import java.awt.Graphics;
import static xephinhtetris.Board.BLOCK_SIZE;
import static xephinhtetris.Board.Board_HEIGHT;

/**
 *
 * @author Nguyen Minh Thang B1805816
 */
public class Shape {
    
    private int x = 4, y = 0; //Toa do cua shape
    private int normal = 600;
    private int fast = 50;
    private int delayTimeForMovement = normal;     
    private long beginTime;    
   
    private int delX = 0;
    private boolean collision = false; //kiem tra va cham (cham day  (bottom), cham shape),khi cham thi board se hieu la dung shape va khoi tao shape moi.
    
    private int[][] coords; //bang vi tri toa do x, y
    private Board board;
    private Color color;
   
    
    public Shape(int[][] coords, Board board, Color color){
        this.coords = coords;
        this.board = board;
        this.color = color;
        delayTimeForMovement = normal;
        delX = 0;
        x = 4;
        y = 0;
    }
    public void setX(int x){
        this.x = x;
    }
    
    public void setY(int y){
        this.y = y;
    }
    
    public void reset(){
        this.x = 4;
        this.y = 0;
        collision = false;
    }
    
    public void update(){
        if (collision){ //Neu cham bottom thi dung shape lai
            //to mau cho board
            for (int row = 0; row < coords.length; row++){
                for (int col = 0; col < coords[0].length; col++){
                    if (coords[row][col] != 0){     
                        board.getBoard()[y + row][x + col] = color;
                    }
                }
            }
            checkLine();
            board.addScore();
            //Goi Shape moi 
            board.setCurrentShape();
            return;  
        }
        
        boolean moveX = true;
        //Kiem tra chuyen dong ngang (tranh truong hop shape chay ra khoi Board)
        if (!(x + delX + coords[0].length > 10) && !(x + delX < 0)){
           for (int row = 0; row < coords.length; row++){
                for (int col = 0; col < coords[row].length; col++){
                    if (coords[row][col] != 0){ 
                        if (board.getBoard()[y + row][x + delX + col] != null){
                            moveX = false;
                        }
                    }
                }
            }
           if (moveX){
            x += delX; //do ngang bang vi tri x cua shape                    
           }
        } 
        delX = 0;   

        //chuyen dong doc        
        if (System.currentTimeMillis() - beginTime > delayTimeForMovement){ //Khi ma thoi gian vuot qua thoi gian delay cho phep thi y moi do xuong              
            if (!(y + 1 + coords.length > Board_HEIGHT)){ //tranh truong hop do xuong khoi board //y+1 de check truong hop neu no di them 1 buoc se va cham
                for (int row = 0; row < coords.length; row++){
                    for (int col = 0; col < coords[row].length; col++){
                        if (coords[row][col] != 0){
                            if (board.getBoard()[y + 1 + row][x + delX + col] != null){
                                collision = true;
                            }
                        }
                    }
               }
               if (!collision){ 
                    y++; //do xuong bang vi tri y cua shape
               }
            } else {
                collision = true;
            }

            beginTime = System.currentTimeMillis(); //Danh dau mot moc thoi gian tai vi tri dat cau lenh
        }
    }    
    
    private void checkLine(){
        int bottomLine = board.getBoard().length - 1;
        for (int topLine = board.getBoard().length - 1; topLine > 0; topLine--){
            int count = 0;
            for (int col = 0; col < board.getBoard()[0].length; col++){
                if (board.getBoard()[topLine][col] != null){
                    count++;
                }
                board.getBoard()[bottomLine][col] = board.getBoard()[topLine][col];             
            }
            if (count < board.getBoard()[0].length){
                bottomLine--;
            }
        }        
    }
    
    public void rotateShape(){
        int rotatedShape[][] = tranposeMatrix(coords);
        reverseRows(rotatedShape);
        //kiem tra ben trai va ben phai board (chong tran)
        if ((x + rotatedShape[0].length > Board.Board_WIDTH)||(y + rotatedShape.length > Board.Board_HEIGHT)){
            return;
        }
        ///kiem tra va cham truoc khi xoay shape
        for (int row = 0; row < rotatedShape.length; row++){
            for (int col = 0; col < rotatedShape[row].length; col++){
                if (rotatedShape[row][col] != 0){
                    if (board.getBoard()[y + row][x + col] != null){
                        return;
                    }
                }
            }
        }
        coords  = rotatedShape;
    }
     
    private int[][] tranposeMatrix(int matrix[][]){
        int temp[][] = new int[matrix[0].length][matrix.length];
        for (int row = 0; row < matrix.length; row++){
            for (int col = 0; col < matrix[0].length; col++){
                temp[col][row] = matrix[row][col];
            }
        }
    return temp;
    }
    
    private void reverseRows(int matrix[][]){
        int middle = matrix.length / 2 ;
        for (int row = 0; row < middle; row++){
            int temp[] = matrix[row];
            matrix[row] = matrix[matrix.length - row - 1];
            matrix[matrix.length - row - 1] = temp;            
        }
    }
    
    public void render(Graphics g){ //ket xuat
         // Ve shape
        for (int row = 0; row < coords.length; row++){
            for (int col = 0; col < coords[0].length; col++){
                if (coords[row][col] != 0){
                    g.setColor(color);
                    g.fillRect(col * BLOCK_SIZE + x*BLOCK_SIZE, row * BLOCK_SIZE + y*BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }
    }
    
    public Color getColor(){
        return this.color;
    }
    
    public void speedUp(){
        delayTimeForMovement = fast;
    }
    
    public void speedDown(){
        delayTimeForMovement = normal;
    }
    
    public void moveRight(){
        delX = 1;
    }
    
    public void moveLeft(){
        delX = -1;
    }
    
    public int[][] getCoords(){
        return coords;
    }
    
    public int getY(){
        return y;
    }
    public int getX(){
        return x;
    }
}
