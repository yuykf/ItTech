package com.fungo.xiaokebang.core.prfs;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/6/20.
 */
public interface PreferenceHelper {


    /**
     * Set login account
     *
     * @param account Account
     */
    void setLoginAccount(String account);

    /**
     * Set login password
     *
     * @param password Password
     */
    void setLoginPassword(String password);

    /**
     * Get login account
     *
     * @return account
     */
    String getLoginAccount();

    /**
     * Get login password
     *
     * @return password
     */
    String getLoginPassword();

    /**
     * Set login status
     *
     * @param isLogin IsLogin
     */
    void setLoginStatus(boolean isLogin);

    /**
     *
     * Get login status
     *
     * @return login status
     */
    boolean getLoginStatus();

    /**
     * Set cookie
     *
     * @param domain Domain
     * @param cookie Cookie
     */
    void setCookie(String domain, String cookie);

    /**
     * Get cookie
     *
     * @param domain Domain
     * @return cookie
     */
    String getCookie(String domain);




    /**
     * Get auto cache state
     *
     * @return if auto cache state
     */
    boolean getAutoCacheState();


    /**
     *
     * @param deviceToken
     */
    void setDeviceToken(String deviceToken);


    String getDeviceToken();

    /**
     * Set auto cache state
     *
     * @param b current auto cache state
     */
    void setAutoCacheState(boolean b);

}
