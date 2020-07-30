package com.fungo.xiaokebang.ui;

import android.content.DialogInterface;

import com.fungo.xiaokebang.R;
import com.fungo.xiaokebang.base.fragment.BaseDialog;

import java.util.ArrayList;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/7/27.
 */
public class DeleteDialog extends BaseDialog {


    private ArrayList<String> titles;

    public DeleteDialog(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    {
        setLayoutId(R.layout.dialog_delete);
        hasBottomUP(true);
    }



    @Override
    protected void initView() {
        super.initView();

        mViewHolder.getView(R.id.view_delete).setOnClickListener(v -> {
            if (clickListener != null) {
//                clickListener.deleteItem(title);
                clickListener.deleteItem(titles); // 根据title 删除记录
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        titles.clear();
    }

    public void setTitles(){


    }
    private ClickListener clickListener;

    public interface ClickListener{
        void deleteItem(ArrayList titles);

    }
}
