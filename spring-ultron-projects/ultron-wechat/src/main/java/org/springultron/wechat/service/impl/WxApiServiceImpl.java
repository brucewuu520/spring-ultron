package org.springultron.wechat.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springultron.core.exception.ApiException;
import org.springultron.core.jackson.Jackson;
import org.springultron.core.result.ApiResult;
import org.springultron.core.utils.DigestUtils;
import org.springultron.core.utils.Lists;
import org.springultron.core.utils.RandomUtils;
import org.springultron.core.utils.StringUtils;
import org.springultron.http.HttpRequest;
import org.springultron.http.HttpResponse;
import org.springultron.redis.RedisClient;
import org.springultron.wechat.DownloadUtils;
import org.springultron.wechat.constant.WxConstants;
import org.springultron.wechat.dto.*;
import org.springultron.wechat.enums.OCR_TYPE;
import org.springultron.wechat.params.WxQrCodeParams;
import org.springultron.wechat.props.WechatProperties;
import org.springultron.wechat.props.WxConf;
import org.springultron.wechat.service.WxApiService;

import java.io.File;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 公众号常用接口服务
 *
 * @author brucewuu
 * @date 2021/4/12 上午10:23
 */
public class WxApiServiceImpl implements WxApiService, WxConstants {
    private static final Logger log = LoggerFactory.getLogger(WxApiServiceImpl.class);

    private final WechatProperties properties;
    private final RedisClient redisClient;

    public WxApiServiceImpl(WechatProperties properties, RedisClient redisClient) {
        this.properties = properties;
        this.redisClient = redisClient;
    }

    /**
     * 检查公众号配置信息
     */
    private WxConf checkWxConfig() {
        WxConf wxConf = properties.getWxConf();
        if (wxConf == null) {
            ApiResult.throwFail("公众号配置：wxConf 不能为空");
        }
        if (StringUtils.isEmpty(wxConf.getAppId())) {
            ApiResult.throwFail("公众号配置：wxConf-appId 不能为空");
        }
        if (StringUtils.isEmpty(wxConf.getAppSecret())) {
            ApiResult.throwFail("公众号配置：wxConf-appSecret 不能为空");
        }
        return wxConf;
    }

    /**
     * 获取公众号access_token
     *
     * @return 公众号 access_token
     */
    @Override
    public String getAccessToken() {
        WxConf wxConf = this.checkWxConfig();
        String access_token = redisClient.getString(WX_TOKEN_CACHE_KEY);
        if (StringUtils.isNotEmpty(access_token)) {
            return access_token;
        }
        JsonNode result = HttpRequest.get(ACCESS_TOKEN)
                .query("appid", wxConf.getAppId())
                .query("secret", wxConf.getAppSecret())
                .query("grant_type", "client_credential")
                .execute()
                .asJsonNode();
        log.info("获取公众号access_token结果: {}", result.toString());
        if (result.hasNonNull("access_token")) {
            access_token = Jackson.getString(result, "access_token");
            int expires_in = Jackson.getIntValue(result, "expires_in", 0) - 3;
            if (expires_in > 0) {
                redisClient.setString(WX_TOKEN_CACHE_KEY, access_token, Duration.ofSeconds(expires_in));
            }
            return access_token;
        } else {
            log.error("获取公众号access_token异常: {}", result.toString());
            int errcode = Jackson.getIntValue(result, "errcode", -1);
            String errmsg = Jackson.getString(result, "errmsg");
            throw new ApiException(errcode, errmsg);
        }
    }

    /**
     * 获取微信API接口IP地址
     *
     * @return ip_list
     */
    @Override
    public List<String> getApiDomainIpList() {
        String access_token = getAccessToken();
        JsonNode result = HttpRequest.get(API_DOMAIN_IP)
                .query("access_token", access_token)
                .execute()
                .asJsonNode();
        if (result.hasNonNull("ip_list")) {
            return Jackson.parseList(result.get("ip_list").toString(), String.class);
        } else {
            log.error("获取微信API接口IP地址异常: {}", result.toString());
            int errcode = Jackson.getIntValue(result, "errcode", -1);
            String errmsg = Jackson.getString(result, "errmsg");
            throw new ApiException(errcode, errmsg);
        }
    }

    /**
     * 获取微信callback IP地址
     *
     * @return ip_list
     */
    @Override
    public List<String> getCallbackIpList() {
        String access_token = getAccessToken();
        JsonNode result = HttpRequest.get(CALLBACK_IP)
                .query("access_token", access_token)
                .execute()
                .asJsonNode();
        if (result.hasNonNull("ip_list")) {
            return Jackson.parseList(result.get("ip_list").toString(), String.class);
        } else {
            log.error("获取微信callback IP地址异常: {}", result.toString());
            int errcode = Jackson.getIntValue(result, "errcode", -1);
            String errmsg = Jackson.getString(result, "errmsg");
            throw new ApiException(errcode, errmsg);
        }
    }

