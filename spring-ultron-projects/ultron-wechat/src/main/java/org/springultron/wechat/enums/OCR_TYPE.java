package org.springultron.wechat.enums;

/**
 * OCR识别类型枚举
 *
 * @author brucewuu
 * @date 2021/4/20 下午7:04
 */
public enum OCR_TYPE {
    /**
     * 银行卡 OCR 识别
     */
    BANK_CARD("https://api.weixin.qq.com/cv/ocr/bankcard"),
    /**
     * 营业执照 OCR 识别
     */
    BUSINESS_LICENSE("https://api.weixin.qq.com/cv/ocr/bizlicense"),
    /**
     * 驾驶证 OCR 识别
     */
    DRIVER_LICENSE("https://api.weixin.qq.com/cv/ocr/drivinglicense"),
    /**
     * 身份证 OCR 识别
     */
    ID_CARD("https://api.weixin.qq.com/cv/ocr/idcard"),
    /**
     * 通用印刷体 OCR 识别
     */
    PRINTED_TEXT("https://api.weixin.qq.com/cv/ocr/comm"),
    /**
     * 行驶证 OCR 识别
     */
    VEHICLE_LICENSE("https://api.weixin.qq.com/cv/ocr/driving");

    /**
     * 接口地址
     */
    private final String url;

    OCR_TYPE(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
