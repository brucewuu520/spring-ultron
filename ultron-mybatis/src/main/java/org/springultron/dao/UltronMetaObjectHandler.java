//package org.springultron.dao;
//
//import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
//import org.apache.ibatis.reflection.MetaObject;
//import org.springframework.context.annotation.Configuration;
//
///**
// * 自动填充
// *
// * @Auther: brucewuu
// * @Date: 2019-06-28 22:46
// * @Description:
// */
//@Configuration
//public class UltronMetaObjectHandler implements MetaObjectHandler {
//
//    @Override
//    public void insertFill(MetaObject metaObject) {
//        // do nothing
//    }
//
//    @Override
//    public void updateFill(MetaObject metaObject) {
//        this.setUpdateFieldValByName("update_at", "current_timestamp", metaObject);
//    }
//}
