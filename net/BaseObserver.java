package com.fancoff.coffeemaker.net;

import com.fancoff.coffeemaker.net.RequstBean.BaseBean;
import com.fancoff.coffeemaker.net.exception.ApiException;
import com.fancoff.coffeemaker.net.exception.ExceptionEngine;
import com.fancoff.coffeemaker.utils.log4j.LogUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by apple on 2017/11/17.
 */

public abstract class BaseObserver<T> implements Observer<T> {
    //mDisposable.dispose();开光，用来切断连接
    private Disposable mDisposable;
    BaseBean baseBean;
    public void cancle(){
        if(mDisposable!=null){
            mDisposable.dispose();
        }

    }

    /**onSubscribe中调用onHandleStart()方法，该方法在BaseObserver对象中实现*/
    @Override
    public void onSubscribe(Disposable d){
        mDisposable = d;
        onHandleStart();
    }

    /**
     * 1、收到的T value转换为BaseBean
     * 2、收到的return_code=1:调用onHandleSuccess(value)方法，BaseObserver对象实现该方法；
     *          return_code!=1:调用onHandleError(baseBean.getReturn_code(),baseBean.getReturn_msg())方法
     *                         BaseObserver对象实现该方法；
     */
    @Override
    public void onNext(T value) {
        baseBean= (BaseBean) value;
//        LogUtil.error(baseBean.toString());
        if(baseBean.getIsSuccess()){  //return_code=1;服务器返回的return_code
            onHandleSuccess(value);
        }else{
            onHandleError(baseBean.getReturn_code(),baseBean.getReturn_msg());
        }


    }
    @Override
    public void onError(Throwable e) {
        LogUtil.error(e.toString());
        Throwable throwable = e;
        //获取最根源的异常
        while(throwable.getCause() != null){
            e = throwable;
            throwable = throwable.getCause();
        }
        ApiException apiException=ExceptionEngine.handleException(e);
        onHandleError(apiException.getCode(), ExceptionEngine.handleException(e).getMsg());
    }

    @Override
    public void onComplete() {

    }

    public abstract void onHandleSuccess(T t);

    public void onHandleError(Throwable e,int code,String msg) {
    }
    public void onHandleError(int code,String msg) {
    }
    public void onHandleComplete() {
    }
    public void onHandleStart() {
    }
}
