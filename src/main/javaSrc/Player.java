package javaSrc;

public class Player {
    private int rank = 1;
    private int credit = 0;
    private int money = 0;
    private String role;
    private String playerId;
    private int practiceChips = 0; //temp
    private String position = "Bank"; //temp
    private PlayerAction playerAct = new PlayerAction(this);
    private boolean hasMoved = false;
    private boolean hasRole = false;

    public Player(){
    	//
    }
    
    public boolean getHasMoved() {
    	return this.hasMoved;
    }
    
    public boolean getHasRole() {
    	return this.hasRole;
    }
    
    public boolean setHasMoved(boolean moved) {
    	this.hasMoved = moved;
    	return this.hasMoved;
    }
    
    public boolean setHasRole(boolean role) {
    	this.hasRole = true;
    	return this.hasRole;
    }

    public String getId(){
        return this.playerId;
    }
    
    public String setId(String name) {
    	this.playerId = name;
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

    public String getRole(){
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

    public void rankUp(int rankRequest, String payment){
        //this call will affect this current player objects rank
        playerAct.rankUp(rankRequest, payment);
        //System.out.println(this.rank);

    }
    
    public Boolean act(int x, int rank){
        //this call will roll and calculate for scene progression when a player decides to act
        return playerAct.act(x, rank);
    }
    
    public void rehearse(){
        //this call will add practice chips to the current player
        playerAct.rehearse();
        System.out.println(this.getChips());
    }

    public String getPos(){
    	return this.position;
    }
    
    public String setPos(String newPos) {
    	this.position = newPos;
    	return this.position;
    }

    public void bonus(String budget){
        playerAct.bonus(budget);
    }

}