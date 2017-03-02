package com.tpnet.imoocvideomerge.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.tpnet.imoocvideomerge.R;
import com.tpnet.imoocvideomerge.base.BaseListViewAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Litp on 2017/2/27.
 */

public class PopupListAdapter extends BaseListViewAdapter<String, PopupListAdapter.PopupViewHolder> {





    public PopupListAdapter(Context context, List<String> mDataList) {
        super(context, mDataList);
    }

    @Override
    protected PopupViewHolder initView(View v) {

        return new PopupViewHolder(v);
    }

    @Override
    protected Integer getLayoutId() {
        return R.layout.item_text;
    }

    @Override
    protected void setData(PopupViewHolder viewHolder,int position,String data) {
        viewHolder.tvText.setText(data);
    }


    static class PopupViewHolder {
        @BindView(R.id.tv_text)
        TextView tvText;

        PopupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
