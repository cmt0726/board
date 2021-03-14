

/* Written by Connor Teige and Connor Dole
    Assignment 4, CS345
    2/27/2021
*/

    class Deadwood {
        public static int playerCount;
        
        public static void main(String[] args) throws Exception{

            String boardXMLFile = args[1];
            String cardXMLFile = args[2];
            
            if (args.length >= 3) {
                boardXMLFile = args[0];
                cardXMLFile = args[1];
                playerCount = Integer.parseInt(args[2]);
                if (playerCount < 2 || playerCount > 8){
                    System.out.println("Invalid player count. Must be from 2 to 8");
                    System.exit(0);
                }
                
                Board board = new Board(playerCount, boardXMLFile, cardXMLFile);
                
            } else {
                boardXMLFile = "src/main/resources/board.xml";
                cardXMLFile = "src/main/resources/cards.xml";
                System.out.println("Invalid usage!");
                System.exit(0);
            }

            

        }

    }
