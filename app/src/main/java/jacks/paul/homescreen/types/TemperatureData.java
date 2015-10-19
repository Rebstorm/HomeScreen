package jacks.paul.homescreen.types;

/**
 * Created by Paul on 09/10/2015.
 */
public class TemperatureData {

    public enum WeatherIcon{
        Sunny,
        Rain,
        Cloudy,
        Haze,
        Mostly_Cloudy,
        Slight_Drizzle,
        Thunderstorm,
        Snow
    }

    public double temperature = 0.0;
    public WeatherIcon weatherIcon = WeatherIcon.Thunderstorm;
    public String windDirection = "ERROR";
    public String windSpeed = "N/A";
    public String humidity = "ERROR";


}
