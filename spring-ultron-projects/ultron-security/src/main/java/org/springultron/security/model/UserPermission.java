package org.springultron.security.model;

import java.io.Serial;
import java.io.Serializable;

/**
 * 角色权限实体
 *
 * @author brucewuu
 * @date 2019/10/23 18:35
 */
public class UserPermission implements Serializable {
    @Serial
    private static final long serialVersionUID = 521;
    /**
     * 父级ID
     */
    private Long pid;
    /**
     * ID
     */
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 值
     */
    private String value;
    /**
     * 类型
     * 0:角色 1:权限
     * 默认是 0:角色
     */
    private Integer type;

    public Long getPid() {
        return pid;
    }

    public UserPermission setPid(Long pid) {
        this.pid = pid;
        return this;
    }

    public Long getId() {
        return id;
    }

    public UserPermission setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserPermission setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public UserPermission setValue(String value) {
        this.value = value;
        return this;
    }

    public Integer getType() {
        return type;
    }

    public UserPermission setType(Integer type) {
        this.type = type;
        return this;
    }
}