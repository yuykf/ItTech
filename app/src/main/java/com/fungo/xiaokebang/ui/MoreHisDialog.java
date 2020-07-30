package com.fungo.xiaokebang.ui;

import com.fungo.xiaokebang.base.fragment.BaseDialog;

import com.fungo.xiaokebang.R;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/7/23.
 */
public class MoreHisDialog extends BaseDialog {

    {
        setLayoutId(R.layout.dialog_more_his);
        hasBottomUP(true);
    }

    private MoreCallBack moreCallBack;

    public MoreHisDialog(MoreCallBack moreCallBack) {
        this.moreCallBack = moreCallBack;
    }

    @Override
    protected void initView() {
        super.initView();

        mViewHolder.getView(R.id.tv_rename).setOnClickListener(v -> {
            if (null != moreCallBack) {
                moreCallBack.rename();
                dismiss();
            }
        });

        mViewHolder.getView(R.id.tv_delete).setOnClickListener(v -> {
            moreCallBack.delete();
            dismiss();
        });

    }

    public interface MoreCallBack{
        void rename();
        void delete();
    }

}
