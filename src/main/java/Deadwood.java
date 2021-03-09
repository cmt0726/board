

/* Written by Connor Teige and Connor Dole
    Assignment 4, CS345
    2/27/2021
*/

    class Deadwood {
        
        public static void main(String[] args) throws Exception{

            String boardXMLFile = args[1];
            String cardXMLFile = args[2];
            int playerCount = 0;
            
            Gui g = new Gui();
            if (args.length >= 3) {
                boardXMLFile = args[0];
                cardXMLFile = args[1];
                playerCount = Integer.parseInt(args[2]);
                if (playerCount < 2 || playerCount > 8){
                    System.out.println("Invalid player count. Must be from 2 to 8");
                    
                }
                System.out.println("success!");
                //Board board = new Board(playerCount, boardXMLFile, cardXMLFile);
                //play(board);
            } else {
                boardXMLFile = "src/main/resources/board.xml";
                cardXMLFile = "src/main/resources/cards.xml";
                System.out.println("Invalid usage!");
                System.exit(0);
            }

            

        }

        public static void play(Board board){
        	System.out.println("Day 1");
            String res = board.executeDay();
            //loops through number of days
            for(int i = 0; i < board.endday.getDayLim(); i++){

                //executes a players action until it receives the flag exit
                //or when the player has executed an action which indicates they can no longer make another action
                //in which case it will go onto the next turn
                while(!((res).toLowerCase().equals("exit"))){
                	System.out.println("Day " + (i+1));
                    res = board.executeDay();
                }
                res = ""; //resets result so the next for loop iteration will go into the while loop
            }
        }

    }
