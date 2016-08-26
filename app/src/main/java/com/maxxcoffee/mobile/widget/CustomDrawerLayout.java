package com.maxxcoffee.mobile.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import com.maxxcoffee.mobile.R;

/**
 * Created by rioswarawan on 7/30/16.
 */
public class CustomDrawerLayout extends DrawerLayout {
    public CustomDrawerLayout(Context context) {
        super(context);
    }

    public CustomDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (!this.isDrawerOpen(Gravity.LEFT)) {
            return super.dispatchTouchEvent(event);
        }
        boolean isOutSideClicked = false;
        if (event.getAction() == MotionEvent.ACTION_UP) {
            View content = findViewById(R.id.drawer_layout);
            int[] contentLocation = new int[2];
            content.getLocationOnScreen(contentLocation);
            Rect rect = new Rect(contentLocation[0],
                    contentLocation[1],
                    contentLocation[0] + content.getWidth(),
                    contentLocation[1] + content.getHeight());
            isOutSideClicked = !(rect.contains((int) event.getX(), (int) event.getY()));
        }
        this.setDrawerLockMode(isOutSideClicked ? DrawerLayout.LOCK_MODE_LOCKED_OPEN : DrawerLayout.LOCK_MODE_UNLOCKED);
        return super.dispatchTouchEvent(event);
    }
}
