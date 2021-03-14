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
    HashMap<String, Integer[]> boardPixelLoc = new HashMap<String, Integer[]>();
    HashMap<String, Integer[][]> boardShotLoc = new HashMap<String, Integer[][]>();
    HashMap<String, Integer[]> setRoleLoc = new HashMap<String, Integer[]>();

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

            //System.out.println(offCardRolesListSize + " OFFCARDROLELIST SIZE");
            String[][] offCardRoleData = new String[offCardRolesListSize][3];

            NodeList areaDim = eEle.getElementsByTagName("area");
            Node areaNode = areaDim.item(0);
            Element areaEle = (Element) areaNode;
            //retreiving pixel locations for where to put cards
            Integer x = Integer.parseInt(areaEle.getAttribute("x"));
            Integer y = Integer.parseInt(areaEle.getAttribute("y"));
            Integer h = Integer.parseInt(areaEle.getAttribute("h"));
            Integer w = Integer.parseInt(areaEle.getAttribute("w"));
            Integer[] locAndDim = {x,y,h,w};
            boardPixelLoc.put(currentSetName, locAndDim);
            Integer[][] curSetRolePixelLoc = new Integer[4][4];
            Integer[] tempPixel = {0,0,0,0};
            for(int l = 0; l < curSetRolePixelLoc.length; l++){curSetRolePixelLoc[l] = tempPixel;}
            //gets the level for each off card role
            
            for(int j = 0; j < offCardRolesListSize; j++){

                Node offCardRoleNode = offCardRolesList.item(j);
                Element oCREle = (Element) offCardRoleNode;
                String currentTileRoleLevel = oCREle.getAttribute("level"); 
                String currentTileRoleName = oCREle.getAttribute("name");
                //System.out.println(currentTileRoleName);

                //System.out.println(currentTileRoleName + " ROLE NAME");
                
                NodeList dims = oCREle.getElementsByTagName("area");
                
                Element dimsNode = (Element) dims.item(0);
                
                //retrieving x, y coords for where off card roles are
                Integer xDim = Integer.parseInt(dimsNode.getAttribute("x"));
                Integer yDim = Integer.parseInt(dimsNode.getAttribute("y"));
                Integer height = Integer.parseInt(dimsNode.getAttribute("h"));
                Integer width = Integer.parseInt(dimsNode.getAttribute("w"));
                Integer[] dimSet = {xDim, yDim, height, width};
                curSetRolePixelLoc[j] = dimSet;

                setRoleName(currentTileRoleName, dimSet);
                     
                String[] temp = {currentTileRoleName, currentTileRoleLevel, "false"};
                offCardRoleData[j] = temp;
            }
            
            locationRoleData.put(currentSetName, offCardRoleData);

            NodeList setTakeList = eEle.getElementsByTagName("take");
            
            Node setTakeNode = setTakeList.item(0);
            Element setTakeEle = (Element) setTakeNode;
            Integer setTakesCount = Integer.parseInt(setTakeEle.getAttribute("number"));

            //This next bit calculates the locations for all shot tiles and then saves them based on the name of that set
            Integer[][] boardShotLocations = new Integer[3][4];
            Integer[] temp = {0, 0, 0, 0};

            for(int l = 0; l < 3; l++){boardShotLocations[l] = temp;} //filling boardShotlocation data with temp values to protect against null collisions

            for(int k = 0; k < setTakeList.getLength(); k++){
                Node setTakeNodeShots = setTakeList.item(k);
                NodeList shotLocations = setTakeNodeShots.getChildNodes();
                Element shotDimensions = (Element) shotLocations.item(0);
                int xDim = Integer.parseInt(shotDimensions.getAttribute("x"));
                int yDim = Integer.parseInt(shotDimensions.getAttribute("y"));
                int height = 47;
                int width = 47;
                Integer[] shotLoc = {xDim, yDim, height, width};
                boardShotLocations[k] = shotLoc;
            }
            boardShotLoc.put(currentSetName, boardShotLocations);
            locationNeighbors.put(currentSetName, neighbors);
            locationShotCount.put(currentSetName, setTakesCount);

            
        }

        //goes through every card and associates and index 0-39 for that card so we can randomly apply them to the sets
        for(int i = 0; i < cardsNlist.getLength(); i++){
            Node nNode = cardsNlist.item(i);
            
            Element eEle = (Element) nNode;
            
            String currentCardBudget = eEle.getAttribute("budget");
            String currentCardName = eEle.getAttribute("name");
            String filePath = eEle.getAttribute("img");
            
            NodeList partCardList = eEle.getElementsByTagName("part");
            String[][] cardDataInstance = new String[partCardList.getLength()][7];

            //gets the name and level for each card as well as x, y coords for each on card role location
            for(int j = 0; j < partCardList.getLength(); j++) {     
                Node currentPart = partCardList.item(j);
                Element curPartEle = (Element) currentPart;
                String curPartName = curPartEle.getAttribute("name");
                String curPartLevel = curPartEle.getAttribute("level");
                NodeList cardArea = curPartEle.getElementsByTagName("area");
                Element cardAreaEle = (Element) cardArea.item(0);
                String xOff = cardAreaEle.getAttribute("x");
                String yOff = cardAreaEle.getAttribute("y");
                String[] cardArr = {curPartName, curPartLevel,currentCardBudget, "false", filePath,xOff,yOff};
                cardDataInstance[j] = cardArr;
            }
            cardData.put(i, cardDataInstance);
        }

        card = new Cards(cardData);
        set = new Sets(locationShotCount, locationNeighbors, locationRoleData, card, boardPixelLoc, boardShotLoc, setRoleLoc);

    }

    public void resetSceneCounts(){
        //Sets scene = new Sets(locationShotCount, locationNeighbors, locationRoleData, card);
        set.resetShotCount();
    }

    public Sets getSet(){return this.set;}
    public Cards getCard(){return this.card;}

    private void setRoleName(String roleName, Integer[]curSetRolePixelLoc){
        setRoleLoc.put(roleName, curSetRolePixelLoc);
    }

}
