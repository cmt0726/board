import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;
import java.awt.Point;
import java.awt.event.MouseEvent;

public class DragPanel extends JPanel{
    
    ImageIcon img = new ImageIcon("C:/Users/conno/git/team_constrictor_345-21wi/src/main/resources/img/dice_b1.png");
    Image imgBoard; //= new ImageIcon("C:/Users/conno/team_constrictor_345-21wi/src/main/resources/img/board.png");
    int WIDTH = img.getIconWidth();
    int HEIGHT = img.getIconHeight();

    //int WIDTH_BOARD = imgBoard.getIconWidth();
    //int WIDTH_HEIGHT = img.getIconHeight();

    Point imageCorner;
    Point prevPt;

    public DragPanel() throws IOException{

        imgBoard = ImageIO.read(new File("C:/Users/conno/git/team_constrictor_345-21wi/src/main/resources/img/board.png"));
        
        imageCorner = new Point(10, 10);
        ClickListener clickListener = new ClickListener();
        DragListener dragListener = new DragListener();
        this.addMouseListener(clickListener);
        this.addMouseMotionListener(dragListener);
    }

    


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(imgBoard, 0, 0, this);
        img.paintIcon(this, g, (int)imageCorner.getX(), (int)imageCorner.getY());
        
        //imgBoard.paintIcon(this, g, (int)imageCorner.getX(), (int)imageCorner.getY());
    }

    private class ClickListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            prevPt = e.getPoint();
        }
    }

    private class DragListener extends MouseMotionAdapter{
        
        public void mouseDragged(MouseEvent e) {
            Point currentPt = e.getPoint();


            if(currentPt.getX() > imageCorner.getX() && currentPt.getX() < imageCorner.getX() + WIDTH){
                if(currentPt.getY() > imageCorner.getY() && currentPt.getY() < imageCorner.getY() + HEIGHT) {
                    imageCorner.translate(
                        (int)(currentPt.getX() - prevPt.getX()),
                        (int)(currentPt.getY() - prevPt.getY())
                    );
                    prevPt = currentPt;
                }
                
            }
            

            
            repaint();
        }

    }
}
