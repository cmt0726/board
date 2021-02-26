package javaSrc;

import java.util.ArrayList;
import java.util.Scanner;

public class Board {
    
    private int[][] playerPosition;
    private int[][] boardPosition;
    private ArrayList role;
    private ArrayList scene;
    private int day;

    private Player[] players;
    private int totalPlayerCount;
    public EndDay endday = new EndDay(this); //passing the Board object into EndDay
    
    public Board(int playerCount){

        totalPlayerCount = playerCount;
        players = new Player[playerCount];
        for(int i = 0; i < playerCount; i++){
            players[i] = new Player();
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
        
        for(int curTurnIdx = 0; curTurnIdx < totalPlayerCount; curTurnIdx++){
            Boolean hasMoreAction = true;
            players[curTurnIdx].setHasMoved(false);
            //TODO implement a system that calcualtes available actions
            //currently this just says you can do anything
            

            while(hasMoreAction){
            	System.out.println("Player " + (curTurnIdx + 1));
                System.out.println("Current Available Actions: Act, Rehearse, Move, TakeRole, RankUp, EndTurn, exit, Active Player");
                res = sc.nextLine();
                

                System.out.println("--> " + res);
                String[] commands = res.split(" ");
                switch(commands[0].toLowerCase()) {
                    case "act":
                    	players[curTurnIdx].act(4 /*temp for scene difficulty*/);
                        //System.out.println("You've acted at scene [CALCULATE SCENE NAME] and you earned [CALCULATE PAYOUT]");
                        hasMoreAction = false;
                        break;
                        
                    case "rehearse":
                    	players[curTurnIdx].rehearse();
                        //System.out.println("You've acted at scene [CALCULATE SCENE NAME] and you earned [CALCULATE PAYOUT]");
                        hasMoreAction = false;
                        break;
                        
                    case "takerole":
                    	if(players[curTurnIdx].getHasRole()) {
                    		System.out.println("You already have a role. You can only act or rehearse.");
                    		break;
                    	}
                    	else {
                    		System.out.println("Choose one of the roles: ");//+ scene.roles)
                    		int resint = sc.nextInt();
                    		switch(resint) {
                    			case 1:
                    				System.out.println("Player " + (curTurnIdx + 1) + " now has the 1role");
                    				break;
                    			case 2:
                    				System.out.println("Player " + (curTurnIdx + 1) + " now has the 2role");
                    				break;
                    			case 3:
                    				System.out.println("Player " + (curTurnIdx + 1) + " now has the 3role");
                    				break;
                    		}
                    	
                    		players[curTurnIdx].setHasRole(true);
                    		hasMoreAction = false;
                    		break;
                    	}
                        
                    case "move":  
                        if(commands.length != 2){
                            System.out.println("Must specify Location to move to");
                            break;
                        }
                        
                        if(players[curTurnIdx].getHasRole()) {
                        	System.out.println("You can only act or rehearse while you have a role.");
                        	break;
                        }
                        
                        if (players[curTurnIdx].getHasMoved()) {
                        	System.out.println("You cannot move twice in one turn.");
                        	break;
                        }
                        else {
                        	String destPos = commands[1]; 
                        	//TODO calculate if that move is actually valid
                        	System.out.println("You've moved from " + players[curTurnIdx].getPos() + " to " + destPos);
                        	players[curTurnIdx].setPos(destPos);
                        	System.out.println(players[curTurnIdx].getPos());
                        	players[curTurnIdx].setHasMoved(true);
                        	break;
                        }
                        
                    case "rankup":         	
                        if(commands.length != 3) {
                            System.out.println("You must specify a rank, 1-6, and method, money or credit");
                            break;
                        }

                        int rankToBe = Integer.parseInt(commands[1]);
                        String method = commands[2];

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
}
