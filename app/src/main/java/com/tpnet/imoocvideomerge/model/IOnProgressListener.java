package com.tpnet.imoocvideomerge.model;

import java.util.List;

/**
 * Created by Litp on 2017/2/24.
 */

public interface IOnProgressListener<T> {

    void success(List<T> successList,List<T> errorList,Exception e);

    void progress(T data,long total,long curr,Integer percent);

}
