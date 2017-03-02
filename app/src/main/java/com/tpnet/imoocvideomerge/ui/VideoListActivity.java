package com.tpnet.imoocvideomerge.ui;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.ProgressBar;

import com.tpnet.imoocvideomerge.R;
import com.tpnet.imoocvideomerge.adapter.VideoListAdapter;
import com.tpnet.imoocvideomerge.base.BaseActivity;
import com.tpnet.imoocvideomerge.base.BaseRecyclerAdapter;
import com.tpnet.imoocvideomerge.base.Constant;
import com.tpnet.imoocvideomerge.bean.FileBean;
import com.tpnet.imoocvideomerge.bean.FolderBean;
import com.tpnet.imoocvideomerge.bean.IMooc;
import com.tpnet.imoocvideomerge.model.IOnProgressListener;
import com.tpnet.imoocvideomerge.presenter.VideoListPre;
import com.tpnet.imoocvideomerge.ui.inter.IShowVideoList;
import com.tpnet.imoocvideomerge.util.LogUtil;
import com.tpnet.imoocvideomerge.util.SystemTool;
import com.tpnet.imoocvideomerge.view.SpanTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoListActivity extends BaseActivity<IShowVideoList, VideoListPre> implements BaseRecyclerAdapter.OnItemClickListener<FileBean>, BaseRecyclerAdapter.OnItemWidgetClickListener<FileBean>, PopupMenu.OnMenuItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_available_space)
    SpanTextView tvAvailableSpace;
    @BindView(R.id.pb_available_space)
    ProgressBar pbAvailableSpace;
    @BindView(R.id.rv_video_list)
    RecyclerView rvVideoList;


    private AlertDialog copyDialog;    //提示复制中的提示框
    private ProgressBar progressBar;   //复制进度条


    private VideoListAdapter mAdapter;     //视频列表适配器
    private FileBean currFileBean;     //当前点击的fileBean

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        ButterKnife.bind(this);

        init();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);    //开启返回按钮
    }


    private void init() {

        //获取剩余空间
        getAvailabSpace();

        if (getIntent() != null) {
            FolderBean folder = getIntent().getParcelableExtra(Constant.INTENT_FOLDER);
            List<FileBean> list = IMooc.getInstance().getFileList(folder.getFolderName());
            mAdapter = new VideoListAdapter(list);
            mAdapter.setOnItemClickListener(this);
            mAdapter.setOnItemWidgetClickListener(this);
            rvVideoList.setLayoutManager(new LinearLayoutManager(this));
            rvVideoList.setAdapter(mAdapter);
            toolbar.setTitle(folder.getFolderRealName());
        } else {
            snackShow("数据获取失败!");
        }

    }

    /**
     * 获取剩余空间
     */
    private void getAvailabSpace(){
        pbAvailableSpace.setProgress(IMooc.getInstance().getAvailablePercent());
        tvAvailableSpace.setText(IMooc.getInstance().getAvailableSpace(),R.string.available_space,getResources().getColor(R.color.blackLight));
    }


    @Override
    protected VideoListPre createPersenter() {
        return new VideoListPre();
    }


    @Override
    public void onItemClick(View v, int position, FileBean data) {
        showSavePopup(v);
    }

    @Override
    public void onItemLongClick(View v, int position, FileBean data) {
        showSavePopup(v);
    }

    /**
     * 显示保存的popupWidows对话框
     * @param v 点击的view
     */
    private void showSavePopup(View v) {
        //弹出保存的对话框
        PopupMenu menuSave = new PopupMenu(this, v);
        menuSave.setOnMenuItemClickListener(this);
        menuSave.inflate(R.menu.menu_popup_file);
        menuSave.show();

    }


    @Override
    public void onItemWidgetClick(View view, int position, FileBean data) {
        this.currFileBean = data;
        showSavePopup(view);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.popup_file_save:   //保存分类视频
                saveViedoFile();
                break;
            case R.id.popup_folder_copy_path:   //复制视频路径
                if (SystemTool.copyToClipboard(this, currFileBean.getFilePath())) {
                    snackShow(R.string.copy_success);
                } else {
                    snackShow(R.string.copy_error);
                }
                break;
            case R.id.popup_file_copy_addr:  //复制视频地址
                if (SystemTool.copyToClipboard(this, currFileBean.getFile_url())) {
                    snackShow(R.string.copy_success);
                } else {
                    snackShow(R.string.copy_error);
                }
                break;
            case R.id.popup_file_copy_name:  //复制视频名称
                if (SystemTool.copyToClipboard(this, currFileBean.getSectionName())) {
                    snackShow(R.string.copy_success);
                } else {
                    snackShow(R.string.copy_error);
                }
                break;

        }

        return false;
    }

    /**
     * 保存单个视频文件
     */
    private void saveViedoFile() {
        LogUtil.e("开始保存");

        showDialog("开始复制...", 0);

        mPresenter.saveFile(currFileBean, new IOnProgressListener<String>() {
            @Override
            public void success(List<String> successList, List<String> errorList, Exception e) {
                if (e == null) {
                    snackShowLong("复制完成");
                } else {
                    snackShowLong("复制失败:" + e.getMessage());

                }
                getAvailabSpace();

                copyDialog.dismiss();

            }

            @Override
            public void progress(String data, long total, long curr, Integer percent) {
                //LogUtil.e("正在复制:"+data+" "+total+" "+curr+" "+percent);
                showDialog("正在复制:" + data, percent);
            }

        });
    }

    /**
     * 显示更新dialog
     * @param text dialog的文本内容
     * @param progress 进度
     */
    private void showDialog(final String text, final Integer progress) {


        if (copyDialog == null) {
            progressBar = new ProgressBar(VideoListActivity.this, null, android.R.attr.progressBarStyleHorizontal);
            progressBar.setMax(100);
            copyDialog = new AlertDialog.Builder(VideoListActivity.this)
                    .setTitle("另存文件")
                    .setMessage(text)
                    .setView(progressBar, 60, 20, 60, 20)
                    .show();
        } else if (copyDialog.isShowing()) {
            progressBar.setProgress(progress);
            copyDialog.setMessage(text);
        } else {
            copyDialog.show();
        }


    }
}
