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


            // Pulls weather data
            Node node = weatherDataNodes.item(0);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element elements = (Element) node;

                    /* Temperature
                     Looks like this:
                     <temperature id="TTT" unit="celsius" value="14.0"/>
                    */

                    data.temperature = Double.parseDouble(elements.getElementsByTagName("temperature").item(0).getAttributes().item(2).getTextContent());

                    /*Wind Direciton
                    Looks like this:
                    <windDirection id="dd" deg="52.3" name="NE"/>
                     */
                    data.windDirection = "Wind Direction: " + elements.getElementsByTagName("windDirection").item(0).getAttributes().item(1).getTextContent() + " " +
                                             elements.getElementsByTagName("windDirection").item(0).getAttributes().item(2).getTextContent();

                    /* Humidity
                    Looks like this:
                    <humidity value="97.8" unit="percent"/>
                     */
                    data.humidity = "Humidity: " + elements.getElementsByTagName("humidity").item(0).getAttributes().item(0).getTextContent() +"%";

                }

            // YR.no provided the weather data symbol in the next tag, therefore we're using it.
            Node symbolId = weatherDataNodes.item(1);
                if (symbolId.getNodeType() == Node.ELEMENT_NODE) {
                    Element elements = (Element) symbolId;

                    /* Gets the symbol-id
                     Looks like this:
                     <symbol id="PartlyCloud" number="3"/>
                     */
                    int id = Integer.valueOf(elements.getElementsByTagName("symbol").item(0).getAttributes().item(1).getTextContent());

                    switch (id){
                        case 1:
                            data.weatherIcon = TemperatureData.WeatherIcon.Sunny;
                            break;
                        case 2:
                            data.weatherIcon = TemperatureData.WeatherIcon.LittleCloudy;
                            break;
                        case 3:
                            data.weatherIcon = TemperatureData.WeatherIcon.PartlyCloudy;
                            break;
                        case 4:
                            data.weatherIcon = TemperatureData.WeatherIcon.Cloudy;
                            break;
                        case 5:
                            data.weatherIcon = TemperatureData.WeatherIcon.LightRainSun;
                            break;
                        case 6:
                            data.weatherIcon = TemperatureData.WeatherIcon.LightRainThunderstorm;
                            break;
                        case 7:
                            data.weatherIcon = TemperatureData.WeatherIcon.Sleet;
                            break;
                        case 8:
                            data.weatherIcon = TemperatureData.WeatherIcon.SnowSun;
                            break;
                        case 9:
                            data.weatherIcon = TemperatureData.WeatherIcon.LightRain;
                            break;
                        case 10:
                            data.weatherIcon = TemperatureData.WeatherIcon.Rain;
                            break;
                        case 11:
                            data.weatherIcon = TemperatureData.WeatherIcon.RainThunder;
                            break;
                        case 12:
                            data.weatherIcon = TemperatureData.WeatherIcon.Sleet;
                            break;
                        case 13:
                            data.weatherIcon = TemperatureData.WeatherIcon.Snow;
                            break;
                        case 14:
                            data.weatherIcon = TemperatureData.WeatherIcon.Thunderstorm;
                            break;
                        case 15:
                            data.weatherIcon = TemperatureData.WeatherIcon.Haze;
                            break;
                        default:
                            data.weatherIcon = TemperatureData.WeatherIcon.Dunno;
                            break;


                        /* LIST OF WEATHER ON YR.NO
                        1 Sun
                        2 LightCloud
                        3 PartlyCloud
                        4 Cloud
                        5 LightRainSun
                        6 LightRainThunderSun
                        7 SleetSun
                        8 SnowSun
                        9 LightRain
                        10 Rain
                        11 RainThunder
                        12 Sleet
                        13 Snow
                        14 SnowThunder
                        15 Fog
                        20 SleetSunThunder
                        21 SnowSunThunder
                        22 LightRainThunder
                        23 SleetThunder
                        24 DrizzleThunderSun
                        25 RainThunderSun
                        26 LightSleetThunderSun
                        27 HeavySleetThunderSun
                        28 LightSnowThunderSun
                        29 HeavySnowThunderSun
                        30 DrizzleThunder
                        31 LightSleetThunder
                        32 HeavySleetThunder
                        33 LightSnowThunder
                        34 HeavySnowThunder
                        40 DrizzleSun
                        41 RainSun
                        42 LightSleetSun
                        43 HeavySleetSun
                        44 LightSnowSun
                        45 HeavysnowSun
                        46 Drizzle
                        47 LightSleet
                        48 HeavySleet
                        49 LightSnow
                        50 HeavySnow

                         */
                    }
                }

            // The longterm forecasts - Sits in the 25th tag
            Node longterm = metaNodes.item(24);

            // TIME IS ALWAYS REPRESENTED AS SUCH: 2015-10-26T10:00:00Z .
            String runtime = longterm.getAttributes().item(1).getNodeValue();
            runtime = runtime.substring(runtime.indexOf("T")+1, runtime.indexOf("Z"));
            String endtime = longterm.getAttributes().item(2).getNodeValue();
            endtime = endtime.substring(endtime.indexOf("T")+1, endtime.indexOf("Z"));


            if(longterm.getNodeType() == Node.ELEMENT_NODE){
                Element elements = (Element) longterm;



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
