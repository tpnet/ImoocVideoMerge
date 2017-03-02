package com.tpnet.imoocvideomerge.presenter;

import com.tpnet.imoocvideomerge.base.BasePersenter;
import com.tpnet.imoocvideomerge.bean.FileBean;
import com.tpnet.imoocvideomerge.model.CommonImpl;
import com.tpnet.imoocvideomerge.model.ICommon;
import com.tpnet.imoocvideomerge.model.IOnProgressListener;
import com.tpnet.imoocvideomerge.ui.inter.IShowVideoList;

/**
 * 视屏列表p层
 * Created by Litp on 2017/2/23.
 */

public class VideoListPre extends BasePersenter<IShowVideoList> {

    private ICommon<String> iCommon;

    public VideoListPre( ) {
        iCommon = new CommonImpl();

    }

    public void saveFile(FileBean currFileBean, IOnProgressListener<String> listener) {
        iCommon.saveFile(currFileBean, listener);

    }

}
