package com.fancoff.coffeemaker.ui.commomView;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.fancoff.coffeemaker.service.DurationUtil;

/**
 * Created by apple on 2017/10/18.
 */

public class MyRadioButton extends android.support.v7.widget.AppCompatRadioButton{

    public MyRadioButton(Context context) {
        super(context);
    }

    public MyRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        DurationUtil.getIns().onTouchScreen(ev);
        return super.dispatchTouchEvent(ev);
    }

    /**
     * ?textP?(0,2)???40????;(2,textP.length)???24????;
     * ???????????textP,???
     */
    public void setTextP(String textP) {
        SpannableStringBuilder sBuilder = new SpannableStringBuilder();
        sBuilder.append(textP);
        sBuilder.setSpan(new AbsoluteSizeSpan(40), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //(start,end)???  ????????????40???
        //??????????,??????????????
        sBuilder.setSpan(new AbsoluteSizeSpan(24), 2, textP.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(sBuilder, BufferType.SPANNABLE);
    }
}
