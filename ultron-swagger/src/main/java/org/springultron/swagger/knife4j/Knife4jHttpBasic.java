package org.springultron.swagger.knife4j;

/**
 * @author brucewuu
 * @date 2020/1/6 11:43
 */
public class Knife4jHttpBasic {
    /**
     * http basic是否开启,默认为false
     */
    private boolean enable = false;
    /**
     * basic 用户名
     */
    private String username;
    /**
     * basic 密码
     */
    private String password;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
