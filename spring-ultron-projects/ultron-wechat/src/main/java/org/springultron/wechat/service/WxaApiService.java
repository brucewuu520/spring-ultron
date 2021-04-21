package org.springultron.wechat.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springultron.core.jackson.Jackson;
import org.springultron.wechat.dto.MediaFile;
import org.springultron.wechat.dto.WxaUserInfo;
import org.springultron.wechat.dto.WxaUserSession;
import org.springultron.wechat.enums.OCR_TYPE;
import org.springultron.wechat.params.WxaTemplate;
import org.springultron.wechat.params.WxaUniformMsg;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

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
     * 验证用户信息完整性
     *
     * @param rawData    微信用户基本信息
     * @param sessionKey 会话密钥
     * @param signature  数据签名
     * @return 是否校验通过
     */
    boolean checkUserInfo(String rawData, String sessionKey, String signature);

    /**
     * 解密用户敏感数据
     *
     * @param encryptedData 包括敏感数据在内的完整用户信息的加密数据
     * @param sessionKey    会话密钥
     * @param iv            加密算法的初始向量
     * @return {@link WxaUserInfo}
     */
    WxaUserInfo decryptUserInfo(String encryptedData, String sessionKey, String iv);

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
    String getPaidUnionId(String openId, String transactionId, String mchId, String outTradeNo);

    /**
     * 生物认证秘钥签名验证
     *
     * @param openId              用户 openid
     * @param resultJSON          调用 wx.startSoterAuthentication，返回的resultJSON
     * @param resultJSONSignature 调用 wx.startSoterAuthentication，返回的resultJSONSignature
     * @return 是否验证成功
     */
    boolean verifySignature(String openId, String resultJSON, String resultJSONSignature);

    /**
     * 获取小程序码
     * 适用于需要的码数量较少的业务场景。通过该接口生成的小程序码，永久有效，有数量限制
     *
     * @param path 扫码进入的小程序页面路径，最大长度 128 字节，不能为空
     * @return 小程序码输入流
     */
    default InputStream getWxaCode(String path) {
        return getWxaCode(path, 430, false, null, false);
    }

    /**
     * 获取小程序码
     * 适用于需要的码数量较少的业务场景。通过该接口生成的小程序码，永久有效，有数量限制
     *
     * @param path  扫码进入的小程序页面路径，最大长度 128 字节，不能为空
     * @param width 二维码的宽度，单位 px。最小 280px，最大 1280px
     * @return 小程序码输入流
     */
    default InputStream getWxaCode(String path, int width) {
        return getWxaCode(path, width, false, null, false);
    }

    /**
     * 获取小程序码
     * 适用于需要的码数量较少的业务场景。通过该接口生成的小程序码，永久有效，有数量限制
     *
     * @param path  扫码进入的小程序页面路径，最大长度 128 字节，不能为空
     * @param width 二维码的宽度，单位 px。最小 280px，最大 1280px
     * @param r     颜色R
     * @param g     颜色R
     * @param b     颜色B
     * @return 小程序码输入流
     */
    default InputStream getWxaCode(String path, int width, String r, String g, String b) {
        Map<String, String> lineColor = new HashMap<>(3);
        lineColor.put("r", r);
        lineColor.put("g", g);
        lineColor.put("b", b);
        return getWxaCode(path, width, false, lineColor, false);
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
    InputStream getWxaCode(String path, int width, boolean autoColor, Map<String, String> lineColor, boolean isHyaline);

    /**
     * 获取无数量限制的小程序码
     * 适用于需要的码数量极多的业务场景。通过该接口生成的小程序码，永久有效，数量暂无限制
     *
     * @param scene 最大32个可见字符，只支持数字，大小写英文以及部分特殊字符：!#$&'()*+,/:;=?@-._~
     * @param page  必须是已经发布的小程序存在的页面（否则报错），例如 pages/index/index，根路径前不要填加/，不能携带参数（参数请放在scene字段里），如果不填写这个字段，默认跳主页面
     * @return 小程序码输入流
     */
    default InputStream getWxaCodeUnlimited(String scene, String page) {
        return getWxaCodeUnlimited(scene, page, 430, false, null, false);
    }

    /**
     * 获取无数量限制的小程序码
     * 适用于需要的码数量极多的业务场景。通过该接口生成的小程序码，永久有效，数量暂无限制
     *
     * @param scene 最大32个可见字符，只支持数字，大小写英文以及部分特殊字符：!#$&'()*+,/:;=?@-._~
     * @param page  必须是已经发布的小程序存在的页面（否则报错），例如 pages/index/index，根路径前不要填加/，不能携带参数（参数请放在scene字段里），如果不填写这个字段，默认跳主页面
     * @param width 二维码的宽度，单位 px。最小 280px，最大 1280px（非必须，默认:430）
     * @return 小程序码输入流
     */
    default InputStream getWxaCodeUnlimited(String scene, String page, int width) {
        return getWxaCodeUnlimited(scene, page, width, false, null, false);
    }

    /**
     * 获取无数量限制的小程序码
     * 适用于需要的码数量极多的业务场景。通过该接口生成的小程序码，永久有效，数量暂无限制
     *
     * @param scene 最大32个可见字符，只支持数字，大小写英文以及部分特殊字符：!#$&'()*+,/:;=?@-._~
     * @param page  必须是已经发布的小程序存在的页面（否则报错），例如 pages/index/index，根路径前不要填加/，不能携带参数（参数请放在scene字段里），如果不填写这个字段，默认跳主页面
     * @param width 二维码的宽度，单位 px。最小 280px，最大 1280px（非必须，默认:430）
     * @param r     颜色R
     * @param g     颜色R
     * @param b     颜色B
     * @return 小程序码输入流
     */
    default InputStream getWxaCodeUnlimited(String scene, String page, int width, String r, String g, String b) {
        Map<String, String> lineColor = new HashMap<>(3);
        lineColor.put("r", r);
        lineColor.put("g", g);
        lineColor.put("b", b);
        return getWxaCodeUnlimited(scene, page, width, false, lineColor, false);
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
    InputStream getWxaCodeUnlimited(String scene, String page, int width, boolean autoColor, Map<String, String> lineColor, boolean isHyaline);

    /**
     * 获取小程序二维码（不推荐使用）
     *
     * @param path  扫码进入的小程序页面路径，最大长度 128 字节，不能为空
     * @param width 二维码的宽度，单位 px。最小 280px，最大 1280px
     * @return 二维码输入流
     */
    InputStream createQrCode(String path, int width);

    /**
     * 生成永久有效小程序scheme码，适用于短信、邮件、外部网页等拉起小程序的业务场景
     * 目前仅针对国内非个人主体的小程序开放
     *
     * @return open link scheme
     */
    default String generateScheme() {
        return generateScheme(null, null, false, null);
    }

    /**
     * 生成到期失效小程序scheme码，适用于短信、邮件、外部网页等拉起小程序的业务场景
     * 目前仅针对国内非个人主体的小程序开放
     *
     * @param expireTime 到期失效的scheme码的失效时间，为Unix时间戳。生成的到期失效scheme码在该时间前有效。最长有效期为1年
     * @return open link scheme
     */
    default String generateScheme(long expireTime) {
        return generateScheme(null, null, true, expireTime);
    }

    /**
     * 生成永久有效小程序scheme码，适用于短信、邮件、外部网页等拉起小程序的业务场景
     * 目前仅针对国内非个人主体的小程序开放
     *
     * @param path  通过scheme码进入的小程序页面路径，必须是已经发布的小程序存在的页面，不可携带query。path为空时会跳转小程序主页
     * @param query 通过scheme码进入小程序时的query，最大1024个字符，只支持数字，大小写英文以及部分特殊字符：!#$&'()*+,/:;=?@-._~
     * @return open link scheme
     */
    default String generateScheme(String path, String query) {
        return generateScheme(path, query, false, null);
    }

    /**
     * 生成到期失效小程序scheme码，适用于短信、邮件、外部网页等拉起小程序的业务场景
     * 目前仅针对国内非个人主体的小程序开放
     *
     * @param path       通过scheme码进入的小程序页面路径，必须是已经发布的小程序存在的页面，不可携带query。path为空时会跳转小程序主页
     * @param query      通过scheme码进入小程序时的query，最大1024个字符，只支持数字，大小写英文以及部分特殊字符：!#$&'()*+,/:;=?@-._~
     * @param expireTime 到期失效的scheme码的失效时间，为Unix时间戳。生成的到期失效scheme码在该时间前有效。最长有效期为1年
     * @return open link scheme
     */
    default String generateScheme(String path, String query, long expireTime) {
        return generateScheme(path, query, true, expireTime);
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
    String generateScheme(String path, String query, boolean isExpire, Long expireTime);

    /**
     * 发送订阅消息
     *
     * @param template 消息模板
     * @return 是否发送成功
     */
    default boolean sendSubscribeMsg(WxaTemplate template) {
        return sendSubscribeMsg(Jackson.toJson(template));
    }

    /**
     * 发送订阅消息
     *
     * @param msgJson 消息json
     * @return 是否发送成功
     */
    boolean sendSubscribeMsg(String msgJson);

    /**
     * 下发小程序和公众号统一的服务消息
     *
     * @param uniformMsg 统一消息模板
     * @return 是否发送成功
     */
    default boolean sendUniformMsg(WxaUniformMsg uniformMsg) {
        return sendUniformMsg(Jackson.toJson(uniformMsg));
    }

    /**
     * 下发小程序和公众号统一的服务消息
     *
     * @param msgJson 消息json
     * @return 是否发送成功
     */
    boolean sendUniformMsg(String msgJson);

    // <================= 客服消息 start =================>

    /**
     * 发送文本客服消息
     * <p>
     * 发送文本消息时，支持添加可跳转小程序的文字连接：
     * 文本内容...<a href="http://www.qq.com" data-miniprogram-appid="appid" data-miniprogram-path="pages/index/index">点击跳小程序</a>
     * 1.data-miniprogram-appid 项，填写小程序appid，则表示该链接跳转小程序；
     * 2.data-miniprogram-path项，填写小程序路径，路径与app.json中保持一致，可带参数；
     * </p>
     *
     * @param openId 用户openId
     * @param text   消息文本
     * @return 是否发送成功
     */
    default boolean sendCustomerTextMsg(String openId, String text) {
        ObjectNode reqBody = Jackson.createObjectNode();
        reqBody.put("touser", openId);
        reqBody.put("msgtype", "text");

        ObjectNode content = Jackson.createObjectNode();
        content.put("content", text);

        reqBody.set("text", content);

        return sendCustomerMsg(reqBody.toString());
    }

    /**
     * 发送图片客服消息
     *
     * @param openId  用户openId
     * @param mediaId 发送的图片的媒体ID，通过 新增素材接口 上传图片文件获得
     * @return 是否发送成功
     */
    default boolean sendCustomerImageMsg(String openId, String mediaId) {
        ObjectNode reqBody = Jackson.createObjectNode();
        reqBody.put("touser", openId);
        reqBody.put("msgtype", "image");

        ObjectNode image = Jackson.createObjectNode();
        image.put("media_id", mediaId);

        reqBody.set("image", image);

        return sendCustomerMsg(reqBody.toString());
    }

    /**
     * 发送图文链接客服消息
     *
     * @param openId      用户openId
     * @param title       消息标题
     * @param description 图文链接消息描述
     * @param url         图文链接消息被点击后跳转的链接
     * @param thumbUrl    图文链接消息的图片链接，支持 JPG、PNG 格式，较好的效果为大图 640 X 320，小图 80 X 80
     * @return 是否发送成功
     */
    default boolean sendCustomerLinkMsg(String openId, String title, String description, String url, String thumbUrl) {
        ObjectNode reqBody = Jackson.createObjectNode();
        reqBody.put("touser", openId);
        reqBody.put("msgtype", "link");

        ObjectNode link = Jackson.createObjectNode();
        link.put("title", title);
        link.put("description", description);
        link.put("url", url);
        link.put("thumb_url", thumbUrl);

        reqBody.set("link", link);

        return sendCustomerMsg(reqBody.toString());
    }

    /**
     * 发送小程序卡片消息
     *
     * @param openId       用户openId
     * @param title        消息标题
     * @param pagePath     小程序的页面路径，跟app.json对齐，支持参数，比如pages/index/index?foo=bar
     * @param thumbMediaId 小程序消息卡片的封面， image 类型的 media_id，通过 新增素材接口 上传图片文件获得，建议大小为 520*416
     * @return 是否发送成功
     */
    default boolean sendMiniProgramPage(String openId, String title, String pagePath, String thumbMediaId) {
        ObjectNode reqBody = Jackson.createObjectNode();
        reqBody.put("touser", openId);
        reqBody.put("msgtype", "miniprogrampage");

        ObjectNode miniprogrampage = Jackson.createObjectNode();
        miniprogrampage.put("title", title);
        miniprogrampage.put("pagepath", pagePath);
        miniprogrampage.put("thumb_media_id", thumbMediaId);

        reqBody.set("miniprogrampage", miniprogrampage);

        return sendCustomerMsg(reqBody.toString());
    }

    /**
     * 发送客服消息给用户
     *
     * @param msgJson 消息json
     * @return 是否发送成功
     */
    boolean sendCustomerMsg(String msgJson);

    /**
     * 下发客服当前输入状态给用户
     *
     * @param openId  用户openId
     * @param command 命令（Typing:对用户下发"正在输入"状态 CancelTyping:取消对用户的"正在输入"状态）
     * @return 是否下发成功
     */
    boolean setCustomerTyping(String openId, String command);

    /**
     * 上传媒体文件到微信服务器，用于发送客服消息或被动回复用户消息
     * 目前仅支持图片
     *
     * @param mediaType 文件类型（目前只支持图片）
     * @param file      文件
     * @return media_id 媒体文件上传后，获取标识，3天内有效。
     */
    String uploadTempMedia(MediaFile.MediaType mediaType, File file);

    /**
     * 获取客服消息内的临时素材，即下载临时的多媒体文件
     * 目前小程序仅支持下载图片文件
     *
     * @param mediaId 媒体文件 ID
     * @return 图片文件输入流
     */
    MediaFile getTempMedia(String mediaId);
    // <================= 客服消息 end =================>

    /**
     * 创建被分享动态消息或私密消息的 activity_id
     *
     * @param unionId 为私密消息创建activity_id时，指定分享者为unionid用户。其余用户不能用此activity_id分享私密消息。openid与unionid填一个即可。私密消息暂不支持云函数生成activity id
     * @param openId  为私密消息创建activity_id时，指定分享者为openid用户。其余用户不能用此activity_id分享私密消息。openid与unionid填一个即可。私密消息暂不支持云函数生成activity id
     * @return {"activity_id": "动态消息的 ID", "expiration_time": "activity_id 的过期时间戳。默认24小时后过期"}
     */
    String createActivityId(String unionId, String openId);

    /**
     * 修改被分享的动态消息
     *
     * @param json 请求参数json
     * @return 是否成功
     */
    boolean setUpdatableMsg(String json);

    /**
     * OCR 识别
     *
     * @param type   识别类型
     * @param imgUrl 图片地址
     * @return 识别结果
     */
    String ocrByUrl(OCR_TYPE type, String imgUrl);

    /**
     * OCR 识别
     *
     * @param type 识别类型
     * @param file 图片文件
     * @return 识别结果
     */
    String ocrByFile(OCR_TYPE type, File file);

    /**
     * 根据提交的用户信息数据获取用户的安全等级 risk_rank，无需用户授权
     *
     * @param json 请求参数json
     * @return 用户风险等级（风险等级0 1 2 3 4）
     */
    int getUserRiskRank(String json);

    /**
     * 校验一张图片是否含有违法违规内容
     *
     * @param file 图片文件
     * @return 检测结果json
     */
    String imgSecCheck(File file);

    /**
     * 检查一段文本是否含有违法违规内容
     *
     * @param text 文本
     * @return 检测结果json
     */
    String textSecCheck(String text);

    /**
     * 异步校验图片/音频是否含有违法违规内容
     *
     * @param mediaType 1:音频;2:图片
     * @param mediaUrl  要检测的多媒体url
     * @return trace_id 任务id，用于匹配异步推送结果
     */
    String mediaCheckAsync(int mediaType, String mediaUrl);
}
