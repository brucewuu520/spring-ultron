package org.springultron.http;

import okhttp3.FormBody;

/**
 * 表单构造器
 *
 * @author brucewuu
 * @date 2019-06-30 10:28
 */
public class FormBuilder {
    private final HttpRequest request;
    private final FormBody.Builder formBuilder;

    static FormBuilder of(final HttpRequest request) {
        return new FormBuilder(request);
    }

    private FormBuilder(final HttpRequest request) {
        this.request = request;
        this.formBuilder = new FormBody.Builder();
    }

    public FormBuilder add(final String name, final String value) {
        this.formBuilder.add(name, value);
        return this;
    }

    public FormBuilder addEncoded(final String name, final String value) {
        this.formBuilder.addEncoded(name, value);
        return this;
    }

    public HttpRequest build() {
        FormBody formBody = formBuilder.build();
        this.request.form(formBody);
        return request;
    }
}
