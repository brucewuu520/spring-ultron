package org.springultron.swagger.knife4j;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Knife4j增强配置
 *
 * @author brucewuu
 * @date 2020/1/6 11:43
 */
@ConfigurationProperties(prefix = "swagger.knife4j")
public class Knife4jProperties {
    /**
     * 是否是生产环境
     */
    private boolean production;

    /**
     * http basic配置
     */
    @NestedConfigurationProperty
    private Knife4jHttpBasic basic;

    /**
     * markdown路径
     */
    private String markdowns;

    public boolean isProduction() {
        return production;
    }

    public void setProduction(boolean production) {
        this.production = production;
    }

    public Knife4jHttpBasic getBasic() {
        return basic;
    }

    public void setBasic(Knife4jHttpBasic basic) {
        this.basic = basic;
    }

    public String getMarkdowns() {
        return markdowns;
    }

    public void setMarkdowns(String markdowns) {
        this.markdowns = markdowns;
    }

}
