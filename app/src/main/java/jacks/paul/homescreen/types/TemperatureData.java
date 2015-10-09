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
    public String WindDirection = "ERROR";
    public String WindSpeed = "N/A";
    public double Humidity = 0.0;


}