    /**
     * 获取帐号的关注者列表
     * 关注者列表由一串openId（加密后的微信号，每个用户对每个公众号的openId是唯一的）组成。
     * 一次拉取调用最多拉取10000个关注者的openId，可以通过多次拉取的方式来满足需求。
     *
     * @param nextOpenId 第一个拉取的openId，不填默认从头开始拉取
     * @return {@link WxUserList}
     */
    @Override
    public WxUserList getUserList(String nextOpenId) {
        String access_token = getAccessToken();
        JsonNode result = HttpRequest.get(USER_LIST)
                .query("access_token", access_token)
                .query("next_openid", nextOpenId)
                .execute()
                .asJsonNode();
        if (result.hasNonNull("data")) {
            return Jackson.parse(result.toString(), WxUserList.class);
        } else {
            log.error("获取帐号的关注者列表异常: --next_openid: {},  {}", nextOpenId, result.toString());
            int errcode = Jackson.getIntValue(result, "errcode", -1);
            String errmsg = Jackson.getString(result, "errmsg");
            throw new ApiException(errcode, errmsg);
        }
    }

    /**
     * 获取用户基本信息（包括UnionID机制）
     *
     * @param openId 用户 openId
     * @param lang   返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
     * @return {@link WxUserInfo}
     */
    @Override
    public WxUserInfo getUserInfo(@NonNull String openId, @Nullable String lang) {
        String access_token = getAccessToken();
        JsonNode result = HttpRequest.get(USER_INFO)
                .query("access_token", access_token)
                .query("openid", openId)
                .query("lang", lang)
                .execute()
                .asJsonNode();
        if (result.hasNonNull("openid")) {
            return Jackson.parse(result.toString(), WxUserInfo.class);
        } else {
            log.error("获取用户基本信息异常, --openId: {}, --异常信息: {}", openId, result.toString());
            int errcode = Jackson.getIntValue(result, "errcode", -1);
            String errmsg = Jackson.getString(result, "errmsg");
            throw new ApiException(errcode, errmsg);
        }
    }

    /**
     * 批量获取用户基本信息（最多支持一次拉取100条）
     *
     * @param userListJson {
     *                     "user_list": [
     *                     {
     *                     "openId": "otvxTs4dckWG7imySrJd6jSi0CWE",
     *                     "lang": "zh_CN"
     *                     },
     *                     {
     *                     "openId": "otvxTs_JZ6SEiP0imdhpi50fuSZg",
     *                     "lang": "zh_CN"
     *                     }
     *                     ]
     *                     }
     * @return {@link List<WxUserInfo>}
     */
    @Override
    public List<WxUserInfo> batchUserInfo(@NonNull String userListJson) {
        String access_token = getAccessToken();
        JsonNode result = HttpRequest.post(BATCH_USER_INFO)
                .query("access_token", access_token)
                .bodyValue(userListJson)
                .execute()
                .asJsonNode();
        if (result.hasNonNull("user_info_list")) {
            return Jackson.parseList(result.get("user_info_list").toString(), WxUserInfo.class);
        } else {
            log.error("批量获取用户基本信息异常, --user_list: {}, --异常信息: {}", userListJson, result.toString());
            int errcode = Jackson.getIntValue(result, "errcode", -1);
            String errmsg = Jackson.getString(result, "errmsg");
            throw new ApiException(errcode, errmsg);
        }
    }

    /**
     * 对指定用户设置备注名
     *
     * @param openId 用户 openId
     * @param remark 新的备注名，长度必须小于30字符
     * @return 是否设置成功
     */
    @Override
    public boolean updateUserRemark(String openId, String remark) {
        String access_token = getAccessToken();
        ObjectNode reqBody = Jackson.createObjectNode();
        reqBody.put("openid", openId);
        reqBody.put("remark", remark);
        JsonNode result = HttpRequest.post(UPDATE_USER_REMARK)
                .query("access_token", access_token)
                .bodyValue(reqBody.toString())
                .execute()
                .asJsonNode();
        int errcode = Jackson.getIntValue(reqBody, "errcode", -1);
        if (errcode == 0) {
            return true;
        } else {
            log.error("对指定用户设置备注名异常: --参数: {}, --异常信息: {}", reqBody.toString(), result.toString());
            return false;
        }
    }

    /**
     * 通过code换取网页授权access_token
     * 首先请注意，这里通过code换取的是一个特殊的网页授权access_token,与基础支持中的access_token（该access_token用于调用其他接口）不同。
     * 公众号可通过下述接口来获取网页授权access_token。如果网页授权的作用域为snsapi_base，则本步骤中获取到网页授权access_token的同时，
     * 也获取到了openId，snsapi_base式的网页授权流程即到此为止。
     *
     * @param code 用户网页授权码
     * @return {@link WxSnsTokenInfo}
     */
    @Override
    public WxSnsTokenInfo oauth2Token(String code) {
        WxConf wxConf = this.checkWxConfig();
        JsonNode result = HttpRequest.get(SNS_OAUTH2_TOKEN)
                .query("appid", wxConf.getAppId())
                .query("secret", wxConf.getAppSecret())
                .query("code", code)
                .query("grant_type", "authorization_code")
                .execute()
                .asJsonNode();
        if (result.hasNonNull("access_token")) {
            return Jackson.parse(result.toString(), WxSnsTokenInfo.class);
        } else {
            log.error("通过code换取网页授权access_token异常, --code: {}, --异常信息: {}", code, result.toString());
            int errcode = Jackson.getIntValue(result, "errcode", -1);
            String errmsg = Jackson.getString(result, "errmsg");
            throw new ApiException(errcode, errmsg);
        }
    }

