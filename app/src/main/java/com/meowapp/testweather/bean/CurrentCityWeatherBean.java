package com.meowapp.testweather.bean;

import com.meowapp.testweather.utils.FormatUtils;

import java.math.BigDecimal;
import java.util.List;

import static android.R.attr.x;
import static java.lang.Integer.parseInt;

/**
 * Created by Maggie on 10/5/2016.
 */

public class CurrentCityWeatherBean {

    public String base;
    public Clouds clouds;
    public String cod;
    public Coord coord;
    public String dt;
    public String id;
    public Main main;
    public String name;
    public Sys sys;
    public List<Weather> weather;
    public Wind wind;
    public Rain rain;
    public Snow snow;
    public String message;

    public class Clouds {
        public String all;
    }

    public class Coord {
        public String lat;
        public String lon;
    }

    public class Main {
        public String grnd_level;
        public String humidity;
        public String pressure;
        public String sea_level;
        public String temp;
        public String temp_max;
        public String temp_min;
    }

    public class Sys {
        public String type;
        public String id;
        public String country;
        public String message;
        public String sunrise;
        public String sunset;
    }

    public class Weather {
        public String description;
        public String icon;
        public String id;
        public String main;
    }

    public class Wind {
        public String deg;
        public String speed;
        public String gust;
    }

    public class Rain {
        public String _3h;
    }

    public class Snow {
        public String _3h;
    }

    /**
     *
     * @return int wind degree, or "0 if cannot no degree provided
     */
    public int getWindDegree() {
        int winDirection = 0;
        if (wind!=null && wind.deg!=null) {
            double degree = Double.parseDouble(wind.deg);
            winDirection = (int) degree;
        }
        return winDirection;
    }

    /**
     *
     * @return String wind direction, or "--" if cannot no temp provided
     */
    public String getWindDirection() {
        String winDirection = "--";
        if (wind!=null && wind.deg!=null) {
            double degree = Double.parseDouble(wind.deg)%360/45;
            String directions[] = {"N", "NE", "E", "SE", "S", "SW", "W", "NW", "N"};
            int i = Integer.parseInt(new BigDecimal(degree).setScale(0, BigDecimal.ROUND_HALF_UP)+"");
            winDirection = directions[i];
            //winDirection = directions[(int) Math.round(((degree % 360) / 45))];
        }
        return winDirection;
    }

    /**
     *
     * @return String temp, or "--" if cannot no temp provided
     */
    public String getTemp(){
        String temp = "--";
        if(main!=null && main.temp!=null){
            int tempInt = (int) Double.parseDouble(main.temp);
            temp = tempInt +""+ (char) 0x00B0 +"C";
        }
        return temp;
    }


    /**
     *
     * @return String humidity, or "--" if cannot no humidity provided
     */
    public String getHumidity(){
        String humidity = "--";
        if (main!=null && main.humidity!=null){
            humidity = main.humidity + "%";
        }
        return humidity;
    }


    /**
     *
     * @return String location, or "--" if cannot no location provided
     */
    public String getLocation(){
        String location = "--";
        if (name!=null){
            location = name;
        }
        return location;
    }

    /**
     *
     * @return String description, or "--" if cannot no description provided
     */
    public String getWeatherDescription(){
        String description="--";
        if (weather!=null && weather.get(0)!= null && weather.get(0).description!=null){
            description = weather.get(0).description;
        }
        return description;
    }

    /**
     *
     * @return String in degree format, or "--" if cannot find Lat Lon info
     */
    public String getLatLonString(){
        String latLonString = "--";
        if (coord!=null && coord.lat !=null && coord.lon!=null){
            double lat = Double.parseDouble(coord.lat);
            double lon = Double.parseDouble(coord.lon);
            latLonString = FormatUtils.getFormattedLocationInDegree(lat, lon);
        }
        return  latLonString;
    }

    /**
     *
     * @return String  "-1" if cannot find weather id
     */
    public String getWeatherIcon(){
        String weatherId = "-1";
        if(weather!=null && weather.get(0)!=null && weather.get(0).icon!=null){
            weatherId = weather.get(0).icon;
            weatherId = weatherId.replaceAll("[a-zA-Z]","");
        }
        return weatherId;
    }
}
