package com.tpnet.imoocvideomerge.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.tpnet.imoocvideomerge.R;
import com.tpnet.imoocvideomerge.base.BaseActivity;
import com.tpnet.imoocvideomerge.base.BasePersenter;
import com.tpnet.imoocvideomerge.view.SpanTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 
 * Created by Litp on 2017/3/1.
 */

public class InfoActivity extends BaseActivity implements SpanTextView.onLinkClickListener {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_three)
    SpanTextView tvThree;

    private final String GITHUB_FLAG = "github_flag";
    private final String RXJAVA_FLAG = "rxjava_flag";
    private final String RXBINDING_FLAG = "rxbinding_flag";
    private final String TEXTVIEW_FLAG = "textview_flag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);


        toolbar.setTitle("简介");
        //toolbar.setNavigationIcon(R.drawable.icon_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initText();

    }

    private void initText(){

        //tvThree.setSpanLink("",GITHUB_FLAG);
        tvThree.setSpanLink("http://blog.csdn.net/niubitianping/article/category/6736107",RXJAVA_FLAG);
        tvThree.setSpanLink("http://blog.csdn.net/niubitianping/article/details/56014611",RXBINDING_FLAG);
        tvThree.setSpanLink("http://blog.csdn.net/niubitianping/article/details/54863694",TEXTVIEW_FLAG);
        tvThree.setOnLinkClickListener(this);

    }

    @Override
    public void onLinkClick(View view, String text,String sign) {
        switch (sign){
            case GITHUB_FLAG:
                openLink(text);
                break;
            case RXJAVA_FLAG:
                openLink(text);
                break;
            case RXBINDING_FLAG:
                openLink(text);
                break;
            case TEXTVIEW_FLAG:
                openLink(text);
                break;
        }
    }


    private void openLink(String link){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(link);
        intent.setData(content_url);
        startActivity(intent);
    }


    @Override
    protected BasePersenter createPersenter() {
        return null;
    }



}