    /**
     * 刷新取网页授权access_token
     *
     * @param refreshToken 通过access_token获取到的refresh_token参数
     * @return @return {@link WxSnsTokenInfo}
     */
    @Override
    public WxSnsTokenInfo refreshOauth2Token(String refreshToken) {
        WxConf wxConf = this.checkWxConfig();
        JsonNode result = HttpRequest.get(SNS_OAUTH2_REFRESH_TOKEN)
                .query("appid", wxConf.getAppId())
                .query("refresh_token", refreshToken)
                .query("grant_type", "refresh_token")
                .execute()
                .asJsonNode();
        if (result.hasNonNull("access_token")) {
            return Jackson.parse(result.toString(), WxSnsTokenInfo.class);
        } else {
            log.error("刷新取网页授权access_token异常, --refreshToken: {}, --异常信息: {}", refreshToken, result.toString());
            int errcode = Jackson.getIntValue(result, "errcode", -1);
            String errmsg = Jackson.getString(result, "errmsg");
            throw new ApiException(errcode, errmsg);
        }
    }

    /**
     * 网页授权拉取用户信息(需scope为 snsapi_userinfo)
     * 如果网页授权作用域为snsapi_userinfo，则此时开发者可以通过access_token和openId拉取用户信息了。
     *
     * @param accessToken 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
     * @param openId      用户的唯一标识
     * @param lang        返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
     * @return {@link WxUserInfo}
     */
    @Override
    public WxUserInfo snsUserInfo(String accessToken, String openId, @Nullable String lang) {
        JsonNode result = HttpRequest.get(SNS_USER_INFO)
                .query("access_token", accessToken)
                .query("openid", openId)
                .query("lang", lang)
                .execute()
                .asJsonNode();
        if (result.hasNonNull("openId")) {
            return Jackson.parse(result.toString(), WxUserInfo.class);
        } else {
            log.error("网页授权拉取用户信息异常, --accessToken: {}, --openId: {}, --异常信息: {}", accessToken, openId, result.toString());
            int errcode = Jackson.getIntValue(result, "errcode", -1);
            String errmsg = Jackson.getString(result, "errmsg");
            throw new ApiException(errcode, errmsg);
        }
    }

    /**
     * 检验网页授权凭证（access_token）是否有效
     *
     * @param accessToken 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
     * @param openId      用户的唯一标识
     * @return access_token 是否有效
     */
    @Override
    public boolean checkSnsAuth(String accessToken, String openId) {
        JsonNode result = HttpRequest.get(SNS_CHECK_AUTH)
                .query("access_token", accessToken)
                .query("openid", openId)
                .execute()
                .asJsonNode();
        int errcode = Jackson.getIntValue(result, "errcode", -1);
        if (errcode == 0) {
            return true;
        } else {
            log.error("授权凭证（access_token）失效: --accessToken: {}, --openId: {}, --异常信息: {}", accessToken, openId, result.toString());
            return false;
        }
    }

    /**
     * 获取jsapi_ticket
     * jsapi_ticket是公众号用于调用微信JS接口的临时票据；正常情况下，jsapi_ticket的有效期为7200秒，通过access_token来获取。
     * 由于获取jsapi_ticket的api调用次数非常有限，频繁刷新jsapi_ticket会导致api调用受限，影响自身业务，开发者必须在自己的服务全局缓存jsapi_ticket 。
     *
     * @return jsapi_ticket
     */
    @Override
    public String getJsapiTicket() {
        String ticket = redisClient.getString(JSAPI_TICKET_CACHE_KEY);
        if (StringUtils.isNotEmpty(ticket)) {
            return ticket;
        }
        String access_token = getAccessToken();
        JsonNode result = HttpRequest.get(JSAPI_TICKET)
                .query("access_token", access_token)
                .query("type", "jsapi")
                .execute()
                .asJsonNode();
        if (result.hasNonNull("ticket")) {
            ticket = Jackson.getString(result, "ticket");
            int expires_in = Jackson.getIntValue(result, "expires_in", 0) - 3;
            if (expires_in > 0) {
                redisClient.setString(JSAPI_TICKET_CACHE_KEY, access_token, Duration.ofSeconds(expires_in));
            }
            return ticket;
        } else {
            log.error("获取jsapi_ticket异常: {}", result.toString());
            int errcode = Jackson.getIntValue(result, "errcode", -1);
            String errmsg = Jackson.getString(result, "errmsg");
            throw new ApiException(errcode, errmsg);
        }
    }

