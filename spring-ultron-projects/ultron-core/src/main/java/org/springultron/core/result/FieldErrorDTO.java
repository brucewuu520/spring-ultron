package org.springultron.core.result;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 字段校验错误信息
 *
 * @author brucewuu
 * @date 2020/8/6 22:00
 */
@Schema(description = "字段校验错误信息")
public class FieldErrorDTO {

    @Schema(description = "字段")
    private String field;

    @Schema(description = "错误信息")
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
