package com.lsh.XXRecyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Author:lsh
 * Version: 1.0
 * Description:
 * Date: 2017/2/27
 */

public class DefaultLoadCreator extends LoadViewCreator {
    // 加载数据的ImageView
    private View mRefreshIv;
    private TextView mTv;
    private ProgressBar mProgressBar;
    private TextView xxrecylcerview_footer_hint_textview;
    private int lastStatus;

    @Override
    public View getLoadView(Context context, ViewGroup parent) {
        View loadView = LayoutInflater.from(context).inflate(R.layout.layout_refresh_footer_view, parent, false);
        mProgressBar = ((ProgressBar) loadView.findViewById(R.id.xxrecylcerview_footer_progressbar));
        xxrecylcerview_footer_hint_textview = ((TextView) loadView.findViewById(R.id.xxrecylcerview_footer_hint_textview));
        return loadView;
    }
    @Override
    public void onPull(int currentDragHeight, int refreshViewHeight, int currentRefreshStatus) {
        //上啦刷新
        if (XXRecycleView.LOAD_STATUS_PULL_DOWN_REFRESH == currentRefreshStatus && lastStatus != XXRecycleView.LOAD_STATUS_PULL_DOWN_REFRESH) {
            mProgressBar.setVisibility(View.INVISIBLE);
            xxrecylcerview_footer_hint_textview.setVisibility(View.VISIBLE);
            xxrecylcerview_footer_hint_textview.setText(R.string.xxrecyclerview_footer_hint_normal);
            lastStatus = XXRecycleView.LOAD_STATUS_PULL_DOWN_REFRESH;
        } else if (XXRecycleView.LOAD_STATUS_LOOSEN_LOADING == currentRefreshStatus && lastStatus != XXRecycleView.LOAD_STATUS_LOOSEN_LOADING) {
            mProgressBar.setVisibility(View.INVISIBLE);
            xxrecylcerview_footer_hint_textview.setVisibility(View.VISIBLE);
            xxrecylcerview_footer_hint_textview.setText(R.string.xxrecyclerview_footer_hint_ready);
            lastStatus = XXRecycleView.LOAD_STATUS_LOOSEN_LOADING;
        }

    }

    @Override
    public void onLoading() {
//        // 刷新的时候不断旋转
        mProgressBar.setVisibility(View.VISIBLE);
        xxrecylcerview_footer_hint_textview.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStopLoad() {
        lastStatus = XXRecycleView.LOAD_STATUS_NORMAL;
        mProgressBar.setVisibility(View.INVISIBLE);
        xxrecylcerview_footer_hint_textview.setVisibility(View.VISIBLE);
        xxrecylcerview_footer_hint_textview.setText(R.string.xxrecyclerview_footer_hint_normal);
    }


}
