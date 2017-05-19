package com.tpnet.imoocvideomerge.model;

import android.content.Context;

import com.tpnet.imoocvideomerge.bean.FileBean;
import com.tpnet.imoocvideomerge.bean.FolderBean;
import com.tpnet.imoocvideomerge.bean.IMooc;
import com.tpnet.imoocvideomerge.model.face.ICommon;
import com.tpnet.imoocvideomerge.model.face.IOnProgressListener;
import com.tpnet.imoocvideomerge.util.FileUtils;
import com.tpnet.imoocvideomerge.util.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 公共的业务逻辑
 * Created by Litp on 2017/2/24.
 */

public class CommonImpl implements ICommon<String> {


    private Disposable copyDisposable = null;


    @Override
    public void saveFolder(final Context context, @NonNull final FolderBean folder, final IOnProgressListener<String> listListener) {

        //保存文件夹


        final List<String> successList = new ArrayList<>();
        final List<String> errorList = new ArrayList<>();

        final List<FileBean> fileList = IMooc.getInstance().getFileList(folder.getFolderName());

        copyDisposable = Observable.create(new ObservableOnSubscribe<FileBean>() {
            @Override
            public void subscribe(ObservableEmitter<FileBean> e) throws Exception {
                for (FileBean fileBean : fileList) {
                    e.onNext(fileBean);
                }
                e.onComplete();
            }
        }).observeOn(Schedulers.io())
                .map(new Function<FileBean, String>() {
                    @Override
                    public String apply(@NonNull FileBean fileBean) throws Exception {
                        String newFilePath = FileUtils.getSaveRootPathDefault(context);

                        newFilePath = newFilePath +
                                folder.getFolderRealName() +
                                File.separator +
                                fileBean.getChapter_seq() +
                                "-" +
                                fileBean.getSection_seq() +
                                fileBean.getSectionName() +
                                "." +
                                fileBean.getFileFormat();


                        if (FileUtils.copyFile(fileBean.getFilePath(), newFilePath)) {
                            successList.add(fileBean.getSectionName());
                            return newFilePath;
                        } else {
                            errorList.add(fileBean.getSectionName());
                            return "";
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        listListener.progress(s
                                , fileList.size()
                                , successList.size()
                                , (int) (successList.size() / (double) fileList.size() * 100));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        listListener.success(successList, errorList, createException(errorList));
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        listListener.success(successList, errorList, createException(errorList));
                    }
                });

    }


    @Override
    public void saveFile(final Context context, final FileBean file, final IOnProgressListener<String> listListener) {

        final StringBuffer newFilePath = new StringBuffer();

        newFilePath.append(FileUtils.getSaveRootPathDefault(context));


        newFilePath.append(
                file.getCourseName() +
                        File.separator +
                        file.getChapter_seq() +
                        "-" +
                        file.getSection_seq() +
                        file.getSectionName() +
                        "." +
                        file.getFileFormat());


        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(final ObservableEmitter<Integer> o) throws Exception {


                LogUtil.e("旧文件路径:" + file.getFilePath());

                FileUtils.copyFile(file.getFilePath(), newFilePath.toString(), new IOnProgressListener<String>() {
                    @Override
                    public void success(List<String> successList, List<String> errorList, Exception e) {
                        if (e == null) {
                            o.onComplete();
                        } else {
                            o.onError(e);
                        }

                    }

                    @Override
                    public void progress(String data, long total, long curr, Integer percent) {
                        o.onNext(percent);
                    }
                });

            }
        }).distinctUntilChanged()   //过滤连续重复的进度
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        listListener.progress(newFilePath.toString(), 0, 0, integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listListener.success(null, Collections.singletonList(file.getFilePath()), new Exception(e));
                    }

                    @Override
                    public void onComplete() {
                        listListener.success(Collections.singletonList(newFilePath.toString()), null, null);

                    }
                });

    }


    private Exception createException(List<String> errorList) {
        Exception e = null;
        if (errorList.size() > 0) {
            StringBuilder errorMsg = new StringBuilder();
            for (String str : errorList) {
                errorMsg.append(str).append("、");
            }
            e = new Exception(errorList.size() + "个视频文件复制失败!" + errorMsg);
        }
        return e;
    }

    /**
     * 取消复制
     */
    public void cancleCopy() {
        if (copyDisposable != null && !copyDisposable.isDisposed()) {
            copyDisposable.dispose();
        }
    }


}
