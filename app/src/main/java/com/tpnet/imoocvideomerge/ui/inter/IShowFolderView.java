package com.tpnet.imoocvideomerge.ui.inter;

import com.tpnet.imoocvideomerge.bean.FolderBean;

import java.util.List;

/**
 *
 * Created by Litp on 2017/2/22.
 */

public interface IShowFolderView{

    void showLoading(boolean isShow);

    //void toFolderMess(List<FolderBean> folderList);

    void toFolderMess();

    void showError(Exception e);

    void toSearchMess(List<FolderBean> folderList);


}
