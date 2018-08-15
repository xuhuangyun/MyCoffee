package com.fancoff.coffeemaker.ui.making;

import com.fancoff.coffeemaker.bean.MakingStatebean;
import com.fancoff.coffeemaker.bean.coffe.ImageBean;

import java.util.ArrayList;

/**
 * Created by apple on 2017/10/8.
 */

public class MakingPageContract {

    /**
     * V视图层
     */
    interface IMakingPageView {
        void showContent(String content);

        void showProgress(boolean show, int progress);

        void showVedio(ArrayList<ImageBean> list);
        void showGif(ArrayList<ImageBean> list);

        void showTitleBg(int res);

        void showStuckCupBtn(boolean show);
        void showErrorContent(String error);


    }

    /**
     * P视图与逻辑处理的连接层
     */
    interface IMakingPagePresenter {
        void showMakingContent(MakingStatebean makingStatebean);
        void cannotRemove();
        void stopMaking();
    }

    interface IMakingResultCallBack {
        void showContent(String content);

        void showProgress(boolean show, int progress);

        void showVedio(ArrayList<ImageBean> list);
        void showGif(ArrayList<ImageBean> list);
        void showTitleBg(int res);

        void showStuckCupBtn(boolean show);
        void showErrorContent(String error);
    }

    /**
     * 逻辑处理层
     */
    interface IMakingPageModel {
        void stopMaking();
        void cannotRemove();
        void showMakingContent(MakingStatebean payBean, IMakingResultCallBack cllback);
    }

}
