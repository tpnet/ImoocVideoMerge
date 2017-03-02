package com.tpnet.imoocvideomerge.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tpnet.imoocvideomerge.R;
import com.tpnet.imoocvideomerge.base.BaseRecyclerAdapter;
import com.tpnet.imoocvideomerge.bean.FolderBean;
import com.tpnet.imoocvideomerge.view.SpanTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by Litp on 2017/2/22.
 */

public class FolderListAdapter extends BaseRecyclerAdapter<FolderBean, FolderListAdapter.FolderViewHolder> {

    public FolderListAdapter(List<FolderBean> dataList) {
        super(dataList);
    }

    @Override
    public FolderViewHolder onCreate(ViewGroup parent, int viewType) {
        return new FolderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_folderlist, parent,false));
    }

    @Override
    public void onBind(FolderViewHolder viewHolder, final int realPosition, final FolderBean data) {
        viewHolder.tvFolderName.setText(data.getFolderRealName());
        viewHolder.tvDownTime.setText(data.getFolderDowntime(),R.string.down_time,R.color.blackLight);
        viewHolder.tvFolderSize.setText(data.getFolderSize(),R.string.folder_size,R.color.blackLight);
        viewHolder.tvFileNum.setText(data.getFolderFileNum(),R.string.file_num,R.color.blackLight);

        Glide.with(viewHolder.getContext())
                .load(data.getFolderThumbPath())
                .error(R.mipmap.ic_launcher)
                .into(viewHolder.ivFolderPic);

        //回调保存的事件
        viewHolder.ibMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListenerWidget != null){
                    mListenerWidget.onItemWidgetClick(v,realPosition,data);
                }
            }
        });



    }





    static class FolderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_folder_pic)
        ImageView ivFolderPic;
        @BindView(R.id.tv_folder_name)
        TextView tvFolderName;
        @BindView(R.id.tv_file_num)
        SpanTextView tvFileNum;
        @BindView(R.id.tv_folder_size)
        SpanTextView tvFolderSize;
        @BindView(R.id.tv_down_time)
        SpanTextView tvDownTime;
        @BindView(R.id.ib_more)
        ImageView ibMore;


        FolderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //ButterKnife.bind(itemView);

        }


        public Context getContext() {
            return itemView.getContext();
        }

    }






}
