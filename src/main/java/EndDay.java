/* Written by Connor Teige and Connor Dole
    Assignment 4, CS345
    2/27/2021
*/
public class EndDay {
    
    private int playerCount;
    private int dayLimit;
    private Board board;

    public EndDay(Board bObject){
        this.board = bObject;
        this.playerCount = this.board.getPlayerCount();
    }

    public void setDayLimit(int dLimit) {
        this.dayLimit = dLimit;
    }

    /**
     * Ends the Game and returns info to display in DragPanel.java
     * @param players
     */
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
        board.setWinningIdx(playerWinnerIdx);
        board.setIsGameEnd(true);
    }

    //Calculates and returns an array of the final scores of each player
    public int[] calculateScore(Player[] players){
        players = board.getPlayers();
        int[] scores = new int[board.getPlayerCount()];
        for(int i = 0; i < players.length; i++){
            scores[i] = players[i].getMoney() + players[i].getCredits() + (players[i].getRank() * 5);
        }
        return scores;
    }

    public int getDayLim(){return this.dayLimit;}

    //moves players back to trailer, resets cards for the board, and increments day counter
    public void resetForNextDay(){
        board.xml.set.generateSceneCards();
        int playerCount = board.getPlayerCount();
        Player[] players = board.getPlayers();
        for(int i = 0; i < playerCount; i++) {
            players[i].setPos("Trailer");
        }

        board.incrementDay();
        board.resetTurn();
        if(board.getDay() > this.dayLimit){
            endGame(players);
        }
    }

    
}
