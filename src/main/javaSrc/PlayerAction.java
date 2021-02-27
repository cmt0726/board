package javaSrc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

//An Aggregation of the increaseRank, Act, and TakeRole classes

//player can Move -> Take Role !-> Act
// Upgrade -> Move, Move -> Upgrade

public class PlayerAction {
    private Player player = null;
    

    public PlayerAction(Player playerOb){
        player = playerOb;
    }

    
	//TODO
	public Boolean act(int playerRank, int difficulty) {
		Boolean success = false;
		Random randRoll = new Random();
		// if(difficulty > playerRank)
		// 	return false;
		
		int roll = randRoll.nextInt(6) + 1;
		System.out.println("PLAYER ROLL FOR ACTING: " + roll);
		int chips = player.getChips();
		if(roll + chips >= difficulty/*difficulty*/) {
			//scene.progress++;
			success = true;
		}
        return success;
	}
	
	public void rehearse() {
		player.setChips(player.getChips() + 1);
	}

	public Integer[] bonus(String budget) {
		Integer[] rolls = new Integer[Integer.parseInt(budget)];

		for(int i = 0; i <Integer.parseInt(budget); i++) {
			rolls[i] = 1 + (int)(Math.random() * (7 - 1));
		}
		Arrays.sort(rolls, Collections.reverseOrder());
		return rolls;
	}

    public void rankUp(int rankRequest, String payment) {
        String temp = "office";
        //TODO : Link up playerPosition from board class
		if(payment.equalsIgnoreCase("money") && player.getPos().equals(temp)){
			if (rankRequest == 2 && (player.getMoney() >= 4)) {
				player.setMoney(player.getMoney() - 4);
                player.setRank(2);
			}
			
			else if (rankRequest == 3 && player.getMoney() >= 10) {
				player.setMoney(player.getMoney() - 10);
                player.setRank(3);
			}
			
			else if (rankRequest == 4 && player.getMoney() >= 18) {
				player.setMoney(player.getMoney() - 18);
                player.setRank(4);
			}
			
			else if (rankRequest == 5 && player.getMoney() >= 28) {
				player.setMoney(player.getMoney() - 28);
                player.setRank(5);
			}
			
			else if (rankRequest == 6 && player.getMoney() >= 40) {
				player.setMoney(player.getMoney() - 40);
                player.setRank(6);
			}
		}
		
		if(player.getPos() == temp && payment == "Credits") {
			if (rankRequest == 2 && (player.getCredits() >= 5)) {
				player.setCredits(player.getCredits() - 5);
                player.setRank(2);
			}
			
			else if (rankRequest == 3 && (player.getCredits() >= 10)) {
				player.setCredits(player.getCredits() - 10);
                player.setRank(3);
			}
			
			else if (rankRequest == 4 && (player.getCredits() >= 15)) {
				player.setCredits(player.getCredits() - 15);
                player.setRank(4);
			}
			
			else if (rankRequest == 5 && (player.getCredits() >= 20)) {
				player.setCredits(player.getCredits() - 20);
                player.setRank(5);
			}
			
			else if (rankRequest == 6 && (player.getCredits() >= 25)) {
				player.setCredits(player.getCredits() - 25);
                player.setRank(6);
			}
		}
	}
}
