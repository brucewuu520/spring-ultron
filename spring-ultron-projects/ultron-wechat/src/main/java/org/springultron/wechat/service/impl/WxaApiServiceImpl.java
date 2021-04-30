package org.springultron.wechat.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springultron.core.exception.ApiException;
import org.springultron.core.jackson.Jackson;
import org.springultron.core.result.ApiResult;
import org.springultron.core.utils.DigestUtils;
import org.springultron.core.utils.Maps;
import org.springultron.core.utils.StringUtils;
import org.springultron.http.HttpRequest;
import org.springultron.http.HttpResponse;
import org.springultron.redis.RedisClient;
import org.springultron.wechat.DownloadUtils;
import org.springultron.wechat.constant.WxaConstants;
import org.springultron.wechat.dto.MediaFile;
import org.springultron.wechat.dto.WxaUserInfo;
import org.springultron.wechat.dto.WxaUserSession;
import org.springultron.wechat.encrypt.WxaBizDataCrypt;
import org.springultron.wechat.enums.OCR_TYPE;
import org.springultron.wechat.props.WechatProperties;
import org.springultron.wechat.props.WxaConf;
import org.springultron.wechat.service.WxaApiService;

import java.io.File;
import java.io.InputStream;
import java.time.Duration;
import java.util.Map;

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
        if (result.hasNonNull("access_token")) {
            access_token = Jackson.getString(result, "access_token");
            int expires_in = Jackson.getIntValue(result, "expires_in", 0) - 3;
            if (expires_in > 0) {
                redisClient.setString(WXA_TOKEN_CACHE_KEY, access_token, Duration.ofSeconds(expires_in));
            }
            return access_token;
        } else {
            log.error("获取access_token异常: {}", result.toString());
            int errcode = Jackson.getIntValue(result, "errcode", -1);
            String errmsg = Jackson.getString(result, "errmsg");
            throw new ApiException(errcode, errmsg);
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
            throw new ApiException(wxaUserSession.getErrCode(), wxaUserSession.getErrMsg());
        }
    }

    /**
     * 验证用户信息完整性
     *
     * @param rawData    微信用户基本信息
     * @param sessionKey 会话密钥
     * @param signature  数据签名
     * @return 是否校验通过
     */
    @Override
    public boolean checkUserInfo(String rawData, String sessionKey, String signature) {
        String sign = DigestUtils.sha1Hex(rawData + sessionKey);
        return sign.equals(signature);
    }

    /**
     * 解密用户敏感数据
     *
     * @param encryptedData 包括敏感数据在内的完整用户信息的加密数据
     * @param sessionKey    会话密钥
     * @param iv            加密算法的初始向量
     * @return {@link WxaUserInfo}
     */
    @Override
    public WxaUserInfo decryptUserInfo(String encryptedData, String sessionKey, String iv) {
        String decryptData = WxaBizDataCrypt.decrypt(encryptedData, sessionKey, iv);
        return Jackson.parse(decryptData, WxaUserInfo.class);
    }

    /**
     * 用户支付完成后，获取该用户的 UnionId，无需用户授权
     * 本接口支持第三方平台代理查询
     *
     * @param openId        支付用户唯一标识（必须）
     * @param transactionId 微信支付订单号（非必须）
     * @param mchId         微信支付分配的商户号，和商户订单号配合使用（非必须）
     * @param outTradeNo    微信支付商户订单号，和商户号配合使用（非必须）
     * @return json {"unionid": "oTmHYjg-tElZ68xxxxxxxxhy1Rgk","errcode": 0,"errmsg": "ok"}
     */
    @Override
    public String getPaidUnionId(String openId, String transactionId, String mchId, String outTradeNo) {
        return HttpRequest.get(GET_PAID_UNION_ID)
                .query("access_token", getAccessToken())
                .query("openid", openId)
                .query("transaction_id", transactionId)
                .query("mch_id", mchId)
                .query("out_trade_no", outTradeNo)
                .execute()
                .asString();
    }

    /**
     * 生物认证秘钥签名验证
     *
     * @param openId              用户 openid
     * @param resultJSON          调用 wx.startSoterAuthentication，返回的resultJSON
     * @param resultJSONSignature 调用 wx.startSoterAuthentication，返回的resultJSONSignature
     * @return 是否验证成功
     */
    @Override
    public boolean verifySignature(String openId, String resultJSON, String resultJSONSignature) {
        ObjectNode reqBody = Jackson.createObjectNode();
        reqBody.put("openid", openId);
        reqBody.put("json_string", resultJSON);
        reqBody.put("json_signature", resultJSONSignature);

        JsonNode result = HttpRequest.post(VERIFY_SIGNATURE)
                .query("access_token", getAccessToken())
                .bodyValue(reqBody.toString())
                .execute()
                .asJsonNode();
        int errcode = Jackson.getIntValue(result, "errcode", 0);
        if (errcode == 0) {
            return Jackson.getBooleanValue(result, "is_ok", false);
        } else {
            log.error("生物认证秘钥签名验证异常: {}", result.toString());
            String errmsg = Jackson.getString(result, "errmsg");
            throw new ApiException(errcode, errmsg);
        }
    }

    /**
     * 获取小程序码
     * 适用于需要的码数量较少的业务场景。通过该接口生成的小程序码，永久有效，有数量限制
     *
     * @param path      扫码进入的小程序页面路径，最大长度 128 字节，不能为空
     * @param width     二维码的宽度，单位 px。最小 280px，最大 1280px（非必须，默认:430）
     * @param autoColor 自动配置线条颜色，如果颜色依然是黑色，则说明不建议配置主色调（默认:false）
     * @param lineColor auto_color 为 false 时生效，使用 rgb 设置颜色 例如 {"r":"xxx","g":"xxx","b":"xxx"} 十进制表示
     * @param isHyaline 是否需要透明底色，为 true 时，生成透明底色的小程序码（默认:false）
     * @return 小程序码输入流
     */
    @Override
    public InputStream getWxaCode(String path, int width, boolean autoColor, Map<String, String> lineColor, boolean isHyaline) {
        ObjectNode reqBody = Jackson.createObjectNode();
        reqBody.put("path", path);
        reqBody.put("width", width);
        reqBody.put("auto_color", autoColor);
        reqBody.put("is_hyaline", isHyaline);
        if (Maps.isNotEmpty(lineColor)) {
            ObjectNode line_color = Jackson.createObjectNode();
            for (Map.Entry<String, String> entry : lineColor.entrySet()) {
                line_color.put(entry.getKey(), entry.getValue());
            }
            reqBody.set("line_color", line_color);
        }

        return HttpRequest.post(GET_WXA_CODE)
                .query("access_token", getAccessToken())
                .bodyValue(reqBody.toString())
                .execute()
                .asStream();
    }

    /**
     * 获取无数量限制的小程序码
     * 适用于需要的码数量极多的业务场景。通过该接口生成的小程序码，永久有效，数量暂无限制
     *
     * @param scene     最大32个可见字符，只支持数字，大小写英文以及部分特殊字符：!#$&'()*+,/:;=?@-._~
     * @param page      必须是已经发布的小程序存在的页面（否则报错），例如 pages/index/index，根路径前不要填加/，不能携带参数（参数请放在scene字段里），如果不填写这个字段，默认跳主页面
     * @param width     二维码的宽度，单位 px。最小 280px，最大 1280px（非必须，默认:430）
     * @param autoColor 自动配置线条颜色，如果颜色依然是黑色，则说明不建议配置主色调（默认:false）
     * @param lineColor auto_color 为 false 时生效，使用 rgb 设置颜色 例如 {"r":"xxx","g":"xxx","b":"xxx"} 十进制表示
     * @param isHyaline 是否需要透明底色，为 true 时，生成透明底色的小程序码（默认:false）
     * @return 小程序码输入流
     */
    @Override
    public InputStream getWxaCodeUnlimited(String scene, String page, int width, boolean autoColor, Map<String, String> lineColor, boolean isHyaline) {
        ObjectNode reqBody = Jackson.createObjectNode();
        reqBody.put("scene", scene);
        reqBody.put("page", page);
        reqBody.put("width", width);
        reqBody.put("auto_color", autoColor);
        reqBody.put("is_hyaline", isHyaline);
        if (Maps.isNotEmpty(lineColor)) {
            ObjectNode line_color = Jackson.createObjectNode();
            for (Map.Entry<String, String> entry : lineColor.entrySet()) {
                line_color.put(entry.getKey(), entry.getValue());
            }
            reqBody.set("line_color", line_color);
        }

        return HttpRequest.post(GET_WXA_CODE_UNLIMITED)
                .query("access_token", getAccessToken())
                .bodyValue(reqBody.toString())
                .execute()
                .asStream();
    }

    /**
     * 获取小程序二维码（不推荐使用）
     *
     * @param path  扫码进入的小程序页面路径，最大长度 128 字节，不能为空
     * @param width 二维码的宽度，单位 px。最小 280px，最大 1280px
     * @return 二维码图片流
     */
    @Override
    public InputStream createQrCode(String path, int width) {
        ObjectNode reqBody = Jackson.createObjectNode();
        reqBody.put("path", path);
        reqBody.put("width", width <= 0 ? 430 : width);

        return HttpRequest.post(CREATE_QR_CODE)
                .query("access_token", getAccessToken())
                .bodyValue(reqBody.toString())
                .execute()
                .asStream();
    }

    /**
     * 生成小程序scheme码，适用于短信、邮件、外部网页等拉起小程序的业务场景
     * 通过该接口，可以选择生成到期失效和永久有效的小程序码，目前仅针对国内非个人主体的小程序开放
     *
     * @param path       通过scheme码进入的小程序页面路径，必须是已经发布的小程序存在的页面，不可携带query。path为空时会跳转小程序主页
     * @param query      通过scheme码进入小程序时的query，最大1024个字符，只支持数字，大小写英文以及部分特殊字符：!#$&'()*+,/:;=?@-._~
     * @param isExpire   生成的scheme码类型，到期失效：true，永久有效：false（默认:false）
     * @param expireTime 到期失效的scheme码的失效时间，为Unix时间戳。生成的到期失效scheme码在该时间前有效。最长有效期为1年。生成到期失效的scheme时必填
     * @return open link scheme
     */
    @Override
    public String generateScheme(String path, String query, boolean isExpire, Long expireTime) {
        ObjectNode reqBody = Jackson.createObjectNode();
        reqBody.put("is_expire", isExpire);
        if (expireTime != null) {
            reqBody.put("expire_time", expireTime);
        }
        if (StringUtils.isNotEmpty(path) && StringUtils.isNotEmpty(query)) {
            ObjectNode jump_wxa = Jackson.createObjectNode();
            jump_wxa.put("path", path);
            jump_wxa.put("query", query);
            reqBody.set("jump_wxa", jump_wxa);
        }

        JsonNode result = HttpRequest.post(GENERATE_SCHEME)
                .query("access_token", getAccessToken())
                .bodyValue(reqBody.toString())
                .execute()
                .asJsonNode();
        int errcode = Jackson.getIntValue(result, "errcode", 0);
        if (errcode == 0) {
            return Jackson.getString(result, "openlink");
        } else {
            log.error("生成小程序scheme码异常: {}", result.toString());
            String errmsg = Jackson.getString(result, "errmsg");
            throw new ApiException(errcode, errmsg);
        }
    }

    /**
     * 发送订阅消息
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
        log.error("发送订阅消息失败: {}", result.toString());
        String errmsg = Jackson.getString(result, "errmsg");
        throw new ApiException(errcode, errmsg);
    }

    /**
     * 下发小程序和公众号统一的服务消息
     *
     * @param msgJson 消息json
     * @return 是否发送成功
     */
    @Override
    public boolean sendUniformMsg(String msgJson) {
        JsonNode result = HttpRequest.post(SEND_UNIFORM_MSG)
                .query("access_token", getAccessToken())
                .bodyValue(msgJson)
                .execute()
                .asJsonNode();
        int errcode = Jackson.getIntValue(result, "errcode", -1);
        if (errcode == 0) {
            return true;
        }
        log.error("下发小程序和公众号统一的服务消息失败: {}", result.toString());
        String errmsg = Jackson.getString(result, "errmsg");
        throw new ApiException(errcode, errmsg);
    }

    // <================= 客户消息 start =================>

    /**
     * 发送客服消息给用户
     *
     * @param msgJson 消息json
     * @return 是否发送成功
     */
    @Override
    public boolean sendCustomerMsg(String msgJson) {
        JsonNode result = HttpRequest.post(SEND_CUSTOMER_MSG)
                .query("access_token", getAccessToken())
                .bodyValue(msgJson)
                .execute()
                .asJsonNode();
        int errcode = Jackson.getIntValue(result, "errcode", -1);
        if (errcode == 0) {
            return true;
        }
        log.error("发送客服消息给用户失败: {}", result.toString());
        String errmsg = Jackson.getString(result, "errmsg");
        throw new ApiException(errcode, errmsg);
    }

    /**
     * 下发客服当前输入状态给用户
     *
     * @param openId  用户openId
     * @param command 命令（Typing:对用户下发"正在输入"状态 CancelTyping:取消对用户的"正在输入"状态）
     * @return 是否下发成功
     */
    @Override
    public boolean setCustomerTyping(String openId, String command) {
        ObjectNode reqBody = Jackson.createObjectNode();
        reqBody.put("touser", openId);
        reqBody.put("command", "command");

        JsonNode result = HttpRequest.post(SET_CUSTOMER_TYPE)
                .query("access_token", getAccessToken())
                .bodyValue(reqBody.toString())
                .execute()
                .asJsonNode();
        int errcode = Jackson.getIntValue(result, "errcode", -1);
        if (errcode == 0) {
            return true;
        }
        log.error("发送客服消息给用户失败: {}", result.toString());
        String errmsg = Jackson.getString(result, "errmsg");
        throw new ApiException(errcode, errmsg);
    }

    /**
     * 上传媒体文件到微信服务器，用于发送客服消息或被动回复用户消息
     * 目前仅支持图片
     *
     * @param mediaType 文件类型（目前只支持图片）
     * @param file      文件
     * @return media_id 媒体文件上传后，获取标识，3天内有效。
     */
    @Override
    public String uploadTempMedia(MediaFile.MediaType mediaType, File file) {
        JsonNode result = HttpRequest.post(UPLOAD_TEMP_MEDIA)
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
        log.error("上传媒体文件到微信服务器失败: {}", result.toString());
        int errcode = Jackson.getIntValue(result, "errcode", -1);
        String errmsg = Jackson.getString(result, "errmsg");
        throw new ApiException(errcode, errmsg);
    }

    /**
     * 获取客服消息内的临时素材，即下载临时的多媒体文件
     * 目前小程序仅支持下载图片文件
     *
     * @param mediaId 媒体文件 ID
     * @return 图片文件输入流
     */
    @Override
    public MediaFile getTempMedia(String mediaId) {
        HttpResponse response = HttpRequest.get(GET_TEMP_MEDIA)
                .query("access_token", getAccessToken())
                .query("media_id", mediaId)
                .execute()
                .response();
        return DownloadUtils.download(response);
    }
    // <================= 客户消息 end =================>

    /**
     * 创建被分享动态消息或私密消息的 activity_id
     *
     * @param unionId 为私密消息创建activity_id时，指定分享者为unionid用户。其余用户不能用此activity_id分享私密消息。openid与unionid填一个即可。私密消息暂不支持云函数生成activity id
     * @param openId  为私密消息创建activity_id时，指定分享者为openid用户。其余用户不能用此activity_id分享私密消息。openid与unionid填一个即可。私密消息暂不支持云函数生成activity id
     * @return {"activity_id": "动态消息的 ID", "expiration_time": "activity_id 的过期时间戳。默认24小时后过期"}
     */
    @Override
    public String createActivityId(String unionId, String openId) {
        HttpRequest request = HttpRequest.get(CREATE_ACTIVITY_ID).query("access_token", getAccessToken());
        if (StringUtils.isNotEmpty(unionId)) {
            request.query("unionid", unionId);
        } else if (StringUtils.isNotEmpty(openId)) {
            request.query("openid", openId);
        }
        return request.execute().asString();
    }

    /**
     * 修改被分享的动态消息
     *
     * @param json 请求参数json
     * @return 是否成功
     */
    @Override
    public boolean setUpdatableMsg(String json) {
        JsonNode result = HttpRequest.post(SET_UPDATABLE_MSG)
                .query("access_token", getAccessToken())
                .bodyValue(json)
                .execute()
                .asJsonNode();
        int errcode = Jackson.getIntValue(result, "errcode", -1);
        if (errcode == 0) {
            return true;
        }
        log.error("修改被分享的动态消息失败: {}", result.toString());
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
     * 根据提交的用户信息数据获取用户的安全等级 risk_rank，无需用户授权
     *
     * @param json 请求参数json
     * @return 用户风险等级（风险等级0 1 2 3 4）
     */
    @Override
    public int getUserRiskRank(String json) {
        JsonNode result = HttpRequest.post(GET_USER_RISK_RANK)
                .query("access_token", getAccessToken())
                .bodyValue(json)
                .execute()
                .asJsonNode();
        int errcode = Jackson.getIntValue(result, "errcode", 0);
        if (errcode == 0) {
            return Jackson.getIntValue(result, "risk_rank", 0);
        }
        log.error("获取用户的安全等级失败: {}", result.toString());
        String errmsg = Jackson.getString(result, "errmsg");
        throw new ApiException(errcode, errmsg);
    }

    /**
     * 校验一张图片是否含有违法违规内容
     *
     * @param file 图片文件
     * @return 检测结果json
     */
    @Override
    public String imgSecCheck(File file) {
        return HttpRequest.post(IMG_SEC_CHECK)
                .query("access_token", getAccessToken())
                .multipartFormBuilder()
                .add("media", file)
                .build()
                .execute()
                .asString();
    }

    /**
     * 检查一段文本是否含有违法违规内容
     *
     * @param text 文本
     * @return 检测结果json
     */
    @Override
    public String textSecCheck(String text) {
        return HttpRequest.post(TEXT_SEC_CHECK)
                .query("access_token", getAccessToken())
                .bodyValue("{\"content\":" + text + "\"}")
                .execute()
                .asString();
    }

    /**
     * 异步校验图片/音频是否含有违法违规内容
     *
     * @param mediaType 1:音频;2:图片
     * @param mediaUrl  要检测的多媒体url
     * @return trace_id 任务id，用于匹配异步推送结果
     */
    @Override
    public String mediaCheckAsync(int mediaType, String mediaUrl) {
        ObjectNode reqBody = Jackson.createObjectNode();
        reqBody.put("media_type", mediaType);
        reqBody.put("media_url", mediaUrl);

        JsonNode result = HttpRequest.post(MEDIA_CHECK_ASYNC)
                .query("access_token", getAccessToken())
                .bodyValue(reqBody.toString())
                .execute()
                .asJsonNode();
        int errcode = Jackson.getIntValue(result, "errcode", 0);
        if (errcode == 0) {
            return Jackson.getString(result, "trace_id");
        }
        log.error("异步校验图片/音频是否含有违法违规内容失败: {}", result.toString());
        String errmsg = Jackson.getString(result, "errmsg");
        throw new ApiException(errcode, errmsg);
    }
}