    /**
     * JS-SDK签名
     *
     * @param url 当前网页的URL，不包含#及其后面部分
     * @return {@link WxJsSdkSign}
     */
    @Override
    public WxJsSdkSign jsSdkSign(String url) {
        final String jsapi_ticket = getJsapiTicket();
        WxJsSdkSign wxJsSdkSign = new WxJsSdkSign();
        wxJsSdkSign.setAppId(properties.getWxConf().getAppId());
        wxJsSdkSign.setNonceStr(RandomUtils.random(16));
        wxJsSdkSign.setTimestamp(String.valueOf(System.currentTimeMillis() / 1000));

        //noinspection StringBufferReplaceableByString
        StringBuilder builder = new StringBuilder()
                .append("jsapi_ticket=").append(jsapi_ticket)
                .append("&noncestr=").append(wxJsSdkSign.getNonceStr())
                .append("&timestamp=").append(wxJsSdkSign.getTimestamp())
                .append("&url=").append(url);
        String signature = DigestUtils.sha1Hex(builder.toString());
        wxJsSdkSign.setSignature(signature);
        return wxJsSdkSign;
    }

    /**
     * 获取微信卡券api_ticket
     * 卡券 api_ticket 是用于调用卡券相关接口的临时票据，有效期为 7200 秒，通过 access_token 来获取。
     * 这里要注意与 jsapi_ticket 区分开来。由于获取卡券 api_ticket 的 api 调用次数非常有限。
     * 频繁刷新卡券 api_ticket 会导致 api 调用受限，影响自身业务，开发者必须在自己的服务全局缓存卡券 api_ticket 。
     *
     * @return 微信卡券 api_ticket
     */
    @Override
    public String getCardApiTicket() {
        String ticket = redisClient.getString(CARD_API_TICKET_CACHE_KEY);
        if (StringUtils.isNotEmpty(ticket)) {
            return ticket;
        }
        String access_token = getAccessToken();
        JsonNode result = HttpRequest.get(JSAPI_TICKET)
                .query("access_token", access_token)
                .query("type", "wx_card")
                .execute()
                .asJsonNode();
        if (result.hasNonNull("ticket")) {
            ticket = Jackson.getString(result, "ticket");
            int expires_in = Jackson.getIntValue(result, "expires_in", 0) - 3;
            if (expires_in > 0) {
                redisClient.setString(CARD_API_TICKET_CACHE_KEY, access_token, Duration.ofSeconds(expires_in));
            }
            return ticket;
        } else {
            log.error("获取微信卡券api_ticket异常: {}", result.toString());
            int errcode = Jackson.getIntValue(result, "errcode", -1);
            String errmsg = Jackson.getString(result, "errmsg");
            throw new ApiException(errcode, errmsg);
        }
    }

    /**
     * 微信卡券签名 cardSign
     *
     * @return {@link WxCardSign}
     */
    @Override
    public WxCardSign cardSign(String cardId, String cardType, String locationId) {
        final String api_ticket = getCardApiTicket();

        WxCardSign wxCardSign = new WxCardSign();
        wxCardSign.setCardId(cardId);
        wxCardSign.setCardType(cardType);
        wxCardSign.setNonceStr(RandomUtils.random(16));
        wxCardSign.setTimestamp(String.valueOf(System.currentTimeMillis() / 1000));

        List<String> valueList = Lists.newArrayList(8);
        valueList.add(properties.getWxConf().getAppId());
        valueList.add(api_ticket);
        valueList.add(cardId);
        valueList.add(cardType);
        valueList.add(locationId);
        valueList.add(wxCardSign.getNonceStr());
        valueList.add(wxCardSign.getTimestamp());
        Collections.sort(valueList);

        StringBuilder builder = new StringBuilder();
        for (String value : valueList) {
            builder.append(value);
        }
        String cardSign = DigestUtils.sha1Hex(builder.toString());
        wxCardSign.setCardSign(cardSign);
        return wxCardSign;
    }

    /**
     * 创建公众号二维码
     *
     * @param params 创建公众号二维码参数 {@link WxQrCodeParams}
     * @return {@link WxQrCode}
     */
    @Override
    public WxQrCode createQrCode(WxQrCodeParams params) {
        String access_token = getAccessToken();
        ObjectNode reqBody = Jackson.createObjectNode();
        reqBody.put("action_name", params.getActionName());
        ObjectNode action_info = Jackson.createObjectNode();
        ObjectNode scene = Jackson.createObjectNode();
        if (params.getSceneId() != null) {
            scene.put("scene_id", params.getSceneId());
        } else if (StringUtils.isNotEmpty(params.getSceneStr())) {
            scene.put("scene_str", params.getSceneStr());
        } else {
            throw new ApiException("场景值ID不能为空");
        }
        action_info.set("scene", scene);
        reqBody.set("action_info", action_info);
        if (params.getExpireSeconds() != null) {
            reqBody.put("expire_seconds", params.getExpireSeconds());
        }

        JsonNode result = HttpRequest.post(CREATE_QRCODE)
                .query("access_token", access_token)
                .bodyValue(reqBody.toString())
                .execute()
                .asJsonNode();
        if (result.hasNonNull("ticket")) {
            return Jackson.parse(result.toString(), WxQrCode.class);
        } else {
            log.error("创建公众号二维码异常, --参数: {}, --错误信息: {}", Jackson.toJson(params), result.toString());
            int errcode = Jackson.getIntValue(result, "errcode", -1);
            String errmsg = Jackson.getString(result, "errmsg");
            throw new ApiException(errcode, errmsg);
        }
    }

