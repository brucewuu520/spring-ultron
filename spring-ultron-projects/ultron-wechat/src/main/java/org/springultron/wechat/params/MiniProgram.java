package org.springultron.wechat.params;

/**
 * 小程序
 *
 * @author brucewuu
 * @date 2021/4/21 下午3:19
 */
public class MiniProgram {
    /**
     * 小程序appid
     */
    private String appid;
    /**
     * 跳转小程序页面路径，可以带参数
     * page/index?foo=bar
     */
    private String pagepath;

    public MiniProgram(String appid, String pagepath) {
        this.appid = appid;
        this.pagepath = pagepath;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPagepath() {
        return pagepath;
    }

    public void setPagepath(String pagepath) {
        this.pagepath = pagepath;
    }
}
