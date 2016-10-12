package com.meowapp.testweather.utils;

import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by Maggie on 10/6/2016.
 */


public enum FontManager {
    TYPEFACE;

    private static Typeface typefaceCourierNew;
    private static Typeface typefaceNanoSans;

    /**
     *
     * @param textView pass a TextView to be change to CourierNew font
     */
    public void setCourierNewTypeface(TextView textView) {
        if (typefaceCourierNew == null)
            typefaceCourierNew = Typeface.createFromAsset(textView.getContext().getAssets(), "fonts/CourierNew.ttf");
        textView.setTypeface(typefaceCourierNew);
    }

    /**
     *
     * @param textView pass a TextView to be change to NanoSans font
     */
    public void setNanoSansTypeface(TextView textView) {
        if (typefaceNanoSans == null)
            //typefaceNanoSans = Typeface.createFromAsset(textView.getContext().getAssets(), "fonts/NotoSansCJKsc-Medium.otf");
            typefaceNanoSans = Typeface.createFromAsset(textView.getContext().getAssets(), "fonts/NotoSans-Bold.ttf");
        textView.setTypeface(typefaceNanoSans);
    }
}