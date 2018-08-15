package com.fancoff.coffeemaker.ui.baseview;

/**
 * Created by apple on 2017/9/10.
 */
/**

 */
public interface IBaseView {

    public void onBack();
    void showProgress(String msg);
    void hideProgress();
    void showToast(String msg);
}
