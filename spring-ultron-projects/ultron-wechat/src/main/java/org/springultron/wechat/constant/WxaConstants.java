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

    /**
     * 生物认证秘钥签名验证
     */
    String VERIFY_SIGNATURE = HOST + "/cgi-bin/soter/verify_signature";

    /**
     * 获取小程序码，通过该接口生成的小程序码，永久有效，有数量限制
     */
    String GET_WXA_CODE = HOST + "/wxa/getwxacode";

    /**
     * 获取小程序码，通过该接口生成的小程序码，永久有效，数量暂无限制
     */
    String GET_WXA_CODE_UNLIMITED = HOST + "/wxa/getwxacodeunlimit";

    /**
     * 创建小程序二维码
     */
    String CREATE_QR_CODE = HOST + "/cgi-bin/wxaapp/createwxaqrcode";

    /**
     * 生成小程序scheme码
     */
    String GENERATE_SCHEME = HOST + "/wxa/generatescheme";

    /**
     * 发送订阅消息
     */
    String SEND_SUBSCRIBE_MSG = HOST + "/cgi-bin/message/subscribe/send";

    /**
     * 下发小程序和公众号统一的服务消息
     */
    String SEND_UNIFORM_MSG = HOST + "/cgi-bin/message/wxopen/template/uniform_send";

    /**
     * 发送客服消息给用户
     */
    String SEND_CUSTOMER_MSG = HOST + "/cgi-bin/message/custom/send";

    /**
     * 下发客服当前输入状态给用户
     */
    String SET_CUSTOMER_TYPE = HOST + "/cgi-bin/message/custom/typing";

    /**
     * 获取客服消息内的临时素材。即下载临时的多媒体文件。目前小程序仅支持下载图片文件
     */
    String GET_TEMP_MEDIA = HOST + "/cgi-bin/media/get";

    /**
     * 上传媒体文件到微信服务器。目前仅支持图片。用于发送客服消息或被动回复用户消息
     */
    String UPLOAD_TEMP_MEDIA = HOST + "/cgi-bin/media/upload";

    /**
     * 创建被分享动态消息或私密消息的 activity_id
     */
    String CREATE_ACTIVITY_ID = HOST + "/cgi-bin/message/wxopen/activityid/create";

    /**
     * 修改被分享的动态消息
     */
    String SET_UPDATABLE_MSG = HOST + "/cgi-bin/message/wxopen/updatablemsg/send";

    /**
     * 根据提交的用户信息数据获取用户的安全等级 risk_rank，无需用户授权
     */
    String GET_USER_RISK_RANK = HOST + "/wxa/getuserriskrank";

    /**
     * 校验一张图片是否含有违法违规内容
     */
    String IMG_SEC_CHECK = HOST + "/wxa/img_sec_check";

    /**
     * 检查一段文本是否含有违法违规内容
     */
    String TEXT_SEC_CHECK = HOST + "/wxa/msg_sec_check";

    /**
     * 异步校验图片/音频是否含有违法违规内容
     */
    String MEDIA_CHECK_ASYNC = HOST + "/wxa/media_check_async";
}
