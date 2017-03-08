package com.lsh.XXRecyclerview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Author:lsh
 * Version: 1.0
 * Description:
 * Date: 2017/2/27
 */

public class XXRecycleView extends PullRefreshRecycleView {

    //最终loadView显示的位置  0 表示全显示
    int finalBottomMargin = 0;
    // 上拉加载更多的辅助类
    private LoadViewCreator mLoadCreator;
    // 上拉加载更多头部的高度
    private int mLoadViewHeight = 0;
    // 上拉加载更多的头部View
    private View mLoadView;
    // 手指按下的Y位置
    private int mFingerDownY;
    // 当前是否正在拖动
    private boolean mCurrentDrag = false;
    // 当前的状态
    private int mCurrentLoadStatus;
    // 默认状态
    public static int LOAD_STATUS_NORMAL = 0x0011;
    // 上拉加载更多状态
    public static int LOAD_STATUS_PULL_DOWN_REFRESH = 0x0022;
    // 松开加载更多状态
    public static int LOAD_STATUS_LOOSEN_LOADING = 0x0033;
    // 正在加载更多状态
    public static int LOAD_STATUS_LOADING = 0x0044;

    public XXRecycleView(Context context) {
        this(context, null);
    }

    public XXRecycleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public XXRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.XXRecyclerView, defStyle, 0);
        boolean refreshEnabled;
        boolean loadMoreEnabled;
        try {
            refreshEnabled = a.getBoolean(R.styleable.XXRecyclerView_refreshEnabled, false);
            loadMoreEnabled = a.getBoolean(R.styleable.XXRecyclerView_loadMoreEnabled, false);
        } finally {
            a.recycle();
        }

        if (refreshEnabled) setPullRefreshEnabled(refreshEnabled);
        if (loadMoreEnabled) setLoadMoreEnabled(loadMoreEnabled);
//        Toast.makeText(context, "refreshEnabled:" + refreshEnabled, Toast.LENGTH_SHORT).show();
    }

    // 先处理上拉加载更多，同时考虑加载列表的不同风格样式，确保这个项目还是下一个项目都能用
    // 所以我们不能直接添加View，需要利用辅助类
    public void addLoadViewCreator(LoadViewCreator loadCreator) {
        this.mLoadCreator = loadCreator;
        addRefreshView();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        addRefreshView();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录手指按下的位置 ,之所以写在dispatchTouchEvent那是因为如果我们处理了条目点击事件，
                // 那么就不会进入onTouchEvent里面，所以只能在这里获取
                mFingerDownY = (int) ev.getRawY();
                break;

            case MotionEvent.ACTION_UP:
                if (mLoadCreator == null || mLoadView == null) return super.dispatchTouchEvent(ev);
                if (mCurrentDrag) {
                    restoreLoadView();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 重置当前加载更多状态
     */
    private void restoreLoadView() {
        int currentBottomMargin = ((MarginLayoutParams) mLoadView.getLayoutParams()).bottomMargin;
        int tempBottomMargin = finalBottomMargin;
        if (mCurrentLoadStatus == LOAD_STATUS_LOOSEN_LOADING) {
            mCurrentLoadStatus = LOAD_STATUS_LOADING;
            tempBottomMargin = 0;
            if (mLoadCreator != null) {
                mLoadCreator.onLoading();
            }
            if (mListener != null) {
                mListener.onLoad();
            }
        }

        int distance = currentBottomMargin - tempBottomMargin;

        // 回弹到指定位置0.3
        ValueAnimator animator = ObjectAnimator.ofFloat(currentBottomMargin, tempBottomMargin).setDuration(distance);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentTopMargin = (float) animation.getAnimatedValue();
                setLoadViewMarginBottom((int) currentTopMargin);
            }
        });
        animator.start();
        mCurrentDrag = false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (mLoadCreator == null || mLoadView == null) return super.onTouchEvent(e);
                // 如果是在最底部才处理，否则不需要处理
                if (canScrollDown() || mCurrentLoadStatus == LOAD_STATUS_LOADING) {
                    // 如果没有到达最底端，也就是说还可以向下滚动就什么都不处理
                    return super.onTouchEvent(e);
                }

