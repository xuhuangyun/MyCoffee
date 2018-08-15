package com.fancoff.coffeemaker.utils;

import java.security.MessageDigest;
/*
	MD5加密工具类
 */
public class MD5Util {
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };
	private final static String[] hexDigitsX = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	/**
	 * 小写
	 * @param origin
	 * @return
	 */
	public static String toMD5LowerCase(String origin) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(
					md.digest(resultString.getBytes()), true);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("MD5加密过程异常", ex);
		}
		return resultString;
	}

	/**
	 * 大写
	 * @param origin
	 * @return
	 * 
	 */
	public static String toMD5Capital(String origin) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");//返回实现指定摘要算法的MessageDigest 对象
			resultString = byteArrayToHexString(
					md.digest(resultString.getBytes()), false);
			//digest(byte[] input)  使用指定的 byte 数组对摘要进行最后更新，然后完成摘要计算，存放哈希值结果的 byte 数组
            //大写
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("MD5加密过程异常", ex);
		}
		return resultString;
	}

	/**
	 * 根据lowercase选择大小写，将byte[]转换为字符串；
	 */
	private static String byteArrayToHexString(byte[] b, boolean lowercase) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i], lowercase));
		}
		return resultSb.toString();
	}

	/**
	 * 将一个字节转换为2个字符，lowercase=ture小写；false大写；
	 */
	private static String byteToHexString(byte b, boolean lowercase) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		if (lowercase) {
			return hexDigitsX[d1] + hexDigitsX[d2];
		} else {
			return hexDigits[d1] + hexDigits[d2];
		}
	}

}
