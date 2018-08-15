package com.fancoff.coffeemaker.ui.baseview;

import android.view.View;

/**
 * Created by apple on 2017/9/10.
 */
/**

 */
public interface IBaseFragment extends  IBaseView{
    public int getLayoutId();
    public void initView(View v);
    public void initData();
    public boolean pauseMainPage();

}
