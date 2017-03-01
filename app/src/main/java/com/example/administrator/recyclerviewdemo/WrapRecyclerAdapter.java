package com.example.administrator.recyclerviewdemo;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Author:lsh
 * Version: 1.0
 * Description:
 * Date: 2017/2/24
 */

public class WrapRecyclerAdapter extends RecyclerView.Adapter {

    private SparseArray<View> mHeaderViews;
    private SparseArray<View> mFooterViews;


    private static int BASE_ITEN_HEADER = 100000;
    private static int BASE_ITEN_FOOTER = 200000;

    private RecyclerView.Adapter mAdapter;

    public WrapRecyclerAdapter(RecyclerView.Adapter adapter) {
        this.mAdapter = adapter;
        mHeaderViews = new SparseArray<>();
        mFooterViews = new SparseArray<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isHeaderViewType(viewType)) {
            View headerView = mHeaderViews.get(viewType);
            return creatHeaderFooterViewHolder(headerView);
        }

        if (isFooterViewType(viewType)) {
            View footerView = mFooterViews.get(viewType);
            return creatHeaderFooterViewHolder(footerView);
        }
        return mAdapter.createViewHolder(parent,viewType);
    }

    private boolean isFooterViewType(int viewType) {
        int i = mFooterViews.indexOfKey(viewType);
        return i>= 0;
    }

    private RecyclerView.ViewHolder creatHeaderFooterViewHolder(View headerView) {

        return new RecyclerView.ViewHolder(headerView) {

        };
    }

    private boolean isHeaderViewType(int viewType) {
        int position = mHeaderViews.indexOfKey(viewType);
        return position>=0;
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderPosition(position)) {
            return mHeaderViews.keyAt(position);
        }
        if (isFooterPosition(position)) {
            position = position - mHeaderViews.size() - mAdapter.getItemCount();
            return mFooterViews.keyAt(position);
        }
        position = position - mHeaderViews.size();
        return mAdapter.getItemViewType(position);
    }

    private boolean isFooterPosition(int position) {
        if (position >= mHeaderViews.size() + mAdapter.getItemCount()) {
            return true;
        }
        return false;
    }

    private boolean isHeaderPosition(int position) {
        if (position < mHeaderViews.size()) {
            return true;
        }
        return false;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderPosition(position)|| isFooterPosition(position)) {
            return;
        }
        position = position - mHeaderViews.size();
        mAdapter.onBindViewHolder(holder,position);
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() +mFooterViews.size() + mHeaderViews.size() ;
    }

    public void addHeaderView(View view) {
        int i = mHeaderViews.indexOfValue(view);
        if (i < 0) {
            mHeaderViews.put(BASE_ITEN_HEADER ++ ,view);
        }
        notifyDataSetChanged();
    }

    public void addFooterView(View view) {
        int i = mFooterViews.indexOfValue(view);
        if (i < 0) {
            mFooterViews.put(BASE_ITEN_FOOTER++, view);
        }
        notifyDataSetChanged();
    }

    public void removeHeaderView(View view) {
        int i = mHeaderViews.indexOfValue(view);
        if (i > 0) {
            mHeaderViews.removeAt(i);
            notifyDataSetChanged();
        }
    }

    public void removeFooterView(View view) {
        int i = mFooterViews.indexOfValue(view);
        if (i > 0) {
            mFooterViews.removeAt(i);
            notifyDataSetChanged();
        }
    }

    public void adjustSpanSize(RecyclerView recyclerView) {
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    boolean isHeaderOrFooter = isHeaderPosition(position) || isFooterPosition(position);
                    return isHeaderOrFooter? layoutManager.getSpanCount():1;
                }
            });
        }
    }
}
