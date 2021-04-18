package org.springultron.wechat.msg.wx.out;

import org.springultron.wechat.msg.wx.in.InMsg;

import java.util.ArrayList;
import java.util.List;

/**
 * 回复图文消息
 *
 * @author brucewuu
 * @date 2021/4/15 下午6:05
 */
public class OutNewsMsg extends OutMsg {
    /**
     * 图文消息信息，注意，如果图文数超过限制，则将只发限制内的条数
     * 当用户发送文本、图片、语音、视频、图文、地理位置这六种消息时，开发者只能回复1条图文消息；其余场景最多可回复8条图文消息
     */
    private List<News> articles = new ArrayList<News>();

    public OutNewsMsg() {
        this.msgType = "news";
    }

    public OutNewsMsg(InMsg inMsg) {
        super(inMsg);
        this.msgType = "news";
    }

    @Override
    protected void customizeXml(StringBuilder sb) {
        final int articleCount = getArticleCount();
        if (articleCount == 0) {
            throw new IllegalStateException("ArticleCount can not be zero");
        }
        sb.append("<ArticleCount>").append(articleCount).append("</ArticleCount>\n");
        sb.append("<Articles>\n");
        for (News news : articles) {
            sb.append("<item>\n");
            sb.append("<Title><![CDATA[").append(nullToBlank(news.getTitle())).append("]]></Title>\n");
            sb.append("<Description><![CDATA[").append(nullToBlank(news.getDescription())).append("]]></Description>\n");
            sb.append("<PicUrl><![CDATA[").append(nullToBlank(news.getPicUrl())).append("]]></PicUrl>\n");
            sb.append("<Url><![CDATA[").append(nullToBlank(news.getUrl())).append("]]></Url>\n");
            sb.append("</item>\n");
        }
        sb.append("</Articles>\n");
    }

    public List<News> getArticles() {
        return articles;
    }

    public void setArticles(List<News> articles) {
        this.articles = articles;
    }

    public Integer getArticleCount() {
        return articles.size();
    }

    /**
     * 图文消息
     */
    public static class News {
        /**
         * 标题
         */
        private String title;
        /**
         * 描述
         */
        private String description;
        /**
         * 图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200（非必须）
         */
        private String picUrl;
        /**
         * 点击图文消息跳转链接
         */
        private String url;

        public News() {
        }

        public News(String title, String description, String picUrl, String url) {
            this.title = title;
            this.description = description;
            this.picUrl = picUrl;
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
