class Deadwood {


    public static void main(String[] args) {
        String boardXMLFile = null;
        String cardXMLFile = null;

        if (args.length >= 2) {
            boardXMLFile = args[0];
            cardXMLFile = args[1];
        } else {
            boardXMLFile = "src/main/resources/board.xml";
            cardXMLFile = "src/main/resources/cards.xml";
        }

    }

}
