package com.tpnet.imoocvideomerge.presenter;

import android.content.Context;

import com.tpnet.imoocvideomerge.base.BasePersenter;
import com.tpnet.imoocvideomerge.bean.FolderBean;
import com.tpnet.imoocvideomerge.contact.FolderContact;
import com.tpnet.imoocvideomerge.model.CommonImpl;
import com.tpnet.imoocvideomerge.model.FolderListImpl;
import com.tpnet.imoocvideomerge.model.face.ICommon;
import com.tpnet.imoocvideomerge.model.face.IOnListener;
import com.tpnet.imoocvideomerge.model.face.IOnLoadListListener;
import com.tpnet.imoocvideomerge.model.face.IOnProgressListener;

import java.util.List;

/**
 * 文件夹列表p层
 * Created by Litp on 2017/2/22.
 */

public class FolderListPre extends BasePersenter<FolderContact.IFloderView> {

    private FolderContact.IFloderModel mModel;

    private ICommon<String> iCommon;

    public FolderListPre() {
        mModel = new FolderListImpl();
        iCommon = new CommonImpl();
    }


    /**
     * 获取慕课网的下载文件夹列表
     */
    public void getFolderList(Context context) {

        if (getView() != null) {
            //回调加载中..
            getView().showLoading();
        }

        //调用model层加载数据
        mModel.getFolderList(context, new IOnListener() {

            @Override
            public void success(Exception e) {
                if (getView() != null) {
                    if (e == null) {
                        getView().showFolderMess();
                        getView().hideLoading();
                    } else {
                        getView().showError(e);
                    }
                }

            }


        });

    }


    /**
     * 搜索文件夹
     *
     * @param searchText 搜索的文本
     */
    public void searchFolder(String searchText) {

        mModel.searchFolder(searchText, new IOnLoadListListener<FolderBean>() {
            @Override
            public void success(List<FolderBean> listData, Exception e) {
                if (getView() != null) {
                    if (e == null) {

                        getView().showSearchMess(listData);
                    } else {
                        getView().showError(e);
                    }
                }

            }


        });


    }


    /**
     * 保存文件夹，
     *
     * @param folder 文件夹
     */

    public void saveFolder(Context context, FolderBean folder, IOnProgressListener<String> listener) {
        iCommon.saveFolder(context, folder, listener);

    }

    public void  cancleCopy(){
        iCommon.cancleCopy();
    }




}
