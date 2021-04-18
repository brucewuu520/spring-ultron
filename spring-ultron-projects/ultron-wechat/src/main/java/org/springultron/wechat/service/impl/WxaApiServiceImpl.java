package org.springultron.wechat.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springultron.core.exception.ApiException;
import org.springultron.core.jackson.Jackson;
import org.springultron.core.result.ApiResult;
import org.springultron.core.utils.StringUtils;
import org.springultron.http.HttpRequest;
import org.springultron.redis.RedisClient;
import org.springultron.wechat.constant.WxaConstants;
import org.springultron.wechat.dto.WxaUserSession;
import org.springultron.wechat.props.WechatProperties;
import org.springultron.wechat.props.WxaConf;
import org.springultron.wechat.service.WxaApiService;

import java.time.Duration;

/**
 * 小程序常用接口服务
 *
 * @author brucewuu
 * @date 2021/4/14 下午3:25
 */
public class WxaApiServiceImpl implements WxaApiService, WxaConstants {
    private static final Logger log = LoggerFactory.getLogger(WxaApiServiceImpl.class);

    private final WechatProperties properties;
    private final RedisClient redisClient;

    public WxaApiServiceImpl(WechatProperties properties, RedisClient redisClient) {
        this.properties = properties;
        this.redisClient = redisClient;
    }

    /**
     * 检查小程序配置信息
     */
    private WxaConf checkWxaConfig() {
        WxaConf wxaConf = properties.getWxaConf();
        if (wxaConf == null) {
            ApiResult.throwFail("小程序配置：wxaConf 不能为空");
        }
        if (StringUtils.isEmpty(wxaConf.getAppId())) {
            ApiResult.throwFail("小程序配置：wxaConf-appId 不能为空");
        }
        if (StringUtils.isEmpty(wxaConf.getAppSecret())) {
            ApiResult.throwFail("小程序配置：wxaConf-appSecret 不能为空");
        }
        return wxaConf;
    }

    /**
     * 获取小程序 access_token
     *
     * @return 小程序 access_token
     */
    @Override
    public String getAccessToken() {
        WxaConf wxaConf = this.checkWxaConfig();
        String access_token = redisClient.getString(WXA_TOKEN_CACHE_KEY);
        if (StringUtils.isNotEmpty(access_token)) {
            return access_token;
        }
        JsonNode result = HttpRequest.get(ACCESS_TOKEN)
                .query("appid", wxaConf.getAppId())
                .query("secret", wxaConf.getAppSecret())
                .query("grant_type", "client_credential")
                .execute()
                .asJsonNode();
        if (result.has("access_token")) {
            access_token = Jackson.getString(result, "access_token");
            int expires_in = Jackson.getIntValue(result, "expires_in", 0) - 3;
            if (expires_in > 0) {
                redisClient.setString(WXA_TOKEN_CACHE_KEY, access_token, Duration.ofSeconds(expires_in));
            }
            return access_token;
        } else {
            log.error("获取access_token异常: {}", result.toString());
            String errmsg = Jackson.getString(result, "errmsg");
            throw new ApiException("获取access_token异常:" + errmsg);
        }
    }

    /**
     * 小程序登录凭证校验
     *
     * @param code 登录时获取的 code
     * @return {@link WxaUserSession}
     */
    @Override
    public WxaUserSession code2Session(String code) {
        WxaConf wxaConf = this.checkWxaConfig();
        WxaUserSession wxaUserSession = HttpRequest.get(CODE2SESSION)
                .query("appid", wxaConf.getAppId())
                .query("secret", wxaConf.getAppSecret())
                .query("js_code", code)
                .query("grant_type", "authorization_code")
                .execute()
                .asObject(WxaUserSession.class);
        if (wxaUserSession.getErrCode() == 0) {
            return wxaUserSession;
        } else {
            log.error("小程序登录凭证校验失败: --code: {}, --异常信息: {}", code, Jackson.toJson(wxaUserSession));
            throw new ApiException("小程序登录凭证校验失败:"  +wxaUserSession.getErrMsg());
        }
    }

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
    @Override
    public String getPaidUnionId(String openid, String transactionId, String mchId, String outTradeNo) {
        JsonNode result = HttpRequest.get(GET_PAID_UNION_ID)
                .query("access_token", getAccessToken())
                .query("openid", openid)
                .query("transaction_id", transactionId)
                .query("mch_id", mchId)
                .query("out_trade_no", outTradeNo)
                .execute()
                .asJsonNode();
        if (result.hasNonNull("unionid")) {
            return Jackson.getString(result, "unionid");
        } else {
            log.error("用户支付完成后，获取该用户的 UnionId异常: {}", result.toString());
            String errmsg = Jackson.getString(result, "errmsg");
            throw new ApiException("用户支付完成后，获取该用户的 UnionId异常:" + errmsg);
        }
    }
}
