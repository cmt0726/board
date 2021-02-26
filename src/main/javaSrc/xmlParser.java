package javaSrc;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.HashMap;


public class xmlParser {

    Sets set;
    NodeList nList;
    Document doc;
    HashMap<String, String[]> locationNeighbors = new HashMap<String, String[]>();
    HashMap<String, Integer> locationShotCount = new HashMap<String, Integer>();
    //locationRoleData.get(setName) => {{"roleName", level},{"roleName", level},{"roleName", level}, etc}
    HashMap<String, String[][]> locationRoleData = new HashMap<String, String[][]>();
    
    
    public xmlParser() throws Exception{

        File inputFile = new File("./resources/board.xml");
        //Code taken from https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        StringBuilder xmlStringBuilder = new StringBuilder();
        xmlStringBuilder.append("<?xml version=\"1.0\"?> <class> </class>");
        //ByteArrayInputStream input = new ByteArrayInputStream(xmlStringBuilder.toString().getBytes("UTF-8"));
        doc = builder.parse(inputFile);


        doc.getDocumentElement().normalize();
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

            
            
        }

        set = new Sets(locationShotCount, locationNeighbors, locationRoleData);

    }

    public void resetSceneCounts(){
        Sets scene = new Sets(locationShotCount, locationNeighbors, locationRoleData);
    }

    public Sets getSet(){return this.set;}

}
