/* Written by Connor Teige and Connor Dole
    Assignment 4, CS345
    2/27/2021
*/
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.HashMap;

public class Board {
    

    private int day;
	private String boardS;
	private String cardS;
    public xmlParser xml;

    private String[] trailerNeighbors = {"Main Street", "Saloon", "Hotel"};
    private String[] officeNeighbors = {"Train Station", "Ranch", "Secret Hideout"};
    private String[] setNames = {"Train Station", "Secret Hideout", "Church", "Hotel", "Main Street", "Jail", "General Store", "Ranch", "Bank", "Saloon"};
    private List<String> traiNeiList = Arrays.asList(trailerNeighbors);
    private List<String> offNeiList = Arrays.asList(officeNeighbors);

	//HashMaps that give data about a specific location or index
    public HashMap<Integer, String[][]> cardData;            //A specific card
    public HashMap<String, String[][]> locationRoleData; 	 //data about different sets
    public HashMap<String, String[][]> locationCardRoleData; //data about a specific card on a specific set
    public HashMap<String, String[]> neighbors; 			 //data about neighbors from a specific set location
	public HashMap<String, Integer[]> boardPixelLoc;
	public HashMap<String, Integer[][]> boardShotLoc;
	public HashMap<String, Integer> sceneShotCount;
	public HashMap<String, Integer[][]> setRoleLoc;

    private Player[] players;
    private int totalPlayerCount;
	private int currentTurn = 0;
    public EndDay endday = new EndDay(this); //passing the Board object into EndDay
    
    public Board(int playerCount, String board, String card) throws Exception{
		
		//Gui g = new Gui();
		this.boardS = board;
		this.cardS = card;

		xml = new xmlParser(boardS, cardS);

		this.cardData = xml.card.cardsData;
		this.locationRoleData = xml.set.locationRoleData;
		this.locationCardRoleData = xml.set.locationCardRoleData;
		this.neighbors = xml.set.neighbors;
		this.boardPixelLoc = xml.set.boardPixelLoc;
		this.boardShotLoc = xml.set.boardShotLoc;
		this.sceneShotCount = xml.set.sceneShotCounter;
		this.setRoleLoc = xml.set.setRoleLoc;

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
		xml.set.generateSceneCards();
        Frame f = new Frame(this);

    }

    public int getDay(){ return this.day;}

	public int getTurnNum(){return this.currentTurn;}

	public void resetTurn(){this.currentTurn = 0;}

	//Handles the end of a players turn
	public void handlePlayerTurn(int idx){



		this.currentTurn++;
		players[idx].setHasMoved(false);
		if(this.currentTurn == totalPlayerCount) {
			this.currentTurn = 0;
		}

	}

	public ArrayList<String> showAvailableCardRoles(int idx) {

		ArrayList<String> roles = new ArrayList<String>();
		String playerLocation = players[idx].getPos();
		Player currentPlayer = players[idx];
		String[][] currentRoleDataOnCard = locationCardRoleData.get(playerLocation);
		
		for(String[] role : currentRoleDataOnCard){
			if(role[3].equals("false") && (Integer.parseInt(role[1]) <= currentPlayer.getRank())){
				roles.add(role[0]);
			}
		}
		return roles;
	}

	public ArrayList<String> showAvailableOffCardRoles(int idx) {

		
		ArrayList<String> roles = new ArrayList<String>();
		String playerLocation = players[idx].getPos();
		Player currentPlayer = players[idx];
		String[][] currentRoleDataOffCard = locationRoleData.get(playerLocation);

		for(String[] role : currentRoleDataOffCard){
			if(role[2].equals("false") && (Integer.parseInt(role[1]) <= currentPlayer.getRank())){
				roles.add(role[0]);
			}
		}

		return roles;
	}

	public Integer[] curSetPixelLoc(int i){
		int x = 0;
		int y = 0;
		if(players[i].getOnCardRole()) {
			String[][] roleLocandData = locationCardRoleData.get(players[i].getPos());
			for(int index = 0; index < roleLocandData.length; index++) {
				if(roleLocandData[i][0].equals(players[i].getRole())) {
					x = boardPixelLoc.get(players[i].getPos())[1] + Integer.parseInt(roleLocandData[i][5]);
					y = boardPixelLoc.get(players[i].getPos())[1] + Integer.parseInt(roleLocandData[i][6]);
				}
			}
			Integer[] ret = {x, y};
			return ret;
		}
		return setRoleLoc.get(players[i].getRole())[0];
	}

