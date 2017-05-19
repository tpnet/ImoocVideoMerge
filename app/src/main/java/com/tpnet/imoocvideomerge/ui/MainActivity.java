package com.tpnet.imoocvideomerge.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.ProgressBar;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tpnet.imoocvideomerge.R;
import com.tpnet.imoocvideomerge.adapter.FolderListAdapter;
import com.tpnet.imoocvideomerge.base.BaseActivity;
import com.tpnet.imoocvideomerge.base.BaseRecyclerAdapter;
import com.tpnet.imoocvideomerge.base.Constant;
import com.tpnet.imoocvideomerge.bean.FolderBean;
import com.tpnet.imoocvideomerge.bean.IMooc;
import com.tpnet.imoocvideomerge.contact.FolderContact;
import com.tpnet.imoocvideomerge.model.face.IOnProgressListener;
import com.tpnet.imoocvideomerge.presenter.FolderListPre;
import com.tpnet.imoocvideomerge.util.DataHelper;
import com.tpnet.imoocvideomerge.util.FileUtils;
import com.tpnet.imoocvideomerge.util.LogUtil;
import com.tpnet.imoocvideomerge.util.SystemTool;
import com.tpnet.imoocvideomerge.util.ToastUtils;
import com.tpnet.imoocvideomerge.view.LoadingPopup;
import com.tpnet.imoocvideomerge.view.SpanTextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

