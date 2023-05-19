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


package org.springultron.doc.utils;

import com.github.xiaoymin.knife4j.core.util.StrUtil;
import org.springframework.core.env.Environment;

import java.util.Objects;

/**
 * Environment 环境变量工具类
 *
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * @date 2022/8/18 22:26
 */
public class EnvironmentUtils {

    /**
     * get String property
     *
     * @param environment  Spring Context Environment
     * @param key          hash-key
     * @param defaultValue default
     */
    public static String resolveString(Environment environment, String key, String defaultValue) {
        if (environment != null) {
            String envValue = environment.getProperty(key);
            if (StrUtil.isNotBlank(envValue)) {
                return envValue;
            }
        }
        return defaultValue;
    }

    /**
     * 获取int类型的值
     *
     * @param environment  环境变量
     * @param key          变量
     * @param defaultValue 默认值
     */
    public static Integer resolveInt(Environment environment, String key, Integer defaultValue) {
        if (environment != null) {
            return Integer.parseInt(Objects.toString(environment.getProperty(key, String.valueOf(defaultValue)), String.valueOf(defaultValue)));
            // return Integer.parseInt(Objects.toString(environment.getProperty(key)), defaultValue);
        }
        return defaultValue;
    }

    /**
     * 获取bool值
     *
     * @param environment  环境变量
     * @param key          变量
     * @param defaultValue 默认值
     */
    public static Boolean resolveBool(Environment environment, String key, Boolean defaultValue) {
        if (environment != null) {
            return Boolean.valueOf(Objects.toString(environment.getProperty(key, defaultValue.toString()), defaultValue.toString()));
        }
        return defaultValue;
    }

}
