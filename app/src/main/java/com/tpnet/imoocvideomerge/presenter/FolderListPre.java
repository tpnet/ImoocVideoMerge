package com.tpnet.imoocvideomerge.presenter;

import com.tpnet.imoocvideomerge.base.BasePersenter;
import com.tpnet.imoocvideomerge.bean.FolderBean;
import com.tpnet.imoocvideomerge.model.CommonImpl;
import com.tpnet.imoocvideomerge.model.FolderListImpl;
import com.tpnet.imoocvideomerge.model.ICommon;
import com.tpnet.imoocvideomerge.model.IGetFolderList;
import com.tpnet.imoocvideomerge.model.IOnListener;
import com.tpnet.imoocvideomerge.model.IOnLoadListListener;
import com.tpnet.imoocvideomerge.model.IOnProgressListener;
import com.tpnet.imoocvideomerge.ui.inter.IShowFolderView;

import java.util.List;

/**
 * Created by Litp on 2017/2/22.
 */

public class FolderListPre extends BasePersenter<IShowFolderView> {

    private IGetFolderList iGetFolderList;

    private ICommon<String> iCommon;

    public FolderListPre() {
        iGetFolderList = new FolderListImpl();
        iCommon = new CommonImpl();
    }


    /**
     * 获取慕课网的下载文件夹列表
     */
    public void getFolderList() {

        if (getView() != null) {
            //回调加载中..
            getView().showLoading(true);
        }

        //调用model层加载数据
        iGetFolderList.getFolderList(new IOnListener() {

            @Override
            public void success(Exception e) {
                if (getView() != null) {
                    if (e == null) {
                        getView().toFolderMess();
                        getView().showLoading(false);
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

        iCommon.searchFolder(searchText, new IOnLoadListListener<FolderBean>() {
            @Override
            public void success(List<FolderBean> listData, Exception e) {
                if (getView() != null) {
                    if (e == null) {

                        getView().toSearchMess(listData);
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

    public void saveFolder(FolderBean folder,IOnProgressListener<String> listener) {
        iCommon.saveFolder(folder, listener);

    }

    public void  cancleCopy(){
        iCommon.cancleCopy();
    }




}
