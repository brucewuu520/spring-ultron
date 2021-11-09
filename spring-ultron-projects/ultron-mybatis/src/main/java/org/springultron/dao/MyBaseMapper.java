//package org.springultron.dao;
//
//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
//import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
//
//import java.util.List;
//
///**
// * 自定义BaseMapper
// *
// * @author brucewuu
// * @date 2020/10/26 上午9:43
// */
//public interface MyBaseMapper<T> extends BaseMapper<T> {
//
//    default LambdaQueryChainWrapper<T> lambdaQueryChain() {
//        return new LambdaQueryChainWrapper<>(this);
//    }
//
//    default LambdaUpdateChainWrapper<T> lambdaUpdateChain() {
//        return new LambdaUpdateChainWrapper<>(this);
//    }
//
//    int insertBatchSomeColumn(List<T> entityList);
//}
