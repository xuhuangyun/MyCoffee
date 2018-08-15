package com.fancoff.coffeemaker.ui.demo;

import android.os.Bundle;
import android.view.View;

import com.fancoff.coffeemaker.R;
import com.fancoff.coffeemaker.ui.baseview.BaseFragment;
import com.fancoff.coffeemaker.ui.commomView.MyButton;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.functions.Consumer;
/*
 *MVP模式示例代码
 */
public class DemoPageFragment extends BaseFragment implements DemoPageContract.IDemoPageView {

    MyButton btn_cancle;
    private DemoPagePresent mainPresent;

    public DemoPageFragment() {
    }

    public static DemoPageFragment newInstance(Bundle args) {
        DemoPageFragment fragment = new DemoPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static DemoPageFragment newInstance() {
        return newInstance(null);
    }

    @Override
    public void initData() {
        mainPresent = new DemoPagePresent(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_demo;
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