public class MainActivity extends BaseActivity<FolderContact.IFloderView, FolderListPre> implements FolderContact.IFloderView, BaseRecyclerAdapter.OnItemClickListener<FolderBean>, BaseRecyclerAdapter.OnItemWidgetClickListener<FolderBean>, PopupMenu.OnMenuItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fat_menu)
    FloatingActionMenu fabMenu;

    @BindView(R.id.fab_info)
    FloatingActionButton fabInfo;
    @BindView(R.id.fab_setting)
    FloatingActionButton fabSetting;
    @BindView(R.id.fab_inner)
    FloatingActionButton fabInner;
    @BindView(R.id.fab_out)
    FloatingActionButton fabOut;
    
    
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.rv_folder)
    RecyclerView rvFolder;


    @BindView(R.id.tv_available_space)
    SpanTextView tvAvailableSpace;
    @BindView(R.id.pb_available_space)
    ProgressBar pbAvailableSpace;


    private SearchView searchView;

    private EditText searchEditView;  //搜索的EditText


    private LoadingPopup loadingPopup;   //加载提示框

    private FolderListAdapter mAdapter;   //所有的视频文件夹


    private FolderListAdapter mSearchAdapter;

    private FolderBean currFolderBean;  //当前选择要保存的的FolderBena

    private AlertDialog copyDialog;    //提示复制中的提示框

    private ProgressBar progressBar;   //复制进度条


    //当前选择的要显示的sd卡的路径的 sp的key
    public final static String SP_SELECT_STROAGE = "SP_SELECT_STROAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        //这句代码使启用Activity回退功能，并显示Toolbar上的左侧回退图标
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

    }

    private void init() {

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            snackShow("获取文件管理权限失败！");
                        } else {
                            mPresenter.getFolderList(MainActivity.this);
                        }
                    }
                });

        getAvailabSpace();

        //点击背景关闭
        fabMenu.setClosedOnTouchOutside(true);
    }

    /**
     * 获取剩余空间
     */
    private void getAvailabSpace() {
        pbAvailableSpace.setProgress(IMooc.getInstance().getAvailablePercent(this));
        tvAvailableSpace.setText(IMooc.getInstance().getAvailableSpace(this), R.string.available_space, getResources().getColor(R.color.blackLight));
    }


    @OnClick({R.id.fab_setting, R.id.fab_info, R.id.fab_inner, R.id.fab_out})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_info:
                //打开个人信息界面
                startActivity(new Intent(this, InfoActivity.class));
                break;
            case R.id.fab_setting:
                //打开设置界面
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.fab_inner:
                //选择内置内存卡
                DataHelper.SetStringSF(this, SP_SELECT_STROAGE, FileUtils.getInnerRootPath());
                mPresenter.getFolderList(this);
                break;
            case R.id.fab_out:
                //选着外置内存卡
                String outPath = FileUtils.getOutRootPath(this);
                if (TextUtils.isEmpty(outPath)) {
                    ToastUtils.showShort("不存在外置内存卡");
                } else {
                    DataHelper.SetStringSF(this, SP_SELECT_STROAGE, outPath);
                    mPresenter.getFolderList(this);
                }
                break;

        }
        fabMenu.close(true);

    }


    @Override
    protected FolderListPre createPersenter() {
        return new FolderListPre();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //指定Toolbar上的视图文件
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);//在菜单中找到对应控件的item

        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchEditView = (EditText) searchView.findViewById(R.id.search_src_text);
        if (searchView != null) {
            RxTextView.textChanges(searchEditView).skip(1)
                    .debounce(300, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .filter(new Predicate<CharSequence>() {
                        @Override
                        public boolean test(@NonNull CharSequence charSequence) throws Exception {
                            //内容的大于0 才发送请求
                            if (charSequence.length() > 0) {
                                return true;
                            } else {
                                //搜索内容为空的时候显示原来的
                                rvFolder.setAdapter(mAdapter);
                                return false;
                            }
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<CharSequence>() {
                        @Override
                        public void accept(@NonNull CharSequence charSequence) throws Exception {
                            mPresenter.searchFolder(String.valueOf(charSequence));
                        }
                    });
        }

        //searchView.setOnQueryTextListener(this);
        return true;
    }


    @Override
    public void showFolderMess() {
        if (mAdapter == null) {
            mAdapter = new FolderListAdapter(IMooc.getInstance().getFolderList());
            //获取文件列表成功，显示出来
            rvFolder.setLayoutManager(new LinearLayoutManager(this));
            rvFolder.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(this);
            mAdapter.setOnItemWidgetClickListener(this);
        } else {
            mAdapter.notifyDataSetChanged();
        }

    }


    @Override
    public void showError(Exception e) {
        loadingPopup.dismiss();
        snackShow("加载失败:" + e.getMessage());
    }


    @Override
    public void showSearchMess(List<FolderBean> folderList) {
        LogUtil.e("搜索数量" + folderList.size());

        mSearchAdapter = new FolderListAdapter(folderList);
        rvFolder.setAdapter(mSearchAdapter);
        mSearchAdapter.setOnItemClickListener(this);

    }


    @Override
    public void onItemClick(View v, int position, FolderBean data) {
        Intent intent = new Intent(this, VideoListActivity.class);
        intent.putExtra(Constant.INTENT_FOLDER, data);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View v, int position, FolderBean data) {
        showSavePopup(v);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
            System.exit(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onItemWidgetClick(View v, int position, FolderBean data) {
        this.currFolderBean = data;
        //显示保存的事件
        showSavePopup(v);
    }

    private void showSavePopup(View v) {
        //弹出保存的对话框

        PopupMenu menuSave = new PopupMenu(this, v);
        menuSave.setOnMenuItemClickListener(this);
        menuSave.inflate(R.menu.menu_popup_folder);

        menuSave.show();

    }

    public void saveViedoFolder() {
        LogUtil.e("开始保存");

        showDialog("开始复制...", 0);

        mPresenter.saveFolder(this, currFolderBean, new IOnProgressListener<String>() {
            @Override
            public void success(List<String> successList, List<String> errorList, Exception e) {
                if (e == null) {
                    snackShowLong("复制完成");
                } else {
                    snackShowLong("复制成功:" + successList.size() + ",复制失败:" + errorList.size());

                }
                getAvailabSpace();
                copyDialog.dismiss();

            }

            @Override
            public void progress(String data, long total, long curr, Integer percent) {
                LogUtil.e("正在复制:" + data + " " + total + " " + curr + " " + percent);
                showDialog("正在复制:" + data, percent);
            }

        });
    }

    private void showDialog(String text, Integer progress) {

        if (copyDialog == null) {
            progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
            progressBar.setMax(100);
            copyDialog = new AlertDialog.Builder(this)
                    .setTitle("另存文件")
                    .setMessage(text)
                    .setView(progressBar, 60, 20, 60, 20)
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //取消复制
                            mPresenter.cancleCopy();
                        }
                    })
                    .show();
        } else if (copyDialog.isShowing()) {
            progressBar.setProgress(progress);
            copyDialog.setMessage(text);
        } else {
            copyDialog.show();
        }

    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popup_folder_save:   //保存分类视频
                saveViedoFolder();
                break;
            case R.id.popup_folder_copy_path:   //复制视频路径
                if (SystemTool.copyToClipboard(this, FileUtils.getShowRootPathDefault(this) + currFolderBean.getFolderName())) {
                    snackShow(R.string.copy_success);
                } else {
                    snackShow(R.string.copy_error);
                }
                break;
            case R.id.popup_folder_copy_pic_path:  //复制视频图片地址
                if (SystemTool.copyToClipboard(this, currFolderBean.getFolderThumbPath())) {
                    snackShow(R.string.copy_success);
                } else {
                    snackShow(R.string.copy_error);
                }
                break;
            case R.id.popup_folder_copy_name:  //复制视频名称
                if (SystemTool.copyToClipboard(this, currFolderBean.getFolderRealName())) {
                    snackShow(R.string.copy_success);
                } else {
                    snackShow(R.string.copy_error);
                }
                break;
            case R.id.popup_folder_copy_time:  //复制视频时间
                if (SystemTool.copyToClipboard(this, currFolderBean.getFolderDowntime())) {
                    snackShow(R.string.copy_success);
                } else {
                    snackShow(R.string.copy_error);
                }
                break;
            case R.id.popup_open_folder:  //打开保存的目录
                FileUtils.openFolder(this, FileUtils.getSaveRootPathDefault(this) + currFolderBean.getFolderRealName());

                break;
            case R.id.popup_open_old_folder:  //打开原的目录
                FileUtils.openFolder(this, FileUtils.getShowRootPathDefault(this) + currFolderBean.getFolderName());

                break;
        }

        return false;
    }

    @Override
    public void showLoading() {
        if (loadingPopup == null) {
            loadingPopup = new LoadingPopup(this);
        }

        rvFolder.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingPopup.showPopupWindow();
            }
        }, 10);

    }

    @Override
    public void hideLoading() {
        if (loadingPopup == null) {
            loadingPopup = new LoadingPopup(this);
        }
        etSearch.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingPopup.dismiss();
            }
        }, 300);
    }
}
