package com.tpnet.imoocvideomerge.presenter;

import android.content.Context;

import com.tpnet.imoocvideomerge.base.BasePersenter;
import com.tpnet.imoocvideomerge.bean.FileBean;
import com.tpnet.imoocvideomerge.contact.VideoContact;
import com.tpnet.imoocvideomerge.model.CommonImpl;
import com.tpnet.imoocvideomerge.model.face.ICommon;
import com.tpnet.imoocvideomerge.model.face.IOnProgressListener;

/**
 * 视屏列表p层
 * Created by Litp on 2017/2/23.
 */

public class VideoListPre extends BasePersenter<VideoContact.IVideoView> {

    private ICommon<String> iCommon;

    public VideoListPre( ) {
        iCommon = new CommonImpl();

    }

    public void saveFile(Context context, FileBean currFileBean, IOnProgressListener<String> listener) {
        iCommon.saveFile(context, currFileBean, listener);

    }

}
