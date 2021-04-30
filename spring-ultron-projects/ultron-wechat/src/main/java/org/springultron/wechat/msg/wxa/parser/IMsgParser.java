package org.springultron.wechat.msg.wxa.parser;

import org.springultron.wechat.msg.wxa.*;

/**
 * 小程序消息解析器
 *
 * @author brucewuu
 * @date 2021/4/16 下午5:54
 */
public interface IMsgParser {
    /**
     * 解析MsgModel
     *
     * @param msgModel MsgModel
     * @return WxaMsg
     */
    default WxaMsg parseMsgModel(MsgModel msgModel) {
        final String msgType = msgModel.getMsgType().toLowerCase();
        if ("text".equals(msgType)) {
            return new WxaTextMsg(msgModel);
        }
        if ("image".equals(msgType)) {
            return new WxaImageMsg(msgModel);
        }
        if ("miniprogrampage".equals(msgType)) {
            return new WxaMiniProgramPageMsg(msgModel);
        }
        if ("event".equalsIgnoreCase(msgType)) {
            if ("user_enter_tempsession".equalsIgnoreCase(msgModel.getEvent())) {
                return new WxaUserEnterSessionEvent(msgModel);
            } else if ("wxa_media_check".equalsIgnoreCase(msgModel.getEvent())) {
                return new WxaMediaCheckEvent(msgModel);
            }
        }
        return new WxaUnknownMsg(msgModel);
    }

    /**
     * 小程序消息解析
     *
     * @param msgStr 消息
     * @return WxaMsg
     */
    WxaMsg parser(String msgStr);
}
