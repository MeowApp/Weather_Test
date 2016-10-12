package com.meowapp.testweather.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maggie on 10/11/2016.
 */

public class ForecastBean {
    public City city;
    public String cnt;
    public String cod;
    public List<DayWeather> list;
    public String message;

    public class City {
        public Coord coord;
        public String country;
        public String id;
        public String name;
        public String population;
    }

    public class Coord {
        public String lat;
        public String lon;
    }

    public class DayWeather {
        public String clouds;
        public String deg;
        public String dt;
        public String humidity;
        public String pressure;
        public String speed;
        public Temp temp;
        public List<ForecastWeather> weather;
    }

    public class Temp {
        public String day;
        public String eve;
        public String max;
        public String min;
        public String morn;
        public String night;
    }

    public class ForecastWeather {
        public String description;
        public String icon;
        public String id;
        public String main;
    }

    /**
     * @return List<UsefulInfo> for current response
     */
    public List<UsefulInfo> getDescription() {
        List<UsefulInfo> result = new ArrayList<>();
        if (list != null && list.size() != 0) {
            for (DayWeather dayWeather : list) {
                UsefulInfo usefulInfo = new UsefulInfo();
                if (dayWeather != null && dayWeather.temp != null && dayWeather.temp.day != null) {

                    int tempInt = (int) Double.parseDouble(dayWeather.temp.day);
                    usefulInfo.setTemp(tempInt + "" + (char) 0x00B0 + "C");
                }
                if (dayWeather != null && dayWeather.weather != null && dayWeather.weather.size() > 0 && dayWeather.weather.get(0) != null && dayWeather.weather.get(0).description != null) {
                    usefulInfo.setDescription(dayWeather.weather.get(0).description);
                }
                result.add(usefulInfo);
            }
        }
        return result;
    }

    /**
     * daily forecast object with useful info only
     */
    public class UsefulInfo {
        public String dayOfWeek = "";
        public String description = "no information";
        public String temp = "--";

        public void setDescription(String description) {
            this.description = description;
        }

        public void setTemp(String temp) {
            this.temp = temp;
        }

        public void setDayOfWeek(String dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }
    }

}
