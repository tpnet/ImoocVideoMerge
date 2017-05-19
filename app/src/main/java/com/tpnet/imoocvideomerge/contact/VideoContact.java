package com.tpnet.imoocvideomerge.contact;

import com.tpnet.imoocvideomerge.base.IModel;
import com.tpnet.imoocvideomerge.base.IView;
import com.tpnet.imoocvideomerge.model.face.IOnListener;

/**
 * 文件夹列表的契约文件，跟随谷歌爸爸的步伐
 * Created by litp on 2017/5/18.
 */

public class VideoContact {

    public interface IVideoView extends IView {

        void showVideoList();


    }

    public interface IVideoModel extends IModel {
        void getFolderList(IOnListener listener);

    }

}
