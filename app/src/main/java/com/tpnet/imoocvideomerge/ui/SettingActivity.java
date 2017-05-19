package com.tpnet.imoocvideomerge.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.tpnet.imoocvideomerge.R;
import com.tpnet.imoocvideomerge.base.BaseActivity;
import com.tpnet.imoocvideomerge.base.BasePersenter;

import butterknife.ButterKnife;

/**
 * 设置的
 * Created by litp on 2017/5/18.
 */

public class SettingActivity extends BaseActivity {
    Toolbar mToolbar;

    @Override
    protected BasePersenter createPersenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mToolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("设置");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction().replace(R.id.frameLayout, new SettingFragment()).commit();

    }
}
