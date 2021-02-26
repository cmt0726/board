package javaSrc;

    class Deadwood {
        
        public static void main(String[] args) throws Exception{
            //xmlParser xml = new xmlParser();
            //HashMap<Integer, String[][]> cardsData = xml.card.cardsData;
            //HashMap<String, Integer> locationShotCount = xml.set.sceneShotCounter;

            
            //String[] temp = {"Train Station", "Secret Hideout", "Church", "Hotel", "Main Street", "Jail", "General Store", "Ranch", "Bank", "Saloon"};
            

            String boardXMLFile = null;
            String cardXMLFile = null;
            int playerCount = 0;

            if (args.length >= 3) {
                boardXMLFile = args[0];
                cardXMLFile = args[1];
                playerCount = Integer.parseInt(args[2]);
                if (playerCount < 2 || playerCount > 8){
                    System.out.println("Invalid player count. Must be from 2 to 8");
                    
                }
                System.out.println("success!");
                Board board = new Board(playerCount);
                play(board);
            } else {
                // boardXMLFile = "src/main/resources/board.xml";
                // cardXMLFile = "src/main/resources/cards.xml";
                System.out.println("Invalid usage!");
                System.exit(0);
            }

            

        }

        public static void play(Board board){
            String res = board.executeDay();
            //loops through number of days
            for(int i = 0; i < board.endday.getDayLim(); i++){

                //executes a players action until it receives the flag exit
                //or when the player has executed an action which indicates they can no longer make another action
                //in which case it will go onto the next turn
                while(!((res).toLowerCase().equals("exit"))){
                    res = board.executeDay();
                }
                res = ""; //resets result so the next for loop iteration will go into the while loop
            }
        }

    }
