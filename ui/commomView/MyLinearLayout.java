package com.fancoff.coffeemaker.ui.commomView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.fancoff.coffeemaker.service.DurationUtil;
import com.fancoff.coffeemaker.utils.glide.ImageLoadUtils;

/**
 * Created by apple on 2017/10/12.
 */
/**
 *
 */
public class MyLinearLayout extends LinearLayout{
    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        DurationUtil.getIns().onTouchScreen(event);//NoActionTime = 0;
        return super.onTouchEvent(event);
    }

    /**

     */
    public void setImage(int resImage) {
        ImageLoadUtils.getInstance().setLayoutImage(this,resImage);
    }
}
