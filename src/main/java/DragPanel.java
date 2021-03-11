import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.awt.Point;
import java.awt.event.MouseEvent;


public class DragPanel extends JPanel implements ActionListener{

    HashMap<String, Integer[][]> approxSetLocs = new HashMap<String, Integer[][]>();
    String[] tileNames = {"Train Station", "Jail", "General Store", "Main Street", "Saloon", "Trailer", "Casting Office", "Ranch", "Secret Hideout", "Bank", "Church", "Hotel"};
    String[] setNames = {"Train Station", "Jail", "General Store", "Main Street", "Saloon", "Ranch", "Secret Hideout", "Bank", "Church", "Hotel"};
    
    int players = Deadwood.playerCount;
    
    int cur;

    double[] previousPlayerLoc = {0, 0};
    
    ImageIcon[] images = new ImageIcon[8];
    ImageIcon[] cardImages = new ImageIcon[10];
    ImageIcon cardBack = new ImageIcon("./src/main/resources/img/cardback.png)");
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
    
    JButton act = new JButton("Act");
    JButton rehearse = new JButton("Rehearse");
    JButton rankUp = new JButton("Rank Up");
    JButton end = new JButton("End Turn");

    Board board;

    public DragPanel(Board boardImp) throws IOException{
        this.board = boardImp;
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
        act.addActionListener(this);
        act.setPreferredSize(new Dimension (200,100));
        act.setVerticalTextPosition(AbstractButton.BOTTOM);
        act.setHorizontalTextPosition(AbstractButton.CENTER);
        act.setBounds(PLAYER_INFO_X, PLAYER_INFO_TOP + PLAYER_INFO_OFFSET*7, 150, 20);
        act.setVisible(false);
        
        add(rehearse);
        rehearse.addActionListener(this);
        rehearse.setPreferredSize(new Dimension (200,100));
        rehearse.setVerticalTextPosition(AbstractButton.BOTTOM);
        rehearse.setHorizontalTextPosition(AbstractButton.CENTER);
        rehearse.setBounds(PLAYER_INFO_X, PLAYER_INFO_TOP + PLAYER_INFO_OFFSET*8, 150, 20);
        rehearse.setVisible(false);
        
        add(rankUp);
        rankUp.addActionListener(this);
        rankUp.setPreferredSize(new Dimension (200,100));
        rankUp.setVerticalTextPosition(AbstractButton.BOTTOM);
        rankUp.setHorizontalTextPosition(AbstractButton.CENTER);
        rankUp.setBounds(PLAYER_INFO_X, PLAYER_INFO_TOP + PLAYER_INFO_OFFSET*9, 150, 20);
        rankUp.setVisible(false);
        
        add(end);
        end.addActionListener(this);
        end.setPreferredSize(new Dimension (200,100));
        end.setVerticalTextPosition(AbstractButton.BOTTOM);
        end.setHorizontalTextPosition(AbstractButton.CENTER);
        end.setBounds(PLAYER_INFO_X, PLAYER_INFO_TOP + PLAYER_INFO_OFFSET*10, 150, 20);
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

    public void renderCards(Graphics g){
        for(int i = 0; i < setNames.length; i++){
            String imagePath = board.locationCardRoleData.get(setNames[i])[0][4];
            int x = board.boardPixelLoc.get(setNames[i])[0];
            int y = board.boardPixelLoc.get(setNames[i])[1];
            int height = board.boardPixelLoc.get(setNames[i])[2];
            int width = board.boardPixelLoc.get(setNames[i])[3];

            cardImages[i] = new ImageIcon("./src/main/resources/img/card_" + imagePath);
            cardImages[i].paintIcon(this, g, x, y);
        }
        
    }
    
    public void fillSetHashMap(){
        
        
        Integer[][] trainLoc = {{6,7},{237,442}};
        approxSetLocs.put(tileNames[0], trainLoc);

        Integer[][] jailLoc = {{266, 6},{591, 234}};
        approxSetLocs.put(tileNames[1], jailLoc);

        Integer[][] generalStoreLoc = {{203, 237},{593, 443}};
        approxSetLocs.put(tileNames[2], generalStoreLoc);

        Integer[][] mainStreetLoc= {{604, 7},{1190,228}};
        approxSetLocs.put(tileNames[3], mainStreetLoc);

        Integer[][] saloonLoc ={{607,202},{972, 438}};
        approxSetLocs.put(tileNames[4], saloonLoc);

        Integer[][] trailerLoc = {{990, 244},{1189, 439}};
        approxSetLocs.put(tileNames[5], trailerLoc);

        Integer[][] castingLoc = {{8, 458},{214, 665}};
        approxSetLocs.put(tileNames[6], castingLoc);

        Integer[][] ranchLoc = {{232, 458},{589, 687}};
        approxSetLocs.put(tileNames[7], ranchLoc);

        Integer[][] hideoutLoc = {{7, 686},{587, 887}};
        approxSetLocs.put(tileNames[8], hideoutLoc);

        Integer[][] bankLoc = {{604, 461},{986, 637}};
        approxSetLocs.put(tileNames[9], bankLoc);

        Integer[][] churchLoc = {{608, 660},{926, 893}};
        approxSetLocs.put(tileNames[10], churchLoc);

        Integer[][] hotelLoc = {{938, 459},{1190, 893}};
        approxSetLocs.put(tileNames[11], hotelLoc);
    }

    public void paintComponent(Graphics g) {
        
        super.paintComponent(g);
        //Image newImage = imgBoard.getScaledInstance(1440, 1080, Image.SCALE_SMOOTH);
        g.drawImage(imgBoard, 0, 0, this);

        int idx = dragListener.currentTileIdx;
        renderCards(g);

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
            System.out.println("In paintComp: x = " + imageCorner[idx].getX() + " y = " + imageCorner[idx].getY());
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
        rankUp.setVisible(true);
        end.setVisible(true);
    }

    public class ClickListener extends MouseAdapter {
        
        DragListener dl;

        public void mousePressed(MouseEvent e) {
            
            prevPt = e.getPoint();
            
            
            System.out.println(prevPt.getX()+ " " + prevPt.getY());
                        		
            for(int i = 0; i < players; i++){
                if(i == board.getTurnNum()){
                    if(prevPt.getX() > imageCorner[i].getX() && prevPt.getX() < imageCorner[i].getX() + WIDTH){
                        if(prevPt.getY() > imageCorner[i].getY() && prevPt.getY() < imageCorner[i].getY() + HEIGHT) {
                            renderPlayerData(i);
                            double[] temp = {imageCorner[i].getX(),imageCorner[i].getY()};
                            previousPlayerLoc = temp;

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
        }

        public void mouseReleased(MouseEvent e) {
            
            if(!dl.isInObject){
                return;
            }
            Point currentPt = e.getPoint();
            
            for(int i = 0; i < tileNames.length; i++) {
                Integer[][] currentSetCheck = approxSetLocs.get(tileNames[i]);
                if(currentPt.getX() > currentSetCheck[0][0] && currentPt.getY() > currentSetCheck[0][1]){
                    if(currentPt.getX() < currentSetCheck[1][0] && currentPt.getY() < currentSetCheck[1][1]) {
                        
                        int idx = dragListener.currentTileIdx;
                        String oldSetLoc = gamePlayers[idx].getPos();
                        boolean isValid = board.validatePlayerMove(oldSetLoc, tileNames[i]);
                        if(isValid){
                            gamePlayers[idx].setPos(tileNames[i]);
                            renderPlayerData(idx);
                            previousPlayerLoc[0] = currentPt.getX();
                            previousPlayerLoc[1] = currentPt.getY();
                        } else {
                            
                            System.out.println(previousPlayerLoc[0] + " " + previousPlayerLoc[1]);
                            imageCorner[idx].move((int)previousPlayerLoc[0], (int)previousPlayerLoc[1]);
                            repaint();
                            return;
                        }
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
    		if(!gamePlayers[cur].getHasRole()) {
				System.out.println("You cannot rehearse without having a role");
			} else {
				gamePlayers[cur].rehearse();
			}
    	}
    	
    	if(e.getSource() == rankUp) {
    		if(!(gamePlayers[cur].getPos().equalsIgnoreCase("Casting Office"))) {
				System.out.println("You must go to the casting office to rank up.");
			} else {
				JFrame ranks = new JFrame("ranks");
				ranks.setSize(500,500);
				ranks.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				ranks.setVisible(true);
			}
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
