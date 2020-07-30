package com.fungo.xiaokebang.core.event;

import android.net.Uri;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/7/16.
 */
public class CaiqieIvEvent {
    //裁切图片路径返回事件
    private String from;
    Uri uri;

    public CaiqieIvEvent(String from, Uri uri) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
