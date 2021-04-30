package org.springultron.wechat.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springultron.core.utils.DigestUtils;
import org.springultron.core.utils.StringUtils;
import org.springultron.core.utils.WebUtils;
import org.springultron.wechat.msg.MsgEncryptKit;
import org.springultron.wechat.msg.wx.InMsgParser;
import org.springultron.wechat.msg.wx.in.*;
import org.springultron.wechat.msg.wx.in.event.*;
import org.springultron.wechat.msg.wx.out.OutMsg;
import org.springultron.wechat.msg.wx.out.OutTextMsg;
import org.springultron.wechat.props.WechatProperties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * 公众号消息接收控制器
 *
 * @author brucewuu
 * @date 2021/4/14 下午2:30
 */
public abstract class WxMsgController {
    protected static final Logger log = LoggerFactory.getLogger(WxMsgController.class);

    @Autowired
    protected WechatProperties properties;

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    /**
     * 处理接收到的文本消息
     *
     * @param textMsg 文本消息
     */
    protected abstract void processTextMsg(InTextMsg textMsg);

    /**
     * 处理接收到的图片消息
     *
     * @param imageMsg 图片消息
     */
    protected abstract void processImageMsg(InImageMsg imageMsg);

    /**
     * 处理接收到的语音消息
     *
     * @param voiceMsg 语音消息
     */
    protected abstract void processVoiceMsg(InVoiceMsg voiceMsg);

    /**
     * 处理接收到的视频消息
     *
     * @param videoMsg 视频消息
     */
    protected abstract void processVideoMsg(InVideoMsg videoMsg);

    /**
     * 处理接收到的小视频消息
     *
     * @param shortVideoMsg 小视频消息
     */
    protected abstract void processShortVideoMsg(InShortVideoMsg shortVideoMsg);

    /**
     * 处理接收到的地址位置消息
     *
     * @param locationMsg 地址位置消息
     */
    protected abstract void processLocationMsg(InLocationMsg locationMsg);

    /**
     * 处理接收到的链接消息
     *
     * @param linkMsg 链接消息
     */
    protected abstract void processLinkMsg(InLinkMsg linkMsg);

    /**
     * 处理接收到的关注/取消关注事件
     *
     * @param followEvent 关注/取关事件
     */
    protected abstract void processFollowEvent(InFollowEvent followEvent);

    /**
     * 处理接收到的扫描带参数二维码事件
     *
     * @param qrCodeEvent 扫描带参数二维码事件
     */
    protected abstract void processQrCodeEvent(InQrCodeEvent qrCodeEvent);

    /**
     * 处理接收到的上报地理位置事件
     *
     * @param locationEvent 上报地理位置事件
     */
    protected abstract void processLocationEvent(InLocationEvent locationEvent);

    /**
     * 处理接收到的自定义菜单点击事件
     *
     * @param menuEvent 用户点击自定义菜单事件
     */
    protected abstract void processMenuEvent(InMenuEvent menuEvent);

    /**
     * 处理接收到的群发结果推送事件
     *
     * @param massEvent 群发结果推送事件
     */
    protected abstract void processMassEvent(InMassEvent massEvent);

    /**
     * 处理接收到的用户操作订阅通知弹窗推送事件
     *
     * @param subscribeMsgPopupEvent 用户操作订阅通知弹窗推送事件
     */
    protected abstract void processSubscribeMsgPopupEvent(InSubscribeMsgPopupEvent subscribeMsgPopupEvent);

    /**
     * 处理接收到的订阅通知发送结果推送事件
     *
     * @param subscribeMsgPopupEvent 订阅通知发送结果推送事件
     */
    protected abstract void processSubscribeMsgSentEvent(InSubscribeMsgSentEvent subscribeMsgPopupEvent);

    /**
     * 处理接收到的未知事件
     *
     * @param unknownEvent 未知事件
     */
    protected abstract void processUnknownEvent(InUnknownEvent unknownEvent);

    /**
     * 处理接收到的未知消息
     *
     * @param unknownMsg 未知消息
     */
    protected abstract void processUnknownMsg(InUnknownMsg unknownMsg);

