package org.springultron.wechat.msg.wxa;

import org.springultron.wechat.msg.wxa.parser.IMsgParser;
import org.springultron.wechat.msg.wxa.parser.JsonMsgParser;
import org.springultron.wechat.msg.wxa.parser.XmlMsgParser;
import org.springultron.wechat.props.WxaConf;

/**
 * 小程序消息解析
 *
 * @author brucewuu
 * @date 2021/4/16 下午7:30
 */
public class WxaMsgParser {
    static IMsgParser XML_MSG_PARSER = new XmlMsgParser();
    static IMsgParser JSON_MSG_PARSER = new JsonMsgParser();

    public static WxaMsg parseWxaMsg(String msgStr, WxaConf.MsgType msgType) {
        if (msgType == WxaConf.MsgType.XML) {
            return XML_MSG_PARSER.parser(msgStr);
        } else {
            return JSON_MSG_PARSER.parser(msgStr);
        }
    }

}
