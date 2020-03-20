package org.springultron.security.model;

import java.io.Serializable;

/**
 * 角色权限实体
 *
 * @author brucewuu
 * @date 2019/10/23 18:35
 */
public class UserPermission implements Serializable {
    private static final long serialVersionUID = 9527;

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

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}