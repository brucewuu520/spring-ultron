package org.springultron.wechat.dto;

/**
 * 微信图文消息
 *
 * @author brucewuu
 * @date 2021/4/21 下午7:48
 */
public class MaterialNews {
    /**
     * 标题
     */
    private String title;
    /**
     * 图文消息的封面图片素材id（必须是永久mediaId）
     */
    private String thumb_media_id;
    /**
     * 作者（非必须）
     */
    private String author;
    /**
     * 图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空
     * 如果本字段为没有填写，则默认抓取正文前54个字
     */
    private String digest;
    /**
     * 是否显示封面，0为false，即不显示，1为true，即显示
     */
    private boolean show_cover_pic;
    /**
     * 图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS,
     * 涉及图片url必须来源 "上传图文消息内的图片获取URL"接口获取。外部图片url将被过滤
     */
    private String content;
    /**
     * 图文消息的原文地址，即点击“阅读原文”后的URL
     */
    private String content_source_url;
    /**
     * 是否打开评论（非必须）
     * 0不打开，1打开
     */
    private Integer need_open_comment;
    /**
     * 是否粉丝才可评论（非必须）
     * 0所有人可评论，1粉丝才可评论
     */
    private Integer only_fans_can_comment;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumb_media_id() {
        return thumb_media_id;
    }

    public void setThumb_media_id(String thumb_media_id) {
        this.thumb_media_id = thumb_media_id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public boolean isShow_cover_pic() {
        return show_cover_pic;
    }

    public void setShow_cover_pic(boolean show_cover_pic) {
        this.show_cover_pic = show_cover_pic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent_source_url() {
        return content_source_url;
    }

    public void setContent_source_url(String content_source_url) {
        this.content_source_url = content_source_url;
    }

    public Integer getNeed_open_comment() {
        return need_open_comment;
    }

    public void setNeed_open_comment(Integer need_open_comment) {
        this.need_open_comment = need_open_comment;
    }

    public Integer getOnly_fans_can_comment() {
        return only_fans_can_comment;
    }

    public void setOnly_fans_can_comment(Integer only_fans_can_comment) {
        this.only_fans_can_comment = only_fans_can_comment;
    }
}