    /**
     * 通过ticket换取二维码
     * ticket正确情况下，http 返回码是200，是一张图片，可以直接展示或者下载
     *
     * @param ticket 二维码 ticket
     * @return 二维码地址
     */
    @Override
    public String showQrcode(String ticket) {
        return "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticket;
    }

    // <================= 公众号菜单配置 start =================>

    /**
     * 查询当前菜单配置（包括默认菜单和全部个性化菜单信息）
     *
     * @return 菜单json
     */
    @Override
    public String getMenuInfo() {
        return HttpRequest.get(MENU_INFO).query("access_token", getAccessToken()).execute().asString();
    }

    /**
     * 创建自定义菜单
     *
     * @param menuJson 自定义菜单json
     * @return 是否成功
     */
    @Override
    public boolean createMenu(String menuJson) {
        JsonNode result = HttpRequest.post(CREATE_MENU)
                .query("access_token", getAccessToken())
                .bodyValue(menuJson)
                .execute()
                .asJsonNode();
        int errcode = Jackson.getIntValue(result, "errcode", -1);
        if (errcode == 0) {
            return true;
        } else {
            log.error("创建自定义菜单异常: {}", result.toString());
            return false;
        }
    }

    /**
     * 删除所有自定义菜单（包括默认菜单和全部个性化菜单）
     *
     * @return 是否成功
     */
    @Override
    public boolean deleteAllMenu() {
        JsonNode result = HttpRequest.get(DELETE_MENU).query("access_token", getAccessToken()).execute().asJsonNode();
        int errcode = Jackson.getIntValue(result, "errcode", -1);
        if (errcode == 0) {
            return true;
        } else {
            log.error("删除自定义菜单异常: {}", result.toString());
            return false;
        }
    }

    /**
     * 创建个性化菜单
     *
     * @param menuJson 个性化菜单json
     * @return 个性化菜单ID menuId
     */
    @Override
    public String createConditionalMenu(String menuJson) {
        JsonNode result = HttpRequest.post(CREATE_CONDITIONAL_MENU)
                .query("access_token", getAccessToken())
                .bodyValue(menuJson)
                .execute()
                .asJsonNode();
        String menuId = Jackson.getString(result, "menuid");
        if (StringUtils.isNotEmpty(menuId)) {
            return menuId;
        } else {
            log.error("创建个性化菜单异常: {}", result.toString());
            int errcode = Jackson.getIntValue(result, "errcode", -1);
            String errmsg = Jackson.getString(result, "errmsg");
            throw new ApiException(errcode, errmsg);
        }
    }

    /**
     * 删除个性化菜单
     *
     * @param menuId 个性化菜单ID
     * @return 是否成功
     */
    @Override
    public boolean deleteConditionalMenu(String menuId) {
        JsonNode result = HttpRequest.post(DELETE_CONDITIONAL_MENU)
                .query("access_token", getAccessToken())
                .bodyValue("{\"menuid\":" + menuId + "\"}")
                .execute()
                .asJsonNode();
        int errcode = Jackson.getIntValue(result, "errcode", -1);
        if (errcode == 0) {
            return true;
        } else {
            log.error("删除个性化菜单异常: {}", result.toString());
            return false;
        }
    }

    /**
     * 测试个性化菜单匹配结果
     *
     * @param userId 可以是粉丝的openId，也可以是粉丝的微信号
     * @return menuJson
     */
    @Override
    public String tryMatchConditionalMenu(String userId) {
        return HttpRequest.post(TRY_MATCH_CONDITIONAL_MENU)
                .query("access_token", getAccessToken())
                .bodyValue("{\"user_id\":" + userId + "\"}")
                .execute()
                .asString();
    }
    // <================= 公众号菜单配置 end =================>

    // <================= 素材管理 start =================>

    /**
     * 上传临时素材
     *
     * @param mediaType 素材格式
     * @param file      文件
     * @return media_id 媒体文件上传后，获取标识
     */
    @Override
    public String uploadMedia(MediaFile.MediaType mediaType, File file) {
        JsonNode result = HttpRequest.post(UPLOAD_MEDIA)
                .query("access_token", getAccessToken())
                .query("type", mediaType.toString())
                .multipartFormBuilder()
                .add("media", file)
                .build()
                .execute()
                .asJsonNode();
        if (result.hasNonNull("media_id")) {
            return Jackson.getString(result, "media_id");
        }
        log.error("上传临时素材异常: {}", result.toString());
        int errcode = Jackson.getIntValue(result, "errcode", -1);
        String errmsg = Jackson.getString(result, "errmsg");
        throw new ApiException(errcode, errmsg);
    }

