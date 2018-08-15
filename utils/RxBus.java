package com.fancoff.coffeemaker.utils;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * 跨进程通讯
 */
public class RxBus {
    private static  RxBus BUS;
    private final Subject<Object> mBus;

    private RxBus() {//构造方法：
        mBus = PublishSubject.create().toSerialized();
    }

    public static RxBus getInstance() {
        if(BUS==null){
            BUS= new RxBus();
        }
        return BUS;
    }

    /**
     * 将数据添加到订阅  发送
     *  post()方法就是使用onNext(Object o)方法，将Event对象发送出去;
     *  post()方法发布一个 Event 对象给 bus，然后由 bus 转发给订阅者们
     *  1、DurationUtil类，timeToIntent()方法中：
     *        RxBus.getInstance().post(new MsgEvent(MyConstant.ACTION.INTENT_TO_REMOVE, event.getTag()));
     *  2、MainActivity类，onCreate()方法中：
     *       RxBus.getInstance().register(MsgEvent.class).subscribe(new Consumer<MsgEvent>()
     */
    public void post(@NonNull Object obj) {
        mBus.onNext(obj);
    }

    /**
     * 转换为特定类型的Obserbale
     * 方法能够获得一个包含目标事件的 Observable，订阅者对其订阅即可响应。
     * 方法内使用了ofType操作符，ofType源码内就是filter + cast的组合，只发送特定的类型。
     * fliter用来判断是否为指定的类型，cast将一个Observable转换为指定的特殊Observable
     */
    public <T> Observable<T> register(Class<T> tClass) {
        return mBus.ofType(tClass);
    }

    public Observable<Object> register() {
        return mBus;
    }

    /**是否已有观察者订阅*/
    public boolean hasObservers() {
        return mBus.hasObservers();
    }

    public void unregisterAll() {
        //会将所有由mBus生成的Observable都置completed状态,后续的所有消息都收不到了
        mBus.onComplete();
        BUS=null;
    }



}