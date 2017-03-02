package com.tpnet.imoocvideomerge.model;

import java.util.List;

/**
 * Created by Litp on 2017/2/24.
 */

public interface IOnLoadListListener<T> {

    void success(List<T> listData,Exception e);

}