    /**
     * 获取临时素材
     *
     * @param mediaId 媒体文件ID
     * @return {@link MediaFile}
     */
    @Override
    public MediaFile getMedia(String mediaId) {
        HttpResponse response = HttpRequest.get(GET_MEDIA)
                .query("access_token", getAccessToken())
                .query("media_id", mediaId)
                .execute()
                .response();
        return DownloadUtils.download(response);
    }

    /**
     * 获取高清语音素材
     *
     * @param mediaId 媒体文件ID，即uploadVoice接口返回的serverID
     * @return {@link MediaFile}
     */
    @Override
    public MediaFile getJsSdkMedia(String mediaId) {
        HttpResponse response = HttpRequest.get(GET_JS_SDK_MEDIA)
                .query("access_token", getAccessToken())
                .query("media_id", mediaId)
                .execute()
                .response();
        return DownloadUtils.download(response);
    }

    /**
     * 新增永久图文素材
     *
     * @param articlesJson 图文消息json
     * @return 图文消息素材的media_id
     */
    @Override
    public String addNews(String articlesJson) {
        JsonNode result = HttpRequest.post(ADD_NEWS)
                .query("access_token", getAccessToken())
                .bodyValue(articlesJson)
                .execute()
                .asJsonNode();
        if (result.hasNonNull("media_id")) {
            return Jackson.getString(result, "media_id");
        }
        log.error("新增永久图文素材异常: {}", result.toString());
        int errcode = Jackson.getIntValue(result, "errcode", -1);
        String errmsg = Jackson.getString(result, "errmsg");
        throw new ApiException(errcode, errmsg);
    }

    /**
     * 修改永久图文素材
     *
     * @param articlesJson 图文消息json
     * @return 是否修改成功
     */
    @Override
    public boolean updateNews(String articlesJson) {
        JsonNode result = HttpRequest.post(UPDATE_NEWS)
                .query("access_token", getAccessToken())
                .bodyValue(articlesJson)
                .execute()
                .asJsonNode();
        int errcode = Jackson.getIntValue(result, "errcode", -1);
        if (errcode == 0) {
            return true;
        } else {
            log.error("修改永久图文素材异常: {}", result.toString());
            return false;
        }
    }

    /**
     * 新增永久视频素材
     *
     * @param title        视频素材的标题
     * @param introduction 视频素材的描述
     * @param file         视频文件
     * @return {"media_id":MEDIA_ID,"url":URL}
     */
    @Override
    public String addVideoMaterial(String title, String introduction, File file) {
        ObjectNode formData = Jackson.createObjectNode();
        formData.put("title", title);
        formData.put("introduction", introduction);

        return HttpRequest.post(ADD_MATERIAL)
                .query("access_token", getAccessToken())
                .query("type", "video")
                .multipartFormBuilder()
                .add("description", formData.toString())
                .add("media", file)
                .build()
                .execute()
                .asString();
    }

    /**
     * 新增其他类型永久素材
     *
     * @param mediaType 文件类型
     * @param file      素材文件
     * @return {"media_id":MEDIA_ID,"url":URL}
     */
    @Override
    public String addMaterial(MediaFile.MediaType mediaType, File file) {
        return HttpRequest.post(ADD_MATERIAL)
                .query("access_token", getAccessToken())
                .query("type", mediaType.toString())
                .multipartFormBuilder()
                .add("media", file)
                .build()
                .execute()
                .asString();
    }

    /**
     * 获取永久素材
     *
     * @param mediaId 要获取的素材的media_id
     * @return 素材内容json
     */
    @Override
    public String getMaterial(String mediaId) {
        return HttpRequest.post(GET_MATERIAL)
                .query("access_token", getAccessToken())
                .bodyValue("{\"media_id\":" + mediaId + "\"}")
                .execute()
                .asString();
    }

    /**
     * 删除永久素材
     *
     * @param mediaId 要删除的素材的media_id
     * @return 是否删除成功
     */
    @Override
    public boolean deleteMaterial(String mediaId) {
        JsonNode result = HttpRequest.post(DELETE_MATERIAL)
                .query("access_token", getAccessToken())
                .bodyValue("{\"media_id\":" + mediaId + "\"}")
                .execute()
                .asJsonNode();
        int errcode = Jackson.getIntValue(result, "errcode", -1);
        if (errcode == 0) {
            return true;
        } else {
            log.error("删除永久素材异常: {}", result.toString());
            return false;
        }
    }

    /**
     * 获取素材总数
     *
     * @return {"voice_count":语音总数量,"video_count":视频总数量,"image_count":图片总数量,"news_count":图文总数量}
     */
    @Override
    public String getMaterialCount() {
        return HttpRequest.get(GET_MATERIAL_COUNT)
                .query("access_token", getAccessToken())
                .execute()
                .asString();
    }

