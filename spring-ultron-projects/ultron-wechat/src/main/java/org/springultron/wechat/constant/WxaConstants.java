package org.springultron.wechat.constant;

/**
 * 小程序常量
 *
 * @author brucewuu
 * @date 2021/4/14 下午3:26
 */
public interface WxaConstants {
    /**
     * host
     */
    String HOST = "https://api.weixin.qq.com";

    /**
     * 获取 access_token
     */
    String ACCESS_TOKEN = HOST + "/cgi-bin/token";

    /**
     * 小程序 access_token 缓存key
     */
    String WXA_TOKEN_CACHE_KEY = "WXA:ACCESS:TOKEN";

    /**
     * 小程序授权登录
     */
    String CODE2SESSION = HOST + "/sns/jscode2session";

    /**
     * 用户支付完成后，获取该用户的 UnionId，无需用户授权
     */
    String GET_PAID_UNION_ID = HOST + "/wxa/getpaidunionid";
}
