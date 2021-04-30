package org.springultron.dao;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * 日期字段自动填充
 * 实现数据库插入/更新操作自动填充日期字段
 *
 * @author brucewuu
 * @date 2019-06-28 22:46
 */
public class UltronMetaObjectHandler implements MetaObjectHandler {

    private final UltronMybatisPlusProperties.AutoFill autoFill;

    public UltronMetaObjectHandler(UltronMybatisPlusProperties properties) {
        this.autoFill = properties.getAutoFill();
    }

    @Override
    public boolean openInsertFill() {
        return autoFill.isEnableInsertFill();
    }

    @Override
    public boolean openUpdateFill() {
        return autoFill.isEnableUpdateFill();
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        Object createAt = this.getFieldValByName(autoFill.getCreateAtField(), metaObject);
        Object updateAt = this.getFieldValByName(autoFill.getUpdateAtField(), metaObject);
        if (createAt == null || updateAt == null) {
            LocalDateTime now = LocalDateTime.now();
            if (createAt == null) {
                this.strictInsertFill(metaObject, autoFill.getCreateAtField(), LocalDateTime.class, now);
            }
            if (updateAt == null) {
                this.strictInsertFill(metaObject, autoFill.getUpdateAtField(), LocalDateTime.class, now);
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object updateAt = this.getFieldValByName(autoFill.getUpdateAtField(), metaObject);
        if (updateAt == null) {
            this.strictUpdateFill(metaObject, autoFill.getUpdateAtField(), LocalDateTime.class, LocalDateTime.now());
        }
    }
}