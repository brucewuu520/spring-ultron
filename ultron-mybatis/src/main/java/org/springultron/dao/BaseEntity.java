package org.springultron.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

/**
 * 数据库操作实体基类
 *
 * @Auther: brucewuu
 * @Date: 2019-05-28 10:55
 * @Description:
 */
public abstract class BaseEntity {
    /**
     * 数据库ID主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 数据创建时间
     */
    private LocalDateTime createAt;
    /**
     * 数据修改时间
     */
//    @TableField(fill = FieldFill.UPDATE)
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
