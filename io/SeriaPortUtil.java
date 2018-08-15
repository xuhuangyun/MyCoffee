package com.fancoff.coffeemaker.io;

import android.content.Context;

import com.fancoff.coffeemaker.Application.MyApp;
import com.fancoff.coffeemaker.utils.log4j.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android_serialport_api.SerialPort;

/**
 * Created by apple on 2018/3/22.
 */

public class SeriaPortUtil {
    SerialPort mSerialPort;
    private FileInputStream mInputStream;
    private FileOutputStream mOutputStream;

    public FileInputStream getmInputStream() {
        if (MyApp.IOTEST) {
            try {
//
                mInputStream = MyApp.getIns().openFileInput("iotest2.txt");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return mInputStream;

    }

    public void setmInputStream(FileInputStream mInputStream) {
        this.mInputStream = mInputStream;
    }

    public FileOutputStream getmOutputStream() {
        if (MyApp.IOTEST) {
            try {
                mOutputStream = MyApp.getIns().openFileOutput("iotest2.txt", Context.MODE_PRIVATE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return mOutputStream;
    }

    public void setmOutputStream(FileOutputStream mOutputStream) {

        this.mOutputStream = mOutputStream;
    }

    public synchronized boolean isInit() {
        if (!MyApp.IOTEST) {
            if (mSerialPort == null) {
                try {
                    mSerialPort = new SerialPort(new File(
                            "/dev/ttyS3"), 9600, 0);  ///dev/ttyS4
                    if (mSerialPort != null) {
                        mOutputStream = (FileOutputStream) mSerialPort.getOutputStream();
                        mInputStream = (FileInputStream) mSerialPort.getInputStream();
                        if (mOutputStream != null && mInputStream != null) {
                            return true;
                        } else {
                            mSerialPort = null;
                        }

                    }
                } catch (Exception e1) {
                    LogUtil.error("SeriaPortUtil:" + e1.toString());
                    mSerialPort = null;
                    mOutputStream = null;
                    mInputStream = null;
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }

    }

    private static final SeriaPortUtil ourInstance = new SeriaPortUtil();

    public static SeriaPortUtil getInstance() {
        return ourInstance;
    }

    private SeriaPortUtil() {
    }
}
