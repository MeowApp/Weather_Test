package com.meowapp.testweather.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Single instance MyRequestQInstance
 *
 * Created by Maggie on 10/5/2016.
 */

public class MyRequestQInstance {

    private static MyRequestQInstance mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private MyRequestQInstance(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized MyRequestQInstance getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyRequestQInstance(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}
