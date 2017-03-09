package com.lsh.XXRecyclerview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:lsh
 * Version: 1.0
 * Description:
 * Date: 2017/2/24
 */

public abstract class CommonRecyclerAdapter<T> extends RecyclerView.Adapter implements View.OnClickListener, View.OnLongClickListener {

    private Context mContext;
    private List<T> mDatas = new ArrayList<>();
    private int mLayoutId;
    private LayoutInflater mInflater;
    private MultiTypeSupport mMultiTypeSupport;

    public CommonRecyclerAdapter(Context context, List<T> datas, @LayoutRes int layoutId) {
        this(context, datas, null);
        this.mLayoutId = layoutId;
    }

    public CommonRecyclerAdapter(Context context, List<T> datas, MultiTypeSupport multiTypeSupport) {
        this.mContext = context;
        if (datas != null) this.mDatas.addAll(datas);
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

        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        CommonViewHolder commonViewHolder = new CommonViewHolder(view);
        return commonViewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        CommonViewHolder commonViewHolder = (CommonViewHolder) holder;
        commonViewHolder.getItemView().setTag(R.id.Tag_1, commonViewHolder);
        convert(commonViewHolder, mDatas.get(position), position);
    }

//    public abstract void convert(CommonViewHolder holder, T t, int position);


    public abstract void convert(CommonViewHolder helper, T item, int position, boolean itemChanged);

    public void convert(CommonViewHolder helper, T item, int position) {
        boolean itemChanged = helper.associatedObject == null || !helper.associatedObject.equals(item);
        helper.associatedObject = item;
        convert(helper, item, position, itemChanged);
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public List<T> getDatas() {
        return mDatas;
    }

    @Override
    public void onClick(View v) {
        CommonViewHolder commonViewHolder = (CommonViewHolder) v.getTag(R.id.Tag_1);
        if (commonViewHolder != null) {
            int position = commonViewHolder.getPosition();
            if (mOnItemClickListener != null)
                mOnItemClickListener.onItemClickListener(commonViewHolder, position);
        }

    }

    @Override
    public boolean onLongClick(View v) {
        CommonViewHolder commonViewHolder = (CommonViewHolder) v.getTag(R.id.Tag_1);
        if (commonViewHolder != null) {
            int position = commonViewHolder.getPosition();
            if (mOnItemLongClickListener != null)
                mOnItemLongClickListener.onItemLongClickListener(commonViewHolder, position);
            return true;
        }
        return false;
    }

    public interface OnItemClickListener {
        void onItemClickListener(CommonViewHolder commonViewHolder, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClickListener(CommonViewHolder commonViewHolder, int position);
    }

    public OnItemLongClickListener mOnItemLongClickListener;
    public OnItemClickListener mOnItemClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void notifydataChanged(List<T> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public void add(T elem) {
        mDatas.add(elem);
        notifyDataSetChanged();
    }

    public void addAll(List<T> elem) {
        mDatas.addAll(elem);
        notifyDataSetChanged();
    }

    public void set(T oldElem, T newElem) {
        setAt(mDatas.indexOf(oldElem), newElem);
    }

    public void setAt(int index, T elem) {
        mDatas.set(index, elem);
        notifyDataSetChanged();
    }

    public void remove(T elem) {
        mDatas.remove(elem);
        notifyDataSetChanged();
    }

    public void removeAt(int index) {
        mDatas.remove(index);
        notifyDataSetChanged();
    }

    public void replaceAll(List<T> elem) {
        mDatas.clear();
        mDatas.addAll(elem);
        notifyDataSetChanged();
    }

    public boolean contains(T elem) {
        return mDatas.contains(elem);
    }

    /**
     * Clear data list
     */
    public void clear() {
        mDatas.clear();
        notifyDataSetChanged();
    }

}
