package org.springultron.wechat.controller;

import org.springultron.wechat.msg.wx.in.*;
import org.springultron.wechat.msg.wx.in.event.InLocationEvent;
import org.springultron.wechat.msg.wx.in.event.InMassEvent;
import org.springultron.wechat.msg.wx.in.event.InQrCodeEvent;
import org.springultron.wechat.msg.wx.in.event.InUnknownEvent;

/**
 * 公众号消息接收适配器
 * <p>
 * 对 WxMsgController 部分抽象方法提供了默认实现，节省不必要的抽象方法实现
 * </p>
 *
 * @author brucewuu
 * @date 2021/4/14 下午7:54
 */
public abstract class WxMsgControllerAdapter extends WxMsgController {

    /**
     * 处理接收到的图片消息
     *
     * @param imageMsg 图片消息
     */
    @Override
    protected void processImageMsg(InImageMsg imageMsg) {
        renderDefault();
    }

    /**
     * 处理接收到的语音消息
     *
     * @param voiceMsg 语音消息
     */
    @Override
    protected void processVoiceMsg(InVoiceMsg voiceMsg) {
        renderDefault();
    }

    /**
     * 处理接收到的视频消息
     *
     * @param videoMsg 视频消息
     */
    @Override
    protected void processVideoMsg(InVideoMsg videoMsg) {
        renderDefault();
    }

    /**
     * 处理接收到的小视频消息
     *
     * @param shortVideoMsg 小视频消息
     */
    @Override
    protected void processShortVideoMsg(InShortVideoMsg shortVideoMsg) {
        renderDefault();
    }

    /**
     * 处理接收到的地址位置消息
     *
     * @param locationMsg 地址位置消息
     */
    @Override
    protected void processLocationMsg(InLocationMsg locationMsg) {
        renderDefault();
    }

    /**
     * 处理接收到的链接消息
     *
     * @param linkMsg 链接消息
     */
    @Override
    protected void processLinkMsg(InLinkMsg linkMsg) {
        renderDefault();
    }

    /**
     * 处理接收到的扫描带参数二维码事件
     *
     * @param qrCodeEvent 扫描带参数二维码事件
     */
    @Override
    protected void processQrCodeEvent(InQrCodeEvent qrCodeEvent) {
        renderDefault();
    }

    /**
     * 处理接收到的上报地理位置事件
     *
     * @param locationEvent 上报地理位置事件
     */
    @Override
    protected void processLocationEvent(InLocationEvent locationEvent) {
        renderDefault();
    }

    /**
     * 处理接收到的群发结果推送事件
     *
     * @param massEvent 群发结果推送事件
     */
    @Override
    protected void processMassEvent(InMassEvent massEvent) {
        renderDefault();
    }

    /**
     * 处理接收到的未知事件
     *
     * @param unknownEvent 未知事件
     */
    @Override
    protected void processUnknownEvent(InUnknownEvent unknownEvent) {
        renderDefault();
    }

    /**
     * 处理接收到的未知消息
     *
     * @param unknownMsg 未知消息
     */
    @Override
    protected void processUnknownMsg(InUnknownMsg unknownMsg) {
        renderDefault();
    }
}
