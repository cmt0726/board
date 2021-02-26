package javaSrc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.HashMap;

public class Board {
    
    private int[][] playerPosition;
    private int[][] boardPosition;
    private ArrayList role;
    private ArrayList scene;
    private int day;
    public xmlParser xml = new xmlParser();

    private String[] trailerNeighbors = {"Main Street", "Saloon", "Hotel"};
    private String[] officeNeighbors = {"Train Station", "Ranch", "Secret Hideout"};
    private String[] setNames = {"Train Station", "Secret Hideout", "Church", "Hotel", "Main Street", "Jail", "General Store", "Ranch", "Bank", "Saloon"};
    private List<String> traiNeiList = Arrays.asList(trailerNeighbors);
    private List<String> offNeiList = Arrays.asList(trailerNeighbors);

    public HashMap<Integer, String[][]> cardData = xml.card.cardsData;
    public HashMap<String, String[][]> locationRoleData = xml.set.locationRoleData;
    public HashMap<String, String[][]> locationCardRoleData= xml.set.locationCardRoleData;
    public HashMap<String, String[]> neighbors = xml.set.neighbors;

    private Player[] players;
    private int totalPlayerCount;
    public EndDay endday = new EndDay(this); //passing the Board object into EndDay
    
    public Board(int playerCount) throws Exception{
       
    	Scanner sc = new Scanner(System.in);

        totalPlayerCount = playerCount;
        players = new Player[playerCount];
        for(int i = 0; i < playerCount; i++){
            players[i] = new Player();
            System.out.println("Player name :");
            players[i].setId(sc.next());
        }
        if(playerCount <= 3) {
            endday.setDayLimit(3);
        } else {
            endday.setDayLimit(4);
        }

        if(playerCount == 5){
            for(Player p : players)
                p.setCredits(2);
        } else if(playerCount == 6){
            for(Player p : players)
                p.setCredits(4);
        } else if(playerCount >= 7){
            for(Player p : players) {
                p.setCredits(4);
                p.setRank(2);
            }
        }
        totalPlayerCount = playerCount;

        

    }

    public int getDay(){ return this.day;}

    //getScene
    //getPlayerPos
    
    public int[][] getBoardPosition(){return this.boardPosition;}

    //changePlayerPos
    //setScene
    //setPlayerPosition


