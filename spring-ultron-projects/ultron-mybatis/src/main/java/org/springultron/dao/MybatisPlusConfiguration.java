package org.springultron.dao;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

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
public class MybatisPlusConfiguration {

    /**
     * 新的分页插件
     * 一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor=false 避免缓存出现问题
     */
    @Bean
    @ConditionalOnMissingBean(MybatisPlusInterceptor.class)
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页插件，默认MYSQL
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        // 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // 防止全表更新与删除插件
//        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return interceptor;
    }

    /**
     * 自定义Configuration
     */
    @Bean
    @ConditionalOnMissingBean(ConfigurationCustomizer.class)
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> {
            configuration.setUseDeprecatedExecutor(false);
            configuration.setDefaultEnumTypeHandler(MybatisEnumTypeHandler.class);
        };
    }

    /**
     * 自定义SQL注入
     */
    @Bean
    @ConditionalOnMissingBean(MySqlInjector.class)
    public MySqlInjector sqlInjector() {
        return new MySqlInjector();
    }

    /**
     * 日期字段自动填充
     */
    @Bean
    @ConditionalOnMissingBean(MetaObjectHandler.class)
    @ConditionalOnProperty(value = "ultron.mybatis-plus.auto-fill.enable", matchIfMissing = true)
    public MetaObjectHandler metaObjectHandler(MybatisPlusAutoFillProperties properties) {
        return new UltronMetaObjectHandler(properties);
    }
}