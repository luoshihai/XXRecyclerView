package com.lsh.XXListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/11/19.
 */
public abstract class CommonListAdapter<T> extends BaseAdapter {
    private boolean isHomeFragment = false;
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<T> mDatas;
    protected final int mItemLayoutId;

    public CommonListAdapter(Context context, List<T> mDatas, int itemLayoutId) {
        this.mContext = context.getApplicationContext();
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = mDatas;
        this.mItemLayoutId = itemLayoutId;
    }

    public CommonListAdapter(Context context, List<T> mDatas, int itemLayoutId, boolean isHomeFragment) {
        this.isHomeFragment = isHomeFragment;
        this.mContext = context.getApplicationContext();
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = mDatas;
        this.mItemLayoutId = itemLayoutId;
    }

    @Override
    public int getCount() {
        if (isHomeFragment) {
            return  (mDatas == null ? 0 : mDatas.size() % 3 == 0 ? mDatas.size() / 3 : mDatas.size() / 3 + 1);
        } else {
            return mDatas == null ? 0 : mDatas.size();
        }

    }
    public List<T> getDatas() {
        return mDatas;
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = getViewHolder(position, convertView,
                parent);
        convert(viewHolder, getItem(position));
        return viewHolder.getConvertView();
    }

    public abstract void convert(ViewHolder helper, T item,boolean itemChanged);

    public  void convert(ViewHolder helper, T item){
        boolean itemChanged = helper.associatedObject == null || !helper.associatedObject.equals(item);
        helper.associatedObject = item;
        convert(helper,item,itemChanged);
    }

    private ViewHolder getViewHolder(int position, View convertView,
                                     ViewGroup parent) {
        return ViewHolder.get(mContext, convertView, parent, mItemLayoutId,
                position);
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
        set(mDatas.indexOf(oldElem), newElem);
    }

    public void set(int index, T elem) {
        mDatas.set(index, elem);
        notifyDataSetChanged();
    }

    public void remove(T elem) {
        mDatas.remove(elem);
        notifyDataSetChanged();
    }

    public void remove(int index) {
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

    /** Clear data list */
    public void clear() {
        mDatas.clear();
        notifyDataSetChanged();
    }
}