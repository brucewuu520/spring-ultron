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

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.core.conf.ExtensionsConstants;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import io.swagger.v3.oas.models.Operation;
import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;

/**
 * @author <a href="xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * @date 2023/2/26 12:14
 */
public class Knife4jJakartaOperationCustomizer implements GlobalOperationCustomizer {

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        // 解析支持作者、接口排序
        // https://gitee.com/xiaoym/knife4j/issues/I6FB9I
        ApiOperationSupport operationSupport = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), ApiOperationSupport.class);
        if (operationSupport != null) {
            if (StrUtil.isNotBlank(operationSupport.author())) {
                operation.addExtension(ExtensionsConstants.EXTENSION_AUTHOR, operationSupport.author());
            }
            if (operationSupport.order() != 0) {
                operation.addExtension(ExtensionsConstants.EXTENSION_ORDER, operationSupport.order());
            }
        }
        return operation;
    }
}
