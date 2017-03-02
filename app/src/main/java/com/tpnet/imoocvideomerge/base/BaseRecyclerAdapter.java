package com.tpnet.imoocvideomerge.base;

import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.tpnet.imoocvideomerge.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 作用:
 *
 * @author LITP
 * @date 2016/11/17
 */

public abstract class BaseRecyclerAdapter<T, K extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<K> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    private List<T> mDatas = new ArrayList<>();

    private View mHeaderView;

    private OnItemClickListener<T> mListener;
    protected OnItemWidgetClickListener<T> mListenerWidget;


    public BaseRecyclerAdapter(List<T> dataList) {
        if (dataList != null) {
            this.mDatas = dataList;
        }
    }



    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    public void addDatas(ArrayList<T> datas) {
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            if (mHeaderView != null) return TYPE_HEADER;
        }
        return TYPE_NORMAL;
    }

    @Override
    public K onCreateViewHolder(ViewGroup parent, final int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return (K) new Holder(mHeaderView);
        }
        return onCreate(parent, viewType);
    }


    @Override
    public void onBindViewHolder(K viewHolder, int position) {
        if (getItemViewType(position) == TYPE_HEADER || mDatas.size() <= 0) {
            return;
        }

        final int pos = getRealPosition(viewHolder);
        final T data = mDatas.get(pos);
        onBind(viewHolder, pos, data);

        if (mListener != null) {

            //设置背景点击效果,为了兼用系统，大于21android5.0 而且控件的cardView 才设置，
            if(viewHolder.itemView instanceof CardView && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                viewHolder.itemView.setBackgroundResource(R.drawable.rv_selector);
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(v,pos, data);
                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mListener.onItemLongClick(v,pos,data);
                    //true为不加短按onClick,false为加入短按
                    return true;
                }
            });
        }
    }


    /**
     * 处理，为GridLayoutManager添加header
     *
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_HEADER ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    /**
     * 处理，为StaggeredGridLayoutManager添加header
     *
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(K holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && holder.getLayoutPosition() == 0) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    private int getRealPosition(K holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemCount(){
        return mHeaderView == null ? mDatas.size() : mDatas.size() + 1;
    }




    public abstract K onCreate(ViewGroup parent, final int viewType);

    public abstract void onBind(K viewHolder, int realPosition, T data);

    private class Holder extends RecyclerView.ViewHolder {
        Holder(View itemView) {
            super(itemView);
        }
    }

    public void setOnItemClickListener(OnItemClickListener<T> li) {
        mListener = li;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(View v,int position, T data);
        void onItemLongClick(View v,int position, T data);
    }

    public void setOnItemWidgetClickListener(OnItemWidgetClickListener<T> li) {
        mListenerWidget = li;
    }

    public interface  OnItemWidgetClickListener<T>{
        void onItemWidgetClick(View view,int position, T data);
    }


}