	public boolean isPlayerOnCard(int i) {
		return players[i].getOnCardRole();
	}

	public int[] handlePlayerAct(int idx, String role) {
		int[] loc = {0, 0, 0, 0};
		String playerLocation = players[idx].getPos();
		Player currentPlayer = players[idx];
		String[][] currentRoleDataOffCard = locationRoleData.get(playerLocation);
		String[][] currentRoleDataOnCard = locationCardRoleData.get(playerLocation);
		Boolean playerHasAvailableRole = false;

		if(currentRoleDataOffCard[0][2].equals("true")){
			System.out.println("This set is has wrapped up, you must move to another location");
			currentPlayer.setHasRole(false);
			currentPlayer.setOnCardRole(false);
			return loc;
		}

		if(!(currentPlayer.getHasRole()) && role != null){
			
		
			String roleSelection = role;
			
			for(int i = 0; i < currentRoleDataOffCard.length; i++){
				String nameOfPotentialRole = currentRoleDataOffCard[i][0];
				String partBudgetS = currentRoleDataOnCard[0][2];
				if(nameOfPotentialRole.equals(roleSelection)) {

					Integer[][] setRolelocations = setRoleLoc.get(roleSelection);
					
					loc[0] = setRolelocations[0][0];
					loc[1] = setRolelocations[0][1];
					loc[2] = setRolelocations[0][2];
					loc[3] = setRolelocations[0][3];
					
					

					Integer selectedRoleRank = Integer.parseInt(currentRoleDataOffCard[i][1]);
					currentPlayer.setCurrentRoleRank(selectedRoleRank);

					//Means the player chose a role that is OffCard and we're associating a player position with that fact
					xml.set.locationRoleData.put(playerLocation, currentRoleDataOffCard);

					//System.out.println("Current Role Budget:" + partBudgetS);
					currentPlayer.setHasRole(true);
					currentPlayer.setRole(roleSelection);
					if(!currentPlayer.getHasMoved()) {

						Integer partBudget = Integer.parseInt(partBudgetS);
						
						Boolean success = currentPlayer.act(partBudget, currentPlayer.getRank());
						
						if(success){

							currentPlayer.setCredits(currentPlayer.getCredits() + 1);
							currentPlayer.setMoney(currentPlayer.getMoney() + 1);
					
							xml.set.decreaseShotCount(playerLocation);
							//System.out.println("You have succeeded! You gain 1 credit and 1 dollar");
							currentRoleDataOffCard[i][2] = "True";

						//off card guys get no bonus money
						//This means that specific scene has no more shots to take and that we should commence payout
							if(xml.set.sceneShotCounter.get(playerLocation) == 0) {
								currentRoleDataOffCard[0][2] = "true";
								locationRoleData.put(playerLocation, currentRoleDataOffCard);
								payout(players,playerLocation ,currentPlayer.bonus(partBudgetS));
								for(int j = 0; j < players.length;j++) {
									if(!players[j].getHasRole()) {
										players[j].setChips(0);
									}
								}
							
								currentPlayer.setHasRole(false);
								currentPlayer.setOnCardRole(false);

								int totalActiveScenes = 0;

								//This checks whether or not there are more than 1 scene left, if not, the day is done
								for(String scName : setNames){
									if(xml.set.sceneShotCounter.get(scName) >= 1) {
										totalActiveScenes++;
									} 
								}
								if(totalActiveScenes < 2) {
									endday.resetForNextDay();
									//res = "exit";
								}
							
							}

						
						} else {

							currentPlayer.setMoney(currentPlayer.getMoney() + 1);
							
							if(selectedRoleRank > currentPlayer.getRank()){
								System.out.println("You're not high enough rank for this role");
								return loc;
							}
							System.out.println("You have failed! You gain 1 dollar");
							currentRoleDataOffCard[i][2] = "True";

						}
					} else {
						return loc;
					}
				}
			}

			for(int i = 0; i < currentRoleDataOnCard.length; i++){
				String roleName = currentRoleDataOnCard[i][0];
				if(roleName.equals(roleSelection)) {

					//sets that cards role to True, that there is a player working on it so that no one else can.
					currentRoleDataOnCard[i][3] = "True";
					xml.set.locationCardRoleData.put(playerLocation, currentRoleDataOnCard);

					
					loc[0] = Integer.parseInt(currentRoleDataOnCard[i][5]) + boardPixelLoc.get(currentPlayer.getPos())[0];
					loc[1] = Integer.parseInt(currentRoleDataOnCard[i][6]) + boardPixelLoc.get(currentPlayer.getPos())[1];
					loc[2] = 40;
					loc[3] = 40;

					//System.out.println("You have chosen: " + roleName);
					Integer selectedRoleRank = Integer.parseInt(currentRoleDataOnCard[i][1]);
					Integer cardRoleBudget = Integer.parseInt(currentRoleDataOnCard[i][2]);
					String cardRoleBudgetS = currentRoleDataOnCard[i][2];
					currentPlayer.setCurrentRoleRank(selectedRoleRank);

					//System.out.println("Current Role Budget:" + cardRoleBudget);
					currentPlayer.setHasRole(true);
					currentPlayer.setOnCardRole(true);
					
					if(!currentPlayer.getHasMoved()) {

						Boolean success = currentPlayer.act(cardRoleBudget, currentPlayer.getRank());
										
						if(success){
							currentPlayer.setCredits(currentPlayer.getCredits() + 2);
							xml.set.decreaseShotCount(playerLocation);
							//System.out.println("You have succeeded! You gain 2 credits");

							//No more shots left on this card
							if(xml.set.sceneShotCounter.get(playerLocation) == 0) {
								currentRoleDataOffCard[0][2] = "true";
								locationRoleData.put(playerLocation, currentRoleDataOffCard);
							
								payout(players,playerLocation ,currentPlayer.bonus(cardRoleBudgetS));

								//resets number of practice chips for each player
								for(int j = 0; j < players.length;j++) {
									if(!players[j].getHasRole()) {
										players[j].setChips(0);
									}
								}
								currentPlayer.setHasRole(false);
								currentPlayer.setOnCardRole(false);
								int totalActiveScenes = 0;
								//checking if day is done
								for(String scName : setNames){
									if(xml.set.sceneShotCounter.get(scName) >= 1) {
										totalActiveScenes++;
									} 
								}
								if(totalActiveScenes < 2) {
									endday.resetForNextDay();
									//res = "exit";
								} 
							
							}
							
						} else {
							return loc;
							
						}
						return loc;
					} else {
						return loc;
					}
				}
			}
		} else { //The player is currently working a role

			String roleBudget = currentRoleDataOnCard[0][2];

			if(currentPlayer.getOnCardRole()){
				//System.out.println("Current Role budget:" + roleBudget);
				
				if(!currentPlayer.getHasMoved()) {
					Boolean success = currentPlayer.act(Integer.parseInt(roleBudget), currentPlayer.getRank());
					if(success){
						currentPlayer.setCredits(currentPlayer.getCredits() + 2);
						xml.set.decreaseShotCount(playerLocation);
						//System.out.println("You have succeeded! You gain 2 credits");

						if(xml.set.sceneShotCounter.get(playerLocation) == 0) {

							currentRoleDataOffCard[0][2] = "true"; //this scene is done, no more shots left.
							locationRoleData.put(playerLocation, currentRoleDataOffCard); //set the data for that location with the fact there are no more shots left
						
							payout(players,playerLocation ,currentPlayer.bonus(roleBudget));
							for(int j = 0; j < players.length;j++) {
								if(!players[j].getHasRole()) {
									players[j].setChips(0);
								}
							}
							currentPlayer.setOnCardRole(false);
							currentPlayer.setHasRole(false);
							int totalActiveScenes = 0;
							for(String scName : setNames){
								if(xml.set.sceneShotCounter.get(scName) >= 1) {
									totalActiveScenes++;
								} 
							}
							if(totalActiveScenes < 2) {
								endday.resetForNextDay();
								//res = "exit";
							}
						}
						
					} else {
						System.out.println("You have failed your role and earned NOTHING!");
					}
				}
			} else {
				//System.out.println("CURRENT ROLE BUDGET:" + roleBudget);
				if(!currentPlayer.getHasMoved()) {
					Boolean success = currentPlayer.act(Integer.parseInt(roleBudget), currentPlayer.getRank());
					if(success){

						currentPlayer.setCredits(currentPlayer.getCredits() + 1);
						currentPlayer.setMoney(currentPlayer.getMoney() + 1);
				
						xml.set.decreaseShotCount(playerLocation);
						//System.out.println("You have succeeded! You gain 1 credit and 1 dollar");
						currentRoleDataOffCard[0][2] = "True";

					//off card guys get no bonus money
						if(xml.set.sceneShotCounter.get(playerLocation) == 0) {

							currentRoleDataOffCard[0][2] = "true";
							locationRoleData.put(playerLocation, currentRoleDataOffCard);

							payout(players,playerLocation ,currentPlayer.bonus(roleBudget));
							for(int j = 0; j < players.length;j++) {
								if(!players[j].getHasRole()) {
									players[j].setChips(0);
								}
							}
							currentPlayer.setOnCardRole(false);
							currentPlayer.setHasRole(false);

							int totalActiveScenes = 0;

							for(String scName : setNames){
								if(xml.set.sceneShotCounter.get(scName) >= 1) {
									totalActiveScenes++;
								} 
							}
							if(totalActiveScenes < 2) {
								endday.resetForNextDay();
								//res = "exit";
							}
						}
					
					} else {
						currentPlayer.setMoney(currentPlayer.getMoney() + 1);
						if(Integer.parseInt(currentRoleDataOffCard[0][1]) > currentPlayer.getRank()){
							//System.out.println("You're not high enough rank for this role");
							return loc;
						}
						//System.out.println("You have failed! You gain 1 dollar");
						currentRoleDataOffCard[0][2] = "True";
					}
				}
			}                              
		}
		return loc;
	}

