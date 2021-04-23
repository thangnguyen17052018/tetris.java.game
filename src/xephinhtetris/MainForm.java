package xephinhtetris;

//import java.awt.event.KeyListener;
import javax.swing.JFrame;

/**
 *
 * @author Nguyen Minh Thang B1805816
 */
public class MainForm {
    
    public static final int WIDTH=445, HEIGHT=628;

    private Board board;
    //private Title title;
    private JFrame window;
    private Title title;
    
    public MainForm(){
        window = new JFrame("XepHinhTetris");
        window.setSize(WIDTH, HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Dat hanh dong mac dinh se xay ra khi nguoi dung dong FRAME.Dong toan bo cac frame lien quan toi no   
        window.setResizable(false);//Dam bao chac chan rang game window k the doi size
        window.setLocationRelativeTo(null);//Frame xuat hien chinh giua man hinh
        
        board = new Board();
        title = new Title(this);
        
        
        
        window.addKeyListener(board);
        window.addKeyListener(title);
        window.add(title);
        
        window.setVisible(true);
    }
    
     public void startTetris() {
        window.remove(title);
        window.addMouseMotionListener(board);
        window.addMouseListener(board);
        window.add(board);
        board.startGame();
        window.revalidate();//thay đổi một thuộc tính sẽ ảnh hưởng đến chiều rộng / chiều cao của chúng
    }
    
    public static void main (String args[]){
        MainForm f = new MainForm();
      
    }
    
}
