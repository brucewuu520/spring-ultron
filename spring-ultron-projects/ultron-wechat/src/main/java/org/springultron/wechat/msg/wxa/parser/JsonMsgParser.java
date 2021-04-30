package org.springultron.wechat.msg.wxa.parser;

import org.springultron.core.jackson.Jackson;
import org.springultron.wechat.msg.wxa.MsgModel;
import org.springultron.wechat.msg.wxa.WxaMsg;

/**
 * json消息解析器
 *
 * @author brucewuu
 * @date 2021/4/16 下午5:56
 */
public class JsonMsgParser implements IMsgParser {
    /**
     * json消息解析
     *
     * @param msgStr json消息
     * @return WxaMsg
     */
    @Override
    public WxaMsg parser(String msgStr) {
        MsgModel msgModel = Jackson.parse(msgStr, MsgModel.class);
        return parseMsgModel(msgModel);
    }
}
