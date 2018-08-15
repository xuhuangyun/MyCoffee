package com.fancoff.coffeemaker.ui.making;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import com.fancoff.coffeemaker.Application.MyApp;
import com.fancoff.coffeemaker.R;
import com.fancoff.coffeemaker.bean.MakingStatebean;
import com.fancoff.coffeemaker.bean.coffe.ImageBean;
import com.fancoff.coffeemaker.ui.baseview.BaseFragment;
import com.fancoff.coffeemaker.ui.commomView.MyImageView;
import com.fancoff.coffeemaker.ui.commomView.MyLinearLayout;
import com.fancoff.coffeemaker.ui.commomView.MyRelativeLayout;
import com.fancoff.coffeemaker.ui.commomView.MyTextView;
import com.fancoff.coffeemaker.ui.commomView.MyVideoView;
import com.fancoff.coffeemaker.utils.SizeUtil;
import com.fancoff.coffeemaker.utils.StringUtil;
import com.fancoff.coffeemaker.utils.ToastUtil;
import com.fancoff.coffeemaker.utils.glide.ImageLoadUtils;
import com.fancoff.coffeemaker.utils.log4j.LogUtil;
import com.jakewharton.rxbinding2.view.RxView;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;
import me.zhouzhuo.zzhorizontalprogressbar.ZzHorizontalProgressBar;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/*
    制作界面
 */
public class MakingPageFragment extends BaseFragment implements MakingPageContract.IMakingPageView {

    private MakingPagePresent mainPresent;
    ZzHorizontalProgressBar number_progress_bar;
    MyRelativeLayout root_layout;
    MyVideoView vedioView;
    MyTextView t_title, t_title_en, t_making_content, t_cancle, t_ok, t_making_error;
    MyLinearLayout l_title_bg;
    MyRelativeLayout vedioViewParent, r_progress;
    MyImageView myImageView;    //新加

    public MakingPageFragment() {
    }

