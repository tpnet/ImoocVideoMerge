package com.tpnet.imoocvideomerge.base;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 *
 *
 * Created by Litp on 2017/2/22.
 */
public abstract class BaseActivity<V,T extends BasePersenter<V>> extends RxAppCompatActivity {


    public T mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = createPersenter();
        if(mPresenter != null){
            mPresenter.attachView((V)this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter != null){
            mPresenter.detachView();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();  //返回上一页
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void snackShow(String text){
        Snackbar.make(getWindow().peekDecorView(), text, Snackbar.LENGTH_SHORT).show();
    }

    protected void snackShow(Integer textId){
        snackShow(getResources().getString(textId));
    }
    protected void snackShowLong(String text){
        Snackbar.make(getWindow().peekDecorView(), text, Snackbar.LENGTH_LONG).show();
    }

    protected abstract T createPersenter();
}
