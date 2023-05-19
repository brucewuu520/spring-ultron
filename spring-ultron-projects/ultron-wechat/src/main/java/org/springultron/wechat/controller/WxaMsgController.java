package org.springultron.wechat.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springultron.core.utils.DigestUtils;
import org.springultron.core.utils.StringUtils;
import org.springultron.core.utils.WebUtils;
import org.springultron.wechat.msg.MsgEncryptKit;
import org.springultron.wechat.msg.wxa.*;
import org.springultron.wechat.props.WechatProperties;
import org.springultron.wechat.props.WxaConf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * 小程序消息接收控制器
 *
 * @author brucewuu
 * @date 2021/4/14 下午4:10
 */
public abstract class WxaMsgController {
    protected static final Logger log = LoggerFactory.getLogger(WxaMsgController.class);

    @Autowired
    protected WechatProperties properties;

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    /**
     * 处理接收到的文本消息
     * 用户在客服会话中发送文本消息时将产生如下数据包
     *
     * @param textMsg 文本消息
     */
    protected abstract void processTextMsg(WxaTextMsg textMsg);

    /**
     * 处理接收到的图片消息
     * 用户在客服会话中发送图片消息时将产生如下数据包
     *
     * @param imageMsg 图片消息
     */
    protected abstract void processImageMsg(WxaImageMsg imageMsg);

    /**
     * 处理接收到小程序卡片消息
     * 用户在客服会话中发送小程序卡片消息时将产生如下数据包
     *
     * @param miniProgramPageMsg 小程序卡片消息
     */
    protected abstract void processMiniProgramPageMsg(WxaMiniProgramPageMsg miniProgramPageMsg);

    /**
     * 处理接收到的进入会话事件
     * 用户在小程序“客服会话按钮”进入客服会话时将产生如下数据包
     *
     * @param userEnterSessionEvent 进入会话事件
     */
    protected abstract void processUserEnterSessionMsg(WxaUserEnterSessionEvent userEnterSessionEvent);

    /**
     * 校验图片/音频是否含有违法违规内容异步检测结果
     *
     * @param mediaCheckEvent 异步检测结果事件
     */
    protected abstract void processMediaCheckEvent(WxaMediaCheckEvent mediaCheckEvent);

    /**
     * 处理未知消息
     *
     * @param unknownMsg 未知消息
     */
    protected abstract void processUnknownMsg(WxaUnknownMsg unknownMsg);

    /**
     * 接收微信小程序推送的消息
     */
    @RequestMapping("/wxa/message/receive")
    public void receiveMessage() throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        final String echostr = request.getParameter("echostr");
        if (StringUtils.isNotEmpty(echostr)) { // 消息安全校验
            checkSignature(echostr);
        } else { // 接收消息
            String msgStr = getWxaMsgStr();
            if (properties.isDevMode()) {
                log.info("接收消息:\n{}", msgStr);
            }

            WxaMsg wxaMsg = WxaMsgParser.parseWxaMsg(msgStr, properties.getWxaConf().getMsgType());
            if (wxaMsg instanceof WxaTextMsg) {
                processTextMsg((WxaTextMsg) wxaMsg);
            } else if (wxaMsg instanceof WxaImageMsg) {
                processImageMsg((WxaImageMsg) wxaMsg);
            } else if (wxaMsg instanceof WxaMiniProgramPageMsg) {
                processMiniProgramPageMsg((WxaMiniProgramPageMsg) wxaMsg);
            } else if (wxaMsg instanceof WxaUserEnterSessionEvent) {
                processUserEnterSessionMsg((WxaUserEnterSessionEvent) wxaMsg);
            } else if (wxaMsg instanceof WxaMediaCheckEvent) {
                processMediaCheckEvent((WxaMediaCheckEvent) wxaMsg);
            } else if (wxaMsg instanceof WxaUnknownMsg unknownMsg) {
                unknownMsg.setMsgStr(msgStr);
                processUnknownMsg(unknownMsg);
            }

            // 直接回复success（推荐方式），避免出现向用户下发系统提示：“该小程序客服暂时无法提供服务，请稍后再试”
            WebUtils.renderText(response, "success");
        }
    }

    /**
     * 小程序消息安全校验
     */
    private void checkSignature(String echostr) {
        final String signature = request.getParameter("signature");
        final String timestamp = request.getParameter("timestamp");
        final String nonce = request.getParameter("nonce");
        if (properties.isDevMode()) {
            log.info("小程序消息安全校验, --signature: {}", signature);
        }

        String[] strArray = new String[]{properties.getWxaConf().getToken(), timestamp, nonce};
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
                log.error("小程序消息安全校验失败，请检查token配置: --signature: {}, --timestamp: {}, nonce: {}", signature, timestamp, nonce);
                writer.write("check signature failure");
            }
        } catch (IOException e) {
            log.error("小程序消息安全校验异常", e);
        }
    }

    private String getWxaMsgStr() {
        String msgStr = readData(request);

        if (StringUtils.isEmpty(msgStr)) {
            throw new RuntimeException("请不要在浏览器中请求该连接，调试请查看: https://mp.weixin.qq.com/debug/cgi-bin/apiinfo");
        }

        final WxaConf wxaConf = properties.getWxaConf();
        if (wxaConf.isEncryptMessage()) {
            if (properties.isDevMode()) {
                log.info("接收原始消息:\n{}", msgStr);
            }
            msgStr = MsgEncryptKit.decryptWxaMsg(wxaConf, msgStr, request.getParameter("msg_signature"), request.getParameter("timestamp"), request.getParameter("nonce"));
        }
        return msgStr;
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
}
