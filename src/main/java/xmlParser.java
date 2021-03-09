/* Written by Connor Teige and Connor Dole
    Assignment 4, CS345
    2/27/2021
*/
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.HashMap;


public class xmlParser {

    Sets set;
    Cards card;
    NodeList nList;
    NodeList cardsNlist;
    Document doc;
    Document docCards;

    HashMap<String, String[]> locationNeighbors = new HashMap<String, String[]>();
    HashMap<String, Integer> locationShotCount = new HashMap<String, Integer>();
    //locationRoleData.get(setName) => {{"roleName", level},{"roleName", level},{"roleName", level}, etc}
    HashMap<String, String[][]> locationRoleData = new HashMap<String, String[][]>();

    //card hashMaps
    //This returns {{"RoleName", "level#"},{"RoleName", "level#"},{"RoleName", "level#"}, etc...}
    HashMap<Integer, String[][]> cardData = new HashMap<Integer, String[][]>();
    
    
    public xmlParser(String board, String card) throws Exception{

        File inputFile = new File(board);
        File inputFileCards = new File(card);

        //Code taken from https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        StringBuilder xmlStringBuilder = new StringBuilder();
        xmlStringBuilder.append("<?xml version=\"1.0\"?> <class> </class>");
      
        doc = builder.parse(inputFile);
        docCards = builder.parse(inputFileCards);

        doc.getDocumentElement().normalize();
        //
        docCards.getDocumentElement().normalize();
        populateHash();
 
    }

    public void printEle(String ele){
        nList = doc.getElementsByTagName(ele);
        
        for(int i = 0; i < nList.getLength(); i++){
            Node nNode = nList.item(i);
            System.out.print(nNode.getNodeName() + ": ");
            Element eEle = (Element) nNode;
            System.out.println(eEle.getAttribute("name"));
        }
    }

    //This function will populate the hashmaps associated with Sets.java and Cards.java
    private void populateHash(){
        nList = doc.getElementsByTagName("set");
        cardsNlist = docCards.getElementsByTagName("card");
        
        //gets the list of neighbors for each set
        for(int i = 0; i < nList.getLength(); i++){
            
            Node nNode = nList.item(i);
            
            Element eEle = (Element) nNode;
            
            String currentSetName = eEle.getAttribute("name");
            NodeList neighborNlist = eEle.getElementsByTagName("neighbor");

            int neighborSize = neighborNlist.getLength();

            String[] neighbors = new String[neighborSize];
            for(int j = 0; j < neighborSize; j++){

                
                Node neighborNode = neighborNlist.item(j);
                Element neighborEle = (Element) neighborNode;
                String currentTileNeighbor = neighborEle.getAttribute("name");
                neighbors[j] = currentTileNeighbor;
                
                
            }
            
            NodeList offCardRolesList = eEle.getElementsByTagName("part");
            int offCardRolesListSize = offCardRolesList.getLength();
            String[][] offCardRoleData = new String[offCardRolesListSize][3];

            //gets the level for each off card role
            for(int j = 0; j < offCardRolesListSize; j++){

                Node offCardRoleNode = offCardRolesList.item(j);
                Element oCREle = (Element) offCardRoleNode;
                String currentTileRoleLevel = oCREle.getAttribute("level"); 
                String currentTileRoleName = oCREle.getAttribute("name");
                String[] temp = {currentTileRoleName, currentTileRoleLevel, "false"};
                offCardRoleData[j] = temp;

            }
            
            locationRoleData.put(currentSetName, offCardRoleData);

            NodeList setTakeList = eEle.getElementsByTagName("take");
            Node setTakeNode = setTakeList.item(0);
            Element setTakeEle = (Element) setTakeNode;
            Integer setTakesCount = Integer.parseInt(setTakeEle.getAttribute("number"));

            locationNeighbors.put(currentSetName, neighbors);
            locationShotCount.put(currentSetName, setTakesCount);

            
        }

        //goes through every card and associates and index 0-39 for that card so we can randomly apply them to the sets
        for(int i = 0; i < cardsNlist.getLength(); i++){
            Node nNode = cardsNlist.item(i);
            
            Element eEle = (Element) nNode;
            
            String currentCardBudget = eEle.getAttribute("budget");
            String currentCardName = eEle.getAttribute("name");
            
            NodeList partCardList = eEle.getElementsByTagName("part");
            String[][] cardDataInstance = new String[partCardList.getLength()][4];

            //gets the name and level for each card 
            for(int j = 0; j < partCardList.getLength(); j++) {     
                Node currentPart = partCardList.item(j);
                Element curPartEle = (Element) currentPart;
                String curPartName = curPartEle.getAttribute("name");
                String curPartLevel = curPartEle.getAttribute("level");
                String[] cardArr = {curPartName, curPartLevel,currentCardBudget, "false"};
                cardDataInstance[j] = cardArr;
            }
            cardData.put(i, cardDataInstance);
        }

        card = new Cards(cardData);
        set = new Sets(locationShotCount, locationNeighbors, locationRoleData, card);

    }

    public void resetSceneCounts(){
        //Sets scene = new Sets(locationShotCount, locationNeighbors, locationRoleData, card);
        set.resetShotCount(locationShotCount);
    }

    public Sets getSet(){return this.set;}
    public Cards getCard(){return this.card;}

}
