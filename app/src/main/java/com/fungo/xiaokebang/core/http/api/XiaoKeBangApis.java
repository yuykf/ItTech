package com.fungo.xiaokebang.core.http.api;


import com.fungo.xiaokebang.core.bean.BaseResponse;
import com.fungo.xiaokebang.core.bean.login.AuthCodeData;
import com.fungo.xiaokebang.core.bean.login.LoginData;


import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/6/24.
 */
public interface XiaoKeBangApis {

    //
    String HOST = "http://47.98.52.228:10000/";

    /**
     * @return
     */
    @POST("/login")
    Observable<BaseResponse<LoginData>> getRegisterData(@Body RequestBody body);


//(String phoneNumber, String app, String udid, String ip, String osType, String deviceToken, int captcha, String signature)
    @GET("/captcha/{phoneNumber}")
    Observable<BaseResponse<AuthCodeData>> getAuthCode(@Path("phoneNumber") String phoneNumber,
                                                       @Query("app") String app,
                                                       @Query("ip") String ip,
                                                       @Query("ts") int ts,
                                                       @Query("signature") String signature);


    // @GET("users/{username}")
    //    Observable<User> getUser(@Path("username") String username);


}
