package org.springultron.wechat.service;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springultron.core.jackson.Jackson;
import org.springultron.wechat.dto.*;
import org.springultron.wechat.enums.OCR_TYPE;
import org.springultron.wechat.params.WxQrCodeParams;
import org.springultron.wechat.params.WxTemplate;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公众号常用接口服务
 *
 * @author brucewuu
 * @date 2021/4/12 上午10:22
 */
public interface WxApiService {
    /**
     * 获取公众号access_token
     *
     * @return 公众号 access_token
     */
    String getAccessToken();

    /**
     * 获取微信API接口IP地址
     *
     * @return ip_list
     */
    List<String> getApiDomainIpList();

    /**
     * 获取微信callback IP地址
     *
     * @return ip_list
     */
    List<String> getCallbackIpList();

    /**
     * 获取帐号的关注者列表
     * 关注者列表由一串openId（加密后的微信号，每个用户对每个公众号的openId是唯一的）组成。
     * 一次拉取调用最多拉取10000个关注者的openId，可以通过多次拉取的方式来满足需求。
     *
     * @param nextOpenId 第一个拉取的openId，不填默认从头开始拉取
     * @return {@link WxUserList}
     */
    WxUserList getUserList(@Nullable String nextOpenId);

    /**
     * 获取用户基本信息（包括UnionID机制）
     *
     * @param openId 用户 openId
     * @param lang   返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
     * @return {@link WxUserInfo}
     */
    WxUserInfo getUserInfo(@NonNull String openId, @Nullable String lang);

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
    List<WxUserInfo> batchUserInfo(@NonNull String userListJson);

    /**
     * 对指定用户设置备注名
     *
     * @param openId 用户 openId
     * @param remark 新的备注名，长度必须小于30字符
     * @return 是否设置成功
     */
    boolean updateUserRemark(String openId, String remark);

    /**
     * 通过code换取网页授权access_token
     * 首先请注意，这里通过code换取的是一个特殊的网页授权access_token,与基础支持中的access_token（该access_token用于调用其他接口）不同。
     * 公众号可通过下述接口来获取网页授权access_token。如果网页授权的作用域为snsapi_base，则本步骤中获取到网页授权access_token的同时，
     * 也获取到了openId，snsapi_base式的网页授权流程即到此为止。
     *
     * @param code 用户网页授权码
     * @return {@link WxSnsTokenInfo}
     */
    WxSnsTokenInfo oauth2Token(String code);

    /**
     * 刷新取网页授权access_token
     *
     * @param refreshToken 通过access_token获取到的refresh_token参数
     * @return @return {@link WxSnsTokenInfo}
     */
    WxSnsTokenInfo refreshOauth2Token(String refreshToken);

    /**
     * 网页授权拉取用户信息(需scope为 snsapi_userinfo)
     * 如果网页授权作用域为snsapi_userinfo，则此时开发者可以通过access_token和openId拉取用户信息了。
     *
     * @param accessToken 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
     * @param openId      用户的唯一标识
     * @param lang        返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
     * @return {@link WxUserInfo}
     */
    WxUserInfo snsUserInfo(String accessToken, String openId, @Nullable String lang);

    /**
     * 检验网页授权凭证（access_token）是否有效
     *
     * @param accessToken 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
     * @param openId      用户的唯一标识
     * @return access_token 是否有效
     */
    boolean checkSnsAuth(String accessToken, String openId);

    /**
     * 获取jsapi_ticket
     * jsapi_ticket是公众号用于调用微信JS接口的临时票据；正常情况下，jsapi_ticket的有效期为7200秒，通过access_token来获取。
     * 由于获取jsapi_ticket的api调用次数非常有限，频繁刷新jsapi_ticket会导致api调用受限，影响自身业务，开发者必须在自己的服务全局缓存jsapi_ticket 。
     *
     * @return jsapi_ticket
     */
    String getJsapiTicket();

    /**
     * JS-SDK签名
     *
     * @param url 当前网页的URL，不包含#及其后面部分
     * @return {@link WxJsSdkSign}
     */
    WxJsSdkSign jsSdkSign(String url);

