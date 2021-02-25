package javaSrc;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;


public class xmlParser {
    
    public xmlParser() throws Exception{
        //Code taken from https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        StringBuilder xmlStringBuilder = new StringBuilder();
        xmlStringBuilder.append("<?xml version=1.0?> <class> </class>");
        ByteArrayInputStream input = new ByteArrayInputStream(xmlStringBuilder.toString().getBytes("UTF-8"));
        Document doc = builder.parse(input);

        Element root = doc.getDocumentElement();
    }

}
