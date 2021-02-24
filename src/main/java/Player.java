public class Player {
    private int rank;
    private int credit;
    private int money;
    private int role;
    private String playerId;

    public Player(){
        //
    }

    public int getRank(){return this.rank;}
    public int getCredit(){return this.credit;}
    public int getMoney(){return this.money;}
    public int getRole(){return this.role;}
    public String getPlayerId(){return this.playerId;}
    public int setRank(int newRank){
        this.rank = newRank;
        return this.rank;
    }

    public int setMoney(int payout){
        this.money += payout;
        return this.money;
    }

    public int setCredit(int payout){
        this.credit += payout;
        return this.credit;
    }

}
