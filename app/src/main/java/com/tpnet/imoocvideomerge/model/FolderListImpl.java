package com.tpnet.imoocvideomerge.model;

import android.content.Context;

import com.tpnet.imoocvideomerge.base.Constant;
import com.tpnet.imoocvideomerge.bean.FileBean;
import com.tpnet.imoocvideomerge.bean.FolderBean;
import com.tpnet.imoocvideomerge.bean.IMooc;
import com.tpnet.imoocvideomerge.contact.FolderContact;
import com.tpnet.imoocvideomerge.model.face.IOnListener;
import com.tpnet.imoocvideomerge.model.face.IOnLoadListListener;
import com.tpnet.imoocvideomerge.util.FileUtils;
import com.tpnet.imoocvideomerge.util.LogUtil;
import com.tpnet.imoocvideomerge.util.RegularTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 逻辑操作，获取文件夹列表
 * Created by Litp on 2017/2/22.
 */

public class FolderListImpl implements FolderContact.IFloderModel {

    //private List<FolderBean> folderList = new ArrayList<>();


    @Override
    public void searchFolder(String searchText, IOnLoadListListener<FolderBean> listListener) {


        List<FolderBean> folderList = new ArrayList<>();
        for (FolderBean folderBean : IMooc.getInstance().getFolderList()) {

            if (folderBean.getFolderRealName().matches(RegularTool.getSearchReg(searchText))) {
                folderList.add(folderBean);
            }
        }

        listListener.success(folderList, null);
    }


    @Override
    public void getFolderList(Context context, final IOnListener listener) {


        //video文件夹
        File file = new File(Constant.getProjectPath(context));
        if (file.exists() && file.listFiles() != null) {

            Observable.fromArray(file.listFiles())
                    .observeOn(Schedulers.io())
                    .flatMap(new Function<File, ObservableSource<FolderBean>>() {
                        @Override
                        public ObservableSource<FolderBean> apply(@NonNull File file) throws Exception {
                            if (file.isDirectory()) {
                                //每个分类视频文件夹

                                FolderBean folderBean = new FolderBean();

                                folderBean.setFolderFileNum(file.listFiles().length + "");
                                folderBean.setFolderName(file.getName());
                                folderBean.setFolderDowntime(file.lastModified());

                                //保存信息到单例
                                IMooc.getInstance().addFolder(folderBean);


                                //保存文件夹信息
                                folderBean.setFolderSize(
                                        FileUtils.getFormatSize(getImoocFolderInfo(file, file.getName())
                                        ));

                                FileBean fileBean = IMooc.getInstance().getFileList(file.getName()).get(0);
                                folderBean.setFolderThumbPath(fileBean.getThumb_url());
                                folderBean.setFolderRealName(fileBean.getCourseName());

                                return Observable.just(folderBean);

                            }
                            return null;
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<FolderBean>() {
                        @Override
                        public void accept(@NonNull FolderBean folderBean) throws Exception {
                            //得到floderBean信息
                            //folderList.add(folderBean);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            throwable.printStackTrace();
                            LogUtil.e("获取文件夹信息有抛出错误:" + throwable.toString());
                            listener.success(new Exception("获取文件夹信息有抛出错误:" + throwable.toString()));
                        }
                    }, new Action() {
                        @Override
                        public void run() throws Exception {
                            //完毕
                            listener.success(null);
                        }
                    });


        } else {
            listener.success(new Exception("没有找到慕课网下载的视频"));
        }


    }


    /**
     * 获取每个分类视频文件夹大小和 设置信息
     *
     * @param file File实例
     * @return long
     */
    private long getImoocFolderInfo(File file, String preFolderName) {

        long size = 0;
        try {
            File[] fileList = file.listFiles();  //每个小视频的文件夹

            for (File aFileList : fileList) {

                if (aFileList.isDirectory()) {   //1940.all

                    FileBean fileBean = parseFileJson(new File(aFileList.getAbsolutePath() + File.separator + "json.txt"));

                    if (fileBean != null) {
                        long cacheSize = getImoocFolderInfo(aFileList, "");
                        fileBean.setFileRealSize(String.valueOf(cacheSize));
                        fileBean.setVideoFolderName(aFileList.getName());
                        //保存
                        IMooc.getInstance().addFile(preFolderName, fileBean);
                        size = size + cacheSize;
                    } else {
                        //如果是文件夹就递归继续获取
                        size = size + getImoocFolderInfo(aFileList, "");
                    }

                } else {

                    //获取文件的长度
                    size = size + aFileList.length();

                }
            }

        } catch (Exception e) {
            LogUtil.e("设置IMooc数据错误" + e.getMessage());
            e.printStackTrace();
        }

        return size;
    }


    /**
     * 解析json
     *
     * @param jsonFile json视频文件信息
     * @return
     */
    private FileBean parseFileJson(@NonNull File jsonFile) {


        String json = FileUtils.readFile(jsonFile);

        try {
            JSONObject jsonObject = new JSONObject(json);

            FileBean fileBean = new FileBean(jsonObject.optInt("section_seq")
                    , jsonObject.optString("thumb_url")
                    , jsonObject.optString("sectionName")
                    , jsonObject.optInt("courseId")
                    , jsonObject.optString("file_url")
                    , jsonObject.optInt("chapter_seq")
                    , jsonObject.optString("extras")
                    , jsonObject.optInt("recvsize")
                    , jsonObject.optInt("filesize")
                    , jsonObject.optInt("sectionId")
                    , jsonObject.optLong("downtime")
                    , jsonObject.optInt("chapterId")
                    , jsonObject.optInt("sectionType")
                    , jsonObject.optString("courseName")

            );
            String type = fileBean.getFile_url().substring(fileBean.getFile_url().lastIndexOf("."));

            fileBean.setFilePath(jsonFile.getParent() + File.separator + "down" + type);
            return fileBean;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }


}
