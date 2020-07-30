package com.fungo.xiaokebang.utils;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/7/27.
 */
public class TokenHelper   {

    private HashMap<String, Object> hashMap = new HashMap<>();
    private static final TokenHelper ourInstance = new TokenHelper();

    public static TokenHelper getInstance() {
        return ourInstance;
    }

    private TokenHelper() {
    }


    public RequestBody createRequestBody(HashMap<String, Object> map) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (map != null) {
            hashMap.putAll(map);
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), new JSONObject(hashMap).toString());
        return body;
    }



}
