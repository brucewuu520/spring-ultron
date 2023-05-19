/*
 * Copyright © 2017-2023 Knife4j(xiaoymin@foxmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springultron.doc.configuration;

import java.util.List;

/**
 * 接口文档 Http Basic 认证登录配置
 *
 * @author brucewuu
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * @date 2023/5/6 13:23
 */
public class Knife4jHttpBasic {
    /**
     * Http Basic是否开启（默认为不开启）
     */
    private boolean enable = false;

    /**
     * Http Basic 用户名（默认：admin）
     */
    private String username;

    /**
     * Http Basic 密码（默认：123321）
     */
    private String password;

    /**
     * All configured urls need to be verified by basic，Only support Regex
     */
    private List<String> includes;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getIncludes() {
        return includes;
    }

    public void setIncludes(List<String> includes) {
        this.includes = includes;
    }
}
