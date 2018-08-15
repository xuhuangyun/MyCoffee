package com.fancoff.coffeemaker.utils;

import android.content.Context;
import android.widget.Toast;
/*
	Toast提醒工具
 */
public class ToastUtil {
	/** 之前显示的内容 */
	private static String oldMsg ;
	/** Toast对象 */
	private static Toast toast = null ;
	/** 第一次时间 */
	private static long oneTime = 0 ;
	/** 第二次时间 */
	private static long twoTime = 0 ;

    /**
     * Toast显示内容message
     * toast:
     *   空：toast显示传入的message；oneTime为当前系统时间ms
     *   非空：twoTime为当前系统时间ms；
     *      message=oldMsg：twoTime - oneTime > Toast.LENGTH_SHORT则toast.show();
     *      meseage!=oldMsg:pldMsg=message；设置toast内容为message，并显示；
     * oneTime = twoTime ;
     */
	public static void showToast(Context context, String message) {
		if(toast == null){
			toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
			toast.show() ;
			oneTime = System.currentTimeMillis() ;
		}else{
			twoTime = System.currentTimeMillis() ;
			if(message.equals(oldMsg)){
				if(twoTime - oneTime > Toast.LENGTH_SHORT){
					toast.show() ;
				}
			}else{
				oldMsg = message ;
				toast.setText(message) ;
				toast.show() ;
			}
		}
		oneTime = twoTime ;

	}


}
