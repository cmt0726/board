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
    int players = Deadwood.playerCount;
    
    ImageIcon[] images = new ImageIcon[8];

    ImageIcon img1 = new ImageIcon("./src/main/resources/img/dice_r1.png");
    ImageIcon img2 = new ImageIcon("./src/main/resources/img/dice_b1.png");
    ImageIcon img3 = new ImageIcon("./src/main/resources/img/dice_g1.png");
    ImageIcon img4 = new ImageIcon("./src/main/resources/img/dice_v1.png");
    ImageIcon img5 = new ImageIcon("./src/main/resources/img/dice_c1.png");
    ImageIcon img6 = new ImageIcon("./src/main/resources/img/dice_o1.png");
    ImageIcon img7 = new ImageIcon("./src/main/resources/img/dice_p1.png");
    ImageIcon img8 = new ImageIcon("./src/main/resources/img/dice_w1.png");

    Image imgBoard; //= new ImageIcon("C:/Users/conno/team_constrictor_345-21wi/src/main/resources/img/board.png");

    
    int WIDTH = img1.getIconWidth();
    int HEIGHT = img1.getIconHeight();

    //int WIDTH_BOARD = imgBoard.getIconWidth();
    //int WIDTH_HEIGHT = img.getIconHeight();

    Point[] imageCorner = new Point[8];
    Point prevPt;
    DragListener dragListener = new DragListener();
    ClickListener clickListener = new ClickListener(dragListener);

    public DragPanel() throws IOException{
        images[0] = img1; images[1] = img2; images[2] = img3; images[3] = img4; images[4] = img5; images[5] = img6; images[6] = img7; images[7] = img8;


        imgBoard = ImageIO.read(new File("./src/main/resources/img/board.png"));
        
        for(int i = 0; i < 4; i++){
            imageCorner[i] = new Point(1000 + (i * 40), 300);
        }
        for(int i = 0; i < 4; i++){
            imageCorner[i+4] = new Point(1000 + (i * 40), 340);
        }
        
        
        
        this.addMouseListener(clickListener);
        this.addMouseMotionListener(dragListener);

    }

    public void paintComponent(Graphics g) {
        
        super.paintComponent(g);
        g.drawImage(imgBoard, 0, 0, this);

        int idx = dragListener.currentTileIdx;

        if(idx == -1){

            for(int i = 0; i < players; i++){
                images[i].paintIcon(this, g, (int)imageCorner[i].getX(), (int)imageCorner[i].getY());
            }

        } else {

            for(int i = 0; i < players; i++){
                if(idx == i){continue;}
                images[i].paintIcon(this, g, (int)imageCorner[i].getX(), (int)imageCorner[i].getY());
            }
            images[idx].paintIcon(this, g, (int)imageCorner[idx].getX(), (int)imageCorner[idx].getY());
        }

        
        //imgBoard.paintIcon(this, g, (int)imageCorner.getX(), (int)imageCorner.getY());
    }

    public class ClickListener extends MouseAdapter {
        
        DragListener dl;

        public void mousePressed(MouseEvent e) {
            
            prevPt = e.getPoint();
            

            //System.out.println(currentPt.getX()+ " " + currentPt.getY());
                        		
            for(int i = 0; i < players; i++){
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
        int currentTileIdx = -1;
        
        public void mouseDragged(MouseEvent e) {

            if(!isInObject){
                return;
            }
            
            Point currentPt = e.getPoint();
                        
            imageCorner[this.currentTileIdx].translate(
                (int)(currentPt.getX() - prevPt.getX()),
                (int)(currentPt.getY() - prevPt.getY())
            );
            prevPt = currentPt;
            
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
