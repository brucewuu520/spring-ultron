package org.springultron.mongodb;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springultron.mongodb.converter.DBObjectToJsonNodeConverter;
import org.springultron.mongodb.converter.JsonNodeToDocumentConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * MongoDb自动配置
 *
 * @author brucewuu
 * @date 2020/4/5 13:42
 */
@Configuration(proxyBeanMethods = false)
public class MongoDbConfiguration {

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>(2);
        converters.add(DBObjectToJsonNodeConverter.INSTANCE);
        converters.add(JsonNodeToDocumentConverter.INSTANCE);
        return new MongoCustomConversions(converters);
    }

    /**
     * 移除 _class field
     * 参考博客 https://blog.csdn.net/bigtree_3721/article/details/82787411
     */
    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDbFactory factory, MongoMappingContext context, BeanFactory beanFactory) {
        // 创建 DbRefResolver 对象
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        // 创建 MappingMongoConverter 对象
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, context);
        // 设置 conversions 属性
        try {
            converter.setCustomConversions(beanFactory.getBean(CustomConversions.class));
        } catch (NoSuchBeanDefinitionException e) {
            // ignore
        }
        // 设置 typeMapper 属性，从而移除 _class field 。
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return converter;
    }
}
