package com.fancoff.coffeemaker.utils;


public class SizeUtil {
//    static double withSi = (1080 * 1.000 / DensityUtil.getScreenWidth());
//static double heightSi = (1920 * 1.000 / DensityUtil.getScreenHeight());
    static double withSi = (1080 * 1.000 / 1080);
    static double heightSi = (1920 * 1.000 / 1920);
    static double pisize = (withSi + heightSi) * 1.000 / 2;

    public static int screenWidth() {
        return 1080;
    }

    public static int screenHeight() {
        return 1920;
    }

    public static int widthSize(int dp) {
//		return (int) (dp/withSi);
        return dp;
    }

    public static int heightSize(int dp) {
        return dp;
    }

    public static int fontSize(int dp) {
        return (int) (dp * pisize);
    }
    public static int getLargesetSize() {
        return fontSize(70);
    }
    public static int getLargesetSize2() {
        return fontSize(55);
    }

    public static int getLargeSize() {
        return fontSize(50);
    }

    public static int getBigSize() {
        return fontSize(40);
    }

    public static int getNomalSize() {
        return fontSize(30);
    }

    public static int getSmallSize() {
        return fontSize(20);
    }

    public static int getMinSize() {
        return fontSize(10);
    }

}
