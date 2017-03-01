package com.example.administrator.recyclerviewdemo;

/**
 * Author:lsh
 * Version: 1.0
 * Description:
 * Date: 2017/2/24
 */

public interface MultiTypeSupport<T> {
    public int getLayoutId(T item, int position);
}
