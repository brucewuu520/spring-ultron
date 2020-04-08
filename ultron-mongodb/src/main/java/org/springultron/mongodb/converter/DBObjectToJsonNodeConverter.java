package org.springultron.mongodb.converter;

import com.fasterxml.jackson.databind.JsonNode;
import org.bson.BasicBSONObject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.lang.Nullable;
import org.springultron.core.jackson.Jackson;

/**
 * Mongo DBObject 转 JsonNode
 *
 * @author brucewuu
 * @date 2020/4/8 12:12
 */
@ReadingConverter
public enum  DBObjectToJsonNodeConverter implements Converter<BasicBSONObject, JsonNode> {
    /**
     * 单例模式
     */
    INSTANCE;

    @Nullable
    @Override
    public JsonNode convert(BasicBSONObject source) {
        return Jackson.getInstance().valueToTree(source);
    }
}