    /**
     * 获取微信卡券api_ticket
     * 卡券 api_ticket 是用于调用卡券相关接口的临时票据，有效期为 7200 秒，通过 access_token 来获取。
     * 这里要注意与 jsapi_ticket 区分开来。由于获取卡券 api_ticket 的 api 调用次数非常有限。
     * 频繁刷新卡券 api_ticket 会导致 api 调用受限，影响自身业务，开发者必须在自己的服务全局缓存卡券 api_ticket 。
     *
     * @return 微信卡券 api_ticket
     */
    String getCardApiTicket();

    /**
     * 微信卡券签名 cardSign
     *
     * @return {@link WxCardSign}
     */
    WxCardSign cardSign(String cardId, String cardType, String locationId);

    /**
     * 创建公众号二维码
     *
     * @param params 创建公众号二维码参数 {@link WxQrCodeParams}
     * @return {@link WxQrCode}
     */
    WxQrCode createQrCode(WxQrCodeParams params);

    /**
     * 通过ticket换取二维码
     * ticket正确情况下，http 返回码是200，是一张图片，可以直接展示或者下载
     *
     * @param ticket 二维码 ticket
     * @return 二维码地址
     */
    String showQrcode(String ticket);

    // <================= 公众号菜单配置 start =================>

    /**
     * 查询当前菜单配置（包括默认菜单和全部个性化菜单信息）
     *
     * @return 菜单json
     */
    String getMenuInfo();

    /**
     * 创建自定义菜单
     *
     * @param menuJson 自定义菜单json
     * @return 是否成功
     */
    boolean createMenu(String menuJson);

    /**
     * 删除所有自定义菜单（包括默认菜单和全部个性化菜单）
     *
     * @return 是否成功
     */
    boolean deleteAllMenu();

    /**
     * 创建个性化菜单
     *
     * @param menuJson 个性化菜单json
     * @return 个性化菜单ID menuId
     */
    String createConditionalMenu(String menuJson);

    /**
     * 删除个性化菜单
     *
     * @param menuId 个性化菜单ID
     * @return 是否成功
     */
    boolean deleteConditionalMenu(String menuId);

    /**
     * 测试个性化菜单匹配结果
     *
     * @param userId 可以是粉丝的openId，也可以是粉丝的微信号
     * @return menuJson
     */
    String tryMatchConditionalMenu(String userId);
    // <================= 公众号菜单配置 end =================>

    // <================= 素材管理 start =================>

    /**
     * 上传临时素材
     *
     * @param mediaType 素材格式
     * @param file      文件
     * @return media_id 媒体文件上传后，获取标识
     */
    String uploadMedia(MediaFile.MediaType mediaType, File file);

    /**
     * 获取临时素材
     *
     * @param mediaId 媒体文件ID
     * @return {@link MediaFile}
     */
    MediaFile getMedia(String mediaId);

    /**
     * 获取高清语音素材
     *
     * @param mediaId 媒体文件ID，即uploadVoice接口返回的serverID
     * @return {@link MediaFile}
     */
    MediaFile getJsSdkMedia(String mediaId);

    /**
     * 新增永久图文素材
     *
     * @param newsList 图文消息列表
     * @return 图文消息素材的media_id
     */
    default String addNews(List<MaterialNews> newsList) {
        Map<String, Object> reqBody = new HashMap<>(1);
        reqBody.put("articles", newsList);

        return addNews(Jackson.toJson(reqBody));
    }

    /**
     * 新增永久图文素材
     *
     * @param articlesJson 图文消息json
     * @return 图文消息素材的media_id
     */
    String addNews(String articlesJson);

    /**
     * 修改永久图文素材
     *
     * @param mediaId 要修改的图文消息的id
     * @param index   要更新的文章在图文消息中的位置（多图文消息时，此字段才有意义），第一篇为0
     * @param news    图文消息
     * @return 是否修改成功
     */
    default boolean updateNews(String mediaId, int index, MaterialNews news) {
        Map<String, Object> reqBody = new HashMap<>(3);
        reqBody.put("media_id", mediaId);
        reqBody.put("index", index);
        reqBody.put("articles", news);

        return updateNews(Jackson.toJson(reqBody));
    }

    /**
     * 修改永久图文素材
     *
     * @param articlesJson 图文消息json
     * @return 是否修改成功
     */
    boolean updateNews(String articlesJson);

