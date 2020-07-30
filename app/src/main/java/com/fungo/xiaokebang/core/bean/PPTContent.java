package com.fungo.xiaokebang.core.bean;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Class:
 * Other: 页面bean
 * Create by yuy on  2020/7/6.
 */
public class PPTContent  implements Serializable {

    public String contentType; //内容类型  视频，图片，题目 (题目的话需要细分到题目类型 题目样式)

    public String contentStr; //内容地址 视频，图片  题目的话注入 题目title iv answer ...

    public int xuanzeNums; //选择题答案个数

    public String realAnswer; //题目答案

    public String titleIvPath; //题目图像路径

//    public String selfContent; //拍摄的内容链接
    public boolean isLuzhiing; //标志是否正在拍摄

    public boolean isClicked; //
    public String videoUrl;  //ppt 对应的拍摄视频地址

    //草稿标志
    public boolean isDeleteChoose; //

    public String[] selectUris = new String[4];
    public String[] selectStrings = new String[5]; //abcd title

    public PPTContent(String contentType, String contentStr) {
        this.contentType = contentType;
        this.contentStr = contentStr;
    }


    protected PPTContent(Parcel in) {
        contentType = in.readString();
        contentStr = in.readString();
        xuanzeNums = in.readInt();
        realAnswer = in.readString();
        titleIvPath = in.readString();
        isLuzhiing = in.readByte() != 0;
        isClicked = in.readByte() != 0;
        videoUrl = in.readString();
        isDeleteChoose = in.readByte() != 0;
        selectUris = in.createStringArray();
        selectStrings = in.createStringArray();
    }
//
//    public static final Creator<PPTContent> CREATOR = new Creator<PPTContent>() {
//        @Override
//        public PPTContent createFromParcel(Parcel in) {
//            return new PPTContent(in);
//        }
//
//        @Override
//        public PPTContent[] newArray(int size) {
//            return new PPTContent[size];
//        }
//    };

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentStr() {
        return contentStr;
    }

    public void setContentStr(String contentStr) {
        this.contentStr = contentStr;
    }

    @Override
    public String toString() {
        return "PPTContent{" +
                "contentType='" + contentType + '\'' +
                ", contentStr='" + contentStr + '\'' +
                ", selfContent='" + videoUrl + '\'' +
                '}';
    }


//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(contentType);
//        dest.writeString(contentStr);
//        dest.writeInt(xuanzeNums);
//        dest.writeString(realAnswer);
//        dest.writeString(titleIvPath);
//        dest.writeByte((byte) (isLuzhiing ? 1 : 0));
//        dest.writeByte((byte) (isClicked ? 1 : 0));
//        dest.writeString(videoUrl);
//        dest.writeByte((byte) (isDeleteChoose ? 1 : 0));
//        dest.writeStringArray(selectUris);
//        dest.writeStringArray(selectStrings);
//    }
}
