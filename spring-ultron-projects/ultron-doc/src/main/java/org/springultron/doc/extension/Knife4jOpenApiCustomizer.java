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

package org.springultron.doc.extension;

import com.github.xiaoymin.knife4j.core.conf.GlobalConstants;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springultron.doc.configuration.Knife4jProperties;
import org.springultron.doc.configuration.Knife4jSetting;

import java.util.HashMap;
import java.util.Map;

/**
 * 增强扩展属性支持
 *
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * @date 2022/12/11 22:40
 */
public class Knife4jOpenApiCustomizer implements GlobalOpenApiCustomizer {

    private final Knife4jProperties knife4jProperties;

    public Knife4jOpenApiCustomizer(Knife4jProperties knife4jProperties) {
        this.knife4jProperties = knife4jProperties;
    }

    @Override
    public void customise(OpenAPI openApi) {
        if (knife4jProperties.isEnable()) {
            Knife4jSetting setting = knife4jProperties.getSetting();
            OpenApiExtensionResolver openApiExtensionResolver = new OpenApiExtensionResolver(setting, knife4jProperties.getDocuments());
            // 解析初始化
            openApiExtensionResolver.start();
            Map<String, Object> objectMap = new HashMap<>(3);
            objectMap.put(GlobalConstants.EXTENSION_OPEN_SETTING_NAME, setting);
            objectMap.put(GlobalConstants.EXTENSION_OPEN_MARKDOWN_NAME, openApiExtensionResolver.getMarkdownFiles());
            openApi.addExtension(GlobalConstants.EXTENSION_OPEN_API_NAME, objectMap);
        }
    }
}
