package org.springultron.mongodb;

import org.springframework.data.annotation.Id;

/**
 * 自增主键实体
 *
 * @param <T> 主键泛型
 * @author brucewuu
 * @date 2020/4/8 11:24
 */
public abstract class IncIdEntity<T> {

    @Id
    private T id;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }
}
