package com.fancoff.coffeemaker.ui.baseview;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fancoff.coffeemaker.Application.MyConstant;
import com.fancoff.coffeemaker.R;
import com.fancoff.coffeemaker.bean.MessageBean;
import com.fancoff.coffeemaker.bean.RemoveEvent;
import com.fancoff.coffeemaker.service.DurationUtil;
import com.fancoff.coffeemaker.ui.commomView.MyLinearLayout;
import com.fancoff.coffeemaker.ui.commomView.MyProgressBar;
import com.fancoff.coffeemaker.ui.commomView.MyTextView;
import com.fancoff.coffeemaker.utils.SizeUtil;
import com.fancoff.coffeemaker.utils.ToastUtil;
import com.jakewharton.rxbinding2.view.RxView;
import com.trello.rxlifecycle2.components.support.RxFragment;

import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 * 基础Fragment类，所有Fragment都需要继承改BaseFragment
 */

/**
 *
 */
public abstract class BaseFragment extends RxFragment implements IBaseFragment {
    private Bundle args;

    private OnFragmentInteractionListener mListener;

    public BaseFragment() {
        // Required empty public constructor
    }

    RemoveEvent removeEvent;

    /**
     * 根据传入参数touchResetTime设置不同的controlType；
     * 向RemoveEvent对象removeEvent中传入tag、controlType、time参数
     * removeEvent:
     *    tag = BaseFragment
     *    controlType = TYPE_TOUCH_RESET_TIME/TYPE_TOUCH_NO_RESET_TIME
     *    timeToRemove = time;
     * 1、工程菜单显示showSettingPageFragment会调用addControlOutTime(),该对话框的移除事件；
     */
    public void addControlOutTime(int time, boolean touchResetTime) {
        if (touchResetTime) {
            removeEvent = new RemoveEvent(this.getClass().getName(), RemoveEvent.TYPE_TOUCH_RESET_TIME, time);
        } else {
            removeEvent = new RemoveEvent(this.getClass().getName(), RemoveEvent.TYPE_TOUCH_NO_RESET_TIME, time);
        }

    }

    /**
     * 隐藏progerss： 接口IBaseView中抽象方法hideProgress()的实现
     * localProgressView
     *     true：隐藏progressLayout；
     *     false：将消息HIDE_PROGRESS发送到MainActivity中；
     */
    public void hideProgress() {
        if (localProgressView) {
            hideProgressThisFragment();
        } else {
            sendMsgToActivity(new MessageBean(MessageBean.ACTION_HIDEPROGRESS, MyConstant.WHAT.HIDE_PROGRESS));
        }

    }

    /**
     * 显示progerss：接口IBaseView中抽象方法showProgress()的实现
     * localProgressView
     * true：
     * false：发送SHOW_PROGRESS消息到MainActivity；
     */
    public void showProgress(String msg) {
        if (localProgressView) {
            showProgressThisFragment(msg);
        } else {
            sendMsgToActivity(new MessageBean(MessageBean.ACTION_PROGRESS, MyConstant.WHAT.SHOW_PROGRESS, msg));
        }
    }

    /**发送ACTION_BACK到MAinActivity中*/
    @Override
    public void onBack() {
        sendMsgToActivity(new MessageBean(MessageBean.ACTION_BACK, this.getClass().getName()));
    }

    /*
        打开此页面后，是否暂停首页轮播视频和图片
     */
    @Override
    public boolean pauseMainPage() {
        return false;
    }

    /**
     * 清空移除时间removeEvent；NoActionTime = 0
     * pauseMainPage：
     *   true：暂停首页视频和图片轮播：发送ACTION_CONTENT到MainActivity中；
     *   false：
     */
    @Override
    public void onDestroy() {
        if (removeEvent != null) {
            DurationUtil.getIns().cancleRemoveTime(removeEvent);
            removeEvent = null;
        }
        DurationUtil.getIns().seTimeToAwake();
        if (pauseMainPage()) {
            //暂停首页轮播视频和图片
            sendMsgToActivity(new MessageBean(MessageBean.ACTION_CONTENT, MyConstant.WHAT.MAIN_ONRESUME, this.getClass().getName()));
        }
        super.onDestroy();
    }

    boolean localProgressView;

    /**
     * 其他fragment会实现getLayoutId()方法；localProgressView = false；
     * 增加removeEvent到ArrayList<RemoveEvent> fragmentRemoveTask列表；
     * NoActionTime = 0；
     * 猜是调用其他fragment的initView和initData方法；
     * 暂停主页面的视频和图片轮播是，发送ACTION_CONTENT到MainActivity中；
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(getLayoutId(), container, false);
        localProgressView = false;
        if (removeEvent != null) {
            DurationUtil.getIns().startRemoveTime(removeEvent);
        }
        DurationUtil.getIns().seTimeToAwake();
        initView(v);
        initData();
        if (pauseMainPage()) {
            sendMsgToActivity(new MessageBean(MessageBean.ACTION_CONTENT, MyConstant.WHAT.MAIN_ONPAUSE, this.getClass().getName()));
        }
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args = getArguments();
    }

    /**
     * 将bean的消息传送到MainActivity中；
     * MainActivity中实现了onFragmentInteraction()方法
     */
    public void sendMsgToActivity(MessageBean bean) {
        if (mListener != null) {
            mListener.onFragmentInteraction(bean);
        }
    }

    /***/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) { //判断其左边对象是否为其右边类的实例
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**mListener = null;*/
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

    }

    /**显示内容msg*/
    public void showToast(String msg) {
        ToastUtil.showToast(getContext(), msg);

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * 此接口必须由包含此片段的活动实现，以允许该片段中的交互与该活动中可能包含的其他片段通信。
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /**
     * MainActivity中实现了该接口
     * 为了实现Fragment和与之关联的Activity之间的信息传递,我们可以在Fragment中定义一个接口, 然后在Activity中实现这个接口.
     * 这个Fragment会在onAttach()方法中捕捉到Activity中实现的这个接口, 然后通过调用接口中的方法实现与Activity的交流.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(MessageBean obj);
    }

    /***************LocalFragmentProgress********************/
    MyLinearLayout progressLayout;
    MyProgressBar progressBar;
    MyTextView t_content;

    /**
     * localProgressView = true;
     * LinearLayout参数：宽=屏幕宽度*7/9=840,高为包含文字
     * 获得控件id：progressLayout，t_content;progerss；
     * 设置文本内容t_content大小为40
     */
    public void initViewProgress(View v) {
        localProgressView = true;
        LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(SizeUtil.screenWidth() * 7 / 9, LinearLayout.LayoutParams.WRAP_CONTENT);
//        RelativeLayout.LayoutParams l2=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
//        layout_dialog.setLayoutParams(l2);
        progressLayout = (MyLinearLayout) v.findViewById(R.id.progressLayout);
        t_content = (MyTextView) v.findViewById(R.id.t_content);
        progressBar = (MyProgressBar) v.findViewById(R.id.progress);
        t_content.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtil.getBigSize());

    }

    /**
     * progressLayout不为空，隐藏progressLayout；
     */
    public void hideProgressThisFragment() {
        if (progressLayout != null) {
            progressLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 显示progess：设置文本内容和可视progress；
     * MyTextView t_content不为空，设置消息msg到t_content；
     * MyLinearLayout progressLayout不为空，设置为可视；
     */
    public void showProgressThisFragment(String msg) {
        if (t_content != null) {
            t_content.setText(msg);
        }
        if (progressLayout != null) {
            progressLayout.setVisibility(View.VISIBLE);
        }


    }

}
