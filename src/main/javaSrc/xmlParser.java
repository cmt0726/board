package javaSrc;
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
    
    
    public xmlParser() throws Exception{

        File inputFile = new File("./resources/board.xml");
        File inputFileCards = new File("./resources/cards.xml");
        //Code taken from https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        StringBuilder xmlStringBuilder = new StringBuilder();
        xmlStringBuilder.append("<?xml version=\"1.0\"?> <class> </class>");
        //ByteArrayInputStream input = new ByteArrayInputStream(xmlStringBuilder.toString().getBytes("UTF-8"));
        doc = builder.parse(inputFile);
        docCards = builder.parse(inputFileCards);

        doc.getDocumentElement().normalize();
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

    private void populateHash(){
        nList = doc.getElementsByTagName("set");
        cardsNlist = docCards.getElementsByTagName("card");
        
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
            String[][] offCardRoleData = new String[offCardRolesListSize][2];

            for(int j = 0; j < offCardRolesListSize; j++){

                Node offCardRoleNode = offCardRolesList.item(j);
                Element oCREle = (Element) offCardRoleNode;
                String currentTileRoleLevel = oCREle.getAttribute("level"); 
                String currentTileRoleName = oCREle.getAttribute("name");
                String[] temp = {currentTileRoleName, currentTileRoleLevel};
                offCardRoleData[j] = temp;

            }
            
            locationRoleData.put(currentSetName, offCardRoleData);

            NodeList setTakeList = eEle.getElementsByTagName("take");
            Node setTakeNode = setTakeList.item(0);
            Element setTakeEle = (Element) setTakeNode;
            Integer setTakesCount = Integer.parseInt(setTakeEle.getAttribute("number"));

            locationNeighbors.put(currentSetName, neighbors);
            locationShotCount.put(currentSetName, setTakesCount);

            //time to populate cards

            
        }

        for(int i = 0; i < cardsNlist.getLength(); i++){
            Node nNode = cardsNlist.item(i);
            
            Element eEle = (Element) nNode;
            
            String currentCardBudget = eEle.getAttribute("budget");
            String currentCardName = eEle.getAttribute("name");
            // NodeList cardSceneNumber = eEle.getElementsByTagName("scene");
            // Node scene = cardSceneNumber.item(0);
            // scene.get
            NodeList partCardList = eEle.getElementsByTagName("part");
            String[][] cardDataInstance = new String[partCardList.getLength()][3];
            for(int j = 0; j < partCardList.getLength(); j++) {     
                Node currentPart = partCardList.item(j);
                Element curPartEle = (Element) currentPart;
                String curPartName = curPartEle.getAttribute("name");
                String curPartLevel = curPartEle.getAttribute("level");
                String[] cardArr = {curPartName, curPartLevel,currentCardBudget};
                cardDataInstance[j] = cardArr;
            }
            cardData.put(i, cardDataInstance);
        }

        card = new Cards(cardData);
        set = new Sets(locationShotCount, locationNeighbors, locationRoleData);

    }

    public void resetSceneCounts(){
        Sets scene = new Sets(locationShotCount, locationNeighbors, locationRoleData);
    }

    public Sets getSet(){return this.set;}
    public Cards getCard(){return this.card;}

}
