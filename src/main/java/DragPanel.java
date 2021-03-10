import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Dimension;
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
    //We can use this to algorithmically upgrade peoples dice
    String[] imagePaths = {"./src/main/resources/img/dice_r1.png","./src/main/resources/img/dice_b1.png","./src/main/resources/img/dice_g1.png","./src/main/resources/img/dice_v1.png",
                            "./src/main/resources/img/dice_c1.png","./src/main/resources/img/dice_o1.png","./src/main/resources/img/dice_p1.png","./src/main/resources/img/dice_w1.png"};
    
    //This guy is kept to calculate die width and height
    ImageIcon img1 = new ImageIcon("./src/main/resources/img/dice_r1.png");
    

    Image imgBoard;

    Player[] gamePlayers;

    
    int WIDTH = img1.getIconWidth();
    int HEIGHT = img1.getIconHeight();
    int PLAYER_INFO_X = 1272;
    int PLAYER_INFO_TOP = 50;
    int PLAYER_INFO_OFFSET = 30;

    //int WIDTH_BOARD = imgBoard.getIconWidth();
    //int WIDTH_HEIGHT = img.getIconHeight();

    Point[] imageCorner = new Point[8];
    Point prevPt;
    DragListener dragListener = new DragListener();
    ClickListener clickListener = new ClickListener(dragListener);
    //player info text boxes
    JLabel currentActivePlayer = new JLabel();
    JLabel activePlayerCash = new JLabel();
    JLabel playerRank = new JLabel();
    JLabel playerRole = new JLabel();
    JLabel playerCredits = new JLabel();
    JLabel playerRehearsalPoints = new JLabel();
    JLabel playerLocation = new JLabel();

    public DragPanel(Board board) throws IOException{

        
        gamePlayers = board.getPlayers();

        //Dimension size = currentActivePlayer.getPreferredSize();

        //setting offsets from board to place active playerInfo
        currentActivePlayer.setBounds(PLAYER_INFO_X, PLAYER_INFO_TOP, 150, 20);
        activePlayerCash.setBounds(PLAYER_INFO_X, PLAYER_INFO_TOP + PLAYER_INFO_OFFSET, 150, 20);
        playerCredits.setBounds(PLAYER_INFO_X, PLAYER_INFO_TOP + PLAYER_INFO_OFFSET*2, 150, 20);
        playerRole.setBounds(PLAYER_INFO_X, PLAYER_INFO_TOP + PLAYER_INFO_OFFSET*3, 150, 20);
        playerRank.setBounds(PLAYER_INFO_X, PLAYER_INFO_TOP + PLAYER_INFO_OFFSET*4, 150, 20);  
        playerRehearsalPoints.setBounds(PLAYER_INFO_X, PLAYER_INFO_TOP + PLAYER_INFO_OFFSET*5, 150, 20);
        playerLocation.setBounds(PLAYER_INFO_X, PLAYER_INFO_TOP + PLAYER_INFO_OFFSET*6, 150, 20);

        for(int i = 0; i < players; i++){
            gamePlayers[i].setPlayerImage(imagePaths[i]);
        }

        imgBoard = ImageIO.read(new File("./src/main/resources/img/board.png"));
        
        for(int i = 0; i < 4; i++){
            imageCorner[i] = new Point(1000 + (i * 40), 300);
        }
        for(int i = 0; i < 4; i++){
            imageCorner[i+4] = new Point(1000 + (i * 40), 340);
        }
        
        //Adding the JLabels that include player data to the JPanel
        this.add(currentActivePlayer);
        this.add(activePlayerCash);
        this.add(playerRank);
        this.add(playerRole);
        this.add(playerCredits);
        this.add(playerRehearsalPoints);
        this.add(playerLocation);

        this.addMouseListener(clickListener);
        this.addMouseMotionListener(dragListener);
        this.setLayout(null); //we want absolute positioning
    }

    public void paintComponent(Graphics g) {
        
        super.paintComponent(g);
        g.drawImage(imgBoard, 0, 0, this);

        int idx = dragListener.currentTileIdx;

        if(idx == -1){
            
            for(int i = 0; i < players; i++){
                ImageIcon currentPlayerDie = gamePlayers[i].getPlayerImage();
                currentPlayerDie.paintIcon(this, g, (int)imageCorner[i].getX(), (int)imageCorner[i].getY());
            }

        } else {

            for(int i = 0; i < players; i++){
                if(idx == i){continue;}
                ImageIcon currentPlayerDie = gamePlayers[i].getPlayerImage();
                currentPlayerDie.paintIcon(this, g, (int)imageCorner[i].getX(), (int)imageCorner[i].getY());
            }
            ImageIcon currentPlayerDie = gamePlayers[idx].getPlayerImage();
            currentPlayerDie.paintIcon(this, g, (int)imageCorner[idx].getX(), (int)imageCorner[idx].getY());
        }

    }

    public class ClickListener extends MouseAdapter {
        
        DragListener dl;

        public void mousePressed(MouseEvent e) {
            
            prevPt = e.getPoint();
            

            //System.out.println(currentPt.getX()+ " " + currentPt.getY());
                        		
            for(int i = 0; i < players; i++){
                if(prevPt.getX() > imageCorner[i].getX() && prevPt.getX() < imageCorner[i].getX() + WIDTH){
                    if(prevPt.getY() > imageCorner[i].getY() && prevPt.getY() < imageCorner[i].getY() + HEIGHT) {

                        
                        currentActivePlayer.setText("Player: " + gamePlayers[i].getId());
                        activePlayerCash.setText("Cash: " + String.valueOf(gamePlayers[i].getMoney()));
                        playerRank.setText("Rank: " + gamePlayers[i].getRank());
                        if(gamePlayers[i].getHasRole()){
                            playerRole.setText("Current Role: " + gamePlayers[i].getRole());
                        } else {
                            playerRole.setText("Current Role: none");
                        }
                        
                        playerCredits.setText("Credits: " + gamePlayers[i].getCredits());
                        playerRehearsalPoints.setText("Rehearsal Points: " + gamePlayers[i].getRehearsalPoints());
                        playerLocation.setText("Location: " + gamePlayers[i].getPos());
                        

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
