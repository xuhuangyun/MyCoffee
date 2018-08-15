package com.fancoff.coffeemaker.ui.logo;

import android.os.Bundle;
import android.view.View;

import com.fancoff.coffeemaker.R;
import com.fancoff.coffeemaker.ui.baseview.BaseFragment;
import com.fancoff.coffeemaker.ui.commomView.MyButton;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.functions.Consumer;
/**
 * logo展示界面（暂时无用）
 */
public class LogoPageFragment extends BaseFragment implements LogoPageContract.ILogoPageView {

    MyButton btn_cancle;

    private LogoPagePresent mainPresent;

    public LogoPageFragment() {
    }

    public static LogoPageFragment newInstance(Bundle args) {
        LogoPageFragment fragment = new LogoPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static LogoPageFragment newInstance() {
        return newInstance(null);
    }

    @Override
    public void initData() {
        mainPresent = new LogoPagePresent(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_logo;
    }

    @Override
    public void initView(View v) {
        btn_cancle=(MyButton)v.findViewById(R.id.btn_cancle);

        RxView.clicks(btn_cancle).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                onBack();

            }
        });

    }
    @Override
    public boolean pauseMainPage() {
        return false;
    }
}