    /**
     * 新增永久视频素材
     *
     * @param title        视频素材的标题
     * @param introduction 视频素材的描述
     * @param file         视频文件
     * @return {"media_id":MEDIA_ID,"url":URL}
     */
    String addVideoMaterial(String title, String introduction, File file);

    /**
     * 新增其他类型永久素材
     *
     * @param mediaType 文件类型
     * @param file      素材文件
     * @return {"media_id":MEDIA_ID,"url":URL}
     */
    String addMaterial(MediaFile.MediaType mediaType, File file);

    /**
     * 获取永久素材
     *
     * @param mediaId 要获取的素材的media_id
     * @return 素材内容json
     */
    String getMaterial(String mediaId);

    /**
     * 删除永久素材
     *
     * @param mediaId 要删除的素材的media_id
     * @return 是否删除成功
     */
    boolean deleteMaterial(String mediaId);

    /**
     * 获取素材总数
     *
     * @return {"voice_count":语音总数量,"video_count":视频总数量,"image_count":图片总数量,"news_count":图文总数量}
     */
    String getMaterialCount();

    /**
     * 批量获取素材列表
     *
     * @param mediaType 素材类型
     * @param offset    从全部素材的该偏移位置开始返回，0表示从第一个素材 返回
     * @param count     返回素材的数量，取值在1到20之间
     * @return 素材json
     */
    String batchGetMaterial(MediaFile.MediaType mediaType, int offset, int count);
    // <================= 素材管理 end =================>

    // <================= 群发接口和原创接口校验 start =================>

    /**
     * 上传图文消息内的图片获取URL【订阅号与服务号认证后均可用】
     * <p>
     * 本接口所上传的图片不占用公众号的素材库中图片数量的5000个的限制
     * 图片仅支持jpg/png格式，大小必须在1MB以下
     * </p>
     *
     * @param file 图片文件
     * @return 图片URL
     */
    String uploadImg(File file);

    /**
     * 上传图文消息素材【订阅号与服务号认证后均可用】
     *
     * @param news 图文消息
     * @return media_id
     */
    default String uploadNews(MaterialNews news) {
        Map<String, Object> reqBody = new HashMap<>(1);
        reqBody.put("articles", news);

        return uploadNews(Jackson.toJson(reqBody));
    }

    /**
     * 上传图文消息素材【订阅号与服务号认证后均可用】
     *
     * @param newsJson 图文消息json
     * @return media_id
     */
    String uploadNews(String newsJson);

    /**
     * 上传群发消息里的视频消息素材【订阅号与服务号认证后均可用】
     *
     * @param mediaId     此处media_id需通过素材管理->新增素材来得到
     * @param title       视频标题
     * @param description 视频描述
     * @return media_id
     */
    String uploadVideo(String mediaId, String title, String description);

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
    String massSendAll(String mgsJson);

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
    String massSend(String msgJson);

    /**
     * 删除群发【订阅号与服务号认证后均可用】
     *
     * @param msgId      消息发送任务的ID
     * @param articleIdx 要删除的文章在图文消息中的位置，第一篇编号为1，该字段不填或填0会删除全部文章，非必须
     * @return 是否成功
     */
    boolean massDelete(@NonNull String msgId, @Nullable String articleIdx);

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
    String massPreview(String msgJson);

    /**
     * 查询群发消息发送状态【订阅号与服务号认证后均可用】
     *
     * @param msgId 消息发送任务的ID
     * @return msg_status（消息发送后的状态，SEND_SUCCESS表示发送成功，SENDING表示发送中，SEND_FAIL表示发送失败，DELETE表示已删除）
     */
    String massGetStatus(String msgId);
    // <================= 群发接口和原创接口校验 end =================>

    /**
     * 发送订阅通知
     *
     * @param wxTemplate 消息模板
     * @return 是否发送成功
     */
    default boolean sendSubscribeMsg(WxTemplate wxTemplate) {
        return sendSubscribeMsg(wxTemplate.toString());
    }

    /**
     * 发送订阅通知
     *
     * @param msgJson 消息json
     * @return 是否发送成功
     */
    boolean sendSubscribeMsg(String msgJson);

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
     * 发送语义理解请求
     *
     * @param json 请求参数json（参考微信文档）
     * @return 语义理解结果
     */
    String semanticSearch(String json);
}
