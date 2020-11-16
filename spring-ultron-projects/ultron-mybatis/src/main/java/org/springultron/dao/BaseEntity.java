package org.springultron.dao;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

/**
 * 数据库实体基类
 *
 * @author brucewuu
 * @date 2019-05-28 10:55
 */
public abstract class BaseEntity {
    /**
     * 数据库ID主键
     */
    @ApiModelProperty(value = "ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 数据创建时间
     */
    @ApiModelProperty(value = "创建时间", position = 98)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createAt;

    /**
     * 数据修改时间
     */
    @ApiModelProperty(value = "更新时间", position = 99)
    @TableField(fill = FieldFill.INSERT_UPDATE)
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
