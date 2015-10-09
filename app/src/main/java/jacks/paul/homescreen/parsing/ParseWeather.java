package jacks.paul.homescreen.parsing;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import jacks.paul.homescreen.types.TemperatureData;

/**
 * Created by Paul on 2015-10-08.
 */
public class ParseWeather {

    // Where we put the data
    TemperatureData data = new TemperatureData();

    public WeatherInterface delegate = null;

    public ParseWeather(){
        //Empty constructor
    }
    public void getWeather(String input){

        try {

            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
            Document xmlDoc = builder.parse(new InputSource(new StringReader(input)));

            xmlDoc.getDocumentElement().normalize();

            // Meta data
            NodeList metaNodes = xmlDoc.getElementsByTagName("time");
            // Weather data
            NodeList weatherDataNodes = xmlDoc.getElementsByTagName("location");



            Node node = weatherDataNodes.item(0);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element elements = (Element) node;

                    /* Temperature
                     Looks like this:
                     <temperature id="TTT" unit="celsius" value="14.0"/>
                    */
                    if(elements.getElementsByTagName("temperature").item(0).getAttributes().item(2).getTextContent() != null)
                        data.temperature = Double.parseDouble(elements.getElementsByTagName("temperature").item(0).getAttributes().item(2).getTextContent());

                }


            delegate.dataReceived(data);


        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
