package org.springultron.mongodb.converter;

import org.bson.BasicBSONObject;
import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springultron.core.jackson.Jackson;
import tools.jackson.databind.JsonNode;

/**
 * Mongo BSONObject 转 JsonNode
 *
 * @author brucewuu
 * @date 2020/4/8 12:12
 */
@ReadingConverter
public enum BSONObjectToJsonNodeConverter implements Converter<BasicBSONObject, JsonNode> {
    /**
     * 单例模式
     */
    INSTANCE;

    @Nullable
    @Override
    public JsonNode convert(@Nullable BasicBSONObject source) {
        return null == source ? null : Jackson.getInstance().valueToTree(source);
    }
}
