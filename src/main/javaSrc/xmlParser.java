package javaSrc;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.HashMap;


public class xmlParser {

    NodeList nList;
    Document doc;
    HashMap<String, String[]> locationNeighbors = new HashMap<String, String[]>();
    
    
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
            //System.out.print(nNode.getNodeName() + ": ");
            Element eEle = (Element) nNode;
            //Element neighborNode = (Element) eEle.getElementsByTagName("neighbors");
            //System.out.println(eEle.getAttribute("name"));
            String currentSetName = eEle.getAttribute("name");
            NodeList neighborNlist = eEle.getElementsByTagName("neighbor");
            int neighborSize = neighborNlist.getLength();
            String[] neighbors = new String[neighborSize];
            for(int j = 0; j < neighborSize; j++){

                
                Node neighborNode = neighborNlist.item(j);
                Element neighborEle = (Element) neighborNode;
                String currentTileNeighbor = neighborEle.getAttribute("name");
                neighbors[j] = currentTileNeighbor;
                //System.out.print(currentTileNeighbor + " ");
                
            }
            //System.out.println();
            locationNeighbors.put(currentSetName, neighbors);
            
        }
    }

}
