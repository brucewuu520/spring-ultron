package org.springultron.dao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 分页查询条件
 *
 * @author brucewuu
 * @date 2019-06-07 22:09
 */
@ApiModel(description = "分页查询条件")
public class PageQuery {

    @ApiModelProperty(value = "第几页", notes = "默认值:1", example = "1")
    private Integer current;

    @ApiModelProperty(value = "数量(默认:10)", notes = "默认值:10", example = "10", position = 1)
    private Integer size;

    @ApiModelProperty(value = "升序字段", position = 2)
    private String[] ascs;

    @ApiModelProperty(value = "降序字段", position = 3)
    private String[] descs;

    @ApiModelProperty(value = "是否查询总数", position = 4)
    private boolean searchCount = true;

    @ApiModelProperty(value = "当前最后一行的ID", position = 5)
    private Long currentId;

    @ApiModelProperty(value = "搜索关键字", position = 6)
    private String keyword;

    @ApiModelProperty(value = "筛选状态", position = 7)
    private Integer status;

    @ApiModelProperty(value = "筛选类型/类别ID", position = 8)
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

    public boolean isSearchCount() {
        return searchCount;
    }

    public void setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
    }

    public Long getCurrentId() {
        return currentId;
    }

    public void setCurrentId(Long currentId) {
        this.currentId = currentId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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
