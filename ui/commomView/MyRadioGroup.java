package com.fancoff.coffeemaker.ui.commomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RadioGroup;

import com.fancoff.coffeemaker.service.DurationUtil;

/**
 * Created by apple on 2017/10/18.
 */

public class MyRadioGroup extends RadioGroup{
    public MyRadioGroup(Context context) {
        super(context);
    }

    public MyRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        DurationUtil.getIns().onTouchScreen(ev);
        return super.dispatchTouchEvent(ev);
    }
}
