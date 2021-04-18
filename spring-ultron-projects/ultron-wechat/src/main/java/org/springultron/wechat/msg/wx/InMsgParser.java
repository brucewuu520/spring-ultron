package org.springultron.wechat.msg.wx;

import org.springultron.core.utils.StringUtils;
import org.springultron.wechat.msg.XmlHelper;
import org.springultron.wechat.msg.wx.in.*;
import org.springultron.wechat.msg.wx.in.event.*;

/**
 * 接收消息解析
 *
 * @author brucewuu
 * @date 2021/4/14 上午10:36
 */
public class InMsgParser {

    private InMsgParser() {
        throw new UnsupportedOperationException("can not be construct.");
    }

    /**
     * 从 xml 中解析出各类消息与事件
     *
     * @param xmlStr xml字符串
     * @return {InMsg}
     */
    public static InMsg parse(String xmlStr) {
        XmlHelper xmlHelper = XmlHelper.of(xmlStr);
        return doParse(xmlHelper, xmlStr);
    }

    /**
     * 消息类型
     * 1：text 文本消息
     * 2：image 图片消息
     * 3：voice 语音消息
     * 4：video 视频消息 / shortvideo 小视频消息
     * 5：location 地址位置消息
     * 6：link 链接消息
     * 7：event 事件
     */
    private static InMsg doParse(XmlHelper xmlHelper, String xmlStr) {
        final String toUserName = xmlHelper.getString("//ToUserName");
        final String fromUserName = xmlHelper.getString("//FromUserName");
        final Integer createTime = xmlHelper.getNumber("//CreateTime").intValue();
        final String msgType = xmlHelper.getString("//MsgType");

        if ("text".equals(msgType)) {
            return parseInTextMsg(xmlHelper, toUserName, fromUserName, createTime, msgType);
        }
        if ("image".equals(msgType)) {
            return parseInImageMsg(xmlHelper, toUserName, fromUserName, createTime, msgType);
        }
        if ("voice".equals(msgType)) {
            return parseInVoiceMsg(xmlHelper, toUserName, fromUserName, createTime, msgType);
        }
        if ("video".equals(msgType)) {
            return parseInVideoMsg(xmlHelper, toUserName, fromUserName, createTime, msgType);
        }
        if ("shortvideo".equals(msgType)) {
            return parseInShortVideoMsg(xmlHelper, toUserName, fromUserName, createTime, msgType);
        }
        if ("location".equals(msgType)) {
            return parseInLocationMsg(xmlHelper, toUserName, fromUserName, createTime, msgType);
        }
        if ("link".equals(msgType)) {
            return parseInLinkMsg(xmlHelper, toUserName, fromUserName, createTime, msgType);
        }
        if ("event".equals(msgType)) {
            return parseInEvent(xmlHelper, toUserName, fromUserName, createTime, msgType, xmlStr);
        }

        return parseInUnknownMsg(xmlHelper, toUserName, fromUserName, createTime, msgType, xmlStr);
    }

    private static InMsg parseInTextMsg(XmlHelper xmlHelper, String toUserName, String fromUserName, Integer createTime, String msgType) {
        InTextMsg msg = new InTextMsg(toUserName, fromUserName, createTime, msgType);
        msg.setMsgId(xmlHelper.getString("//MsgId"));
        msg.setContent(xmlHelper.getString("//Content"));
        return msg;
    }

    private static InMsg parseInImageMsg(XmlHelper xmlHelper, String toUserName, String fromUserName, Integer createTime, String msgType) {
        InImageMsg msg = new InImageMsg(toUserName, fromUserName, createTime, msgType);
        msg.setMsgId(xmlHelper.getString("//MsgId"));
        msg.setPicUrl(xmlHelper.getString("//PicUrl"));
        msg.setMediaId(xmlHelper.getString("//MediaId"));
        return msg;
    }

    private static InMsg parseInVoiceMsg(XmlHelper xmlHelper, String toUserName, String fromUserName, Integer createTime, String msgType) {
        InVoiceMsg msg = new InVoiceMsg(toUserName, fromUserName, createTime, msgType);
        msg.setMsgId(xmlHelper.getString("//MsgId"));
        msg.setMediaId(xmlHelper.getString("//MediaId"));
        msg.setFormat(xmlHelper.getString("//Format"));
        msg.setRecognition(xmlHelper.getString("//Recognition"));
        return msg;
    }

