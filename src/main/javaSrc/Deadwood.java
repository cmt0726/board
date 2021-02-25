package javaSrc;

    class Deadwood {

        public static void main(String[] args) {
            String boardXMLFile = null;
            String cardXMLFile = null;
            int playerCount = 0;

            if (args.length >= 3) {
                boardXMLFile = args[0];
                cardXMLFile = args[1];
                playerCount = Integer.parseInt(args[2]);
                if (playerCount < 2 || playerCount > 8){
                    System.out.println("Invalid player count. Must be from 2 to 8");
                    //error
                }
                Board board = new Board(playerCount);
            } else {
                boardXMLFile = "src/main/resources/board.xml";
                cardXMLFile = "src/main/resources/cards.xml";
            }

        }

    }
