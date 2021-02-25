package java;
import java.util.ArrayList;
import java.util.Scanner;

public class Board {
    
    private int[][] playerPosition;
    private int[][] boardPosition;
    private ArrayList role;
    private ArrayList scene;
    private int day;

    private Player[] players;
    private int totalPlayerCount;
    private EndDay endday = new EndDay(this); //passing the Board object into EndDay
    
    public Board(int playerCount){

        players = new Player[playerCount];
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
        }
        totalPlayerCount = playerCount;

        //when end day is detected
        // while(game != end){
        //     for each player
        //         parse player action until no more possible action
        //     dayEnd();
        // }

        // if(nextLine == end turn){
        //     //set players to [i + 1]
        // }

    }

    public int getDay(){ return this.day;}

    //getScene
    //getPlayerPos
    
    public int[][] getBoardPosition(){return this.boardPosition;}

    //changePlayerPos
    //setScene
    //setPlayerPosition
}
