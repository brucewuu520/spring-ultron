package org.springultron.dao;

import java.io.Serializable;

/**
 * 分页查询条件
 *
 * @Auther: brucewuu
 * @Date: 2019-06-07 22:09
 * @Description:
 */
public class Query implements Serializable {
    private static final long serialVersionUID = -8776470243161254413L;

    /**
     * 当前页
     */
    private Integer current;
    /**
     * 每页多少条数据
     */
    private Integer size;
    /**
     * 升序字段
     */
    private String[] ascs;
    /**
     * 降序字段
     */
    private String[] descs;

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String[] getAscs() {
        return ascs;
    }

    public void setAscs(String[] ascs) {
        this.ascs = ascs;
    }

    public String[] getDescs() {
        return descs;
    }

    public void setDescs(String[] descs) {
        this.descs = descs;
    }
}
