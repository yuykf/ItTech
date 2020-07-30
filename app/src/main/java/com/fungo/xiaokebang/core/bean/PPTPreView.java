package com.fungo.xiaokebang.core.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/7/3.
 */

public class PPTPreView implements Serializable {

    public String itemType; //pre item类型标志
    public boolean isSelected; //选中标志
    public List<PPTContent> mContentList; //每个ppt 数据集合

    public PPTPreView(String itemType) {
        mContentList = new ArrayList<>();
        this.itemType = itemType;
    }


    protected PPTPreView(Parcel in) {
        itemType = in.readString();
        isSelected = in.readByte() != 0;
    }

//    public static final Creator<PPTPreView> CREATOR = new Creator<PPTPreView>() {
//        @Override
//        public PPTPreView createFromParcel(Parcel in) {
//            return new PPTPreView(in);
//        }
//
//        @Override
//        public PPTPreView[] newArray(int size) {
//            return new PPTPreView[size];
//        }
//    };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(itemType);
//        dest.writeByte((byte) (isSelected ? 1 : 0));
//    }
}
