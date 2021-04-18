package org.springultron.wechat.msg;

import org.springultron.core.jackson.Jackson;
import org.springultron.wechat.encrypt.AesException;
import org.springultron.wechat.encrypt.WXBizMsgCrypt;
import org.springultron.wechat.props.WxConf;
import org.springultron.wechat.props.WxaConf;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

/**
 * 消息加解密工具类
 *
 * @author brucewuu
 * @date 2021/4/14 下午5:26
 */
public class MsgEncryptKit {
    private static final String XML_FORMATTER = "<xml><ToUserName><![CDATA[toUser]]></ToUserName><Encrypt><![CDATA[%1$s]]></Encrypt></xml>";

    /**
     * 加密公众号消息
     *
     * @param wxConf    公众号配置
     * @param replyMsg  公众平台待回复用户的消息，xml格式的字符串
     * @param timestamp 时间戳，可以自己生成，也可以用URL参数的timestamp
     * @param nonce     随机串，可以自己生成，也可以用URL参数的nonce
     * @return encryptWxMsg
     */
    public static String encryptWxMsg(WxConf wxConf, String replyMsg, String timestamp, String nonce) {
        try {
            return encryptMsg(wxConf.getAppId(), wxConf.getToken(), wxConf.getEncodingAesKey(), replyMsg, timestamp, nonce);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解密公众号消息
     *
     * @param wxConf       公众号配置
     * @param encryptedMsg 密文，对应POST请求的数据
     * @param msgSignature 签名串，对应URL参数的msg_signature
     * @param timestamp    时间戳，对应URL参数的timestamp
     * @param nonce        随机串，对应URL参数的nonce
     * @return decryptWxMsg
     */
    public static String decryptWxMsg(WxConf wxConf, String encryptedMsg, String msgSignature, String timestamp, String nonce) {
        try {
            return decryptMsg(wxConf.getAppId(), wxConf.getToken(), wxConf.getEncodingAesKey(), encryptedMsg, msgSignature, timestamp, nonce);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加密小程序消息
     *
     * @param wxaConf   小程序配置
     * @param replyMsg  公众平台待回复用户的消息，xml格式的字符串
     * @param timestamp 时间戳，可以自己生成，也可以用URL参数的timestamp
     * @param nonce     随机串，可以自己生成，也可以用URL参数的nonce
     * @return encryptWxaMsg
     */
    public static String encryptWxaMsg(WxaConf wxaConf, String replyMsg, String timestamp, String nonce) {
        try {
            return encryptMsg(wxaConf.getAppId(), wxaConf.getToken(), wxaConf.getEncodingAesKey(), replyMsg, timestamp, nonce);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解密小程序消息
     *
     * @param wxaConf      小程序配置
     * @param encryptedMsg 密文，对应POST请求的数据
     * @param msgSignature 签名串，对应URL参数的msg_signature
     * @param timestamp    时间戳，对应URL参数的timestamp
     * @param nonce        随机串，对应URL参数的nonce
     * @return decryptWxaMsg
     */
    public static String decryptWxaMsg(WxaConf wxaConf, String encryptedMsg, String msgSignature, String timestamp, String nonce) {
        try {
            if (wxaConf.getMsgType() == WxaConf.MsgType.XML) { // xml消息
                return decryptMsg(wxaConf.getAppId(), wxaConf.getToken(), wxaConf.getEncodingAesKey(), encryptedMsg, msgSignature, timestamp, nonce);
            } else { // json消息
                String encryptedJsonMsg = Jackson.readTree(encryptedMsg).get("Encrypt").asText();
                WXBizMsgCrypt pc = new WXBizMsgCrypt(wxaConf.getAppId(), wxaConf.getToken(), wxaConf.getEncodingAesKey());
                // 此处 timestamp 如果与加密前的不同则报签名不正确的异常
                return pc.decryptJsonMsg(encryptedJsonMsg, msgSignature, timestamp, nonce);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加密消息
     *
     * @param appId          公众平台appId
     * @param token          公众平台上，开发者设置的token
     * @param encodingAesKey 公众平台上，开发者设置的EncodingAESKey
     * @param replyMsg       公众平台待回复用户的消息，xml格式的字符串
     * @param timestamp      时间戳，可以自己生成，也可以用URL参数的timestamp
     * @param nonce          随机串，可以自己生成，也可以用URL参数的nonce
     * @return encryptMsg
     * @throws AesException AesException
     */
    private static String encryptMsg(String appId, String token, String encodingAesKey, String replyMsg, String timestamp, String nonce) throws AesException {
        WXBizMsgCrypt pc = new WXBizMsgCrypt(appId, token, encodingAesKey);
        return pc.encryptMsg(replyMsg, timestamp, nonce);
    }

    /**
     * 解密消息
     *
     * @param appId          公众平台appId
     * @param token          公众平台上，开发者设置的token
     * @param encodingAesKey 公众平台上，开发者设置的EncodingAESKey
     * @param encryptedMsg   密文，对应POST请求的数据
     * @param msgSignature   签名串，对应URL参数的msg_signature
     * @param timestamp      时间戳，可以自己生成，也可以用URL参数的timestamp
     * @param nonce          随机串，可以自己生成，也可以用URL参数的nonce
     * @return decryptMsg
     * @throws ParserConfigurationException ParserConfigurationException
     * @throws SAXException                 SAXException
     * @throws IOException                  IOException
     * @throws AesException                 AesException
     */
    private static String decryptMsg(String appId, String token, String encodingAesKey, String encryptedMsg, String msgSignature, String timestamp, String nonce) throws ParserConfigurationException, SAXException, IOException, AesException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        StringReader sr = new StringReader(encryptedMsg);
        InputSource is = new InputSource(sr);
        Document document = db.parse(is);

        Element root = document.getDocumentElement();
        NodeList nodeList1 = root.getElementsByTagName("Encrypt");
        String encrypt = nodeList1.item(0).getTextContent();
        String fromXML = String.format(XML_FORMATTER, encrypt);

        if (encodingAesKey == null) {
            throw new IllegalStateException("encodingAesKey can not be null, config encodingAesKey first.");
        }

        WXBizMsgCrypt pc = new WXBizMsgCrypt(appId, token, encodingAesKey);
        // 此处 timestamp 如果与加密前的不同则报签名不正确的异常
        return pc.decryptXmlMsg(fromXML, msgSignature, timestamp, nonce);
    }
}
