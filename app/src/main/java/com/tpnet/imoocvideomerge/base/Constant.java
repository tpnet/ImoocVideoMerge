package com.tpnet.imoocvideomerge.base;

import android.content.Context;

import com.tpnet.imoocvideomerge.util.FileUtils;

import java.io.File;

/**
 * 常量类
 * Created by Litp on 2017/2/22.
 */

public class Constant {


    public static final String INTENT_FOLDER = "folder";  //传递folderName的intent标识

    //视频列表目录
    public static final String getProjectPath(Context context) {
        String rootPath = FileUtils.getShowRootPathDefault(context);
        rootPath += "Android"
                + File.separator + "data" + File.separator
                + "cn.com.open.mooc" + File.separator + "video" + File.separator;

        return rootPath;
    }
    
    
}
