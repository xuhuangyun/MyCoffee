package com.fancoff.coffeemaker.ui.commomView;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.fancoff.coffeemaker.service.DurationUtil;

/**
 * Created by apple on 2017/10/12.
 */

public class MyTabLayout extends TabLayout {
    public MyTabLayout(Context context) {
        super(context);
    }

    public MyTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        DurationUtil.getIns().onTouchScreen(ev);
        return super.dispatchTouchEvent(ev);
    }
}
