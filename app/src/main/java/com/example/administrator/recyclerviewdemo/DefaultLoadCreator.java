package com.example.administrator.recyclerviewdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
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

    @Override
    public View getLoadView(Context context, ViewGroup parent) {
        View refreshView = LayoutInflater.from(context).inflate(R.layout.layout_refresh_header_view, parent, false);
        mRefreshIv = refreshView.findViewById(R.id.refresh_iv);
        mTv = (TextView) refreshView.findViewById(R.id.tv);
        return refreshView;
    }

    @Override
    public void onPull(int currentDragHeight, int refreshViewHeight, int currentRefreshStatus) {
        if (currentRefreshStatus == XXRecycleView.LOAD_STATUS_LOADING) {
            mRefreshIv.setVisibility(View.VISIBLE);
            mTv.setVisibility(View.GONE);
            float rotate = ((float) currentDragHeight) / refreshViewHeight;
            // 不断下拉的过程中不断的旋转图片
            mRefreshIv.setRotation(rotate * 360);
        } else if (currentRefreshStatus == XXRecycleView.LOAD_STATUS_PULL_DOWN_REFRESH) {
            mRefreshIv.setVisibility(View.GONE);
            mTv.setVisibility(View.VISIBLE);
            mTv.setText("上拉加载更多");
        } else if (currentRefreshStatus == XXRecycleView.LOAD_STATUS_LOOSEN_LOADING) {
            mRefreshIv.setVisibility(View.GONE);
            mTv.setVisibility(View.VISIBLE);
            mTv.setText("松开加载更多");
        } else if (currentRefreshStatus == XXRecycleView.LOAD_STATUS_NORMAL) {

        }
    }

    @Override
    public void onLoading() {
        // 刷新的时候不断旋转
        RotateAnimation animation = new RotateAnimation(0, 720,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setRepeatCount(-1);
        animation.setDuration(3000);
        mRefreshIv.startAnimation(animation);
        mTv.setVisibility(View.GONE);
        mRefreshIv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStopLoad() {
        // 停止加载的时候清除动画
        mRefreshIv.setRotation(0);
        mRefreshIv.clearAnimation();
        mTv.setVisibility(View.VISIBLE);
        mRefreshIv.setVisibility(View.GONE);
        mTv.setText("上拉加载更多");
    }


}
