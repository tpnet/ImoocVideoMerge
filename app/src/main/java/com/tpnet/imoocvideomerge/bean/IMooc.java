package com.tpnet.imoocvideomerge.bean;

import android.content.Context;

import com.tpnet.imoocvideomerge.util.FileUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IMooc {


    private static IMooc instance;


    private IMooc() {
    }

    public static IMooc getInstance() {
        if (instance == null) {
            instance = new IMooc();
        }
        return instance;
    }


    private  List<FolderBean> folderList = new ArrayList<>();  //保存第一层的 视频总列表

    private Map<String,List<FileBean>> fileMap = new HashMap<>();  //根据文件夹名标名字识来获取对应的子文件列表


    private String availableSpace = ""; //可用控件
    private Integer availablePercent = 0; //占用了空间


    public  List<FileBean> getFileList(String folderName){
        return  fileMap.get(folderName);
    }

    public  List<FolderBean> getFolderList(){
        return  folderList;
    }




    public void addFolder(FolderBean folderBean){
        folderList.add(folderBean);
    }

    public Boolean addFile(String folderName,FileBean fileBean) {
        if(fileMap.containsKey(folderName)){
            //已经有了
            if(fileMap.get(folderName).contains(fileBean)){
                return false;
            }else{
                fileMap.get(folderName).add(fileBean);
                return true;
            }
        }else{
            //需要转换为ArrayList，不然会报异常 UnsupportedOperationException
            fileMap.put(folderName, new ArrayList(Arrays.asList(fileBean)));
            return true;
        }

    }


    public Boolean addFileList(String folderName,List<FileBean> fileList){
        if(fileMap.containsKey(folderName)){
            fileMap.get(folderName).clear();
            fileMap.put(folderName,fileList);
            return true;
        }else{
            fileMap.put(folderName,fileList);
            return false;
        }

    }


    public String getAvailableSpace(Context context) {
        return FileUtils.getFormatSize(FileUtils.getSDAvailableSize(context));
    }


    public Integer getAvailablePercent(Context context) {
        return FileUtils.getSDAvailablePercent(context);
    }


}