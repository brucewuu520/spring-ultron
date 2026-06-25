package org.springultron.mongodb.converter;

import org.bson.Document;
import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import tools.jackson.databind.node.ObjectNode;

/**
 * JsonNode 转 Mongo Document
 *
 * @author brucewuu
 * @date 2020/4/8 15:00
 */
@WritingConverter
public enum JsonNodeToDocumentConverter implements Converter<ObjectNode, Document> {
    /**
     * 单例模式
     */
    INSTANCE;

    @Nullable
    @Override
    public Document convert(@Nullable ObjectNode source) {
        return null == source ? null : Document.parse(source.toString());
    }
}
