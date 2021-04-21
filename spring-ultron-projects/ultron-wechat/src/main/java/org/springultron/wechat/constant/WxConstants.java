package org.springultron.wechat.constant;

/**
 * 公众号常量
 *
 * @author brucewuu
 * @date 2021/4/12 上午10:13
 */
public interface WxConstants {
    /**
     * host
     */
    String HOST = "https://api.weixin.qq.com";

    /**
     * 获取 access_token
     */
    String ACCESS_TOKEN = HOST + "/cgi-bin/token";

    /**
     * 公众号 access_token 缓存key
     */
    String WX_TOKEN_CACHE_KEY = "WX:ACCESS:TOKEN";

    /**
     * 获取微信API接口 IP地址
     */
    String API_DOMAIN_IP = HOST + "/cgi-bin/get_api_domain_ip";

    /**
     * 获取微信callback IP地址
     */
    String CALLBACK_IP = HOST + "/cgi-bin/getcallbackip";

    /**
     * 获取帐号的关注者列表
     */
    String USER_LIST = HOST + "/cgi-bin/user/get";

    /**
     * 获取用户基本信息（包括UnionID机制）
     */
    String USER_INFO = HOST + "/cgi-bin/user/info";

    /**
     * 批量获取用户基本信息
     */
    String BATCH_USER_INFO = HOST + "/cgi-bin/user/info/batchget";

    /**
     * 对指定用户设置备注名
     */
    String UPDATE_USER_REMARK = HOST + "/user/info/updateremark";

    /**
     * 通过code换取网页授权access_token
     */
    String SNS_OAUTH2_TOKEN = HOST + "/sns/oauth2/access_token";

    /**
     * 刷新取网页授权access_token
     */
    String SNS_OAUTH2_REFRESH_TOKEN = HOST + "/sns/oauth2/refresh_token";

    /**
     * 网页授权拉取用户信息(需scope为 snsapi_userinfo)
     */
    String SNS_USER_INFO = HOST + "/sns/userinfo";

    /**
     * 检验授权凭证（access_token）是否有效
     */
    String SNS_CHECK_AUTH = HOST + "/sns/auth";

    /**
     * 获取jsapi_ticket
     */
    String JSAPI_TICKET = HOST + "/cgi-bin/ticket/getticket";

    /**
     * jsapi_ticket 缓存key
     */
    String JSAPI_TICKET_CACHE_KEY = "WX:JSAPI:TICKET";

    /**
     * 微信卡券 api_ticket 缓存key
     */
    String CARD_API_TICKET_CACHE_KEY = "WX:CARD:API:TICKET";

    /**
     * 公众号二维码创建
     */
    String CREATE_QRCODE = HOST + "/cgi-bin/qrcode/create";

    /**
     * 查询当前菜单配置（包括默认菜单和全部个性化菜单信息）
     */
    String MENU_INFO = HOST + "/cgi-bin/get_current_selfmenu_info";

    /**
     * 创建自定义菜单
     */
    String CREATE_MENU = HOST + "/cgi-bin/menu/create";

    /**
     * 删除所有自定义菜单（包括默认菜单和全部个性化菜单）
     */
    String DELETE_MENU = HOST + "/cgi-bin/menu/delete";

    /**
     * 创建个性化菜单
     */
    String CREATE_CONDITIONAL_MENU = HOST + "/cgi-bin/menu/addconditional";

    /**
     * 删除个性化菜单
     */
    String DELETE_CONDITIONAL_MENU = HOST + "/cgi-bin/menu/delconditional";

    /**
     * 测试个性化菜单匹配结果
     */
    String TRY_MATCH_CONDITIONAL_MENU = HOST + "/cgi-bin/menu/trymatch";

    /**
     * 新增临时素材
     */
    String UPLOAD_MEDIA = HOST + "/cgi-bin/media/upload";

    /**
     * 获取临时素材
     */
    String GET_MEDIA = HOST + "/cgi-bin/media/get";

    /**
     * 获取高清语音素材
     */
    String GET_JS_SDK_MEDIA = HOST + "/cgi-bin/media/get/jssdk";

    /**
     * 新增永久图文素材
     */
    String ADD_NEWS = HOST + "/cgi-bin/material/add_news";

    /**
     * 修改永久图文素材
     */
    String UPDATE_NEWS = HOST + "/cgi-bin/material/update_news";

    /**
     * 新增其他类型永久素材
     */
    String ADD_MATERIAL = HOST + "/cgi-bin/material/add_material";

    /**
     * 获取永久素材
     */
    String GET_MATERIAL = HOST + "/cgi-bin/material/get_material";

    /**
     * 删除永久素材
     */
    String DELETE_MATERIAL = HOST + "/cgi-bin/material/del_material";

    /**
     * 获取素材总数
     */
    String GET_MATERIAL_COUNT = HOST + "/cgi-bin/material/get_materialcount";

    /**
     * 批量获取素材列表
     */
    String BATCH_GET_MATERIAL = HOST + "/cgi-bin/material/batchget_material";

    /**
     * 上传图文消息内的图片获取URL【订阅号与服务号认证后均可用】
     */
    String MEDIA_UPLOAD_IMG = HOST + "/cgi-bin/media/uploadimg";

    /**
     * 上传图文消息素材【订阅号与服务号认证后均可用】
     */
    String MEDIA_UPLOAD_NEWS = HOST + "/cgi-bin/media/uploadnews";

    /**
     * 上传视频消息素材【订阅号与服务号认证后均可用】
     */
    String MEDIA_UPLOAD_VIDEO = HOST + "/cgi-bin/media/uploadvideo";

    /**
     * 根据标签进行群发【订阅号与服务号认证后均可用】
     */
    String MASS_MSG_SEND_ALL = HOST + "/cgi-bin/message/mass/sendall";

    /**
     * 根据OpenID列表群发【订阅号不可用，服务号认证后可用】
     */
    String MASS_MSG_SEND = HOST + "/cgi-bin/message/mass/send";

    /**
     * 删除群发【订阅号与服务号认证后均可用】
     */
    String MASS_MSG_DELETE = HOST + "/cgi-bin/message/mass/delete";

    /**
     * 群发消息预览接口【订阅号与服务号认证后均可用】
     */
    String MASS_PREVIEW = HOST + "/cgi-bin/message/mass/preview";

    /**
     * 查询群发消息发送状态【订阅号与服务号认证后均可用】
     */
    String MASS_GET_STATUS = HOST + "/cgi-bin/message/mass/get";

    /**
     * 发送订阅通知
     */
    String SEND_SUBSCRIBE_MSG = "/cgi-bin/message/subscribe/bizsend";

    /**
     * 语义理解
     */
    String SEMANTIC_URL = HOST + "/semantic/semproxy/search";
}
