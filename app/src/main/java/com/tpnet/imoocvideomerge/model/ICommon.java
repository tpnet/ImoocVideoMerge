package com.tpnet.imoocvideomerge.model;

import com.tpnet.imoocvideomerge.bean.FileBean;
import com.tpnet.imoocvideomerge.bean.FolderBean;

/**
 * 公共逻辑接口
 * Created by Litp on 2017/2/24.
 */

public interface ICommon <T> {

    void searchFolder(String searchText, IOnLoadListListener<FolderBean> listListener);



    void saveFolder(FolderBean folder, IOnProgressListener<T> listListener);

    void saveFile(FileBean folder, IOnProgressListener<T> listListener);

    void cancleCopy();

}
