package com.tpnet.imoocvideomerge.contact;

import android.content.Context;

import com.tpnet.imoocvideomerge.base.IModel;
import com.tpnet.imoocvideomerge.base.IView;
import com.tpnet.imoocvideomerge.bean.FolderBean;
import com.tpnet.imoocvideomerge.model.face.IOnListener;
import com.tpnet.imoocvideomerge.model.face.IOnLoadListListener;

import java.util.List;

/**
 * 文件夹列表的契约文件，跟随谷歌爸爸的步伐
 * Created by litp on 2017/5/18.
 */

public class FolderContact {

    public interface IFloderView extends IView {

        void showFolderMess();

        void showError(Exception e);

        void showSearchMess(List<FolderBean> folderList);
    }

    public interface IFloderModel extends IModel {
        void getFolderList(Context context, IOnListener listener);

        void searchFolder(String searchText, IOnLoadListListener<FolderBean> listListener);


    }

}
