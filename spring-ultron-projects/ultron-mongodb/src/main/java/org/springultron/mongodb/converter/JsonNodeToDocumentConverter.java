package org.springultron.mongodb.converter;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.lang.Nullable;

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
