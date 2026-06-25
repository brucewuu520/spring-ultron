package org.springultron.dao;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 分页查询条件
 *
 * @author brucewuu
 * @date 2019-06-07 22:09
 */
@Schema(description = "分页查询条件")
public class PageQuery {

    @Schema(description = "第几页(默认:1)", defaultValue = "1")
    private Integer current;

    @Schema(description = "数量(默认:10)", example = "10", defaultValue = "10")
    private Integer size;

    @Schema(description = "升序字段")
    private String[] ascs;

    @Schema(description = "降序字段")
    private String[] descs;

    @Schema(description = "是否查询总数")
    private boolean searchCount = true;

    @Schema(description = "最后一行数据ID")
    private Long lastId;

    @Schema(description = "搜索关键字")
    private String keyword;

    @Schema(description = "筛选状态")
    private Integer status;

    @Schema(description = "筛选状态2")
    private Integer state;

    @Schema(description = "筛选类型/类别ID")
    private Integer typeId;

    @Schema(description = "筛选类型/类别名称")
    private String typeName;

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

    public Long getLastId() {
        return lastId;
    }

    public void setLastId(Long lastId) {
        this.lastId = lastId;
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
