package com.fungo.xiaokebang.ui.ppt;

import android.service.autofill.SaveCallback;
import android.view.View;

import com.fungo.xiaokebang.base.fragment.BaseDialog;

import com.fungo.xiaokebang.R;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/7/22.
 */
public class SavePPTDialog extends BaseDialog {


    {
        setLayoutId(R.layout.dialog_save_ppt);
    }


    public SavePPTDialog(PPTSaveBack pptSaveBack) {
        this.saveCallback = pptSaveBack;
    }

    @Override
    protected void initView() {
        super.initView();

        //不保存
        mViewHolder.getView(R.id.tv_dismiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //消失
                dismiss();
            }
        });

        //保存
        mViewHolder.getView(R.id.tv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != saveCallback) {
                    saveCallback.savePPTDatas();
                }
            }
        });
    }

    private PPTSaveBack saveCallback;

    public interface PPTSaveBack{
        void savePPTDatas();
    }

}
