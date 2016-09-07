package com.maxxcoffee.mobile.widget.edittext;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.widget.EditText;

import com.maxxcoffee.mobile.R;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

/**
 * Created by Rio Swarawan on 11/16/2015.
 */
public class EditTextShowPassword extends ShowHidePasswordEditText {
    public EditTextShowPassword(Context context) {
        super(context);
    }

    public EditTextShowPassword(Context context, AttributeSet attrs) {
        super(context, attrs);
//        Typeface type = Typeface.createFromAsset(context.getAssets(), "font/Lato-Bold.ttf");
        Typeface type = Typeface.createFromAsset(context.getAssets(), "font/HelveticaNeueLTPro-Lt.otf");
        setTypeface(type);
    }

    @Override
    public void setTintColor(@ColorInt int tintColor) {
        super.setTintColor(getResources().getColor(R.color.black_transparent));
    }
}
