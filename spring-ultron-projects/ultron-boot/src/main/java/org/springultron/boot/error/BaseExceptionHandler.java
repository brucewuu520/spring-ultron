package org.springultron.boot.error;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springultron.core.result.ApiResult;
import org.springultron.core.result.FieldErrorDTO;
import org.springultron.core.result.ResultStatus;
import org.springultron.core.utils.Lists;
import org.springultron.core.utils.StringUtils;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
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
        FieldError fieldError = result.getFieldError();
        if (fieldError != null) {
            ApiResult<Object> apiResult = ApiResult.failed(ResultStatus.PARAM_BIND_FAILED.getCode(), fieldError.getDefaultMessage());
            ArrayList<FieldErrorDTO> fieldErrorDTOS = Lists.newArrayList(1);
            fieldErrorDTOS.add(FieldErrorDTO.of(fieldError.getField(), fieldError.getDefaultMessage()));
            apiResult.setFieldErrors(fieldErrorDTOS);
            return apiResult;
        } else {
            return ApiResult.failed(ResultStatus.PARAM_BIND_FAILED);
        }
    }

    /**
     * 参数约束处理 ConstraintViolation
     *
     * @param violations 校验结果
     * @return ApiResult
     */
    protected ApiResult<Object> handleError(Set<ConstraintViolation<?>> violations) {
        ArrayList<FieldErrorDTO> fieldErrorDTOS = Lists.newArrayList(violations.size());
        String message = null;
        while (violations.iterator().hasNext()) {
            ConstraintViolation<?> violation = violations.iterator().next();
            String path = ((PathImpl) violation.getPropertyPath()).getLeafNode().getName();
            fieldErrorDTOS.add(FieldErrorDTO.of(path, violation.getMessage()));
            if (StringUtils.isEmpty(message)) {
                message = violation.getMessage();
            }
        }
        ApiResult<Object> apiResult = ApiResult.failed(ResultStatus.PARAM_VALID_FAILED.getCode(), message);
        apiResult.setFieldErrors(fieldErrorDTOS);
        return apiResult;
    }
}