	public int[] calcValidActionSet(int i) {
		int[] actionSet = {0, 0, 0, 0};
		if(!players[i].getHasRole()){
			if(!(players[i].getPos().equalsIgnoreCase("Casting Office") || players[i].getPos().equalsIgnoreCase("Trailer"))) {
				actionSet[0] = 1;
			}
		} else if (players[i].getHasRole() && players[i].getHasMoved()) {
			//actionSet[0] = 1;
			//actionSet[1] = 1;
		} else if (players[i].getHasRole()){
			actionSet[0] = 1;
			actionSet[1] = 1;
		}
		if (players[i].getPos().equalsIgnoreCase("Casting Office")) {
			actionSet[2] = 1;
		}
		return actionSet;
	}

	public boolean validatePlayerMove(String curPos, String destPos){
		boolean adj = false;
		Player player = players[currentTurn];
		if(destPos == null){return false;}
		if(curPos.equals(destPos)){return true;}
		if (player.getHasMoved()) {
			System.out.println("You cannot move twice in one turn.");
			return adj;
		} 
	
		if(player.getHasRole()) {
			System.out.println("You can only act or rehearse while you have a role.");
			return adj;
		} 
		
		System.out.print("Move where?: ");
		
		String[] moves = neighbors.get(player.getPos());
   
	
		if(moves != null) {
			for(int i = 0; i < moves.length; i++) {
				if(moves[i].equals(destPos)) {
					adj = true;
				}
			}
		}

		if(destPos.equalsIgnoreCase("Trailer")){
			for(int i = 0; i < trailerNeighbors.length; i++) {
				if(trailerNeighbors[i].equalsIgnoreCase(player.getPos())) {
					adj = true;
				}
			}
		} else if(destPos.equalsIgnoreCase("Casting Office")) {
			//System.out.println("YOU BE TRYING TO GO TO OFFICE");
			for(int i = 0; i < officeNeighbors.length; i++) {
				if(officeNeighbors[i].equalsIgnoreCase(player.getPos())) {
					adj = true;
				}
			}
		}
	
		if(player.getPos().equalsIgnoreCase("Trailer") && traiNeiList.contains(destPos)) {
			adj = true;
		}
	
		if (player.getPos().equalsIgnoreCase("Casting Office") && offNeiList.contains(destPos)) {
			adj = true;
		}
	
		if (!adj) {
			System.out.println("You cannot move there from here.");
			return false;
		}
	
		if (player.getPos().equalsIgnoreCase("Casting Office") && !offNeiList.contains(destPos)) {
			System.out.println("You cannot move there from here.");
			return false;
		}  
	
		else {
			
			System.out.println("You've moved from " + player.getPos() + " to " + destPos);
			player.setPos(destPos);
			
			player.setHasMoved(true);
			return true;
		}
		
	}

