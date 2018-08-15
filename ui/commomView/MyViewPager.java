package com.fancoff.coffeemaker.ui.commomView;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.fancoff.coffeemaker.service.DurationUtil;

/**
 * Created by apple on 2017/10/12.
 */

public class MyViewPager extends ViewPager{
    public MyViewPager(Context context) {
        super(context);
    }
    boolean isCanScroll;
    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        DurationUtil.getIns().onTouchScreen(ev);
        return super.dispatchTouchEvent(ev);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        DurationUtil.getIns().onTouchScreen(ev);
        return isCanScroll && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        DurationUtil.getIns().onTouchScreen(ev);
        return isCanScroll && super.onTouchEvent(ev);

    }
}
