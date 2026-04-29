package org.springultron.dao;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

/**
 * 数据库实体基类
 *
 * @author brucewuu
 * @date 2026-04-27 14:10
 */
public abstract class BaseEntity {
    /**
     * 数据库ID主键
     */
    @ApiModelProperty(value = "ID")
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 数据创建时间
     */
    @ApiModelProperty(value = "创建时间", position = 98)
    @Column(onInsertValue = "now()")
    private LocalDateTime createAt;

    /**
     * 数据修改时间
     */
    @ApiModelProperty(value = "更新时间", position = 99)
    @Column(onInsertValue = "now()", onUpdateValue = "now()")
    private LocalDateTime updateAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
}