	public boolean rankUp(int i, int rankToBe, String method) {
		if(!(players[i].getPos().equalsIgnoreCase("casting office"))) {
			System.out.println("You must go to the casting office to rank up.");
			return false;
		}
	
		
		

		if(rankToBe <= players[i].getRank()) {
			System.out.println("You are already that rank or higher.");
			return false;
		}
	
		players[i].rankUp(rankToBe, method);

		if(players[i].getRank() != rankToBe) {
			return false;
		}

		return true;
		
	}

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
    	
    	for(int i = 0; i < players.length; i++) {
    		players[i].setPos("trailer");
    	}
        
        Scanner sc = new Scanner(System.in);
        String res = "";
        
        //HashMap<String, Integer> sceneShotCounter = xml.set.sceneShotCounter;

        

        while(!res.equals("exit")) {
        	for(int curTurnIdx = 0; curTurnIdx < totalPlayerCount; curTurnIdx++){
        		Player currentPlayer = players[curTurnIdx];
        		Boolean hasMoreAction = true;
        		currentPlayer.setHasMoved(false);
            
        		while(hasMoreAction){
        			System.out.println(currentPlayer.getId());
        			Boolean playerHasAvailableRole = false;
        			System.out.println("Current Available Actions: Act, Rehearse, Move, RankUp, EndTurn, Active Player, exit, Show Players");
        			res = sc.nextLine();
                

        			System.out.println("--> " + res);
        			String[] commands = res.split(" ");
        			switch(commands[0].toLowerCase()) {
        				case "act":
                            if(currentPlayer.getPos().equals("office") || currentPlayer.getPos().equals("trailer")){
                                System.out.println("There are no roles for you here!");
                                break;
                            }
                            
                        
        					hasMoreAction = false;
        					break;
                        
        				case "rehearse":
        					if(currentPlayer.getChips() > 4) {
        						System.out.println("You already have guaranteed success, you must act.");
        						break;
        					}
        					
        					if(!currentPlayer.getHasRole()) {
        						System.out.println("You cannot rehearse without having a role");
        						break;
        					}
        					
        					currentPlayer.rehearse();
                        
        					hasMoreAction = false;
        					break;
                        
        				case "rankup":         	
                    	
        					
        					break;
                        
        				case "endturn":
        					hasMoreAction = false;
        					break;
                        
        				case "exit":
        					res = "exit";
        					hasMoreAction = false;
        					break;
                        
        				case "active":
        					System.out.println("Current player is " + players[curTurnIdx].getId());
        					System.out.println("Current player is at " + players[curTurnIdx].getPos());
        					System.out.println("Rank : " + players[curTurnIdx].getRank());
        					System.out.println("Money : " + players[curTurnIdx].getMoney());
        					System.out.println("Credits : " + players[curTurnIdx].getCredits());
        					System.out.println("Practice Chips : " + players[curTurnIdx].getChips());
        					break;
                        
        				case "show":
        					for(int i = 0; i < players.length; i++) {
        						System.out.println(players[i].getId() + " is at " + players[i].getPos());
        					}
        					break;
        					//add any actions you feel would be helpful
        			}
        		}
        		if(res.equals("exit")) {
        			break;
        		}
        	}
        }
        //res = "exit";
        return res;
    }

    public int getPlayerCount(){return this.totalPlayerCount;}
    public Player[] getPlayers(){return this.players;}
    public void incrementDay(){this.day += 1;}

    public void payout(Player[] players, String finishedRoleLoc, Integer[] payout){

        int validPlayers = 0;
        for(int i = 0; i < totalPlayerCount; i++){
            if(players[i].getPos().equals(finishedRoleLoc)) {
                players[i].setHasRole(false);
            }
        }
        for(int i = 0; i < totalPlayerCount; i++){
            if(players[i].getPos().equals(finishedRoleLoc) && players[i].getOnCardRole()){
                
                validPlayers++;
            }
        }
        if(validPlayers == 0)
            return;
        Player[] playersCopy = new Player[validPlayers];
        int idx = 0;
        for(int i = 0; i < totalPlayerCount; i++){
            if(players[i].getPos().equals(finishedRoleLoc) && players[i].getOnCardRole()){
                playersCopy[idx] = players[i];
                idx++;
            }
        }
        
        Arrays.sort(playersCopy, (a, b) -> a.getCurrentRoleRank().compareTo(b.getCurrentRoleRank()));

        for(int i = 0; i < payout.length; i++) {
            Player playerToBePaid = playersCopy[i % validPlayers];
            System.out.println("Player: " + playerToBePaid.getId() + " You went from $" + playerToBePaid.getMoney());
            playerToBePaid.setMoney(playerToBePaid.getMoney() + payout[i]);
            playerToBePaid.setHasRole(false);
            System.out.print(" -> $" + playerToBePaid.getMoney() + "\n");
        }
         

    }

}
