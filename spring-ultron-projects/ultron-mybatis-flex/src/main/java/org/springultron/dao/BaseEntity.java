package org.springultron.dao;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import io.swagger.v3.oas.annotations.media.Schema;

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
    @Schema(description = "ID")
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 数据创建时间
     */
    @Schema(description = "创建时间")
    @Column(onInsertValue = "now()")
    private LocalDateTime createAt;

    /**
     * 数据修改时间
     */
    @Schema(description = "更新时间")
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
