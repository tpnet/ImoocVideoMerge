package com.tpnet.imoocvideomerge.base;

import android.app.Application;

import com.tpnet.imoocvideomerge.util.LogUtil;

/**
 * Created by Litp on 2017/2/22.
 */

public class BaseApplication extends Application{

    private final String tag = BaseApplication.class.getSimpleName();

    private static BaseApplication instance;

    /*
     * 是否完成  整个项目
     */
    public static boolean isCompleteProject = false;


    public static BaseApplication getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

    }


    /**
     * 初始化
     */
    private void init() {

        LogUtil.setDebug(!isCompleteProject);
        LogUtil.e("isDebug: " + !isCompleteProject);

    }


}
