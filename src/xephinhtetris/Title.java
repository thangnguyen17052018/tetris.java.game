package xephinhtetris;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Title extends JPanel implements KeyListener, ActionListener {

    private static final long serialVersionUID = 1L;
    private BufferedImage instructions, keyboards;
    private MainForm window;
    private BufferedImage[] playButton = new BufferedImage[2];
    private Timer timer;
    private static int id;
    private long highScore;
    private String username = new String();

    public Title(MainForm window){
            try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String dbUrl= "jdbc:sqlserver://THANGKY1705\\SQLEXPRESS:1433;databaseName=highscore;user=mylogin;password=mylogin";
            Connection con= DriverManager.getConnection(dbUrl);
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("Select MAX(id),username,score From PLAYER\n" +"group by id,score,username");
          
            while(rs.next()){
                    System.out.println("id: "+rs.getInt(1));
                    id = rs.getInt(1) +1;
                    username = rs.getString("username");
                    highScore = rs.getLong("score");
            }
           
            con.close();
        } catch (Exception ex){
            ex.printStackTrace();
        }
            instructions = ImageLoader.loadImage("/ok.jpg");
            keyboards = ImageLoader.loadImage("/key.png");
            timer = new Timer(1000/60, new ActionListener(){

                    @Override
                    public void actionPerformed(ActionEvent e) {
                            repaint();
                    }

            });
            timer.start();
//            TextField txtUsername;
//            txtUsername = new TextField();
//            txtUsername.setBounds(50, 120, 200, 30);
//            this.add(txtUsername);
//            this.setLayout(null);
            this.window = window;           
    }

    public void paintComponent(Graphics g){
            super.paintComponent(g);

//		g.setColor(Color.BLACK);
//		
//		g.fillRect(0, 0, MainForm.WIDTH, MainForm.HEIGHT);
        

            g.drawImage(instructions, 0, 0, null);
            g.drawImage(keyboards, 90, 240, null);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Consolas", Font.BOLD, 25));
            g.drawString("Nhấn SPACE để vào game!", 45, MainForm.HEIGHT - 80);
            
            g.setColor(Color.WHITE);
            g.setFont(new Font("Consolas", Font.BOLD, 20));
            g.drawString("Điểm cao nhất: ", MainForm.WIDTH / 2 - 100 , 50);
            
            g.setColor(Color.WHITE);
            g.setFont(new Font("Consolas", Font.BOLD, 20));
            g.drawString(username + " - " + highScore, MainForm.WIDTH / 2 - 90 , 80);
            
            


    }	

    @Override
    public void keyTyped(KeyEvent e) {
        if(e.getKeyChar() == KeyEvent.VK_SPACE) {
            window.startTetris();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void actionPerformed(ActionEvent ae) {
            
       
    }
}
