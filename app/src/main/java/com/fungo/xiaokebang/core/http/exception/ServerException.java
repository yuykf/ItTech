package com.fungo.xiaokebang.core.http.exception;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/6/24.
 */
public class ServerException extends Exception {

    private int code;

    public ServerException(String message) {
        super(message);
    }

    public ServerException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
