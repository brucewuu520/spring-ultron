package org.springultron.dao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 分页查询条件
 *
 * @author brucewuu
 * @date 2019-06-07 22:09
 */
@ApiModel(description = "分页查询条件")
public class PageQuery implements Serializable {
    private static final long serialVersionUID = -8776470243161254413L;
    /**
     * 当前页
     */
    @ApiModelProperty(value = "当前页", notes = "默认值:1", example = "1")
    private Integer current;
    /**
     * 每页多少条数据
     */
    @ApiModelProperty(value = "每页多少条数据", notes = "默认值:10", example = "10", position = 1)
    private Integer size;
    /**
     * 升序字段
     */
    @ApiModelProperty(value = "升序字段", position = 2)
    private String[] ascs;
    /**
     * 降序字段
     */
    @ApiModelProperty(value = "降序字段", position = 3)
    private String[] descs;
    /**
     * 是否查询总数
     * 默认只第1页时查询
     */
    @ApiModelProperty(value = "是否查询总数,默认只第1页时查询", position = 4)
    private Boolean isSearchCount;

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

    public Boolean getSearchCount() {
        return isSearchCount;
    }

    public void setSearchCount(Boolean searchCount) {
        isSearchCount = searchCount;
    }
}
