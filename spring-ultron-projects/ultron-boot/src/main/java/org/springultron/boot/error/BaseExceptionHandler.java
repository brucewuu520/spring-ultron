package org.springultron.boot.error;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.validation.BindingResult;
import org.springultron.core.result.ApiResult;
import org.springultron.core.result.ResultStatus;

import javax.validation.ConstraintViolation;
import java.util.Optional;
import java.util.Set;

/**
 * 异常捕获处理
 *
 * @author brucewuu
 * @date 2020/7/26 20:14
 */
public abstract class BaseExceptionHandler {

    /**
     * 参数绑定处理 BindingResult
     *
     * @param result BindingResult
     * @return ApiResult
     */
    protected ApiResult<Object> handleError(BindingResult result) {
        return Optional.ofNullable(result.getFieldError())
                .map(error -> String.format("%s:%s", error.getField(), error.getDefaultMessage()))
                .map(message -> ApiResult.failed(ResultStatus.PARAM_BIND_FAILED.getCode(), message))
                .orElseGet(() -> ApiResult.failed(ResultStatus.PARAM_BIND_FAILED));
    }

    /**
     * 参数约束处理 ConstraintViolation
     *
     * @param violations 校验结果
     * @return ApiResult
     */
    protected ApiResult<Object> handleError(Set<ConstraintViolation<?>> violations) {
        ConstraintViolation<?> violation = violations.iterator().next();
        String path = ((PathImpl) violation.getPropertyPath()).getLeafNode().getName();
        String message = String.format("%s:%s", path, violation.getMessage());
        return ApiResult.failed(ResultStatus.PARAM_VALID_FAILED.getCode(), message);
    }
}
