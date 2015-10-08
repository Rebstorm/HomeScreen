package jacks.paul.homescreen.parsing;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Paul on 2015-10-08.
 */
public class XMLParser {

    public XMLParser(String input){

        try {


            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(input));





        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }



    }
}
