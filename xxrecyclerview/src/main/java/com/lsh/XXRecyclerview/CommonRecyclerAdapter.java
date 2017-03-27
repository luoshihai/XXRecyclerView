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

    /**
     *
     * @param helper
     * @param item 每个条目的数据
     * @param position 这个postition是包含头和脚的 请注意
     * @param itemChanged 数据是否发生改变
     */
    public abstract void convert(CommonViewHolder helper, T item, int position, boolean itemChanged);

    public void convert(CommonViewHolder helper, T item, int position) {
        boolean itemChanged = helper.associatedObject == null || !helper.associatedObject.equals(item);
        helper.associatedObject = item;
        convert(helper, item, position, itemChanged);
    }

    /**
     * 得到条目的数量, 这个数量是你数据的数量  不包含头和脚
     * @return 条目的数量
     */
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

    /**
     * 设置条目长按监听
     * @param onItemLongClickListener
     */
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    /**
     * 设置条目点击监听
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    /**
     * 替换数据 和replaceAll()方法重复
     * @param datas
     */
    public void notifydataChanged(List<T> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 添加一个条目
     * @param elem
     */
    public void add(T elem) {
        mDatas.add(elem);
        notifyDataSetChanged();
    }

    public void addAt(int index, T elem) {
        mDatas.add(index, elem);
        notifyDataSetChanged();
    }
    /**
     * 添加一个集合数据
     * @param elem
     */
    public void addAll(List<T> elem) {
        mDatas.addAll(elem);
        notifyDataSetChanged();
    }

    /**
     * 替换某个位置的数据
     * @param oldElem
     * @param newElem
     */
    public void set(T oldElem, T newElem) {
        setAt(mDatas.indexOf(oldElem), newElem);
    }

    /**
     * 替换某个位置的数据
     * @param index
     * @param elem
     */
    public void setAt(int index, T elem) {
        mDatas.set(index, elem);
        notifyDataSetChanged();
    }

    public void remove(T elem) {
        mDatas.remove(elem);
        notifyDataSetChanged();
    }

    /**
     * 移除某个条目
     * @param index  在datas中的角标
     */
    public void removeAt(int index) {
        mDatas.remove(index);
        notifyDataSetChanged();
    }

    /**
     * 替换全部
     * @param elem
     */
    public void replaceAll(List<T> elem) {
        mDatas.clear();
        mDatas.addAll(elem);
        notifyDataSetChanged();
    }

    /**
     * 是否包含某个条目
     * @param elem
     * @return
     */
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
