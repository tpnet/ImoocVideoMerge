package com.tpnet.imoocvideomerge.model.face;

import android.content.Context;

import com.tpnet.imoocvideomerge.bean.FileBean;
import com.tpnet.imoocvideomerge.bean.FolderBean;

/**
 * 常用逻辑接口
 * Created by Litp on 2017/2/24.
 */

public interface ICommon <T> {


    void saveFolder(Context context, FolderBean folder, IOnProgressListener<T> listListener);

    void saveFile(Context context, FileBean folder, IOnProgressListener<T> listListener);

    void cancleCopy();

}
