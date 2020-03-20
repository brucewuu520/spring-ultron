package org.springultron.security.model;

import java.io.Serializable;

/**
 * security 保存的用户信息
 *
 * @author brucewuu
 * @date 2020/3/18 10:49
 */
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1314L;

    /**
     * 用户名（必需）
     */
    private String username;
    /**
     * 密码（非必需）
     */
    private String password;
    /**
     * 用户ID（必需）
     */
    private Long userId;
    /**
     * 用户手机号（非必需）
     */
    private String mobile;
    /**
     * 状态（非必需）
     * 0:禁用 1:启用
     */
    private Integer status;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
