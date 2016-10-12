package com.meowapp.testweather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.meowapp.testweather.bean.CurrentCityWeatherBean;
import com.meowapp.testweather.bean.ForecastBean;
import com.meowapp.testweather.utils.BitmapUtils;
import com.meowapp.testweather.utils.Constants;
import com.meowapp.testweather.utils.FontManager;
import com.meowapp.testweather.utils.GPSTracker;
import com.meowapp.testweather.utils.GsonRequest;
import com.meowapp.testweather.utils.MyRequestQInstance;
import com.meowapp.testweather.utils.PermissionUtils;
import com.meowapp.testweather.utils.WeatherImg;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    //protected static final String TAG = "MainActivity";

    TextView tv_address;
    TextView tv_latlon;
    TextView tv_description;
    TextView tv_temp;
    TextView tv_humidity;
    TextView tv_wind;
    ImageView iv_weather;
    ImageView iv_refresh;
    ImageView iv_wind;


    /**
     * Provides the entry point to Google Play services.
     */
    GoogleApiClient mGoogleApiClient;

    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * if isRefreshing should not let user press refresh button
     */
    boolean isRefreshing = false;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_latlon = (TextView) findViewById(R.id.tv_latlon);
        tv_description = (TextView) findViewById(R.id.tv_description);
        tv_temp = (TextView) findViewById(R.id.tv_temp);
        tv_humidity = (TextView) findViewById(R.id.tv_humidity);
        tv_wind = (TextView) findViewById(R.id.tv_wind);
        iv_weather = (ImageView) findViewById(R.id.iv_weather);
        iv_refresh = (ImageView) findViewById(R.id.iv_refresh);
        iv_wind = (ImageView) findViewById(R.id.iv_wind);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // customize TextView Fonts
        FontManager.TYPEFACE.setCourierNewTypeface(tv_latlon);
        FontManager.TYPEFACE.setNanoSansTypeface(tv_address);
        FontManager.TYPEFACE.setNanoSansTypeface(tv_description);
        FontManager.TYPEFACE.setNanoSansTypeface(tv_temp);
        FontManager.TYPEFACE.setNanoSansTypeface(tv_humidity);
        FontManager.TYPEFACE.setNanoSansTypeface(tv_wind);


        // check permission, request runtime permission for API > 23
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission to access the location is missing. Request a permission
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, false);

        } else {
            //already have permission
            buildGoogleApiClient();
        }


        iv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRefreshing) {
                    return;
                }
                startRefresh();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        //Log.d(TAG, "buildGoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        //Log.d(TAG, "onConnected");

        // GoogleApiClient object successfully connects, get actual location and weather
        getActualLocationAndWeather();
    }

    /**
     * GoogleApiClient object successfully connects, get actual location and weather
     */
    private void getActualLocationAndWeather() {

        // get current location
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        } catch (SecurityException e) {
            //unreachable
            return;
        }

        // get current location success
        if (mLastLocation != null) {

            //Log.d(TAG, "Current Location" + mLastLocation.getLatitude() + ", " + mLastLocation.getLongitude());
            getCurrentWeather();

        } else {    //get current location fail

            //Log.d(TAG, "mLastLocation null");
            Toast.makeText(this, "Current Location not known, please make sure the Location service is ON, will show default city Singapore weather", Toast.LENGTH_LONG).show();
            getDefaultLocationWeather();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        //Log.d(TAG, "onRequestPermissionsResult");

        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {

            //permission granted

            //Log.d(TAG, "isPermissionGranted");
            buildGoogleApiClient();

        } else {
            //Log.d(TAG, "Permission not Granted");

            // permission not granted and show default city weather
            Toast.makeText(this, "No permission to locate the current city, will show default city Singapore weather", Toast.LENGTH_LONG).show();

            getDefaultLocationWeather();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

        //Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());

        // ErrorCode() == 2 means AVD google play service not up to date, try use GPS service
        if (result.getErrorCode() == 2) {
            GPSTracker gps = new GPSTracker(MainActivity.this);

            // Check if GPS enabled
            if (gps.canGetLocation()) {

                mLastLocation = gps.getLocation();
                getCurrentWeather();

            } else {    // GPS not enabled
                getDefaultLocationWeather();
                Toast.makeText(this, "Location service is OFF, will show default city Singapore weather", Toast.LENGTH_LONG).show();
            }
        }else {

            // conncetion failed for other reasons, show default

            getDefaultLocationWeather();
            Toast.makeText(this, "Location service failed, will show default city Singapore weather", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. Call connect() to
        // attempt to re-establish the connection.
        //Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    /**
     * use mLastLocation to get actual location weather
     */
    private void getCurrentWeather() {

        if (mLastLocation != null) {
            //Log.d(TAG, "have actual location");
            String latlon = "lat=" + mLastLocation.getLatitude() + "&lon=" + mLastLocation.getLongitude();
            request(latlon);
        }

    }

    /**
     * get default city Singapore weather
     */
    private void getDefaultLocationWeather() {
        String city = Constants.DEFAULT_SINGAPORE;
        request(city);
    }

    /**
     * generate GsonRequest
     * @param url_location
     */
    private void request(final String url_location) {

        isRefreshing = true;
        showRefreshAnimation();

        // request current weather

        final String completeCurrentUrl = Constants.URL_HEAD + Constants.CURRENT + url_location + Constants.URL_TAIL;
        GsonRequest<CurrentCityWeatherBean> gsonRequest = new GsonRequest<>(completeCurrentUrl, CurrentCityWeatherBean.class, null, new Response.Listener<CurrentCityWeatherBean>() {
            @Override
            public void onResponse(CurrentCityWeatherBean response) {

                //Log.d(TAG, completeCurrentUrl);
                tv_address.setText(response.getLocation());
                tv_latlon.setText(mLastLocation == null ? "Current Location unknown" : response.getLatLonString());
                tv_description.setText(response.getWeatherDescription());
                tv_temp.setText(response.getTemp());
                tv_humidity.setText(response.getHumidity());
                tv_wind.setText(response.getWindDirection());
                iv_wind.setRotation(180 + response.getWindDegree());

                // init weather image
                Integer resId = WeatherImg.getWeatherImg(response.getWeatherIcon());
                if (resId != null)
                    loadWeatherImg(resId);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tv_address.setText("Network Error");
                //Log.d(TAG, error.toString());
                Toast.makeText(MainActivity.this, "Network Error", Toast.LENGTH_LONG).show();
            }
        });

        MyRequestQInstance.getInstance(this).addToRequestQueue(gsonRequest);

        // requesst weather forecast

        final String completeForecastUrl = Constants.URL_HEAD + Constants.FORECAST + url_location + Constants.URL_TAIL;
        GsonRequest<ForecastBean> gsonForecastRequest = new GsonRequest<>(completeForecastUrl, ForecastBean.class, null, new Response.Listener<ForecastBean>() {
            @Override
            public void onResponse(ForecastBean response) {

                //Log.d(TAG, completeForecastUrl);

                List<ForecastBean.UsefulInfo> usefulInfos = response.getDescription();

                // init DayOfWeek

                Calendar calendar = Calendar.getInstance();
                for (int i = 0; i < usefulInfos.size(); i++) {
                    calendar.add(Calendar.DATE, 1);
                    DateFormat sdf = new SimpleDateFormat("EEEE");
                    String dayOfWeek = sdf.format(calendar.getTime());
                    usefulInfos.get(i).setDayOfWeek(dayOfWeek);
                }

                //init recyclerView

                mRecyclerView.setHasFixedSize(true);
                // use a linear layout manager
                mLayoutManager = new LinearLayoutManager(MainActivity.this);
                mRecyclerView.setLayoutManager(mLayoutManager);

                mRecyclerView.setAdapter(new RVAdapter(usefulInfos));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d(TAG, error.toString());
                Toast.makeText(MainActivity.this, "Network Error", Toast.LENGTH_LONG).show();
            }
        });

        MyRequestQInstance.getInstance(this).addToRequestQueue(gsonForecastRequest);
    }

    /**
     * show weather image
     * @param resId
     */
    private void loadWeatherImg(Integer resId) {

        Bitmap bm = BitmapUtils.decodeSampledBitmapFromResource(getResources(), resId, iv_weather.getWidth(), iv_weather.getHeight());
        iv_weather.setImageBitmap(bm);
    }

    /**
     *  get refresh data
     */
    public void startRefresh() {

        showRefreshAnimation();

        //location request fail last time, get default city
        if (mLastLocation == null) {
            getDefaultLocationWeather();
        } else {
            // have actual location, refresh for current location weather
            getActualLocationAndWeather();
        }
    }

    /**
     * showRefreshAnimation
     */
    private void showRefreshAnimation() {
        Animation animation = new RotateAnimation(0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setRepeatCount(1);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isRefreshing = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isRefreshing = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        iv_refresh.clearAnimation();
        iv_refresh.startAnimation(animation);
    }
}
