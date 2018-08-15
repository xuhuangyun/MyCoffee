package com.fancoff.coffeemaker.utils.audio;

/**
 * Created by apple on 2018/4/10.
 */

public class AudioUtil {
    /**
     * 先暂停mPlayThread，并设置其为null；
     * 新建mPlayThread线程传入频率，设置左右声道并开启该线程；
     */
    public void playSound(int rate) {
        if (null != mPlayThread) {
            mPlayThread.stopPlay();
            mPlayThread = null;
        }
        mPlayThread = new PlayThread(rate);
        mPlayThread.setChannel(true, true);
        mPlayThread.start();
    }

    private static final AudioUtil ourInstance = new AudioUtil();

    public static AudioUtil getInstance() {
        return ourInstance;
    }

    private AudioUtil() {
    }

    private PlayThread mPlayThread;

    public void release() {
        if (null != mPlayThread) {
            mPlayThread.stopPlay();
            mPlayThread = null;
        }
    }
}
