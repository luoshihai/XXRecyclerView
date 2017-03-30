package com.lsh.XXListView;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by Administrator on 2016/11/19.
 * 公共的
 */
public class ViewHolder {

    private final SparseArray<View> mViews;


    public int mPosition;
    private View mConvertView;
    private static Context mContext;
    //用来装数据是否改变的量
    Object associatedObject;

    private ViewHolder(Context context, ViewGroup parent, int layoutId,
                       int position) {
        this.mPosition = position;
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        // setTag
        mConvertView.setTag(this);
    }

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * 拿到一个ViewHolder对象
     *
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static ViewHolder get(Context context, View convertView,
                                 ViewGroup parent, int layoutId, int position) {
        mContext = context;
        if (convertView == null) {
            return new ViewHolder(mContext, parent, layoutId, position);
        }
        ViewHolder tag = (ViewHolder) convertView.getTag();
        tag.mPosition = position;
        return tag;
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public <T extends View> T getViewBytag(String viewTag) {
        View viewWithTag = mConvertView.findViewWithTag(viewTag);
        int viewId = viewWithTag.getId();
        View view = mViews.get(viewId);
        if (view == null) {
            view = viewWithTag;
//            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public ViewHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);
        return this;
    }

    /**
     * 给CheckBox设置状态
     *
     * @param viewId
     * @param ischecked
     * @return
     */

    public ViewHolder setChecked(int viewId, boolean ischecked) {
        CheckBox view = getView(viewId);
        view.setChecked(ischecked);
        return this;
    }

    /**
     * 给按钮设能否被点击
     * @param viewId
     * @param isenable
     * @return
     */

    public ViewHolder setEnable(int viewId, boolean isenable) {
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
    public ViewHolder setImageBitmap(int viewId, Bitmap bm) {
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
    public ViewHolder setTvBackGround(int viewId, int drawableRes) {
        TextView view = getView(viewId);
        view.setBackgroundResource(drawableRes);
        return this;
    }




    public int getPosition() {
        return mPosition;
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