    /**新建MakingPageFragment对象fragment，传入参数args："obj", makingStatebean*/
    public static MakingPageFragment newInstance(Bundle args) {
        MakingPageFragment fragment = new MakingPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static MakingPageFragment newInstance() {
        return newInstance(null);
    }

    /**
     * 1、新建P层对象实例；
     * 2、获取当前制作的状态，并进行状态显示：title背景、文字内容、错误提示、视频等
     */
    @Override
    public void initData() {
        mainPresent = new MakingPagePresent(this);
        mainPresent.showMakingContent((MakingStatebean) getArguments().getSerializable("obj"));
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_making;
    }

    /**
     * 1、获取各控件的id；
     * 2、无法移除、确认移除按键监听：
     *    确认移除：
     *        成功：发送ACTION_PROGRESS消息（正在准备重新制作）到MainActivity，MAinActivity进行显示
     *              重新制作饮品；
     *              UI线程中，隐藏progress，并发送ACTION_BACK到MAinActivity中
     *        失败：UI线程中，用toast显示失败消息；隐藏progress；
     *    无法移除：设置制作状态的stuckCupTime=1，发送ACTION_BACK到MAinActivity中
     */
    @Override
    public void initView(View v) {
        LogUtil.action("open MakingPageFragment");
        t_making_error = (MyTextView) v.findViewById(R.id.t_making_error);
        t_cancle = (MyTextView) v.findViewById(R.id.t_cancle);
        t_ok = (MyTextView) v.findViewById(R.id.t_ok);
        l_title_bg = (MyLinearLayout) v.findViewById(R.id.l_title_bg);
        root_layout = (MyRelativeLayout) v.findViewById(R.id.root_layout);
        root_layout.setImage(R.drawable.cf_dialog_bg);
        r_progress = (MyRelativeLayout) v.findViewById(R.id.r_progress);
        r_progress.setImage(R.drawable.cf_progressbg);
        vedioViewParent = (MyRelativeLayout) v.findViewById(R.id.vedioViewParent);
        vedioViewParent.setImage(R.drawable.cf_vedio_bg);
        t_title = (MyTextView) v.findViewById(R.id.t_title);
        t_title_en = (MyTextView) v.findViewById(R.id.t_title_en);
        t_making_content = (MyTextView) v.findViewById(R.id.t_making_content);
        t_making_error.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getNomalSize());
        t_making_content.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getNomalSize());
        t_cancle.setImage(R.drawable.cf_btn_remove_bg);
        t_ok.setImage(R.drawable.cf_btn_remove_bg);
        number_progress_bar = (ZzHorizontalProgressBar) v.findViewById(R.id.number_progress_bar);
        vedioView = (MyVideoView) v.findViewById(R.id.img_show);
        myImageView = (MyImageView) v.findViewById(R.id.img_showgif);//新加
        RxView.clicks(t_ok).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                showProgress(getString(R.string.restartMake));
                MakingPageMode.getIns().restartMaking(new MakingPageMode.RestartMakingCallBack() {
                    @Override
                    public void onSuccess() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideProgress();
                                onBack();
                            }
                        });
                    }

                    @Override
                    public void onFailed(final String msg) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showToast(getActivity(), msg);
                                hideProgress();
                            }
                        });
                    }
                });


            }
        });
        RxView.clicks(t_cancle).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                mainPresent.cannotRemove();
                onBack();
            }
        });

    }

    @Override
    public boolean pauseMainPage() {
        return false;
    }


    /**
     * 1、隐藏进度条；
     * 2、打印日志：close MakingPageFragment
     * 3、调用P层的停止制作；P层会调用M层的停止制作；
     * 4、img_show暂停，释放
     */
    @Override
    public void onDestroy() {
        hideProgress();
        LogUtil.action("close MakingPageFragment");
        mainPresent.stopMaking();
        vedioView.pause();
        vedioView.suspend();
        vedioView=null;
        myImageView.setImageBitmap(null);
        if (MyApp.oPenLeakCanary) {
            RefWatcher refWatcher = MyApp.getRefWatcher(getActivity());
            refWatcher.watch(this);
        }
        //在Activity或Fragment 的 Destroy方法中添加检测
        // （很好理解，就是判断一个Activity或Fragment想要被销毁的时候，
        // 是否还有其他对象持有其引用导致Activity或Fragment不能被回收，从而导致内存泄露)
        super.onDestroy();
    }

    /**显示t_making_content;并打印日志*/
    @Override
    public void showContent(String content) {
        t_making_content.setText(content);
        LogUtil.action(content);
    }

    /**传入进度值，进度条是否可视*/
    @Override
    public void showProgress(boolean show, int progress) {
        number_progress_bar.setProgress(progress);
        r_progress.setVisibility(show ? View.VISIBLE : View.GONE);

    }

    int iplay = 0;

    /**
     * 加载视频进行播放
     */
    public void get(final ArrayList<ImageBean> list) {
        if (iplay >= list.size()) {
            iplay = 0;
        }
        vedioView.setVideoPath(list.get(iplay).getUrl());//获得当前iplay的url给img_show路径
        iplay++;
        IjkMediaPlayer.OnCompletionListener onCompletionListener = new IjkMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                LogUtil.action("showVedio:onCompletion");
                if (list.size() == 1) {//只有1个视频循环播放
                    iMediaPlayer.start();
                } else {//多个视频，递归调用播放
                    get(list);
                }

            }
        };
        vedioView.setOnCompletionListener(onCompletionListener);
        vedioView.start();

    }

    /**
     * 1、视频列表为空，id_=img_show为不可视；
     * 2、视频列表不为空，id=img_show可视：加载视频进行播放
     */
    @Override
    public void showVedio(final ArrayList<ImageBean> list) {
        LogUtil.action("showVedio");
        if (list == null || list.size() <= 0) {
            LogUtil.action("showVedio:release");
            vedioView.suspend();
            vedioView.setVisibility(View.GONE);
            return;
        }
        vedioView.setVisibility(View.VISIBLE);
        iplay = 0;
        get(list);

    }

//新加
    @Override
    public void showGif(ArrayList<ImageBean> list) {
        LogUtil.action("showGif");
        if (list == null || list.size() <= 0) {
            LogUtil.action("showGif:release");
            myImageView.setVisibility(View.GONE);
            ImageLoadUtils.getInstance().loadNetImage(myImageView, "");
            return;
        }
        myImageView.setVisibility(View.VISIBLE);
        ImageLoadUtils.getInstance().loadNetImage(myImageView, list.get(0).getUrl());
    }

    @Override
    public void showTitleBg(int res) {
        l_title_bg.setImage(res);
    }

    /**
     * 根据是否卡杯，进行无法移除和确认移除两个按钮显示或隐藏
     */
    @Override
    public void showStuckCupBtn(boolean show) {
        if (show) {
            t_cancle.setVisibility(View.VISIBLE);
            t_ok.setVisibility(View.VISIBLE);
        } else {
            t_cancle.setVisibility(View.GONE);
            t_ok.setVisibility(View.GONE);
        }

    }

    /**根据是否有错误信息error，对t_making_error进行显示或者隐藏；设置错误内容到t_making_error进行显示*/
    @Override
    public void showErrorContent(String error) {
        if (StringUtil.isStringEmpty(error)) {
            t_making_error.setVisibility(View.GONE);
        } else {
            t_making_error.setVisibility(View.VISIBLE);
        }
        t_making_error.setText(error);

    }

}
