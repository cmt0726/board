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

    public void endGame(Player[] players) {
        
        int[] ret = calculateScore(players);
        int playerWinnerIdx = 0;
        int maxScore = 0;
        for(int i = 0; i < ret.length; i++){
            if(ret[i] > maxScore){
                playerWinnerIdx = i;
                maxScore = ret[i];
            }
        }
        System.out.println("The winner is: " + playerWinnerIdx);
        System.exit(1);
        
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

    public void resetForNextDay(){
        board.xml.set.generateSceneCards();
        int playerCount = board.getPlayerCount();
        Player[] players = board.getPlayers();
        for(int i = 0; i < playerCount; i++) {
            players[i].setPos("Trailer");
        }
        board.incrementDay();
        if(board.getDay() > this.dayLimit){
            endGame(players);
        }
    }

    //generateScene
    
}