    /**
     * 批量获取素材列表
     *
     * @param mediaType 素材类型
     * @param offset    从全部素材的该偏移位置开始返回，0表示从第一个素材 返回
     * @param count     返回素材的数量，取值在1到20之间
     * @return 素材json
     */
    @Override
    public String batchGetMaterial(MediaFile.MediaType mediaType, int offset, int count) {
        ObjectNode reqBody = Jackson.createObjectNode();
        reqBody.put("type", mediaType.toString());
        reqBody.put("offset", offset);
        reqBody.put("count", count);

        return HttpRequest.post(BATCH_GET_MATERIAL)
                .query("access_token", getAccessToken())
                .bodyValue(reqBody.toString())
                .execute()
                .asString();
    }
    // <================= 素材管理 end =================>

    // <================= 群发接口和原创接口校验 start =================>

    /**
     * 上传图文消息内的图片获取URL【订阅号与服务号认证后均可用】
     *
     * @param file 图片文件
     * @return 图片URL
     */
    @Override
    public String uploadImg(File file) {
        JsonNode result = HttpRequest.post(MEDIA_UPLOAD_IMG)
                .query("access_token", getAccessToken())
                .multipartFormBuilder()
                .add("media", file)
                .build()
                .execute()
                .asJsonNode();
        if (result.hasNonNull("url")) {
            return Jackson.getString(result, "url");
        } else {
            log.error("上传图文消息内的图片获取URL异常: {}", result.toString());
            int errcode = Jackson.getIntValue(result, "errcode", -1);
            String errmsg = Jackson.getString(result, "errmsg");
            throw new ApiException(errcode, errmsg);
        }
    }

    /**
     * 上传图文消息素材【订阅号与服务号认证后均可用】
     *
     * @param newsJson 图文消息json
     * @return media_id
     */
    @Override
    public String uploadNews(String newsJson) {
        JsonNode result = HttpRequest.post(MEDIA_UPLOAD_NEWS)
                .query("access_token", getAccessToken())
                .bodyValue(newsJson)
                .execute()
                .asJsonNode();
        if (result.hasNonNull("media_id")) {
            return Jackson.getString(result, "media_id");
        } else {
            log.error("上传图文消息素材异常: {}", result.toString());
            int errcode = Jackson.getIntValue(result, "errcode", -1);
            String errmsg = Jackson.getString(result, "errmsg");
            throw new ApiException(errcode, errmsg);
        }
    }

    /**
     * 上传群发消息里的视频消息素材【订阅号与服务号认证后均可用】
     *
     * @param mediaId     此处media_id需通过素材管理->新增素材来得到
     * @param title       视频标题
     * @param description 视频描述
     * @return media_id
     */
    @Override
    public String uploadVideo(String mediaId, String title, String description) {
        ObjectNode reqBody = Jackson.createObjectNode();
        reqBody.put("media_id", mediaId);
        reqBody.put("title", title);
        reqBody.put("description", description);
        JsonNode result = HttpRequest.post(MEDIA_UPLOAD_VIDEO)
                .query("access_token", getAccessToken())
                .bodyValue(reqBody.toString())
                .execute()
                .asJsonNode();
        if (result.hasNonNull("media_id")) {
            return Jackson.getString(result, "media_id");
        } else {
            log.error("上传群发消息里的视频消息素材异常: {}", result.toString());
            int errcode = Jackson.getIntValue(result, "errcode", -1);
            String errmsg = Jackson.getString(result, "errmsg");
            throw new ApiException(errcode, errmsg);
        }
    }

    /**
     * 根据标签进行群发【订阅号与服务号认证后均可用】
     *
     * @param mgsJson 消息json
     * @return 结果json {
     * "errcode":0,
     * "errmsg":"send job submission success",
     * "msg_id":34182, -消息发送任务的ID
     * "msg_data_id": 206227730 -消息的数据ID，该字段只有在群发图文消息时，才会出现。可以用于在图文分析数据接口中，获取到对应的图文消息的数据，是图文分析数据接口中的msgid字段中的前半部分，详见图文分析数据接口中的msgid字段的介绍
     * }
     */
    @Override
    public String massSendAll(String mgsJson) {
        return HttpRequest.post(MASS_MSG_SEND_ALL)
                .query("access_token", getAccessToken())
                .bodyValue(mgsJson)
                .execute()
                .asString();
    }

    /**
     * 根据openId列表群发【订阅号不可用，服务号认证后可用】
     *
     * @param msgJson 消息json
     * @return 结果json {
     * "errcode":0,
     * "errmsg":"send job submission success",
     * "msg_id":34182, -消息发送任务的ID
     * "msg_data_id": 206227730 -消息的数据ID，，该字段只有在群发图文消息时，才会出现。可以用于在图文分析数据接口中，获取到对应的图文消息的数据，是图文分析数据接口中的msgid字段中的前半部分，详见图文分析数据接口中的msgid字段的介绍
     * }
     */
    @Override
    public String massSend(String msgJson) {
        return HttpRequest.post(MASS_MSG_SEND)
                .query("access_token", getAccessToken())
                .bodyValue(msgJson)
                .execute()
                .asString();
    }

