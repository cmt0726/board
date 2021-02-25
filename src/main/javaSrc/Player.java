package javaSrc;
public class Player {
    private int rank = 1;
    private int credit = 0;
    private int money = 0;
    private int role;
    private String playerId;
    private int practiceChips = 0; //temp
    private int[] currentPos = {0,0};

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

        PlayerAction playerAct = new PlayerAction(this);

        //this call will affect this current player objects rank
        playerAct.rankUp(rankRequest, payment);

    }

}