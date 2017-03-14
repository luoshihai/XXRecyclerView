package com.lsh.XXRecyclerview;

/**
 * Author:lsh
 * Version: 1.0
 * Description:
 * Date: 2017/2/24
 */

/**
 * 多条目布局
 * @param <T>
 */
public interface MultiTypeSupport<T> {
    public int getLayoutId(T item, int position);
}
