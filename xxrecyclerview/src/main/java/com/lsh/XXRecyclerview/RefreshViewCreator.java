package com.lsh.XXRecyclerview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Author:lsh
 * Version: 1.0
 * Description:
 * Date: 2017/2/24
 */

public abstract class RefreshViewCreator {

    public abstract View getRefreshView(Context context, ViewGroup parent);

    public abstract void onPull(int currentDragHeight, int refreshViewHeight, int currentRefreshStatus);

    public abstract void onRefreshing();

    public abstract void onStopRefresh();
}
