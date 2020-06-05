package org.springultron.security.model;

import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

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
    private final String username;
    /**
     * 加密后的密码（非必需）
     */
    private String password;
    /**
     * 用户ID（必需）
     */
    private final Long userId;
    /**
     * 用户手机号（非必需）
     */
    private final String mobile;
    /**
     * 自定义参数（非必需）
     */
    private final Map<String, Object> extras;
    /**
     * 状态（非必需）
     * 0:禁用 1:启用
     */
    private final Integer status;

    UserInfo(String username, String password, Long userId, String mobile, Map<String, Object> extras, Integer status) {
        this.username = username;
        this.password = password;
        this.userId = userId;
        this.mobile = mobile;
        this.extras = extras;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Long getUserId() {
        return userId;
    }

    public String getMobile() {
        return mobile;
    }

    public Map<String, Object> getExtras() {
        return extras;
    }

    public Object getExtra(String key) {
        if (extras == null) {
            return null;
        }
        return extras.get(key);
    }

    public Integer getStatus() {
        return status;
    }

    public void eraseCredentials() {
        this.password = null;
    }

    @Override
    public boolean equals(Object rhs) {
        return rhs instanceof UserInfo && this.username.equals(((UserInfo) rhs).username);
    }

    @Override
    public int hashCode() {
        return this.username.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append(": ");
        sb.append("Username: ").append(this.username).append("; ");
        sb.append("Password: [PROTECTED]; ");
        sb.append("UserId: ").append(this.userId).append("; ");
        sb.append("Mobile: ").append(this.mobile).append("; ");
        sb.append("Status: ").append(this.status).append("; ");
        if (this.extras != null && !this.extras.isEmpty()) {
            for (Map.Entry<String, Object> entry : this.extras.entrySet()) {
                sb.append(entry.getKey()).append(entry.getValue()).append("; ");
            }
        }
        return sb.toString();
    }

    public static UserInfoBuilder builder(String username) {
        return builder().username(username);
    }

    public static UserInfoBuilder builder() {
        return new UserInfo.UserInfoBuilder();
    }

    public static class UserInfoBuilder {
        private String username;
        private String password;
        private Long userId;
        private String mobile;
        private Map<String, Object> extras;
        private Integer status;
        private Function<String, String> passwordEncoder;

        private UserInfoBuilder() {
            this.passwordEncoder = (password) -> password;
        }

        public UserInfoBuilder username(String username) {
            Assert.notNull(username, "username cannot be null");
            this.username = username;
            return this;
        }

        public UserInfoBuilder password(String password) {
            Assert.notNull(password, "password cannot be null");
            this.password = password;
            return this;
        }

        public UserInfoBuilder passwordEncoder(Function<String, String> encoder) {
            Assert.notNull(encoder, "encoder cannot be null");
            this.passwordEncoder = encoder;
            return this;
        }

        public UserInfoBuilder userId(Long userId) {
            Assert.notNull(userId, "userId cannot be null");
            this.userId = userId;
            return this;
        }

        public UserInfoBuilder mobile(String mobile) {
            Assert.notNull(userId, "mobile cannot be null");
            this.mobile = mobile;
            return this;
        }

        public UserInfoBuilder extras(Map<String, Object> extras) {
            this.extras = extras;
            return this;
        }

        public UserInfoBuilder putExtra(String key, Object value) {
            if (this.extras == null) {
                this.extras = new HashMap<>();
            }
            this.extras.put(key, value);
            return this;
        }

        public UserInfoBuilder status(Integer status) {
            this.status = status;
            return this;
        }

        public UserInfo build() {
            String encodedPassword = this.passwordEncoder.apply(this.password);
            return new UserInfo(username, password, userId, mobile, extras, status);
        }
    }
}
