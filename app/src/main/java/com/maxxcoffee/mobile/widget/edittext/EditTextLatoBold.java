package com.maxxcoffee.mobile.widget.edittext;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Rio Swarawan on 11/16/2015.
 */
public class EditTextLatoBold extends EditText {
    public EditTextLatoBold(Context context) {
        super(context);
    }

    public EditTextLatoBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface type = Typeface.createFromAsset(context.getAssets(), "font/Lato-Bold.ttf");
        setTypeface(type);
    }

}
