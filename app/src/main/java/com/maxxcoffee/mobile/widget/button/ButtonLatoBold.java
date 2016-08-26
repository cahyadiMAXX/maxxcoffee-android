package com.maxxcoffee.mobile.widget.button;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Rio Swarawan on 11/16/2015.
 */
public class ButtonLatoBold extends Button {
    public ButtonLatoBold(Context context) {
        super(context);
    }

    public ButtonLatoBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface type = Typeface.createFromAsset(context.getAssets(), "font/HelveticaNeueLTPro-Lt.otf");
//        Typeface type = Typeface.createFromAsset(context.getAssets(), "font/Lato-Bold.ttf");
        setTypeface(type);
    }

}