    /**
     * 接收微信公众号推送的消息
     */
    @RequestMapping("/message/receive")
    public void receiveMessage() throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        final String echostr = request.getParameter("echostr");
        if (StringUtils.isNotEmpty(echostr)) { // 消息安全校验
            checkSignature(echostr);
        } else { // 接收消息
            String msgXml = getInMsgXml();
            if (properties.isDevMode()) {
                log.info("接收消息:\n{}", msgXml);
            }
            // 解析消息并根据消息类型分发到相应的处理方法
            InMsg msg = InMsgParser.parse(msgXml);
            if (msg instanceof InTextMsg) {
                processTextMsg((InTextMsg) msg);
            } else if (msg instanceof InImageMsg) {
                processImageMsg((InImageMsg) msg);
            } else if (msg instanceof InVoiceMsg) {
                processVoiceMsg((InVoiceMsg) msg);
            } else if (msg instanceof InVideoMsg) {
                processVideoMsg((InVideoMsg) msg);
            } else if (msg instanceof InShortVideoMsg) {
                processShortVideoMsg((InShortVideoMsg) msg);
            } else if (msg instanceof InLocationMsg) {
                processLocationMsg((InLocationMsg) msg);
            } else if (msg instanceof InLinkMsg) {
                processLinkMsg((InLinkMsg) msg);
            } else if (msg instanceof InFollowEvent) {
                processFollowEvent((InFollowEvent) msg);
            } else if (msg instanceof InQrCodeEvent) {
                processQrCodeEvent((InQrCodeEvent) msg);
            } else if (msg instanceof InLocationEvent) {
                processLocationEvent((InLocationEvent) msg);
            } else if (msg instanceof InMenuEvent) {
                processMenuEvent((InMenuEvent) msg);
            } else if (msg instanceof InMassEvent) {
                processMassEvent((InMassEvent) msg);
            } else if (msg instanceof InSubscribeMsgPopupEvent) {
                processSubscribeMsgPopupEvent((InSubscribeMsgPopupEvent) msg);
            } else if (msg instanceof InSubscribeMsgSentEvent) {
                processSubscribeMsgSentEvent((InSubscribeMsgSentEvent) msg);
            } else if (msg instanceof InUnknownEvent) {
                InUnknownEvent unknownEvent = (InUnknownEvent) msg;
                processUnknownEvent(unknownEvent);
                if (properties.isDevMode()) {
                    log.warn("无法识别的事件类型:\n{}", unknownEvent.getXmlStr());
                }
            } else if (msg instanceof InUnknownMsg) {
                InUnknownMsg unknownMsg = (InUnknownMsg) msg;
                processUnknownMsg(unknownMsg);
                if (properties.isDevMode()) {
                    log.warn("无法识别的消息类型:\n{}", unknownMsg.getXmlStr());
                }
            }
        }
    }

    /**
     * 公众号消息安全校验
     */
    private void checkSignature(String echostr) {
        final String signature = request.getParameter("signature");
        final String timestamp = request.getParameter("timestamp");
        final String nonce = request.getParameter("nonce");
        if (properties.isDevMode()) {
            log.info("公众号消息安全校验, --signature: {}", signature);
        }

        String[] strArray = new String[]{properties.getWxConf().getToken(), timestamp, nonce};
        // 排序
        Arrays.sort(strArray);
        // 拼接字符串
        StringBuilder builder = new StringBuilder();
        for (String str : strArray) {
            builder.append(str);
        }

        try (PrintWriter writer = response.getWriter()) {
            // sha1加密
            String sign = DigestUtils.sha1Hex(builder.toString());
            if (sign.equals(signature)) {
                writer.write(echostr);
            } else {
                log.error("公众号消息安全校验失败，请检查token配置: --signature: {}, --timestamp: {}, nonce: {}", signature, timestamp, nonce);
                writer.write("check signature failure");
            }
        } catch (IOException e) {
            log.error("公众号消息安全校验异常", e);
        }
    }

    /**
     * 读取xml消息
     */
    private String getInMsgXml() {
        String msgXml = readData(request);
        if (StringUtils.isEmpty(msgXml)) {
            throw new RuntimeException("请不要在浏览器中请求该连接，调试请查看: https://mp.weixin.qq.com/debug/cgi-bin/apiinfo");
        }
        // 是否需要解密消息
        if (properties.getWxConf().isEncryptMessage()) {
            msgXml = MsgEncryptKit.decryptWxMsg(properties.getWxConf(), msgXml, request.getParameter("msg_signature"), request.getParameter("timestamp"), request.getParameter("nonce"));
        }
        return msgXml;
    }

    /**
     * 读取request data
     */
    private static String readData(HttpServletRequest request) {
        try (BufferedReader br = request.getReader()) {
            String line = br.readLine();
            if (line == null) {
                return "";
            }

            StringBuilder data = new StringBuilder(512);
            data.append(line);

            while ((line = br.readLine()) != null) {
                data.append('\n').append(line);
            }

            return data.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 在接收到微信服务器的 InMsg 消息后后响应 OutMsg 消息
     *
     * @param outMsg 输出对象
     */
    public void renderOutMsg(OutMsg outMsg) {
        String outMsgXml = outMsg.toXml();

        // 开发模式向控制台输出即将发送的 OutMsg 消息的 xml 内容
        if (properties.isDevMode()) {
            log.info("发送消息:\n {}", outMsgXml);
        }

        // 是否需要加密消息
        if (properties.getWxConf().isEncryptMessage()) {
            outMsgXml = MsgEncryptKit.encryptWxMsg(properties.getWxConf(), outMsgXml, request.getParameter("timestamp"), request.getParameter("nonce"));
        }

        WebUtils.renderText(response, outMsgXml);
    }

    /**
     * 回复文本消息
     *
     * @param content 输出的消息
     */
    public void renderOutTextMsg(InMsg inMsg, String content) {
        OutTextMsg outMsg = new OutTextMsg(inMsg);
        outMsg.setContent(content);
        renderOutMsg(outMsg);
    }

    /**
     * 方便没有使用的api返回“success”避免出现向用户下发系统提示：“该公众号暂时无法提供服务，请稍后再试”
     * <p>
     * 可重写该方法
     * </p>
     */
    protected void renderDefault() {
        WebUtils.renderText(response, "success");
    }
}
