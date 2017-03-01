package com.example.administrator.recyclerviewdemo;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Author:lsh
 * Version: 1.0
 * Description:
 * Date: 2017/2/24
 */

public abstract class CommonRecyclerAdapter<T> extends RecyclerView.Adapter {

    private Context mContext;
    private List<T> mDatas;
    private int mLayoutId;
    private LayoutInflater mInflater;
    private MultiTypeSupport mMultiTypeSupport;

    public CommonRecyclerAdapter(Context context, List<T> datas, @LayoutRes int layoutId) {
        this(context, datas, null);
        this.mLayoutId = layoutId;
    }

    public CommonRecyclerAdapter(Context context, List<T> datas, MultiTypeSupport multiTypeSupport) {
        this.mContext = context;
        this.mDatas = datas;
        this.mLayoutId = -1;
        this.mInflater = LayoutInflater.from(context);
        this.mMultiTypeSupport = multiTypeSupport;
    }


    @Override
    public int getItemViewType(int position) {
        if (mMultiTypeSupport != null)
          return mMultiTypeSupport.getLayoutId(mDatas.get(position), position);
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mMultiTypeSupport != null) mLayoutId = viewType;
        View view = mInflater.inflate(mLayoutId, parent, false);
        CommonViewHolder commonViewHolder = new CommonViewHolder(view);
        return commonViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        convert((CommonViewHolder) holder, mDatas.get(position),position);
    }

    public abstract void convert(CommonViewHolder holder, T t, int position);

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }
}
