package com.fancoff.coffeemaker.ui.main;

import com.fancoff.coffeemaker.bean.coffe.CoffeeConfigBean;
import com.fancoff.coffeemaker.bean.coffe.ImageBean;

import java.util.ArrayList;

/**
 * Created by apple on 2017/10/8.
 */

public class MainPageContract {
    public static final int REF_TYPE_SCREEN = 5;
    public static final int REF_TYPE_BANNER = 1;
    public static final int REF_TYP_GOODS = 2;
    public static final int REF_TYP_GUANGAO = 3;
    public static final int REF_TYP_ALL = 4;


    /**
     * V视图层
     */
    interface IMainPageView {
        void showProgress();//可以显示进度条

        void hideProgress();//可以隐藏进度条

        void showGoodsFragments(CoffeeConfigBean coffeeConfigBean);
        void showBarnnerScreen(ArrayList<ImageBean> list);
        void showBarnner(ArrayList<ImageBean> list);

        void showImage(ArrayList<ImageBean> left_bottoms, ImageBean right_bottom1,
                       ImageBean right_bottom2,
                       ImageBean right_bottom3);


    }

    /**
     * P视图与逻辑处理的连接层
     */
    interface IMainPagePresenter {
        void requestData(int type);
    }

    interface IResultCallBack {
        public void onResult(CoffeeConfigBean coffeeConfigBean);
        public void onResultBannerScreen(ArrayList<ImageBean> list);
        public void onResultBanner(ArrayList<ImageBean> list);


        public void onResultPath(ArrayList<ImageBean> left_bottoms, ImageBean right_bottom1,
                                 ImageBean right_bottom2,
                                 ImageBean right_bottom3);
    }

    /**
     * 逻辑处理层
     */
    interface IMainPageModel {
        void requestData(int type, IResultCallBack cllback);
    }

}
