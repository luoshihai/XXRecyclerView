package com.lsh.XXRecyclerview;

import android.graphics.Bitmap;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Author:lsh
 * Version: 1.0
 * Description:
 * Date: 2017/2/24
 */

public class CommonViewHolder extends RecyclerView.ViewHolder {

    private View mItemView;
    private SparseArray<View> mViews;
    Object associatedObject;
    public CommonViewHolder(View itemView) {
        super(itemView);
        this.mItemView = itemView;
        mViews = new SparseArray<>();
    }

    public <T extends View> T getView(@IdRes int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }


    public View getItemView() {
        return mItemView;
    }

    public CommonViewHolder setText(int viewId, String content) {
        TextView textView = getView(viewId);
        textView.setText(content);
        return this;
    }

    public CommonViewHolder setImageResource(int viewId, int resourceId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resourceId);
        return this;
    }

    public CommonViewHolder setBackgroundResource(int viewId, int resourceId) {
        View view = getView(viewId);
        view.setBackgroundResource(resourceId);
        return this;
    }


    /**
     * 给按钮设能否被点击
     * @param viewId
     * @param isenable
     * @return
     */

    public CommonViewHolder setEnable(int viewId, boolean isenable) {
        View view = getView(viewId);
        view.setEnabled(isenable);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param bm
     * @return
     */
    public CommonViewHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }
    /**
     * 给textview设置背景图片
     *
     * @param viewId
     * @param drawableRes
     * @return
     */
    public CommonViewHolder setTvBackGround(int viewId, int drawableRes) {
        TextView view = getView(viewId);
        view.setBackgroundResource(drawableRes);
        return this;
    }

    /**
     *
     * @param viewId 资源文件id
     * @param onClickListener 点击监听回调
     * @return
     */

    public CommonViewHolder setOnClickListener(int viewId,View.OnClickListener onClickListener) {
        View view = getView(viewId);
        view.setOnClickListener(onClickListener);
        return this;
    }

    /**
     *
     * @param viewId 资源文件id
     * @param onLongClickListener 长按回调
     * @return
     */
    public CommonViewHolder setOnLongClickListener(int viewId,View.OnLongClickListener onLongClickListener) {
        View view = getView(viewId);
        view.setOnLongClickListener(onLongClickListener);
        return this;
    }


    //用来装该view最后一次显示的数据
    public Object getAssociatedObject() {
        return associatedObject;
    }

    /**
     * Should be called during convert
     */
    public void setAssociatedObject(Object associatedObject) {
        this.associatedObject = associatedObject;
    }
}
