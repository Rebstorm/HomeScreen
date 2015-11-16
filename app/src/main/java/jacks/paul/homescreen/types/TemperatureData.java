package jacks.paul.homescreen.types;

/**
 * Created by Paul on 09/10/2015.
 */
public class TemperatureData {

    public enum WeatherIcon{
        Sunny,
        LightRain,
        Rain,
        Cloudy,
        LittleCloudy,
        PartlyCloudy,
        LightRainSun,
        LightRainThunderstorm,
        Sleet,
        SnowSun,
        Haze,
        Mostly_Cloudy,
        Slight_Drizzle,
        RainThunder,
        Thunderstorm,
        Snow,
        Dunno

    }

    public int id = 0;
    public double temperature = 0.0;
    public WeatherIcon weatherIcon = WeatherIcon.Thunderstorm;
    public String windDirection = "ERROR";
    public String lastUpdated = "N/A";
    public String humidity = "ERROR";


}
