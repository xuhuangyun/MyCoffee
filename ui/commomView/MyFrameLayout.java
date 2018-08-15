package com.fancoff.coffeemaker.ui.commomView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.fancoff.coffeemaker.service.DurationUtil;

/**
 * Created by apple on 2017/10/12.
 */

public class MyFrameLayout extends FrameLayout{
    public MyFrameLayout(@NonNull Context context) {
        super(context);
    }

    public MyFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        DurationUtil.getIns().onTouchScreen(event);
        return super.onTouchEvent(event);
    }
}
