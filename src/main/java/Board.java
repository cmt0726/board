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
    

    private int day = 1;
	private String boardS;
	private String cardS;
    public xmlParser xml;

    private String[] trailerNeighbors = {"Main Street", "Saloon", "Hotel"};
    private String[] officeNeighbors = {"Train Station", "Ranch", "Secret Hideout"};
    private String[] setNames = {"Train Station", "Secret Hideout", "Church", "Hotel", "Main Street", "Jail", "General Store", "Ranch", "Bank", "Saloon"};
	String[] imagePaths = {"./src/main/resources/img/dice_r1.png","./src/main/resources/img/dice_b1.png","./src/main/resources/img/dice_g1.png","./src/main/resources/img/dice_v1.png",
                            "./src/main/resources/img/dice_c1.png","./src/main/resources/img/dice_o1.png","./src/main/resources/img/dice_p1.png","./src/main/resources/img/dice_w1.png"};
							
    private List<String> traiNeiList = Arrays.asList(trailerNeighbors);
    private List<String> offNeiList = Arrays.asList(officeNeighbors);

	//HashMaps that give data about a specific location or index
    public HashMap<Integer, String[][]> cardData;            //A specific card
    public HashMap<String, String[][]> locationRoleData; 	 //data about different sets
    public HashMap<String, String[][]> locationCardRoleData; //data about a specific card on a specific set
    public HashMap<String, String[]> neighbors; 			 //data about neighbors from a specific set location
	public HashMap<String, Integer[]> boardPixelLoc;		 //data about x, y coords for card locations
	public HashMap<String, Integer[][]> boardShotLoc;		 //data about x, y coords for shot locations
	public HashMap<String, Integer> sceneShotCount;			 //how many shots a scene has left
	public HashMap<String, Integer[]> setRoleLoc;            //data about x, y coords for off card role locations

    private Player[] players;
    private int totalPlayerCount;
	private int currentTurn = 0;
	private boolean hasResetBoard = false;
	private boolean isGameEnd = false;
	private int winningIdx;
    public EndDay endday = new EndDay(this); //passing the Board object into EndDay
    
    public Board(int playerCount, String board, String card) throws Exception{
		
		
		this.boardS = board;
		this.cardS = card;

		xml = new xmlParser(boardS, cardS);

		//initializing data from xml parsing
		this.cardData = xml.card.cardsData;
		this.locationRoleData = xml.set.locationRoleData;
		this.locationCardRoleData = xml.set.locationCardRoleData;
		this.neighbors = xml.set.neighbors;
		this.boardPixelLoc = xml.set.boardPixelLoc;
		this.boardShotLoc = xml.set.boardShotLoc;
		this.sceneShotCount = xml.set.sceneShotCounter;
		this.setRoleLoc = xml.set.setRoleLoc;

    	Scanner sc = new Scanner(System.in);


		//setting up the board based on player count and names
        totalPlayerCount = playerCount;
        players = new Player[playerCount];
        for(int i = 0; i < playerCount; i++){
            players[i] = new Player();
            System.out.println("Player name :");
            players[i].setId(sc.next());
			players[i].setPlayerImage(imagePaths[i]);
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
			for(int i = 0; i < playerCount; i++){
				increasePlayerRankVisual(i);
			}
        }
        totalPlayerCount = playerCount;
		xml.set.generateSceneCards();
        Frame f = new Frame(this);

    }

    public int getDay(){ return this.day;}

	public void setWinningIdx(int i){this.winningIdx = i;}

	public int getWinningIdx(){return this.winningIdx;}

	public void setIsGameEnd(boolean bool){
		this.isGameEnd = bool;
	}

	public boolean getIsGameEnd(){return this.isGameEnd;}

	public boolean getHasResetDay(){return this.hasResetBoard;}

	public void setHasResetDay(boolean bool){ this.hasResetBoard = bool;}

	public int getTurnNum(){return this.currentTurn;}

	public void resetTurn(){this.currentTurn = 0;}

	/**
	 * Takes in an index into the player array,
	 * fetches their image Icon path and changes
	 * the value to the path for the next rank up
	 * @param idx
	 */
	public void increasePlayerRankVisual(int idx){
		String regex = "[0-9]";
		String playerImgFilePath = players[idx].getPlayerImagePath().replaceAll(regex, String.valueOf(players[idx].getRank()));
		players[idx].setPlayerImage(playerImgFilePath);
	}

	//Handles the end of a players turn
	public void handlePlayerTurn(int idx){
		this.currentTurn++;
		players[idx].setHasMoved(false);
		if(this.currentTurn >= totalPlayerCount) {
			resetTurn();
		}
	}
	/**
	 * 
	 * @param idx index into the player array
	 * @return an ArrayList of available Roles for that player that are on Card
	 */
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
	/**
	 * 
	 * @param i index into player array
	 * @return whether or not the player is allowed to still rehearse
	 */
	public boolean increasePracticeChips(int i){
		
		players[i].increasePracticeChip();
		if(players[i].getChips() == 5) {
			return false;
		} else {
			return true;
		}
	}
	/**
	 * 
	 * @param idx index into player array
	 * @return an Array List of available roles for that player that are off Card
	 */
	public ArrayList<String> showAvailableOffCardRoles(int idx) {

		
		ArrayList<String> roles = new ArrayList<String>();
		String playerLocation = players[idx].getPos();
		Player currentPlayer = players[idx];
		String[][] currentRoleDataOffCard = locationRoleData.get(playerLocation);

		for(int i = 0; i < currentRoleDataOffCard.length; i++){
			String[] role = currentRoleDataOffCard[i];
			if(role[2].equals("false") && (Integer.parseInt(role[1]) <= currentPlayer.getRank())){
				roles.add(role[0]);	
			}
		}
		

		return roles;
	}

	/**
	 * 
	 * @param i index into player array
	 * @return An Integer array describing the x, y coords
	 * 	  	   for an on Card Role
	 */
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
		return setRoleLoc.get(players[i].getRole());
	}
	/**
	 * 
	 * @param i index into player array
	 * @return whether or not a player is on a card
	 */
	public boolean isPlayerOnCard(int i) {
		return players[i].getOnCardRole();
	}
	/**
	 * 
	 * @param i index into player array
	 * @return whether or not a set is done for a given player
	 */
	public boolean isSetDone(int i){
		if(players[i].getPos().equalsIgnoreCase("trailer") || players[i].getPos().equalsIgnoreCase("casting office")){
			return false;
		}
		if(sceneShotCount.get(players[i].getPos()) == 0) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 
	 * @param setName
	 * @return whether or not a set is done based on set name
	 */
	public boolean isSetDone(String setName){
		
		if(sceneShotCount.get(setName) == 0) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * This function uses the index and role to figure out whether or not a given player
	 * can act on a certain location. It also handles the random roll that decides if a player
	 * succesfully worked that shot. It also handles the payouts for if a set is done as well as
	 * if the day is done.
	 * @param idx index into player array
	 * @param role the chosen player role
	 * @return x, y coords describing the location for that role on the board
	 */
	public int[] handlePlayerAct(int idx, String role) {
		int[] loc = {0, 0, 0, 0};
		String playerLocation = players[idx].getPos();
		Player currentPlayer = players[idx];
		String[][] currentRoleDataOffCard = locationRoleData.get(playerLocation);
		String[][] currentRoleDataOnCard = locationCardRoleData.get(playerLocation);
		Boolean playerHasAvailableRole = false;

		
		if(sceneShotCount.get(currentPlayer.getPos()) == 0){
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

					Integer[] setRolelocations = setRoleLoc.get(roleSelection);
					
					loc[0] = setRolelocations[0];
					loc[1] = setRolelocations[1];
					loc[2] = setRolelocations[2];
					loc[3] = setRolelocations[3];
					
					

					Integer selectedRoleRank = Integer.parseInt(currentRoleDataOffCard[i][1]);
					currentRoleDataOffCard[i][2] = "true";
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
							
							currentRoleDataOffCard[i][2] = "True";

						//off card guys get no bonus money
						//This means that specific scene has no more shots to take and that we should commence payout
							if(xml.set.sceneShotCounter.get(playerLocation) == 0) {
								currentPlayer.setChips(0);
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
									hasResetBoard = true;
									
								}
							
							}

						
						} else {

							currentPlayer.setMoney(currentPlayer.getMoney() + 1);
							
							if(selectedRoleRank > currentPlayer.getRank()){
								
								return loc;
							}

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
					currentPlayer.setRole(roleSelection);
					
					if(!currentPlayer.getHasMoved()) {

						Boolean success = currentPlayer.act(cardRoleBudget, currentPlayer.getRank());
										
						if(success){
							currentPlayer.setCredits(currentPlayer.getCredits() + 2);
							xml.set.decreaseShotCount(playerLocation);
							//System.out.println("You have succeeded! You gain 2 credits");

							//No more shots left on this card
							if(xml.set.sceneShotCounter.get(playerLocation) == 0) {
								currentPlayer.setChips(0);
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
									hasResetBoard = true;
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
				
				
				if(!currentPlayer.getHasMoved()) {
					Boolean success = currentPlayer.act(Integer.parseInt(roleBudget), currentPlayer.getRank());
					if(success){
						currentPlayer.setCredits(currentPlayer.getCredits() + 2);
						xml.set.decreaseShotCount(playerLocation);
						

						if(xml.set.sceneShotCounter.get(playerLocation) == 0) {
							currentPlayer.setChips(0);
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
								hasResetBoard = true;
							}
						}
						
					} 
				}
			} else {
				
				if(!currentPlayer.getHasMoved()) {
					Boolean success = currentPlayer.act(Integer.parseInt(roleBudget), currentPlayer.getRank());
					if(success){

						currentPlayer.setCredits(currentPlayer.getCredits() + 1);
						currentPlayer.setMoney(currentPlayer.getMoney() + 1);
				
						xml.set.decreaseShotCount(playerLocation);
						
						currentRoleDataOffCard[0][2] = "True";

					//off card guys get no bonus money
						if(xml.set.sceneShotCounter.get(playerLocation) == 0) {
							currentPlayer.setChips(0);
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
								hasResetBoard = true;
							}
						}
					
					} else {
						currentPlayer.setMoney(currentPlayer.getMoney() + 1);
						if(Integer.parseInt(currentRoleDataOffCard[0][1]) > currentPlayer.getRank()){
							
							return loc;
						}
						
						currentRoleDataOffCard[0][2] = "True";
					}
				}
			}                              
		}
		return loc;
	}

	/**
	 * 
	 * @param i index into player array
	 * @return 1's and 0's describing whether or not
	 * 		   a given player can {act, rehearse, rankup, endTurn}
	 */
	public int[] calcValidActionSet(int i) {
		int[] actionSet = {0, 0, 0, 0};
		if(!players[i].getHasRole()){
			if(!(players[i].getPos().equalsIgnoreCase("Casting Office") || players[i].getPos().equalsIgnoreCase("Trailer"))) {
				actionSet[0] = 1;
			}
		} else if (players[i].getHasRole() && players[i].getHasMoved()) {
			//actionSet[0] = 1;
			//actionSet[1] = 1;
		} else if (players[i].getHasRole() && sceneShotCount.get(players[i].getPos()) != 0){
			actionSet[0] = 1;
			if(players[i].getChips() != 5){
				actionSet[1] = 1;
			}
			
		} else if(sceneShotCount.get(players[i].getPos()) == 0){
			actionSet[0] = 0;
			actionSet[1] = 0;
		}
		if (players[i].getPos().equalsIgnoreCase("Casting Office")) {
			actionSet[2] = 1;
		}
		return actionSet;
	}

	/**
	 * 
	 * @param curPos
	 * @param destPos
	 * @return whether or not a player can move that location
	 */
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
	/**
	 * If the player has enough money and isn't already that rank
	 * increases the players rank as well as changing their icon
	 * @param i
	 * @param rankToBe
	 * @param method Money or Credits
	 * @return if the rankup was succesful
	 */
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
    
    public int getPlayerCount(){return this.totalPlayerCount;}
    public Player[] getPlayers(){return this.players;}
    public void incrementDay(){this.day += 1;}
	/**
	 * This Sorts a copy of the player array based on role rank
	 * To determine in what order they should be payed and then
	 * subsequently pays them accordingly
	 * @param players
	 * @param finishedRoleLoc used to retrieve all players at that set location
	 * @param payout an array of integer values between 1-6 for each payout bonus
	 */
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
