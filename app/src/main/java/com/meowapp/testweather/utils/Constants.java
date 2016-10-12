package com.meowapp.testweather.utils;

import static android.R.attr.data;
import static android.R.attr.mode;
import static android.os.Build.ID;
import static com.google.android.gms.measurement.internal.zzl.api;

/**
 * Created by Maggie on 10/9/2016.
 */

public class Constants {

    public static final String URL_HEAD = "http://api.openweathermap.org/data/2.5/";

    public static final String FORECAST = "forecast/daily?cnt=7&";
    public static final String CURRENT = "weather?";

    public static final String DEFAULT_SINGAPORE = "q=Singapore";

    public static final String URL_TAIL = "&units=metric&appid=022a1ba5ba76547d032d2c40aa293bc4";

}
