package com.tpnet.imoocvideomerge.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 密度像素计算类
 * Created by Administrator on 2015/12/14.
 */
public class DensityUtil {

    // 根据屏幕密度转换
    private static float mPixels = 0.0F;
    private static float density = -1.0F;

    /**
     *
     * @param context
     * @param pixels
     * @return
     */
    public static int getDisplayMetrics(Context context, float pixels) {
        if (mPixels == 0.0F)
            mPixels = context.getResources().getDisplayMetrics().density;
        return (int) (0.5F + pixels * mPixels);
    }


    public static int getImageWeidth(Context context , float pixels) {
        LogUtil.e("screen width " + context.getResources().getDisplayMetrics().widthPixels);
        return context.getResources().getDisplayMetrics().widthPixels - 66 - getDisplayMetrics(context, pixels);
    }

    /**
     * 像素转化dip
     * @param pxValue
     * @return
     */
    public static int px2dip( float pxValue){

        final float scale = Resources.getSystem().getDisplayMetrics().density;

        return (int)(pxValue / scale + 0.5f);

    }


    public static int px2sp( float pxValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(float spValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    /**
     * dip转化像素
     * @param dipValue
     * @return
     */
    public static int dip2px(float dipValue){
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);

    }

    /**
     * @param context
     * @param height
     * @return
     */
    public static int getMetricsDensity(Context context , float height) {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(localDisplayMetrics);
        return Math.round(height * localDisplayMetrics.densityDpi / 160.0F);
    }


    public static int fromDPToPix(Context context, int dp) {
        return Math.round(getDensity(context) * dp);
    }

    /**
     * @param context
     * @return
     */
    public static float getDensity(Context context) {
        if (context == null)
            return 0.0f;
        if (density < 0.0F)
            density = context.getResources().getDisplayMetrics().density;
        return density;
    }

    public static int round(Context context, int paramInt) {
        return Math.round(paramInt / getDensity(context));
    }

    public static int getWindowsWidth(Context context){
        //获取屏幕宽
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    public static int getWindowsHeight(Context context){
        //获取屏幕高度
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }

}
