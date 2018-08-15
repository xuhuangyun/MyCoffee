package com.fancoff.coffeemaker.ui.commomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.fancoff.coffeemaker.service.DurationUtil;

/**
 * Created by apple on 2017/10/19.
 */

public class MySeekBar extends android.support.v7.widget.AppCompatSeekBar{


    public MySeekBar(Context context) {
        super(context);
    }

    public MySeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected boolean dispatchHoverEvent(MotionEvent event) {
        DurationUtil.getIns().onTouchScreen(event);
        return super.dispatchHoverEvent(event);
    }

}
