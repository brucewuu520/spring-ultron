/**
 * 对公众平台发送给公众账号的消息加解密示例代码.
 *
 * @copyright Copyright (c) 1998-2014 Tencent Inc.
 */

// ------------------------------------------------------------------------

package org.springultron.wechat.encrypt;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

/**
 * XMLParse class
 * <p>
 * 提供提取消息格式中的密文及生成回复消息格式的接口.
 */
class XMLParse {

    /**
     * 提取出xml数据包中的加密消息
     *
     * @param xmlStr 待提取的xml字符串
     * @return 提取出的加密消息字符串
     * @throws AesException AesException
     */
    static Object[] extract(String xmlStr) throws AesException {
        Object[] result = new Object[3];
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            StringReader sr = new StringReader(xmlStr);
            InputSource is = new InputSource(sr);
            Document document = db.parse(is);

            Element root = document.getDocumentElement();
            NodeList nodeList1 = root.getElementsByTagName("Encrypt");
            NodeList nodeList2 = root.getElementsByTagName("ToUserName");
            result[0] = 0;
            result[1] = nodeList1.item(0).getTextContent();
            result[2] = nodeList2.item(0).getTextContent();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new AesException(AesException.ParseXmlError);
        }
    }

    /**
     * 生成xml消息
     *
     * @param encrypt   加密后的消息密文
     * @param signature 安全签名
     * @param timestamp 时间戳
     * @param nonce     随机字符串
     * @return 生成的xml字符串
     */
    static String generate(String encrypt, String signature, String timestamp, String nonce) {
        //noinspection StringBufferReplaceableByString
        return new StringBuilder()
                .append("<xml>\n")
                .append("<Encrypt><![CDATA[").append(encrypt).append("]]></Encrypt>\n")
                .append("<MsgSignature><![CDATA[").append(signature).append("]]></MsgSignature>\n")
                .append("<TimeStamp>").append(timestamp).append("</TimeStamp>\n")
                .append("<Nonce><![CDATA[").append(nonce).append("]]></Nonce>\n")
                .append("</xml>")
                .toString();
    }
}
