package org.springultron.dao;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * 自动填充
 * 实现数据库更新操作自动填充更新时间update_at字段
 *
 * @author brucewuu
 * @date 2019-06-28 22:46
 */
@Configuration
public class UltronMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        // do nothing
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setUpdateFieldValByName("updateAt", LocalDateTime.now(), metaObject);
    }
}
