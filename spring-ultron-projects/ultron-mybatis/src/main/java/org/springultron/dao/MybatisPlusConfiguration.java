package org.springultron.dao;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.SqlExplainInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springultron.core.jackson.Jackson;

import java.util.ArrayList;
import java.util.List;

/**
 * mybatis plus 自动化配置
 *
 * @author brucewuu
 * @date 2019-05-28 10:58
 */
@MapperScan("com.*.*.mapper")
@EnableTransactionManagement
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(MybatisPlusAutoFillProperties.class)
public class MybatisPlusConfiguration implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        JacksonTypeHandler.setObjectMapper(Jackson.getInstance());
    }

    /**
     * 分页插件
     */
    @Bean
    @ConditionalOnMissingBean({PaginationInterceptor.class})
    public PaginationInterceptor paginationInterceptor() {
        // 开启 count 的 join 优化,只针对 left join !!!
        return new PaginationInterceptor().setCountSqlParser(new JsqlParserCountOptimize(true));
    }

    /**
     * 防止全表更新/删除
     */
    @Bean
    @ConditionalOnMissingBean({SqlExplainInterceptor.class})
    public SqlExplainInterceptor sqlExplainInterceptor() {
        SqlExplainInterceptor sqlExplainInterceptor = new SqlExplainInterceptor();
        List<ISqlParser> sqlParserList = new ArrayList<>(1);
        sqlParserList.add(new BlockAttackSqlParser());
        sqlExplainInterceptor.setSqlParserList(sqlParserList);
        return sqlExplainInterceptor;
    }

    /**
     * 日期字段自动填充
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "ultron.mybatis-plus.auto-fill.enable", matchIfMissing = true)
    public MetaObjectHandler metaObjectHandler(MybatisPlusAutoFillProperties properties) {
        return new UltronMetaObjectHandler(properties);
    }

    /**
     * 乐观锁
     */
    @Bean
    @ConditionalOnMissingBean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

}