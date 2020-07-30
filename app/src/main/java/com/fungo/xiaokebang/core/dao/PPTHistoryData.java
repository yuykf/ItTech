package com.fungo.xiaokebang.core.dao;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/7/8.
 */
@Entity
public class PPTHistoryData {

    @Id(autoincrement = true)
    private Long id;

    //保存的
    private String data;

    public long date;  //时间

    public String title; //标题

    @Generated(hash = 1793010526)
    public PPTHistoryData(Long id, String data, long date, String title) {
        this.id = id;
        this.data = data;
        this.date = date;
        this.title = title;
    }

    @Generated(hash = 1164550330)
    public PPTHistoryData() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getDate() {
        return this.date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



}
