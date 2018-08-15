package com.fancoff.coffeemaker.ui.dialog;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import com.fancoff.coffeemaker.Application.DataCenter;
import com.fancoff.coffeemaker.Application.FilesManage;
import com.fancoff.coffeemaker.R;
import com.fancoff.coffeemaker.bean.coffe.ImageBean;
import com.fancoff.coffeemaker.ui.baseview.BaseFragment;
import com.fancoff.coffeemaker.ui.commomView.MyImageView;
import com.fancoff.coffeemaker.ui.commomView.MyLinearLayout;
import com.fancoff.coffeemaker.ui.commomView.MyRelativeLayout;
import com.fancoff.coffeemaker.ui.commomView.MyTextView;
import com.fancoff.coffeemaker.ui.commomView.MyVideoView;
import com.fancoff.coffeemaker.utils.MediaFile;
import com.fancoff.coffeemaker.utils.SizeUtil;
import com.fancoff.coffeemaker.utils.glide.ImageLoadUtils;
import com.fancoff.coffeemaker.utils.log4j.LogUtil;

import java.util.ArrayList;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * 制作故障退款页面
 */
public class DialogMakeErrorFragment extends BaseFragment {

    MyRelativeLayout root_layout;
    MyVideoView vedioView;
    MyTextView t_making_content;
    MyLinearLayout l_title_bg;
    MyRelativeLayout vedioViewParent;

    public DialogMakeErrorFragment() {
    }


    public static DialogMakeErrorFragment newInstance(Bundle args) {
        DialogMakeErrorFragment fragment = new DialogMakeErrorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static DialogMakeErrorFragment newInstance() {
        return newInstance(null);
    }

    @Override
    public void initData() {
        ArrayList<ImageBean> vedios = DataCenter.getInstance().getFailedVedio();

        if (vedios != null && vedios.size() > 0) {
            if (MediaFile.isVideoFileType(vedios.get(0).getUrl())) {
                showGif(null);
                showVedio(vedios);
            } else {
                showVedio(null);
                showGif(vedios);
            }

        } else {
            showVedio(null);
            showGif(null);
        }



    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_dialog_make_error;
    }

    @Override
    public void initView(View v) {
        LogUtil.action("open DialogMakeErrorFragment");
        l_title_bg = (MyLinearLayout) v.findViewById(R.id.l_title_bg);
        root_layout = (MyRelativeLayout) v.findViewById(R.id.root_layout);
        root_layout.setImage(R.drawable.cf_dialog_bg);
        vedioViewParent = (MyRelativeLayout) v.findViewById(R.id.vedioViewParent);
        vedioViewParent.setImage(R.drawable.cf_vedio_bg);
        t_making_content = (MyTextView) v.findViewById(R.id.t_making_content);
        t_making_content.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getNomalSize());
        vedioView = (MyVideoView) v.findViewById(R.id.img_show);
        myImageView = (MyImageView) v.findViewById(R.id.img_showgif);
        showTitleBg(R.drawable.cf_title_makeerror_bg);
        t_making_content.setText(getArguments().getString("content"));
    }

    @Override
    public boolean pauseMainPage() {
        return false;
    }


    @Override
    public void onDestroy() {
        LogUtil.action("close DialogMakeErrorFragment");
        vedioView.pause();
        vedioView.suspend();
        vedioView = null;
        myImageView.setImageBitmap(null);
        super.onDestroy();

    }

    MyImageView myImageView;

    public void showGif(ArrayList<ImageBean> list) {

        if (list == null || list.size() <= 0) {
            LogUtil.action("showGif:release");
            myImageView.setVisibility(View.GONE);
            ImageLoadUtils.getInstance().loadNetImage(myImageView, "");
            return;
        }
        LogUtil.action("showGif："+list.get(0).getUrl());
        myImageView.setVisibility(View.VISIBLE);
        ImageLoadUtils.getInstance().loadNetImage(myImageView, list.get(0).getUrl());
    }

    int iplay = 0;

    public void get(final ArrayList<ImageBean> list) {
        if (iplay >= list.size()) {
            iplay = 0;
        }
        vedioView.setVideoPath(list.get(iplay).getUrl());

        iplay++;
        IjkMediaPlayer.OnCompletionListener onCompletionListener = new IjkMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                if (list.size() == 1) {
                    iMediaPlayer.start();
                } else {
                    get(list);
                }

            }
        };
        vedioView.setOnCompletionListener(onCompletionListener);
        vedioView.start();
    }

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

    public void showTitleBg(int res) {
        l_title_bg.setImage(res);
    }


}
