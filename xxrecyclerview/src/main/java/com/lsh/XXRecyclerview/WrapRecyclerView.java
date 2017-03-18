package com.lsh.XXRecyclerview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Author:lsh
 * Version: 1.0
 * Description:
 * Date: 2017/2/24
 */

public class WrapRecyclerView extends RecyclerView {
    private ArrayList<View> tempHeaderView;
    private ArrayList<View> tempFooterView;
    private View emptyView;
    private RecyclerView.Adapter mAdapter;
    private boolean isShowEmptyViewWhenHasHeaderOrFooder = false;
    private WrapRecyclerAdapter mWrapRecyclerAdapter;

    public WrapRecyclerView(Context context) {
        this(context, null);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutManager(new LinearLayoutManager(context));
    }

//    @Override
//    public void setLayoutManager(LayoutManager layout) {
//
//        super.setLayoutManager(layout);
//
//    }

    private void checkIfEmpty() {
        Adapter adapter = getAdapter();
        if (emptyView != null && adapter != null && adapter instanceof WrapRecyclerAdapter) {
            WrapRecyclerAdapter wrapRecyclerAdapter = (WrapRecyclerAdapter) adapter;
            boolean emptyViewVisible = isShowEmptyViewWhenHasHeaderOrFooder ? wrapRecyclerAdapter.getItemCount() <= 0 : wrapRecyclerAdapter.getItemCount() - wrapRecyclerAdapter.getHeaderCount() - wrapRecyclerAdapter.getHeaderCount() <= 0;
            emptyView.setVisibility(emptyViewVisible ? VISIBLE : GONE);
            setVisibility(emptyViewVisible ? GONE : VISIBLE);
        }
    }


    @Override
    public void setAdapter(Adapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterAdapterDataObserver(mDataObserver);
            mAdapter = null;
        }
        this.mAdapter = adapter;
        if (adapter instanceof WrapRecyclerAdapter) {
            mWrapRecyclerAdapter = (WrapRecyclerAdapter) adapter;
        } else {
            mWrapRecyclerAdapter = new WrapRecyclerAdapter(adapter);
            if (tempHeaderView != null) {
                for (View view : tempHeaderView) {
                    mWrapRecyclerAdapter.addHeaderView(view);
                }
            }
            if (tempFooterView != null) {
                for (View view : tempFooterView) {
                    mWrapRecyclerAdapter.addFooterView(view);
                }
            }
        }
        super.setAdapter(mWrapRecyclerAdapter);

        mAdapter.registerAdapterDataObserver(mDataObserver);
        mWrapRecyclerAdapter.adjustSpanSize(this);
        checkIfEmpty();
    }


    public void addHeaderView(View view) {
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter.addHeaderView(view);
        } else {
            if (tempHeaderView == null) tempHeaderView = new ArrayList<>();
            tempHeaderView.add(view);
        }
    }

    public void addFooterView(View view) {
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter.addFooterView(view);
        } else {
            if (tempFooterView == null) tempFooterView = new ArrayList<>();
            tempFooterView.add(view);
        }
    }

    public void removeHeaderView(View view) {
        if (mWrapRecyclerAdapter != null) mWrapRecyclerAdapter.removeHeaderView(view);
    }

    public void removeFooterView(View view) {
        if (mWrapRecyclerAdapter != null) mWrapRecyclerAdapter.removeFooterView(view);
    }





    /**
     * 设置空布局
     * @param view
     * @param isShowEmptyViewWhenHasHeaderOrFooder  当有头有脚的时候是否显示空布局
     */
    public void setEmptyView(View view, boolean isShowEmptyViewWhenHasHeaderOrFooder) {
        this.emptyView = view;
        this.isShowEmptyViewWhenHasHeaderOrFooder = isShowEmptyViewWhenHasHeaderOrFooder;
        ViewGroup parent = (ViewGroup) getParent();
        if (emptyView.getParent() == null) {
            parent.addView(emptyView);
        }

        checkIfEmpty();
    }

    /**
     * 使用默认的空布局
     */
    public void setEmptyView() {
        setEmptyView(R.layout.empty_view);
    }
    /**
     * 设置空布局
     * @param view
     */
    public void setEmptyView(View view) {
        setEmptyView(view, false);
    }

    /**
     * 设置空布局
     * @param resurceId 布局id
     */
    public void setEmptyView(@LayoutRes int resurceId) {
       setEmptyView(resurceId,false);
    }

    /**
     * 设置空布局
     * @param resurceId  资源ID
     * @param isShowEmptyViewWhenHasHeaderOrFooder 当有头有脚的时候是否显示空布局
     */
    public void setEmptyView(@LayoutRes int resurceId,boolean isShowEmptyViewWhenHasHeaderOrFooder) {
        ViewGroup rootView = (ViewGroup) getParent();
        View emptyView = LayoutInflater.from(getContext()).inflate(resurceId, rootView, false);
        setEmptyView(emptyView,isShowEmptyViewWhenHasHeaderOrFooder);
    }

    private AdapterDataObserver mDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            if (mAdapter == null) return;
            if (mWrapRecyclerAdapter != mAdapter) {
                mWrapRecyclerAdapter.notifyDataSetChanged();
            }
            checkIfEmpty();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemChanged没效果
            if (mWrapRecyclerAdapter != mAdapter)
                mWrapRecyclerAdapter.notifyItemChanged(positionStart);
            checkIfEmpty();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemChanged没效果
            if (mWrapRecyclerAdapter != mAdapter)
                mWrapRecyclerAdapter.notifyItemChanged(positionStart, payload);
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemInserted没效果
            if (mWrapRecyclerAdapter != mAdapter)
                mWrapRecyclerAdapter.notifyItemInserted(positionStart);
            checkIfEmpty();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemMoved没效果
            if (mWrapRecyclerAdapter != mAdapter)
                mWrapRecyclerAdapter.notifyItemMoved(fromPosition, toPosition);
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyDataSetChanged没效果
            if (mWrapRecyclerAdapter != mAdapter)
                mWrapRecyclerAdapter.notifyItemRemoved(positionStart);
            checkIfEmpty();
        }
    };
}
