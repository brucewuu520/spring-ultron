package org.springultron.wechat.dto;

import java.io.BufferedInputStream;

/**
 * 素材文件
 *
 * @author brucewuu
 * @date 2021/4/21 下午5:24
 */
public class MediaFile {
    /**
     * 文件缓冲流
     */
    private BufferedInputStream stream;
    /**
     * 文件类型
     */
    private String contentType;
    /**
     * 文件大小
     */
    private Long contentLength;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件全名（包括扩展名）
     */
    private String fullName;
    /**
     * 文件后缀
     */
    private String suffix;
    /**
     * 异常信息
     */
    private String error;

    public BufferedInputStream getStream() {
        return stream;
    }

    public void setStream(BufferedInputStream stream) {
        this.stream = stream;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getContentLength() {
        return contentLength;
    }

    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    /**
     * 素材格式
     * <p>
     * 图片（image）
     * 语音（voice）
     * 视频（video）
     * 缩略图（thumb）
     * </p>
     */
    public enum MediaType {
        /**
         * 图片
         */
        IMAGE,
        /**
         * 语音
         */
        VOICE,
        /**
         * 视频
         */
        VIDEO,
        /**
         * 缩略图
         */
        THUMB;

        /**
         * 转换为小写
         */
        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

}
