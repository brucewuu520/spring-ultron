package org.springultron.wechat.service;

import org.springultron.wechat.dto.WxaUserSession;

/**
 * 小程序常用接口服务
 *
 * @author brucewuu
 * @date 2021/4/14 下午3:25
 */
public interface WxaApiService {
    /**
     * 获取小程序 access_token
     *
     * @return 小程序 access_token
     */
    String getAccessToken();

    /**
     * 小程序登录凭证校验
     *
     * @param code 登录时获取的 code
     * @return {@link WxaUserSession}
     */
    WxaUserSession code2Session(String code);

    /**
     * 用户支付完成后，获取该用户的 UnionId，无需用户授权
     * 本接口支持第三方平台代理查询
     *
     * @param openid        支付用户唯一标识（必须）
     * @param transactionId 微信支付订单号（非必须）
     * @param mchId         微信支付分配的商户号，和商户订单号配合使用（非必须）
     * @param outTradeNo    微信支付商户订单号，和商户号配合使用（非必须）
     * @return unionId 用户唯一标识
     */
    String getPaidUnionId(String openid, String transactionId, String mchId, String outTradeNo);
}
