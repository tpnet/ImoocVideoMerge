package com.tpnet.imoocvideomerge.base;

import com.tpnet.imoocvideomerge.util.FileUtils;

import java.io.File;

/**
 * Created by Litp on 2017/2/22.
 */

public class Constant {

    //视频列表目录
    public static final String ROOT_PATH = FileUtils.getRootPath() + "Android"
            + File.separator + "data" + File.separator
            + "cn.com.open.mooc"+ File.separator + "video" + File.separator;


    public static final String INTENT_FOLDER = "folder";  //传递folderName的intent标识

}
