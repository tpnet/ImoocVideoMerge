package com.tpnet.imoocvideomerge.util;

import android.text.TextUtils;

/**
 * 正则表达式工具类
 * Created by Litp on 2017/2/24.
 */

public class RegularTool {

    public static String getSearchReg(String text){
        if (!TextUtils.isEmpty(text)) {
            String[] arr = new String[text.length()];   //用一个数组存放每个字符添加正则表达式
            for (int i = 0; i < text.length(); i++) {
                String cache = text.substring(i, i + 1);
                if(Character.isLetter(cache.toCharArray()[0])){
                    //是字母，转换为支持大小写
                    cache = "["+cache.toLowerCase() + cache.toUpperCase() + "]";
                }
                arr[i] = ".*" + cache;
            }
            text = "";
            for (String anArr : arr) {
                text += anArr;
            }
            text += ".*";
        }else{
            text = ".*";
        }
        return text;
    }

}
