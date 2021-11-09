//package org.springultron.dao;
//
//import com.baomidou.mybatisplus.core.injector.AbstractMethod;
//import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
//import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
//
//import java.util.List;
//
///**
// * 自定义Sql注入
// * 添加批量插入方法
// *
// * @author brucewuu
// * @date 2020/10/25 下午9:58
// */
//public class MySqlInjector extends DefaultSqlInjector {
//
//    @Override
//    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
//        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
//        methodList.add(new InsertBatchSomeColumn());
//        return methodList;
//    }
//}