    private static InMsg parseInVideoMsg(XmlHelper xmlHelper, String toUserName, String fromUserName, Integer createTime, String msgType) {
        InVideoMsg msg = new InVideoMsg(toUserName, fromUserName, createTime, msgType);
        msg.setMediaId(xmlHelper.getString("//MediaId"));
        msg.setThumbMediaId(xmlHelper.getString("//ThumbMediaId"));
        msg.setMsgId(xmlHelper.getString("//MsgId"));
        return msg;
    }

    private static InMsg parseInShortVideoMsg(XmlHelper xmlHelper, String toUserName, String fromUserName, Integer createTime, String msgType) {
        InShortVideoMsg msg = new InShortVideoMsg(toUserName, fromUserName, createTime, msgType);
        msg.setMediaId(xmlHelper.getString("//MediaId"));
        msg.setThumbMediaId(xmlHelper.getString("//ThumbMediaId"));
        msg.setMsgId(xmlHelper.getString("//MsgId"));
        return msg;
    }

    private static InMsg parseInLocationMsg(XmlHelper xmlHelper, String toUserName, String fromUserName, Integer createTime, String msgType) {
        InLocationMsg msg = new InLocationMsg(toUserName, fromUserName, createTime, msgType);
        msg.setMsgId(xmlHelper.getString("//MsgId"));
        msg.setLatitude(xmlHelper.getString("//Location_X"));
        msg.setLongitude(xmlHelper.getString("//Location_Y"));
        msg.setScale(xmlHelper.getString("//Scale"));
        msg.setLabel(xmlHelper.getString("//Label"));
        return msg;
    }

    private static InMsg parseInLinkMsg(XmlHelper xmlHelper, String toUserName, String fromUserName, Integer createTime, String msgType) {
        InLinkMsg msg = new InLinkMsg(toUserName, fromUserName, createTime, msgType);
        msg.setMsgId(xmlHelper.getString("//MsgId"));
        msg.setTitle(xmlHelper.getString("//Title"));
        msg.setDescription(xmlHelper.getString("//Description"));
        msg.setUrl(xmlHelper.getString("//Url"));
        return msg;
    }

    private static InUnknownMsg parseInUnknownMsg(XmlHelper xmlHelper, String toUserName, String fromUserName, Integer createTime, String msgType, String xmlStr) {
        return new InUnknownMsg(toUserName, fromUserName, createTime, msgType, xmlStr);
    }

