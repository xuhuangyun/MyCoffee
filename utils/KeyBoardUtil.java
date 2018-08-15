package com.fancoff.coffeemaker.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.os.ResultReceiver;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
/*
键盘操作类
 */
public class KeyBoardUtil {

	/**
	 * 隐藏软键盘
	 * 
	 * @author gdpancheng@gmail.com 2013-10-12 下午3:46:33
	 * @param view
	 * @return void
	 */
	public static void hideSoftKeyboard(View view) {
		InputMethodManager imm = (InputMethodManager) view.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	/**
	 * 显示软键盘
	 * 
	 * @author gdpancheng@gmail.com 2013-10-12 下午3:46:44
	 * @param view
	 * @return void
	 */
	public static void showSoftkeyboard(View view) {
		showSoftkeyboard(view, null);
	}

	/**
	 * 显示软键盘
	 * 
	 * @author gdpancheng@gmail.com 2013-10-12 下午3:47:19
	 * @param view
	 * @param resultReceiver
	 * @return void
	 */
	public static void showSoftkeyboard(View view, ResultReceiver resultReceiver) {
		Configuration config = view.getContext().getResources()
				.getConfiguration();
		if (config.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
			InputMethodManager imm = (InputMethodManager) view.getContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);

			if (resultReceiver != null) {
				imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT,
						resultReceiver);
			} else {
				imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
			}
		}
	}
	// 如果输入法打�?��关闭，如果没打开则打�?
	public static void toggleSoftInput(Context context, View v) {
		InputMethodManager m = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	// 获取输入法打�?��状�?
	public static boolean isActive(Context context, View v) {

		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		boolean isOpen = imm.isActive();
		// isOpen若返回true，则表示输入法打�?
		return isOpen;
	}
}
