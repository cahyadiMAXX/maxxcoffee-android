package com.maxxcoffee.mobile.widget.textview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Rio Swarawan on 11/16/2015.
 */
public class TextViewLatoRegular extends TextView {

    public TextViewLatoRegular(Context context) {
        super(context);
        setFont(context);
    }

    public TextViewLatoRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context);
    }

    private void setFont(Context context) {
        Typeface type = Typeface.createFromAsset(context.getAssets(), "font/Lato-Regular.ttf");
        setTypeface(type);
    }
}
