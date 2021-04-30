package org.springultron.mongodb;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * 自增主键处理
 *
 * @author brucewuu
 * @date 2020/4/8 11:27
 */
@Component
public class MongoInsertEventListener extends AbstractMongoEventListener<IncIdEntity<?>> {
    /**
     * sequence - 集合名
     */
    private static final String SEQUENCE_COLLECTION_NAME = "sequence";
    /**
     * sequence - 自增值的字段名
     */
    private static final String SEQUENCE_FIELD_VALUE = "value";

    @Autowired
    private MongoTemplate mongoTemplate;

    @SuppressWarnings("unchecked")
    @Override
    public void onBeforeConvert(@NonNull BeforeConvertEvent<IncIdEntity<?>> event) {
        super.onBeforeConvert(event);
        IncIdEntity<Number> entity = (IncIdEntity<Number>) event.getSource();
        if (entity.getId() == null) {
            // 获得下一个ID
            Number id = nextId(entity);
            entity.setId(id);
        }
    }

    /**
     * 获得实体的下一个主键ID
     *
     * @param entity 实体对象
     * @return ID
     */
    private Number nextId(IncIdEntity<?> entity) {
        // 使用实体名的简单类名，作为 ID 编号
        String id = entity.getClass().getSimpleName();
        // 创建 Query 对象
        Query query = new Query(Criteria.where("_id").is(id));
        // 创建 Update 对象
        Update update = new Update();
        // 自增值
        update.inc(SEQUENCE_FIELD_VALUE, 1);
        FindAndModifyOptions options = new FindAndModifyOptions();
        // 如果不存在时，则进行插入
        options.upsert(true);
        // 返回新值
        options.returnNew(true);
        // 执行操作
        JsonNode result = mongoTemplate.findAndModify(query, update, options, JsonNode.class, SEQUENCE_COLLECTION_NAME);
        // 返回主键
        return result == null ? null : result.get(SEQUENCE_FIELD_VALUE).numberValue();
    }
}
