package com.meowapp.testweather.utils;

import com.meowapp.testweather.R;

import java.util.HashMap;

/**
 * Created by Maggie on 10/11/2016.
 */


public class WeatherImg {

    /**
     * weatherImgMap map icon code into weather image
     */
    public static HashMap<String, Integer> weatherImgMap = new HashMap<String, Integer> (){
        {
            put("01", R.drawable.clearsky_01);
            put("02", R.drawable.few_clouds_02);
            put("03", R.drawable.scattered_clouds_03);
            put("04", R.drawable.broken_clouds_04);
            put("09", R.drawable.shower_rain_09);
            put("10", R.drawable.shower_rain_09);
            put("11", R.drawable.thunderstorm_11);
            put("13", R.drawable.snow_13);
            put("50", R.drawable.mist_50);
        }
    };

    /**
     *
     * @param weatherIcon
     * @return resId or null if not found
     */
    public static int getWeatherImg(String weatherIcon){
        return weatherImgMap.get(weatherIcon);
    }
}
