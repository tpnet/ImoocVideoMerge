package com.tpnet.imoocvideomerge.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Litp on 2017/2/27.
 */

public abstract class BaseListViewAdapter<T,K> extends BaseAdapter {

    protected List<T> mDataList;
    protected LayoutInflater mInflater;


    public BaseListViewAdapter(Context context, List<T> mDataList) {
        mInflater = LayoutInflater.from(context);
        if (mDataList != null) {
            this.mDataList = mDataList;
        }
    }



    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //子类实现
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        K viewHolder  = null;
        if(convertView == null){
            convertView = mInflater.inflate(getLayoutId(),null);
            viewHolder = initView(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (K) convertView.getTag();
        }
        setData(viewHolder,position,mDataList.get(position));
        return convertView;
    }


    protected  abstract K initView(View v);

    protected  abstract @LayoutRes Integer getLayoutId();


    protected abstract void setData(K viewHolder,int position,T data);





}
