package com.lsh.XXRecyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author:lsh
 * Version: 1.0
 * Description:
 * Date: 2017/2/24
 */

public class DefaultRefreshCreator extends RefreshViewCreator {
    // 加载数据的ImageView
    private final int ROTATE_ANIM_DURATION = 180;
    private TextView xxrecyclerview_header_hint_textview;
    private TextView xlistview_header_time;
    private ImageView mArrowImageView;
    private ProgressBar mProgressBar;
    private RotateAnimation mRotateUpAnim, mRotateDownAnim;
    private SimpleDateFormat mFormatter;
    protected View refreshView;
    @Override
    public View getRefreshView(Context context, ViewGroup parent) {
         refreshView = LayoutInflater.from(context).inflate(R.layout.layout_refresh_header_view, parent, false);
        xxrecyclerview_header_hint_textview = ((TextView) refreshView.findViewById(R.id.xxrecyclerview_header_hint_textview));
        xlistview_header_time = ((TextView) refreshView.findViewById(R.id.xlistview_header_time));
        mArrowImageView = ((ImageView) refreshView.findViewById(R.id.xxrecyclerview_header_arrow));
        mProgressBar = ((ProgressBar) refreshView.findViewById(R.id.xxrecyclerview_header_progressbar));
        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
        mFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return refreshView;
    }

    private int lastStatus;

    @Override
    public void onPull(int currentDragHeight, int refreshViewHeight, int currentRefreshStatus) {
//        // 不断下拉的过程中不断的旋转图片
        mArrowImageView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        if (PullRefreshRecycleView.REFRESH_STATUS_LOOSEN_REFRESHING == currentRefreshStatus && lastStatus != PullRefreshRecycleView.REFRESH_STATUS_LOOSEN_REFRESHING) {
            mArrowImageView.clearAnimation();
            mArrowImageView.startAnimation(mRotateUpAnim);
            xxrecyclerview_header_hint_textview.setText(R.string.xxrecyclerview_header_hint_ready);
            lastStatus = PullRefreshRecycleView.REFRESH_STATUS_LOOSEN_REFRESHING;
        } else if (PullRefreshRecycleView.REFRESH_STATUS_PULL_DOWN_REFRESH == currentRefreshStatus && lastStatus != PullRefreshRecycleView.REFRESH_STATUS_PULL_DOWN_REFRESH) {
            mArrowImageView.clearAnimation();
            mArrowImageView.startAnimation(mRotateDownAnim);
            xxrecyclerview_header_hint_textview.setText(R.string.xxrecyclerview_header_hint_normal);
            lastStatus = PullRefreshRecycleView.REFRESH_STATUS_PULL_DOWN_REFRESH;
        }
    }

    @Override
    public void onRefreshing() {
        // 刷新的时候不断旋转
        mArrowImageView.clearAnimation();
        mArrowImageView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        xxrecyclerview_header_hint_textview.setText(R.string.xxrecyclerview_header_hint_refreshing);
    }

    @Override
    public void onStopRefresh() {
        // 停止加载的时候清除动画
        mArrowImageView.clearAnimation();
        mArrowImageView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        xxrecyclerview_header_hint_textview.setText(R.string.xxrecyclerview_header_hint_normal);
        Date curDate = new Date(System.currentTimeMillis());
        xlistview_header_time.setText(mFormatter.format(curDate));
    }
}
