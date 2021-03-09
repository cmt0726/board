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
    

    ImageIcon img1 = new ImageIcon("./src/main/resources/img/dice_b1.png");
    ImageIcon img2 = new ImageIcon("./src/main/resources/img/dice_b2.png");
    ImageIcon img3 = new ImageIcon("./src/main/resources/img/dice_b3.png");
    ImageIcon img4 = new ImageIcon("./src/main/resources/img/dice_b4.png");
    ImageIcon img5 = new ImageIcon("./src/main/resources/img/dice_b5.png");
    ImageIcon img6 = new ImageIcon("./src/main/resources/img/dice_b6.png");
    Image imgBoard; 
    
    int WIDTH = img1.getIconWidth();
    int HEIGHT = img1.getIconHeight();

    //int WIDTH_BOARD = imgBoard.getIconWidth();
    //int WIDTH_HEIGHT = img.getIconHeight();

    Point[] imageCorner = new Point[6];
    Point prevPt;

    public DragPanel() throws IOException{

        imgBoard = ImageIO.read(new File("./src/main/resources/img/board.png"));
        
        for(int i = 0; i < 6; i++){
            imageCorner[i] = new Point(0 + (i * 40), 0);
        }
        
        
        DragListener dragListener = new DragListener();
        ClickListener clickListener = new ClickListener(dragListener);
        this.addMouseListener(clickListener);
        this.addMouseMotionListener(dragListener);

    }

    


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(imgBoard, 0, 0, this);
        img1.paintIcon(this, g, (int)imageCorner[0].getX(), (int)imageCorner[0].getY());
        img2.paintIcon(this, g, (int)imageCorner[1].getX(), (int)imageCorner[1].getY());
        img3.paintIcon(this, g, (int)imageCorner[2].getX(), (int)imageCorner[2].getY());
        img4.paintIcon(this, g, (int)imageCorner[3].getX(), (int)imageCorner[3].getY());
        img5.paintIcon(this, g, (int)imageCorner[4].getX(), (int)imageCorner[4].getY());
        img6.paintIcon(this, g, (int)imageCorner[5].getX(), (int)imageCorner[5].getY());
        //imgBoard.paintIcon(this, g, (int)imageCorner.getX(), (int)imageCorner.getY());
    }

    public class ClickListener extends MouseAdapter {
        
        DragListener dl;

        public void mousePressed(MouseEvent e) {
            
            prevPt = e.getPoint();
            

            //System.out.println(currentPt.getX()+ " " + currentPt.getY());

            for(int i = 0; i < 6; i++){
                if(prevPt.getX() > imageCorner[i].getX() && prevPt.getX() < imageCorner[i].getX() + WIDTH){
                    if(prevPt.getY() > imageCorner[i].getY() && prevPt.getY() < imageCorner[i].getY() + HEIGHT) {
                        
                        dl.setIsInObject(true);
                        dl.setTileIdx(i);
                        return;
                    } else {
                        dl.setIsInObject(false);
                    }
                    
                } else {
                    dl.setIsInObject(false);
                }
            }
        }

        public ClickListener(DragListener dlnative) {
            dl = dlnative;
        }


        
    }

    public class DragListener extends MouseMotionAdapter{
        public boolean isInObject = true;
        int currentTileIdx;
        public void mouseDragged(MouseEvent e) {

            if(!isInObject){
                return;
            }
            
            Point currentPt = e.getPoint();

            //System.out.println(currentPt.getX()+ " " + currentPt.getY());

            // for(int i = 0; i < 6; i++){
            //     if(currentPt.getX() > imageCorner[i].getX() && currentPt.getX() < imageCorner[i].getX() + WIDTH){
            //         if(currentPt.getY() > imageCorner[i].getY() && currentPt.getY() < imageCorner[i].getY() + HEIGHT) {
                        
                        imageCorner[this.currentTileIdx].translate(
                            (int)(currentPt.getX() - prevPt.getX()),
                            (int)(currentPt.getY() - prevPt.getY())
                        );
                        prevPt = currentPt;
            //         } 
                    
            //     } 
            // }
            
            repaint();
        }

        public void setIsInObject(boolean statement){
            this.isInObject = statement;
        }

        public void setTileIdx(int i){
            this.currentTileIdx = i;
        }

    }
}
