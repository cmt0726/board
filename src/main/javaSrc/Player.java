package javaSrc;

public class Player {
    private int rank = 1;
    private int credit = 0;
    private int money = 20;
    private int role;
    private String playerId;
    private int practiceChips = 0; //temp
    private int[] currentPos = {0,0};
    private PlayerAction playerAct = new PlayerAction(this);

    public Player(){
    	//
    }

    public String getPlayerId(){
        return this.playerId;
    }

    public int getRank(){
        return this.rank;
    }

    public int getCredits(){
        return this.credit;
    }

    public int getMoney(){
        return this.money;
    }

    public int getRole(){
        return this.role;
    }

    public int setRank(int newRank){
        this.rank = newRank;
        return this.rank;
    }

    public int setMoney(int payout){
        this.money = payout;
        return this.money;
    }

    public int setCredits(int payout){
        this.credit = payout;
        return this.credit;
    }
    
    public int setChips(int chips) {
    	this.practiceChips = chips;
    	return this.practiceChips;
    }
    
    public int getChips() {
    	return this.practiceChips;
    }

    public int[] getPlayerPos(){
        return this.currentPos;
    }

    public void rankUp(int rankRequest, String payment){
        //this call will affect this current player objects rank
        playerAct.rankUp(rankRequest, payment);
        System.out.println(this.rank);

    }
    
    public void act(int x /*scene difficulty*/){
        //this call will affect this current player objects rank
        if (playerAct.act(x)) {
        	System.out.println("Scene progressed");
        }
        else {
        	System.out.println("Scene failed");
        }
    }
    
    public void rehearse(){
        //this call will affect this current player objects rank
        playerAct.rehearse();
        System.out.println(this.getChips());
    }

}