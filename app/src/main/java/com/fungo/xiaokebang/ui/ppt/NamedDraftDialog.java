package com.fungo.xiaokebang.ui.ppt;

import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.drawerlayout.widget.DrawerLayout;

import com.aliyun.common.utils.ToastUtil;
import com.fungo.xiaokebang.R;
import com.fungo.xiaokebang.base.fragment.BaseDialog;
import com.fungo.xiaokebang.utils.TokenHelper;

//import com.fungo.xiaokebang.R;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/7/22.
 */
public class NamedDraftDialog extends BaseDialog {

    {
        setLayoutId(R.layout.dialog_caogao_named);
    }


    private String draftStr;

    @Override
    protected void initView() {
        super.initView();

        mViewHolder.getView(R.id.tv_sure).setOnClickListener( v -> {

            if (draftStrCallBack != null) {
                if (!TextUtils.isEmpty(draftStr)) {
                    draftStrCallBack.callbackDraft(draftStr);
                    dismiss();
                }else {
                    ToastUtil.showToast(getContext(),"请输入文本");
                }
            }
        });


        ((EditText)mViewHolder.getView(R.id.et_name)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                draftStr = s.toString();
                Log.e("draftStr: ", draftStr);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        ((EditText) mViewHolder.getView(R.id.et_name)).setText("");
        ((EditText) mViewHolder.getView(R.id.et_name)).setHint("请输入");
    }


    public NamedDraftDialog(DraftStrCallBack draftStrCallBack) {
        this.draftStrCallBack = draftStrCallBack;
    }

    private DraftStrCallBack draftStrCallBack;

    public interface DraftStrCallBack{
        void callbackDraft(String draftStr);
    }

}
