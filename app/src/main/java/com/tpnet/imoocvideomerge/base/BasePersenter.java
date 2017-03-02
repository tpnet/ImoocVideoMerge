package com.tpnet.imoocvideomerge.base;

import java.lang.ref.WeakReference;

/**
 * p层的父类，持有v和m层的引用
 * Created by Litp on 2017/2/22.
 */

public class BasePersenter<T> {


    //使用弱引用存放View
    private WeakReference<T> mViewRef;


    /**
     * 创建View的弱引用
     * @param view
     */
    public void attachView(T view){
        mViewRef = new WeakReference<T>(view);
    }


    public void detachView(){
        if(mViewRef != null){
            mViewRef.clear();
        }
    }

    protected T getView(){
        return mViewRef.get();
    }


}