    /**
     * 解析各种事件
     */
    private static InEventMsg parseInEvent(XmlHelper xmlHelper, String toUserName, String fromUserName, Integer createTime, String msgType, String xmlStr) {
        final String event = xmlHelper.getString("//Event");
        /*
         * 取消关注事件
         * 注意：由于微信平台出现bug， unsubscribe 事件在有些时候居然多出了 eventKey 值，
         * 多出来的eventKey值例如：last_trade_no_4003942001201604205023621558
         * 所以 unsubscribe 与 subscribe 判断进行了拆分，并且将 unsubscribe
         * 挪至最前面进行判断，以便消除微信平台 bug 的影响
         */
        if ("unsubscribe".equals(event)) {
            return new InFollowEvent(toUserName, fromUserName, createTime, event);
        }

        final String eventKey = xmlHelper.getString("//EventKey");

        if ("subscribe".equals(event)) {
            if (StringUtils.isNotEmpty(eventKey) && eventKey.startsWith("qrscene_")) {
                // 扫描带参数二维码事件 1.用户未关注时，进行关注后的事件推送
                InQrCodeEvent e = new InQrCodeEvent(toUserName, fromUserName, createTime, event);
                e.setEventKey(eventKey);
                e.setTicket(xmlHelper.getString("//Ticket"));
                return e;
            } else {
                // 用户关注公众号事件
                return new InFollowEvent(toUserName, fromUserName, createTime, event);
            }
        }

        if ("SCAN".equals(event)) {
            // 扫描带参数二维码事件 2.用户已关注时的事件推送
            InQrCodeEvent e = new InQrCodeEvent(toUserName, fromUserName, createTime, event);
            e.setEventKey(eventKey);
            e.setTicket(xmlHelper.getString("//Ticket"));
            return e;
        }

        if ("LOCATION".equals(event)) {
            // 上报地理位置事件
            InLocationEvent e = new InLocationEvent(toUserName, fromUserName, createTime, event);
            e.setLatitude(xmlHelper.getString("//Latitude"));
            e.setLongitude(xmlHelper.getString("//Longitude"));
            e.setPrecision(xmlHelper.getString("//Precision"));
            return e;
        }

        if (InMenuEvent.MENU_CLICK.equals(event)) {
            // 自定义菜单事件 1.点击菜单拉取消息时的事件推送
            InMenuEvent e = new InMenuEvent(toUserName, fromUserName, createTime, event);
            e.setEventKey(eventKey);
            return e;
        }

        if (InMenuEvent.MENU_VIEW.equals(event)) {
            // 自定义菜单事件 2.点击菜单跳转链接时的事件推送
            InMenuEvent e = new InMenuEvent(toUserName, fromUserName, createTime, event);
            e.setEventKey(eventKey);
            e.setMenuId(xmlHelper.getString("//MenuId"));
            return e;
        }

        if (InMenuEvent.MENU_SCANCODE_PUSH.equals(event) || InMenuEvent.MENU_SCANCODE_WAIT_MSG.equals(event)) {
            // 自定义菜单事件 3.扫码推事件 和 4.扫码推事件且弹出“消息接收中”提示框
            InMenuEvent e = new InMenuEvent(toUserName, fromUserName, createTime, event);
            e.setEventKey(eventKey);
            String scanType = xmlHelper.getString("//ScanCodeInfo/ScanType");
            String scanResult = xmlHelper.getString("//ScanCodeInfo/ScanResult");
            e.setScanCodeInfo(new InMenuEvent.ScanCodeInfo(scanType, scanResult));
            return e;
        }

        if (InMenuEvent.MENU_PIC_SYS_PHOTO.equals(event)) {
            // 自定义菜单事件 5.pic_sysphoto：弹出系统拍照发图，这个后台其实收不到该菜单的消息，
            // 点击它后，调用的是手机里面的照相机功能，而照相以后再发过来时，就收到的是一个图片消息了
            InMenuEvent e = new InMenuEvent(toUserName, fromUserName, createTime, event);
            e.setEventKey(eventKey);
            return e;
        }

        if (InMenuEvent.MENU_PIC_PHOTO_OR_ALBUM.equals(event)) {
            // 自定义菜单事件 6.pic_photo_or_album：弹出拍照或者相册发图
            InMenuEvent e = new InMenuEvent(toUserName, fromUserName, createTime, event);
            e.setEventKey(eventKey);
            return e;
        }

        if (InMenuEvent.MENU_WX_PIC.equals(event)) {
            // 自定义菜单事件 7.pic_weixin：弹出微信相册发图器
            InMenuEvent e = new InMenuEvent(toUserName, fromUserName, createTime, event);
            e.setEventKey(eventKey);
            return e;
        }

        if (InMenuEvent.MENU_LOCATION_SELECT.equals(event)) {
            // 自定义菜单事件 8.location_select：弹出地理位置选择器
            InMenuEvent e = new InMenuEvent(toUserName, fromUserName, createTime, event);
            e.setEventKey(eventKey);
            return e;
        }

        if (InMenuEvent.MENU_MEDIA_ID.equals(event)) {
            // 自定义菜单事件 9.media_id：下发消息（除文本消息）
            InMenuEvent e = new InMenuEvent(toUserName, fromUserName, createTime, event);
            e.setEventKey(eventKey);
            return e;
        }

        if (InMenuEvent.MENU_VIEW_LIMITED.equals(event)) {
            // 自定义菜单事件 10.view_limited：跳转图文消息URL
            InMenuEvent e = new InMenuEvent(toUserName, fromUserName, createTime, event);
            e.setEventKey(eventKey);
            return e;
        }

        if (InMenuEvent.MENU_VIEW_MINIPROGRAM.equals(event)) {
            // 自定义菜单事件 11.view_miniprogram 跳转微信小程序
            InMenuEvent e = new InMenuEvent(toUserName, fromUserName, createTime, event);
            e.setEventKey(eventKey);
            e.setMenuId(xmlHelper.getString("//MenuId"));
            return e;
        }

        if ("MASSSENDJOBFINISH".equals(event)) {
            // 群发结果推送事件
            InMassEvent e = new InMassEvent(toUserName, fromUserName, createTime, event);
            e.setMsgId(xmlHelper.getString("//MsgID"));
            e.setStatus(xmlHelper.getString("//Status"));
            e.setTotalCount(xmlHelper.getString("//TotalCount"));
            e.setFilterCount(xmlHelper.getString("//FilterCount"));
            e.setSentCount(xmlHelper.getString("//SentCount"));
            e.setErrorCount(xmlHelper.getString("//ErrorCount"));
            return e;
        }

        return new InUnknownEvent(toUserName, fromUserName, createTime, event, xmlStr);
    }
}
