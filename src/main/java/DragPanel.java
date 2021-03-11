import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.awt.Point;
import java.awt.event.MouseEvent;


public class DragPanel extends JPanel{

    HashMap<String, Integer[][]> approxSetLocs = new HashMap<String, Integer[][]>();
    String[] setNames = {"Train Station", "Jail", "General Store", "Main Street", "Saloon", "Trailer", "Casting Office", "Ranch", "Secret Hideout", "Bank", "Church", "Hotel"};
    
    int players = Deadwood.playerCount;
    int cur;
    
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
    
    JButton rehearse = new JButton("Rehearse");
    JButton act = new JButton("Act");
    JButton end = new JButton("End Turn");

    public DragPanel(Board board) throws IOException{

        //fill aproxSetLocs hashmap
        fillSetHashMap();
        
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
        
        add(act);
        act.addActionListener(null);
        act.setPreferredSize(new Dimension (200,100));
        act.setVerticalTextPosition(AbstractButton.BOTTOM);
        act.setHorizontalTextPosition(AbstractButton.CENTER);
        act.setBounds(PLAYER_INFO_X, PLAYER_INFO_TOP + PLAYER_INFO_OFFSET*7, 150, 20);
        act.setVisible(false);
        
        add(rehearse);
        rehearse.addActionListener(null);
        rehearse.setPreferredSize(new Dimension (200,100));
        rehearse.setVerticalTextPosition(AbstractButton.BOTTOM);
        rehearse.setHorizontalTextPosition(AbstractButton.CENTER);
        rehearse.setBounds(PLAYER_INFO_X, PLAYER_INFO_TOP + PLAYER_INFO_OFFSET*8, 150, 20);
        rehearse.setVisible(false);
        
        add(end);
        end.addActionListener(null);
        end.setPreferredSize(new Dimension (200,100));
        end.setVerticalTextPosition(AbstractButton.BOTTOM);
        end.setHorizontalTextPosition(AbstractButton.CENTER);
        end.setBounds(PLAYER_INFO_X, PLAYER_INFO_TOP + PLAYER_INFO_OFFSET*9, 150, 20);
        end.setVisible(false);
        
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
    
    public void fillSetHashMap(){
        
        
        Integer[][] trainLoc = {{6,7},{237,442}};
        approxSetLocs.put(setNames[0], trainLoc);

        Integer[][] jailLoc = {{266, 6},{591, 234}};
        approxSetLocs.put(setNames[1], jailLoc);

        Integer[][] generalStoreLoc = {{203, 237},{593, 443}};
        approxSetLocs.put(setNames[2], generalStoreLoc);

        Integer[][] mainStreetLoc= {{604, 7},{1190,228}};
        approxSetLocs.put(setNames[3], mainStreetLoc);

        Integer[][] saloonLoc ={{607,202},{972, 438}};
        approxSetLocs.put(setNames[4], saloonLoc);

        Integer[][] trailerLoc = {{990, 244},{1189, 439}};
        approxSetLocs.put(setNames[5], trailerLoc);

        Integer[][] castingLoc = {{8, 458},{214, 665}};
        approxSetLocs.put(setNames[6], castingLoc);

        Integer[][] ranchLoc = {{232, 458},{589, 687}};
        approxSetLocs.put(setNames[7], ranchLoc);

        Integer[][] hideoutLoc = {{7, 686},{587, 887}};
        approxSetLocs.put(setNames[8], hideoutLoc);

        Integer[][] bankLoc = {{604, 461},{986, 637}};
        approxSetLocs.put(setNames[9], bankLoc);

        Integer[][] churchLoc = {{608, 660},{926, 893}};
        approxSetLocs.put(setNames[10], churchLoc);

        Integer[][] hotelLoc = {{938, 459},{1190, 893}};
        approxSetLocs.put(setNames[11], hotelLoc);
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

    public void renderPlayerData(int i){
        //setting text for the Jlabels to render
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

        act.setVisible(true);
        rehearse.setVisible(true);
        end.setVisible(true);
    }

    public class ClickListener extends MouseAdapter {
        
        DragListener dl;

        public void mousePressed(MouseEvent e) {
            
            prevPt = e.getPoint();
            
            System.out.println(prevPt.getX()+ " " + prevPt.getY());
                        		
            for(int i = 0; i < players; i++){
                if(prevPt.getX() > imageCorner[i].getX() && prevPt.getX() < imageCorner[i].getX() + WIDTH){
                    if(prevPt.getY() > imageCorner[i].getY() && prevPt.getY() < imageCorner[i].getY() + HEIGHT) {
                    	
                    	cur = i;
                        
                        
                        
                        renderPlayerData(i);

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

        public void mouseReleased(MouseEvent e) {
            if(!dl.isInObject){
                return;
            }
            Point currentPt = e.getPoint();

            for(int i = 0; i < setNames.length; i++) {
                Integer[][] currentSetCheck = approxSetLocs.get(setNames[i]);
                if(currentPt.getX() > currentSetCheck[0][0] && currentPt.getY() > currentSetCheck[0][1]){
                    if(currentPt.getX() < currentSetCheck[1][0] && currentPt.getY() < currentSetCheck[1][1]) {
                        //System.out.println("Current set: " + setNames[i]);
                        int idx = dragListener.currentTileIdx;
                        gamePlayers[idx].setPos(setNames[i]);
                        renderPlayerData(idx);
                    }
                }
            }

        }

        public ClickListener(DragListener dlnative) {
            dl = dlnative;
        }


        
    }
    
    public void actionPerformed(ActionEvent e) {
    	if(e.getSource() == rehearse) {
    		gamePlayers[cur].rehearse();
    		
    	}
    }

    

    public class DragListener extends MouseMotionAdapter{

        public boolean isInObject = true;
        int currentTileIdx = -1;
        
        public void mouseDragged(MouseEvent e) {
            Point currentPt = e.getPoint();

            

            if(!isInObject){
                return;
            }
            
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

        public void detectPlayerLocation(){

        }

    }
}
