package com.tpnet.imoocvideomerge.bean;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 *
 * 每个视频的信息
 * Created by Litp on 2017/2/22.
 */

public class FileBean {






    public FileBean(int section_seq, String thumb_url, String sectionName, int courseId, String file_url, int chapter_seq, String extras, int recvsize, int filesize, int sectionId, long downtime, int chapterId, int sectionType, String courseName) {
        this.section_seq = section_seq;
        this.thumb_url = thumb_url;
        this.sectionName = sectionName;
        this.courseId = courseId;
        this.file_url = file_url;
        this.chapter_seq = chapter_seq;
        this.extras = extras;
        this.recvsize = recvsize;
        this.filesize = filesize;
        this.sectionId = sectionId;
        this.downtime = downtime;
        this.chapterId = chapterId;
        this.sectionType = sectionType;
        this.courseName = courseName;
    }

    /**
     * section_seq : 1
     * thumb_url : http://img.mukewang.com/5746675d0001afc506000338-533-300.jpg
     * sectionName : 开篇导学
     * courseId : 525
     * file_url : http://v1.mukewang.com/5d080112-d8a1-4376-b70d-a4aef3aa5efd/M.mp4
     * chapter_seq : 1
     * extras : {"courseType":"8fd85332445827cd817316190a812d13","lastPlayTime":0}
     * recvsize : 0
     * filesize : 3048061
     * sectionId : 10164
     * downtime : 1487727330679
     * chapterId : 2633
     * sectionType : 1
     * courseName : Android面试解密-Layout_weight
     */

    private String filePath; //这个视频的路径，让操作视频的时候读取
    private String videoFolderName; //视频的文件夹名称
    private String fileRealSize;  //视频真正的大小
    private String fileFormat;  //视频格式，zip/MP4




    private int section_seq;
    private String thumb_url;
    private String sectionName;  //视频的名称
    private int courseId;
    private String file_url;
    private int chapter_seq;
    private String extras;
    private int recvsize;
    private int filesize;
    private int sectionId;
    private long downtime;
    private int chapterId;
    private int sectionType;
    private String courseName;   //视频分类的名称


    public String getFileRealSize() {
        return fileRealSize;
    }

    public void setFileRealSize(String fileRealSize) {
        this.fileRealSize = fileRealSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    public String getVideoFolderName() {
        return videoFolderName;
    }

    public void setVideoFolderName(String videoFolderName) {
        this.videoFolderName = videoFolderName;
    }

    public int getSection_seq() {
        return section_seq;
    }

    public void setSection_seq(int section_seq) {
        this.section_seq = section_seq;
    }

    public String getThumb_url() {
        return thumb_url;
    }

    public void setThumb_url(String thumb_url) {
        this.thumb_url = thumb_url;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public int getChapter_seq() {
        return chapter_seq;
    }

    public void setChapter_seq(int chapter_seq) {
        this.chapter_seq = chapter_seq;
    }

    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

    public int getRecvsize() {
        return recvsize;
    }

    public void setRecvsize(int recvsize) {
        this.recvsize = recvsize;
    }

    public int getFilesize() {
        return filesize;
    }

    public void setFilesize(int filesize) {
        this.filesize = filesize;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public String getDowntime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(downtime);
    }

    public void setDowntime(long downtime) {
        this.downtime = downtime;
    }

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public int getSectionType() {
        return sectionType;
    }

    public void setSectionType(int sectionType) {
        this.sectionType = sectionType;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }


    public String getFileFormat() {
        if(TextUtils.isEmpty(fileFormat)){
            setFileFormat(file_url.substring(file_url.lastIndexOf(".")+1,file_url.length()));
        }
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }
}
