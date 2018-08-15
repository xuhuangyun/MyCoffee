package com.fancoff.coffeemaker.ui.main;

import android.content.Context;

import com.fancoff.coffeemaker.bean.coffe.CoffeeConfigBean;
import com.fancoff.coffeemaker.bean.coffe.ImageBean;

import java.util.ArrayList;

/**
 * Created by apple on 2017/9/10.
 */

public class MainPagePresent implements MainPageContract.IMainPagePresenter {
    MainPageContract.IMainPageView imainview;
    MainPageContract.IMainPageModel mainMode;

    Context mContext;

    public MainPagePresent(MainPageContract.IMainPageView v) {
        imainview = v;
        mainMode = new MainPageMode();
    }

    /**
     * V层的initData()和refAllView()会调用该方法；
     * P层会调用M层的requestData，参数REF_TYP_ALL；
     * M层会回调P层的接口实现方法：
     *    type类型：
     *    1、REF_TYPE_BANNER：
     *           M层会回调P层的onResultBanner：返回顶部图片或视频列表；
     *           P层会调用V层的showBarnner：顶部图片或视频进行轮播
     *    2、REF_TYP_GUANGAO：
     *           M层会回调P层的onResultPath：返回顶部图片或视频列表；
     *           P层会调用V层的showImage：左下图片轮播；右下图片进行显示
     *    3、REF_TYP_GOODS：
     *           M层会回调P层的onResult：返回咖啡工艺；
     *           P层会调用V层的showGoodsFragments：设置大类图片加深（咖啡、奶类、餐包）；更新饮品菜单；
     *    4、REF_TYPE_SCREEN：
     *           M层会回调P层的onResultBannerScreen：返回屏保图片列表；
     *           P层会调用V层的showBarnnerScreen：暂时为空
     *    5、REF_TYP_ALL：
     *           M层会回调P层以上的所有：包括顶部图片视频进行轮播，左下图片进行轮播，右下图片进行显示，
     *                                   设置tab菜单图片加深，更新饮品等。
     */
    @Override
    public void requestData(int type) {
        mainMode.requestData(type, new MainPageContract.IResultCallBack() {
            @Override
            public void onResult(CoffeeConfigBean goodsListBean) {
                imainview.showGoodsFragments(goodsListBean);
            }

            @Override
            public void onResultBannerScreen(ArrayList<ImageBean> list) {
                imainview.showBarnnerScreen(list);
            }

            @Override
            public void onResultBanner(ArrayList<ImageBean> list) {
                imainview.showBarnner(list);

            }


            @Override
            public void onResultPath(ArrayList<ImageBean> left_bottoms, ImageBean right_bottom1, ImageBean right_bottom2, ImageBean right_bottom3) {
                imainview.showImage(left_bottoms, right_bottom1, right_bottom2, right_bottom3);
            }


        });


    }


}
