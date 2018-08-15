package com.fancoff.coffeemaker.ui.commomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ProgressBar;

import com.fancoff.coffeemaker.service.DurationUtil;

/**
 * Created by apple on 2017/10/19.
 */

public class MyProgressBar extends ProgressBar{
    public MyProgressBar(Context context) {
        super(context);
    }

    public MyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected boolean dispatchHoverEvent(MotionEvent event) {
        DurationUtil.getIns().onTouchScreen(event);//NoActionTime = 0;
        return super.dispatchHoverEvent(event);
    }

}
