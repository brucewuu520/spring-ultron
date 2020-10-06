package org.springultron.mongodb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
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
}
