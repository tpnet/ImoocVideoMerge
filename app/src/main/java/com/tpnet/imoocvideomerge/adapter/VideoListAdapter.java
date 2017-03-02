package com.tpnet.imoocvideomerge.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpnet.imoocvideomerge.R;
import com.tpnet.imoocvideomerge.base.BaseRecyclerAdapter;
import com.tpnet.imoocvideomerge.bean.FileBean;
import com.tpnet.imoocvideomerge.util.FileUtils;
import com.tpnet.imoocvideomerge.view.SpanTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by Litp on 2017/2/22.
 */

public class VideoListAdapter extends BaseRecyclerAdapter<FileBean, VideoListAdapter.FileViewHolder> {

    public VideoListAdapter(List<FileBean> dataList) {
        super(dataList);
    }

    @Override
    public FileViewHolder onCreate(ViewGroup parent, int viewType) {
        return new FileViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_list, parent, false));
    }

    @Override
    public void onBind(FileViewHolder viewHolder, final int realPosition, final FileBean data) {

        viewHolder.tvVideoTitle.setText(data.getChapter_seq()+"-"+data.getSection_seq()+" "+data.getSectionName());

        String size = "";
        if(data.getRecvsize() == data.getFilesize()){
            size = FileUtils.getFormatSize(data.getFilesize());
            viewHolder.ivDownState.setImageResource(R.drawable.down_complete);
        }else{
            size = FileUtils.getFormatSize(data.getRecvsize()) + "/" +FileUtils.getFormatSize(data.getFilesize());
            viewHolder.ivDownState.setImageResource(R.drawable.down_ing);
        }
        viewHolder.tvVideoSize.setText(size);


        if(data.getFileFormat().equals("mp4")){
            viewHolder.tvVideoTitle.setImageToLast(R.drawable.mp4_icon);
        }else{
            viewHolder.tvVideoTitle.setImageToLast(R.drawable.zip_icon);
        }

        //mp4还是zip格式
        //viewHolder.ivDownState.setImageResource(R.drawable.down_complete);

        viewHolder.ibMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListenerWidget != null){
                    mListenerWidget.onItemWidgetClick(v,realPosition,data);
                }
            }
        });
    }



    static class FileViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_video_title)
        SpanTextView tvVideoTitle;
        @BindView(R.id.tv_video_size)
        TextView tvVideoSize;
        @BindView(R.id.iv_down_state)
        ImageView ivDownState;
        @BindView(R.id.ib_more)
        ImageView ibMore;


        FileViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }


        public Context getContext() {
            return itemView.getContext();
        }

    }



}
