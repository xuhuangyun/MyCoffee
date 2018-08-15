package com.fancoff.coffeemaker.ui.main;

import com.fancoff.coffeemaker.Application.DataCenter;
import com.fancoff.coffeemaker.bean.coffe.CoffeeConfigBean;

/**
 * Created by apple on 2017/9/10.
 */

public class MainPageMode implements MainPageContract.IMainPageModel {


    /**
     * P层会调用M层的该方法，参数为REF_TYP_ALL
     * 1、获得咖啡工艺；
     * 2、type类型：
     *    2.1、REF_TYPE_BANNER：
     *           回调P层的onResultBanner：返回顶部图片或视频列表；
     *    2.2、REF_TYP_GUANGAO：
     *           回调P层的onResultPath：返回左下图片列表；和有右下图片；
     *    2.3、REF_TYP_GOODS：
     *           回调P层的onResult：返回咖啡工艺；
     *    2.4、REF_TYPE_SCREEN：
     *           回调P层的onResultBannerScreen：返回屏保图片列表；
     *    2.5、REF_TYP_ALL：
     *           回调以上的所有：
     */
    @Override
    public void requestData(int type, final MainPageContract.IResultCallBack cllback) {

        CoffeeConfigBean configBean = DataCenter.getInstance().getNowCoffeeConfigBean();
        if (type == MainPageContract.REF_TYPE_BANNER) {
            cllback.onResultBanner(DataCenter.getInstance().getBannerlist());
        } else if (type == MainPageContract.REF_TYP_GUANGAO) {
            cllback.onResultPath(DataCenter.getInstance().getLeftBottoms(),
                    DataCenter.getInstance().getRightBottom1(),
                    DataCenter.getInstance().getRightBottom2(), DataCenter.getInstance().getRightBottom3());
        } else if (type == MainPageContract.REF_TYP_GOODS) {
            cllback.onResult(configBean);
        } else if (type == MainPageContract.REF_TYPE_SCREEN) {
            cllback.onResultBannerScreen(DataCenter.getInstance().getScreenBannerlist());
        } else if (type == MainPageContract.REF_TYP_ALL) {
            cllback.onResult(configBean);
            cllback.onResultPath(DataCenter.getInstance().getLeftBottoms(),
                    DataCenter.getInstance().getRightBottom1(),
                    DataCenter.getInstance().getRightBottom2(), DataCenter.getInstance().getRightBottom3());
            cllback.onResultBanner(DataCenter.getInstance().getBannerlist());
            cllback.onResultBannerScreen(DataCenter.getInstance().getScreenBannerlist());

        }


    }

}
