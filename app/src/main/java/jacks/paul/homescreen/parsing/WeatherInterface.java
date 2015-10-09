package jacks.paul.homescreen.parsing;

import jacks.paul.homescreen.types.TemperatureData;

/**
 * Created by Paul on 09/10/2015.
 */
public interface WeatherInterface {
    void dataReceived(TemperatureData data);
}
