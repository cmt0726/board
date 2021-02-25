package java;
//An Aggregation of the increaseRank, Act, and TakeRole classes

public class PlayerAction {
    private Player player = null;

    public PlayerAction(Player playerOb){

        player = playerOb;

    }

    
	//TODO
	public int act(int difficulty) {
		int roll = 1 + (int)(Math.random() * (7 - 1)) + player.getChips();
		if(roll >= difficulty) {
			//scene.progress++;
		}
        return 0;
	}
	
	public int rehearse() {
		player.setChips(player.getChips() + 1);
        return player.getChips();
	}

    public void rankUp(int rankRequest, String payment) {
        int[] temp = {0,0};
        //TODO : Link up playerPosition from board class
		if(player.getPlayerPos() == temp  && payment == "Money") {
			if (rankRequest == 2 && (player.getMoney() > 4)) {
				player.setMoney(player.getMoney() - 4);
                player.setRank(2);
			}
			
			else if (rankRequest == 3 && player.getMoney() > 10) {
				player.setMoney(player.getMoney() - 10);
                player.setRank(3);
			}
			
			else if (rankRequest == 4 && player.getMoney() > 18) {
				player.setMoney(player.getMoney() - 18);
                player.setRank(4);
			}
			
			else if (rankRequest == 5 && player.getMoney() > 28) {
				player.setMoney(player.getMoney() - 28);
                player.setRank(5);
			}
			
			else if (rankRequest == 6 && player.getMoney() > 40) {
				player.setMoney(player.getMoney() - 40);
                player.setRank(6);
			}
		}
		
		if(player.getPlayerPos() == temp && payment == "Credits") {
			if (rankRequest == 2 && (player.getCredits() > 5)) {
				player.setCredits(player.getCredits() - 5);
                player.setRank(2);
			}
			
			else if (rankRequest == 3 && (player.getCredits() > 10)) {
				player.setCredits(player.getCredits() - 10);
                player.setRank(3);
			}
			
			else if (rankRequest == 4 && (player.getCredits() > 15)) {
				player.setCredits(player.getCredits() - 15);
                player.setRank(4);
			}
			
			else if (rankRequest == 5 && (player.getCredits() > 20)) {
				player.setCredits(player.getCredits() - 20);
                player.setRank(5);
			}
			
			else if (rankRequest == 6 && (player.getCredits() > 25)) {
				player.setCredits(player.getCredits() - 25);
                player.setRank(6);
			}
		}
	}
}