    /**
     * 删除群发【订阅号与服务号认证后均可用】
     *
     * @param msgId      消息发送任务的ID
     * @param articleIdx 要删除的文章在图文消息中的位置，第一篇编号为1，该字段不填或填0会删除全部文章，非必须
     * @return 是否成功
     */
    @Override
    public boolean massDelete(@NonNull String msgId, @Nullable String articleIdx) {
        ObjectNode reqBody = Jackson.createObjectNode();
        reqBody.put("msgId", msgId);
        reqBody.put("articleIdx", articleIdx);
        JsonNode result = HttpRequest.post(MASS_MSG_DELETE)
                .query("access_token", getAccessToken())
                .bodyValue(reqBody.toString())
                .execute()
                .asJsonNode();
        int errcode = Jackson.getIntValue(result, "errcode", -1);
        if (errcode == 0) {
            return true;
        } else {
            log.error("删除群发异常: {}", result.toString());
            return false;
        }
    }

    /**
     * 群发消息预览接口【订阅号与服务号认证后均可用】
     * <p>
     * 开发者可通过该接口发送消息给指定用户，在手机端查看消息的样式和排版。
     * 为了满足第三方平台开发者的需求，在保留对openId预览能力的同时，增加了对指定微信号发送预览的能力，
     * 但该能力每日调用次数有限制（100次），请勿滥用。
     * </p>
     *
     * @param msgJson 消息json
     * @return msg_id
     */
    @Override
    public String massPreview(String msgJson) {
        JsonNode result = HttpRequest.post(MASS_PREVIEW)
                .query("access_token", getAccessToken())
                .bodyValue(msgJson)
                .execute()
                .asJsonNode();
        if (result.hasNonNull("msg_id")) {
            return Jackson.getString(result, "msg_id");
        } else {
            log.error("群发消息预览接口异常: {}", result.toString());
            int errcode = Jackson.getIntValue(result, "errcode", -1);
            String errmsg = Jackson.getString(result, "errmsg");
            throw new ApiException(errcode, errmsg);
        }
    }

    /**
     * 查询群发消息发送状态【订阅号与服务号认证后均可用】
     *
     * @param msgId 消息发送任务的ID
     * @return msg_status（消息发送后的状态，SEND_SUCCESS表示发送成功，SENDING表示发送中，SEND_FAIL表示发送失败，DELETE表示已删除）
     */
    @Override
    public String massGetStatus(String msgId) {
        JsonNode result = HttpRequest.post(MASS_GET_STATUS)
                .query("access_token", getAccessToken())
                .bodyValue("{\"msg_id\":" + msgId + "\"}")
                .execute()
                .asJsonNode();
        if (result.hasNonNull("msg_status")) {
            return Jackson.getString(result, "msg_status");
        } else {
            log.error("查询群发消息发送状态异常: {}", result.toString());
            int errcode = Jackson.getIntValue(result, "errcode", -1);
            String errmsg = Jackson.getString(result, "errmsg");
            throw new ApiException(errcode, errmsg);
        }
    }
    // <================= 群发接口和原创接口校验 end =================>

    /**
     * 发送订阅通知
     *
     * @param msgJson 消息json
     * @return 是否发送成功
     */
    @Override
    public boolean sendSubscribeMsg(String msgJson) {
        JsonNode result = HttpRequest.post(SEND_SUBSCRIBE_MSG)
                .query("access_token", getAccessToken())
                .bodyValue(msgJson)
                .execute()
                .asJsonNode();
        int errcode = Jackson.getIntValue(result, "errcode", -1);
        if (errcode == 0) {
            return true;
        }
        log.error("发送订阅通知失败: {}", result.toString());
        String errmsg = Jackson.getString(result, "errmsg");
        throw new ApiException(errcode, errmsg);
    }

    /**
     * OCR 识别
     *
     * @param type   识别类型
     * @param imgUrl 图片地址
     * @return 识别结果
     */
    @Override
    public String ocrByUrl(OCR_TYPE type, String imgUrl) {
        return HttpRequest.post(type.getUrl())
                .query("access_token", getAccessToken())
                .queryEncoded("img_url", imgUrl)
                .execute()
                .asString();
    }

    /**
     * OCR 识别
     *
     * @param type 识别类型
     * @param file 图片文件
     * @return 识别结果
     */
    @Override
    public String ocrByFile(OCR_TYPE type, File file) {
        return HttpRequest.post(type.getUrl())
                .query("access_token", getAccessToken())
                .multipartFormBuilder()
                .add("img", file)
                .build()
                .execute()
                .asString();
    }

    /**
     * 发送语义理解请求
     *
     * @param json 请求参数json（参考微信文档）
     * @return 语义理解结果
     */
    @Override
    public String semanticSearch(String json) {
        Map<String, Object> reqBody = Jackson.parseMap(json);
        if (!reqBody.containsKey("appid")) {
            reqBody.put("appid", properties.getWxConf().getAppId());
        }
        return HttpRequest.post(SEMANTIC_URL)
                .query("access_token", getAccessToken())
                .bodyValue(reqBody)
                .execute()
                .asString();
    }
}
