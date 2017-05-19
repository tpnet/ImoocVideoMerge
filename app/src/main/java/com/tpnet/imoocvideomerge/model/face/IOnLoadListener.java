package com.tpnet.imoocvideomerge.model.face;

/**
 * 带类型的回调接口
 * Created by Litp on 2017/2/24.
 */

public interface IOnLoadListener<T> {

    void success(T data,Exception e);

}
