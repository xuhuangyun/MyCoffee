package com.fancoff.coffeemaker.ui.making;

import android.content.Context;

import com.fancoff.coffeemaker.bean.MakingStatebean;
import com.fancoff.coffeemaker.bean.coffe.ImageBean;

import java.util.ArrayList;

/**
 * Created by apple on 2017/9/10.
 */

public class MakingPagePresent implements MakingPageContract.IMakingPagePresenter {
    MakingPageContract.IMakingPageView imainview;
    MakingPageContract.IMakingPageModel mainMode;

    Context mContext;

    public MakingPagePresent(MakingPageContract.IMakingPageView v) {
        imainview = v;
        mainMode = MakingPageMode.getIns();
    }

    /**
     * 显示制作中的title背景图片、进度条、内容、图片、卡杯情况、错误情况、视频等；
     * 接口对象MakingPageContract.IMakingResultCallBack()方法的实现，M层的showMakingContent会回调这些方法
     * showTitleBg：M层返回title背景图片id给P层；P层调用V层进行图片显示；
     * showProgress：M层返回是否需要开进度条及进度值；P层调用V层进行进度条及进度值显示与否；
     * showContent：M层返回内容给P层；P层调用V层进行图片显示；
     * showStuckCupBtn：M层返回是否卡杯给P层；P层调用V层进行无法移除和确认移除的显示或者隐藏；
     * showErrorContent：M层返回是否有错误给P层；P层调用V层进行错误内容显示与否；
     * showVedio：M层返回是对应状态下的视频列表给P层；P层调用V层进行视频加载与播放；
     */
    @Override
    public void showMakingContent(MakingStatebean makingStatebean) {
        mainMode.showMakingContent(makingStatebean, new MakingPageContract.IMakingResultCallBack() {
            @Override
            public void showContent(String content) {
                imainview.showContent(content);
            }

            @Override
            public void showProgress(boolean show,int progress) {
                imainview.showProgress(show,progress);
            }

            @Override
            public void showVedio(ArrayList<ImageBean> list) {
                imainview.showVedio(list);
            }

            @Override
            public void showGif(ArrayList<ImageBean> list) {
                imainview.showGif(list);
            }

            @Override
            public void showTitleBg(int res) {
                imainview.showTitleBg(res);

            }

            @Override
            public void showStuckCupBtn(boolean show) {
                imainview.showStuckCupBtn(show);
            }

            @Override
            public void showErrorContent(String error) {
                imainview.showErrorContent(error);
            }
        });

    }

    @Override
    public void cannotRemove() {
        mainMode.cannotRemove();
    }

    /**
     * 调用M层停止制作：M层将会在任务列表中删除制作饮品的任务
     */
    @Override
    public void stopMaking() {
        mainMode.stopMaking();

    }
}
