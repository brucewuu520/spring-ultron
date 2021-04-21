package org.springultron.wechat.params;

import org.springultron.core.jackson.Jackson;

import java.util.HashMap;

/**
 * 模板消息内容
 *
 * @author brucewuu
 * @date 2021/4/20 下午4:51
 */
public class TemplateData extends HashMap<String, TemplateData.Item> {
    private static final long serialVersionUID = 7551639410660547145L;

    public TemplateData() {

    }

    public TemplateData(String key, TemplateData.Item item) {
        this.put(key, item);
    }

    @Override
    public String toString() {
        return Jackson.toJson(this);
    }

    public static class Item {
        private Object value;
        private String color;

        public Item(Object value) {
            this(value, null);
        }

        public Item(Object value, String color) {
            this.value = value;
            this.color = color;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }
}
