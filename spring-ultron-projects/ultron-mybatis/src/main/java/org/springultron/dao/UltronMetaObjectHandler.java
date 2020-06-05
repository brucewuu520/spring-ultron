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

    private final MybatisPlusAutoFillProperties properties;

    public UltronMetaObjectHandler(MybatisPlusAutoFillProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean openInsertFill() {
        return properties.isEnableInsertFill();
    }

    @Override
    public boolean openUpdateFill() {
        return properties.isEnableUpdateFill();
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        Object createAt = this.getFieldValByName(properties.getCreateAtField(), metaObject);
        Object updateAt = this.getFieldValByName(properties.getUpdateAtField(), metaObject);
        if (createAt == null || updateAt == null) {
            LocalDateTime now = LocalDateTime.now();
            if (createAt == null) {
                this.strictInsertFill(metaObject, properties.getCreateAtField(), LocalDateTime.class, now);
            }
            if (updateAt == null) {
                this.strictInsertFill(metaObject, properties.getUpdateAtField(), LocalDateTime.class, now);
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object updateAt = this.getFieldValByName(properties.getUpdateAtField(), metaObject);
        if (updateAt == null) {
            this.strictUpdateFill(metaObject, properties.getUpdateAtField(), LocalDateTime.class, LocalDateTime.now());
        }
    }
}