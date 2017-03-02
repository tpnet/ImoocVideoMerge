package com.tpnet.imoocvideomerge.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Litp on 2017/2/22.
 */

public class FolderBean implements Parcelable {




    private String folderName;      //该分类视频文件夹的名字

    private String folderRealName;   //该分类视频真正的名字

    private String folderFileNum;
    private String folderDownProgress;
    private long folderDowntime;

    private String folderThumbPath; //视频分类缩略图

    private String folderSize; //文件夹文件大小

    public String getFolderThumbPath() {
        return folderThumbPath;
    }

    public void setFolderThumbPath(String folderThumbPath) {
        this.folderThumbPath = folderThumbPath;
    }

    public String getFolderRealName() {
        return folderRealName;
    }

    public void setFolderRealName(String folderRealName) {
        this.folderRealName = folderRealName;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderFileNum() {
        return folderFileNum;
    }

    public void setFolderFileNum(String folderFileNum) {
        this.folderFileNum = folderFileNum;
    }

    public String getFolderDownProgress() {
        return folderDownProgress;
    }

    public void setFolderDownProgress(String folderDownProgress) {
        this.folderDownProgress = folderDownProgress;
    }

    public String getFolderDowntime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(folderDowntime);

    }

    public void setFolderDowntime(long folderDowntime) {
        this.folderDowntime = folderDowntime;
    }

    public String getFolderSize() {
        return folderSize;
    }

    public void setFolderSize(String folderSize) {
        this.folderSize = folderSize;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.folderName);
        dest.writeString(this.folderRealName);
        dest.writeString(this.folderFileNum);
        dest.writeString(this.folderDownProgress);
        dest.writeLong(this.folderDowntime);
        dest.writeString(this.folderThumbPath);
        dest.writeString(this.folderSize);
    }

    public FolderBean() {
    }

    protected FolderBean(Parcel in) {
        this.folderName = in.readString();
        this.folderRealName = in.readString();
        this.folderFileNum = in.readString();
        this.folderDownProgress = in.readString();
        this.folderDowntime = in.readLong();
        this.folderThumbPath = in.readString();
        this.folderSize = in.readString();
    }

    public static final Parcelable.Creator<FolderBean> CREATOR = new Parcelable.Creator<FolderBean>() {
        @Override
        public FolderBean createFromParcel(Parcel source) {
            return new FolderBean(source);
        }

        @Override
        public FolderBean[] newArray(int size) {
            return new FolderBean[size];
        }
    };
}
