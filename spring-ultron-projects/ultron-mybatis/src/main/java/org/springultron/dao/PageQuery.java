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

    @ApiModelProperty(value = "当前最后一行的ID", position = 5)
    private Long currentId;

    @ApiModelProperty(value = "查询关键词", position = 6)
    private String keywords;

    @ApiModelProperty(value = "筛选状态", position = 7)
    private Integer status;

    @ApiModelProperty(value = "筛选类型/类别ID", position = 7)
    private Integer typeId;

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

    public Long getCurrentId() {
        return currentId;
    }

    public void setCurrentId(Long currentId) {
        this.currentId = currentId;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }
}