//                if (mLoadCreator != null) {
//                    mLoadViewHeight = mLoadView.getMeasuredHeight();
//                }
                // 解决上拉加载更多自动滚动问题
                if (mCurrentDrag) {
                    scrollToPosition(getAdapter().getItemCount() - 1);
                }

                // 获取手指触摸拖拽的距离
                int distanceY = (int) ((e.getRawY() - mFingerDownY) * mDragIndex);
                // 如果是已经到达头部，并且不断的向上拉，那么不断的改变refreshView的marginTop的值
                if (distanceY < 0) {
                    setLoadViewMarginBottom(-distanceY);
                    updateLoadStatus(-distanceY);
                    mCurrentDrag = true;
                    return true;
                }
                break;
        }

        return super.onTouchEvent(e);
    }

    /**
     * 更新加载的状态
     */
    private void updateLoadStatus(int distanceY) {
        if (distanceY <= 0) {
            mCurrentLoadStatus = LOAD_STATUS_NORMAL;
        } else if (distanceY < mLoadViewHeight) {
            mCurrentLoadStatus = LOAD_STATUS_PULL_DOWN_REFRESH;
        } else {
            mCurrentLoadStatus = LOAD_STATUS_LOOSEN_LOADING;
        }

        if (mLoadCreator != null) {
            mLoadCreator.onPull(distanceY, mLoadViewHeight, mCurrentLoadStatus);
        }
    }

    /**
     * 添加底部加载更多View
     */
    private void addRefreshView() {
        Adapter adapter = getAdapter();
        if (adapter != null && mLoadCreator != null) {
            // 添加底部加载更多View
            View loadView = mLoadCreator.getLoadView(getContext(), this);
            if (loadView != null) {
                addFooterView(loadView);
                loadView.setOnClickListener(onLoadViewClickListener);
                if (mLoadView != null) {
                    ((WrapRecyclerAdapter) adapter).removeFooterView(mLoadView);
                }
                this.mLoadView = loadView;
            }
        }
    }

    private OnClickListener onLoadViewClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mCurrentLoadStatus = LOAD_STATUS_LOOSEN_LOADING;
            restoreLoadView();
//            if (mListener != null) mListener.onLoad();
        }
    };

    /**
     * 设置加载View的marginBottom
     */
    public void setLoadViewMarginBottom(int marginBottom) {

        if (mLoadView == null) return;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mLoadView.getLayoutParams();
        if (params == null) return;
        if (marginBottom < -mLoadViewHeight + 1) {
            marginBottom = -mLoadViewHeight + 1;
        }
        params.bottomMargin = marginBottom;
        mLoadView.setLayoutParams(params);
    }

//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        super.onLayout(changed, l, t, r, b);
//        if (changed) {
//            if (mLoadView != null && mLoadViewHeight <= 0) {
//                // 获取头部刷新View的高度
//                mLoadView.measure(0, 0);
//                mLoadViewHeight = mLoadView.getMeasuredHeight();
//                finalBottomMargin = -mLoadViewHeight + 1;
//                if (mLoadViewHeight > 0) {
//                    // 隐藏头部刷新的View  marginTop  多留出1px防止无法判断是不是滚动到头部问题
//                    setLoadViewMarginBottom(-mLoadViewHeight + 1);
//                }
//            }
//        }
//    }

    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     * 判断是不是滚动到了最顶部，这个是从SwipeRefreshLayout里面copy过来的源代码
     */
    public boolean canScrollDown() {
        return ViewCompat.canScrollVertically(this, 1);
    }

    /**
     * 停止加载更多
     */
    public void stopLoad() {
        mCurrentLoadStatus = LOAD_STATUS_NORMAL;
        restoreLoadView();
        if (mLoadCreator != null) {
            mLoadCreator.onStopLoad();
        }
        if (mListener != null) mListener.loadEnd();
    }

    // 处理加载更多回调监听
    private OnLoadMoreListener mListener;

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.mListener = listener;
    }

    public interface OnLoadMoreListener {
        void onLoad();

        void loadEnd();
    }

    /**
     * @param needDefaultLoadView 是否使用默认上拉加载布局
     * @param showLoadMoreFirst   是否显示上拉加载的那个条目  如果设置不显示  效果就和下拉刷新一样  但是现在无效  后面改
     * @param loadCreator         可以使用自定义的脚布局 extents LoadViewCreator 就可以啦
     */
    public void setLoadMoreEnabled(boolean needDefaultLoadView, boolean showLoadMoreFirst, LoadViewCreator loadCreator) {
        if (needDefaultLoadView) {
            if (mLoadCreator != null && mLoadCreator instanceof DefaultLoadCreator) return;
            addLoadViewCreator(new DefaultLoadCreator());
        } else {
            if (getAdapter() != null && getAdapter() instanceof WrapRecyclerAdapter) {
                ((WrapRecyclerAdapter) getAdapter()).removeFooterView(mLoadView);
                mLoadView = null;
                mLoadCreator = null;
            } else {
                Toast.makeText(getContext(), "please set adapter first", Toast.LENGTH_SHORT).show();
            }
        }
        if (mLoadView != null) mLoadView.measure(0, 0);
        if (!showLoadMoreFirst) {
            finalBottomMargin = -mLoadView.getMeasuredHeight() + 1;
        } else {
            finalBottomMargin = 0;
        }

        //现在这个设置无效  还不明白 后面要改
        setLoadViewMarginBottom(finalBottomMargin);

        if (loadCreator != null) addLoadViewCreator(loadCreator);
    }

    public void setLoadMoreEnabled(boolean needDefaultLoadView, boolean showLoadMoreFirst) {
        setLoadMoreEnabled(needDefaultLoadView, showLoadMoreFirst, null);
    }

    public void setLoadMoreEnabled(boolean needDefaultLoadView) {
        setLoadMoreEnabled(needDefaultLoadView, true);
    }

}