    /*
        This function will loop through all players displaying what actions can be taken
        It will then execute those actions
        If an action is one that is turn ending it sets the hasMoreAction flag to false
        and the result variable to "exit"

        if you can do more actions, then it will continue to do so for that player

        I've decided that Act, Move, Endturn, and Exit are turn ending but that is arbitrary

        if you add an action that is turn ending, remember to set the flag to false
        and that case is lowercase

        Currently with 3 players, for 3 days, if every player acts and ends the turn
        this function will end up looping through the userInput 9 times, which is correct
    */
    public String executeDay(){
        
        Scanner sc = new Scanner(System.in);
        String res = "";
        
        //HashMap<String, Integer> sceneShotCounter = xml.set.sceneShotCounter;

        xml.set.generateSceneCards();

        for(int curTurnIdx = 0; curTurnIdx < totalPlayerCount; curTurnIdx++){
            Player currentPlayer = players[curTurnIdx];
            Boolean hasMoreAction = true;
            currentPlayer.setHasMoved(false);
            //TODO implement a system that calcualtes available actions
            //currently this just says you can do anything
            

            while(hasMoreAction){
            	System.out.println(currentPlayer.getId());
                Boolean playerHasAvailableRole = false;
                System.out.println("Current Available Actions: Act, Rehearse, Move, RankUp, EndTurn, exit, Active Player");
                res = sc.nextLine();
                

                System.out.println("--> " + res);
                String[] commands = res.split(" ");
                switch(commands[0].toLowerCase()) {
                    case "act":



                        if(!(currentPlayer.getHasRole())){
                            System.out.println("You must pick a role first from these options:");
                            //System.out.println(currentPlayer.getPos());
                            String[][] currentRoleDataOffCard = locationRoleData.get(currentPlayer.getPos());
                            String[][] currentRoleDataOnCard = locationCardRoleData.get(currentPlayer.getPos());
                            
                            System.out.println("Off card available roles: ");
                            for(String[] role : currentRoleDataOffCard){
                                if(role[2].equals("False") && (Integer.parseInt(role[1]) <= currentPlayer.getRank())){
                                    playerHasAvailableRole = true;
                                    System.out.print(role[0] + " -level-: " + role[1] + " ");
                                }
                            }

                            System.out.println("\nOn card available roles: ");
                            
                            for(String[] role : currentRoleDataOnCard){
                                if(role[3].equals("False") && (Integer.parseInt(role[1]) <= currentPlayer.getRank())){
                                    playerHasAvailableRole = true;
                                    System.out.print(role[0] + " -level-: " + role[1] + " ");
                                }
                            }

                            if(!playerHasAvailableRole){
                                System.out.println("You can't work at this set, you must rankup!");
                                break;
                            }
                            
                            System.out.println("\nChoose one of those Roles");
                            String roleSelection = sc.nextLine();

                            for(int i = 0; i < currentRoleDataOffCard.length; i++){
                                if(currentRoleDataOffCard[i][0].equals(roleSelection)) {
                                    
                                   
                                    System.out.println("You have chosen: " + currentRoleDataOffCard[i][0]);

                                    xml.set.locationRoleData.put(currentPlayer.getPos(), currentRoleDataOffCard);

                                    Boolean success = currentPlayer.act(Integer.parseInt(currentRoleDataOnCard[i][1]), currentPlayer.getRank());
                                    if(success){

                                        currentPlayer.setCredits(currentPlayer.getCredits() + 1);
                                        currentPlayer.setMoney(currentPlayer.getMoney() + 1);
                                        
                                        xml.set.decreaseShotCount(currentPlayer.getPos());
                                        System.out.println("You have succeeded! You gain 1 credit and 1 dollar");
                                        currentRoleDataOffCard[i][2] = "True";
                                        if(xml.set.sceneShotCounter.get(currentPlayer.getPos()) == 0) {
                                            currentPlayer.bonus(currentRoleDataOnCard[i][1]);
                                        }
                                        int totalActiveScenes = 0;
                                        for(String scName : setNames){
                                            if(xml.set.sceneShotCounter.get(scName) >= 1) {
                                                totalActiveScenes++;
                                            } 
                                        }
                                        if(totalActiveScenes < 2) {
                                            endday.resetForNextDay();
                                            res = "exit";
                                        }
                                    } else {
                                        currentPlayer.setMoney(currentPlayer.getMoney() + 1);
                                        if(Integer.parseInt(currentRoleDataOffCard[i][1]) > currentPlayer.getRank()){
                                            System.out.println("You're not high enough rank for this role");
                                            break;
                                        }
                                        System.out.println("You have failed! You gain 1 dollar");
                                        currentRoleDataOffCard[i][2] = "True";
                                    }
                                }
                            }

                            for(int i = 0; i < currentRoleDataOnCard.length; i++){
                                if(currentRoleDataOnCard[i][0].equals(roleSelection)) {

                                    currentRoleDataOnCard[i][3] = "True";
                                    xml.set.locationCardRoleData.put(currentPlayer.getPos(), currentRoleDataOnCard);

                                    System.out.println("You have chosen: " + currentRoleDataOnCard[i][0]);
                                    Boolean success = currentPlayer.act(Integer.parseInt(currentRoleDataOffCard[i][1]), currentPlayer.getRank());
                                    
                                    if(success){
                                        currentPlayer.setCredits(currentPlayer.getCredits() + 2);
                                        xml.set.decreaseShotCount(currentPlayer.getPos());
                                        System.out.println("You have succeeded! You gain 2 credits");
                                        if(xml.set.sceneShotCounter.get(currentPlayer.getPos()) == 0) {
                                            currentPlayer.bonus(currentRoleDataOnCard[i][1]);
                                        }
                                        int totalActiveScenes = 0;
                                        for(String scName : setNames){
                                            if(xml.set.sceneShotCounter.get(scName) >= 1) {
                                                totalActiveScenes++;
                                            } 
                                        }
                                        if(totalActiveScenes < 2) {
                                            endday.resetForNextDay();
                                            res = "exit";
                                        }

                                    } 
                                }
                            }



                        }
                        
                        hasMoreAction = false;
                        break;
                        
                    case "rehearse":
                    currentPlayer.rehearse();
                        
                        hasMoreAction = false;
                        break;

                        
                    case "move":  
                    	
                    	System.out.println("Move where? :");
                    	String destPos = sc.nextLine();
                    	String[] moves = neighbors.get(currentPlayer.getPos());
                        
                        if(players[curTurnIdx].getHasRole()) {
                        	System.out.println("You can only act or rehearse while you have a role.");
                        	break;
                        }
                        
                        if (players[curTurnIdx].getHasMoved()) {
                        	System.out.println("You cannot move twice in one turn.");
                        	break;
                        }
                        
                        if (players[curTurnIdx].getPos().equalsIgnoreCase("trailer") && !traiNeiList.contains(destPos)) {
                        	System.out.println("You cannot move there from here.");
                        	break;
                        }
                        
                        if (players[curTurnIdx].getPos().equalsIgnoreCase("office") && !offNeiList.contains(destPos)) {
                        	System.out.println("You cannot move there from here.");
                        	break;
                        }
                        
                        //else if (Arrays.stream(moves).anyMatch(players[curTurnIdx].getPos()::equals)) {
                        //	System.out.println("a");
                        //	break;
                        //}
                        
                        else {
                        	//System.out.println(traiNeiList.contains(destPos));
                        	 
                        	//TODO calculate if that move is actually valid
                        	System.out.println("You've moved from " + players[curTurnIdx].getPos() + " to " + destPos);
                        	players[curTurnIdx].setPos(destPos);
                        	//System.out.println(players[curTurnIdx].getPos());
                        	players[curTurnIdx].setHasMoved(true);
                        	break;
                        }
                        
                    case "rankup":         	
                        System.out.println("Which rank would you like to be?: ");
                        int rankToBe = Integer.parseInt(sc.nextLine());
                        System.out.println("How would you like to pay?: ");
                        String method = sc.nextLine();

                        if(rankToBe == 7){System.out.println("You can only rank up to 6");}
                        
                        players[curTurnIdx].rankUp(rankToBe, method);
                        
                        if(!(method.equalsIgnoreCase("money")) && !(method.equalsIgnoreCase("credit"))) {
                        	System.out.println("Payement must be money or credit");
                        	break;
                        }
                        
                        if(!(players[curTurnIdx].getPos().equalsIgnoreCase("office"))) {
                        	System.out.println("You must go to the casting office to rank up.");
                        }
                        
                        else if(players[curTurnIdx].getRank() != rankToBe) {
                        	System.out.println("You did not have enough to rank up.");
                        }
                        System.out.println("Current Rank: " + players[curTurnIdx].getRank());
                        break;
                        
                    case "endturn":
                        hasMoreAction = false;
                        break;
                        
                    case "exit":
                        res = "exit";
                        hasMoreAction = false;
                        break;
                        
                    case "active":
                        System.out.println("Current player is player: " + (curTurnIdx+1));
                        System.out.println("Rank : " + players[curTurnIdx].getRank());
                        System.out.println("Money : " + players[curTurnIdx].getMoney());
                        System.out.println("Credits : " + players[curTurnIdx].getCredits());
                        System.out.println("Practice Chips : " + players[curTurnIdx].getChips());
                        break;
                    //add any actions you feel would be helpful
                }
            }
        }
        res = "exit";
        return res;
    }

    public int getPlayerCount(){return this.totalPlayerCount;}
    public Player[] getPlayers(){return this.players;}
    public void incrementDay(){this.day += 1;}
}
