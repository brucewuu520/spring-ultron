package org.springultron.core.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 字段校验错误信息
 *
 * @author brucewuu
 * @date 2020/8/6 22:00
 */
@ApiModel(description = "字段校验错误信息")
public class FieldErrorDTO {

    @ApiModelProperty(value = "字段", position = 1)
    private String field;

    @ApiModelProperty(value = "错误信息", position = 2)
    private String error;

    public FieldErrorDTO() {}

    public static FieldErrorDTO of(String field, String error) {
        return new FieldErrorDTO(field, error);
    }

    private FieldErrorDTO(String field, String error) {
        this.field = field;
        this.error = error;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
