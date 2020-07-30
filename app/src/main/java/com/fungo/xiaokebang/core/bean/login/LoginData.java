package com.fungo.xiaokebang.core.bean.login;

import java.util.List;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/7/27.
 */
public class LoginData {

    //{"uid":740279,"nickname":"17607001623"}
    private String nickname;

    private int uid;


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "LoginData{" +
                "nickname='" + nickname + '\'' +
                ", uid=" + uid +
                '}';
    }
}
