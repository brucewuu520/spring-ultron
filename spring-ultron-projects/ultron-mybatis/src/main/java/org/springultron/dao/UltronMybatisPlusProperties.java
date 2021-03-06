package org.springultron.dao;

import com.baomidou.mybatisplus.annotation.DbType;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Mybatis Plus字段自动填充功能配置项
 *
 * @author brucewuu
 * @date 2020/4/10 15:43
 */
@ConfigurationProperties(prefix = "ultron.mybatis-plus")
public class UltronMybatisPlusProperties {
    /**
     * 数据库类型（默认MYSQL）
     * {@link DbType}
     */
    private DbType dbType = DbType.MYSQL;

    private AutoFill autoFill = new AutoFill();

    public DbType getDbType() {
        return dbType;
    }

    public void setDbType(DbType dbType) {
        this.dbType = dbType;
    }

    public AutoFill getAutoFill() {
        return autoFill;
    }

    public void setAutoFill(AutoFill autoFill) {
        this.autoFill = autoFill;
    }

    /**
     * 自动填充配置
     */
    public static class AutoFill {
        /**
         * 是否开启自动填充，默认开启
         */
        private boolean enable = true;
        /**
         * 是否开启插入填充，默人开启
         */
        private boolean enableInsertFill = true;
        /**
         * 是否开启更新填充，默认开启
         */
        private boolean enableUpdateFill = true;
        /**
         * 创建日期字段名，默认:createAt
         */
        private String createAtField = "createAt";
        /**
         * 更新日期字段名，默认:updateAt
         */
        private String updateAtField = "updateAt";

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public boolean isEnableInsertFill() {
            return enableInsertFill;
        }

        public void setEnableInsertFill(boolean enableInsertFill) {
            this.enableInsertFill = enableInsertFill;
        }

        public boolean isEnableUpdateFill() {
            return enableUpdateFill;
        }

        public void setEnableUpdateFill(boolean enableUpdateFill) {
            this.enableUpdateFill = enableUpdateFill;
        }

        public String getCreateAtField() {
            return createAtField;
        }

        public void setCreateAtField(String createAtField) {
            this.createAtField = createAtField;
        }

        public String getUpdateAtField() {
            return updateAtField;
        }

        public void setUpdateAtField(String updateAtField) {
            this.updateAtField = updateAtField;
        }
    }
}
