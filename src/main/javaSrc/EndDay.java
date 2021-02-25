package javaSrc;
public class EndDay {
    
    private int playerCount;
    private int dayLimit;
    private Board board;

    public EndDay(Board bObject){
        this.board = bObject;
    }

    public void setDayLimit(int dLimit) {
        this.dayLimit = dLimit;
    }

    public int[] endGame(Player[] players) {
        int day = this.board.getDay();
        //
        int[] ret = calculateScore(players);
        return ret;
    }

    //Calculates and returns an array of the final scores of each player
    public int[] calculateScore(Player[] players){
        int[] scores = new int[playerCount];
        for(int i = 0; i < players.length; i++){
            scores[i] = players[i].getMoney() + players[i].getCredits() + (players[i].getRank() * 5);
        }
        return scores;
    }

    public int getDayLim(){return this.dayLimit;}

    //generateScene
    
}
