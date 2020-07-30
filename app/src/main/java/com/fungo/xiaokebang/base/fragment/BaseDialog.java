package com.fungo.xiaokebang.base.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


import androidx.annotation.LayoutRes;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.fungo.xiaokebang.utils.ViewHolder;

import static java.security.AccessController.getContext;

/**
 * 说明：
 * Created by jjs on 2019/9/13.
 */
public class BaseDialog extends DialogFragment {

    protected int mLayoutId;
    private OnCustomListener mCustomListener;
    public ViewHolder mViewHolder;
    private boolean isBottom;
    private boolean windowTransparent;
    private boolean dialogTransparent = true;
    private DialogInterface.OnDismissListener dismissListener;

    /**
     * 设置布局id
     */
    public BaseDialog setLayoutId(@LayoutRes int layoutId) {
        mLayoutId = layoutId;
        return this;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mRootView = inflater.inflate(mLayoutId, container);
        mViewHolder = new ViewHolder(getContext(), mRootView);
        updateView();
        if (mCustomListener != null) {
            mCustomListener.onCustom(mViewHolder, this);
        }
        return mRootView;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (dismissListener != null) {
            dismissListener.onDismiss(dialog);
        }
    }

    public void setDismissListener(DialogInterface.OnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    //继承方式使用时，在构造方法中初始化数据，initView中解析绑定数据
    //展示后需要更新UI时，需判断viewholder是否为空，因为调用show()到onCreateView需要一段时间
    protected void initView() {
    }

    //做一次非空判断，从走initview
    public void updateView() {
        if (mViewHolder != null) {
            initView();
        }
    }

    //会在初始化时调用一次，可通过viewHolder拿到所有控件
    public BaseDialog setCustomListener(OnCustomListener customListener) {
        this.mCustomListener = customListener;
        return this;
    }

    public interface OnCustomListener {
        void onCustom(ViewHolder holder, BaseDialog dialog);
    }

    public void show(FragmentActivity activity) {
        try {
            //在每个add事务前增加一个remove事务，防止连续的add
            activity.getSupportFragmentManager().beginTransaction().remove(this).commit();
        } catch (Exception e) {
            //同一实例使用不同的tag会异常,这里捕获一下
            e.printStackTrace();
        }
        super.show(activity.getSupportFragmentManager(), "");
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            int divierId = getDialog().getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
            View divider = getDialog().findViewById(divierId);
            if (divider != null) {
                divider.setBackgroundColor(Color.TRANSPARENT);
            }
        } catch (Exception e) {
        }
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        if (isBottom) {
            params.gravity = Gravity.BOTTOM;
            window.setAttributes(params);
        }
        if (windowTransparent) {
            params.dimAmount = 0.0f;
            window.setAttributes(params);
        }
        if (dialogTransparent) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

    }

    public BaseDialog hasTransparentForWindow(boolean windowTransparent) {
        this.windowTransparent = windowTransparent;
        return this;
    }

    public BaseDialog hasTransparentForDialog(boolean dialogTransparent) {
        this.dialogTransparent = dialogTransparent;
        return this;
    }

    public void hasBottomUP(boolean isBottom) {
        this.isBottom = isBottom;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


}
