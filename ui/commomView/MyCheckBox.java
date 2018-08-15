package com.fancoff.coffeemaker.ui.commomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.fancoff.coffeemaker.service.DurationUtil;

/**
 * Created by apple on 2018/3/29.
 */

public class MyCheckBox extends android.support.v7.widget.AppCompatCheckBox{
    public MyCheckBox(Context context) {
        super(context);
    }

    public MyCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected boolean dispatchHoverEvent(MotionEvent event) {
        DurationUtil.getIns().onTouchScreen(event);
        return super.dispatchHoverEvent(event);
    }

}
