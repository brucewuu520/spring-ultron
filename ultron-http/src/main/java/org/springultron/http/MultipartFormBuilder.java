package org.springultron.http;

import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.springframework.lang.Nullable;

/**
 * Multipart 表单构造器
 *
 * @Auther: brucewuu
 * @Date: 2019-06-30 13:52
 * @Description:
 */
public class MultipartFormBuilder {
    private final HttpRequest request;
    private final MultipartBody.Builder formBuilder;

    static MultipartFormBuilder of(final HttpRequest request) {
        return new MultipartFormBuilder(request);
    }

    private MultipartFormBuilder(final HttpRequest request) {
        this.request = request;
        this.formBuilder = new MultipartBody.Builder();
    }

    public MultipartFormBuilder add(final String name, final String value) {
        this.formBuilder.addFormDataPart(name, value);
        return this;
    }

//    public MultipartFormBuilder add(final String name, @Nullable final String filename, final File file) {
//        final RequestBody fileBody = RequestBody.create(file, null);
//        this.formBuilder.addFormDataPart(name, filename, fileBody);
//        return this;
//    }

    public MultipartFormBuilder add(final String name, @Nullable final String filename, final RequestBody fileBody) {
        this.formBuilder.addFormDataPart(name, filename, fileBody);
        return this;
    }

    public MultipartFormBuilder addPart(final MultipartBody.Part part) {
        this.formBuilder.addPart(part);
        return this;
    }

    public MultipartFormBuilder addPart(final RequestBody requestBody) {
        this.formBuilder.addPart(requestBody);
        return this;
    }

    public MultipartFormBuilder addPart(@Nullable final Headers headers, final RequestBody requestBody) {
        this.formBuilder.addPart(headers, requestBody);
        return this;
    }

    public HttpRequest build() {
        this.formBuilder.setType(MultipartBody.FORM);
        this.request.multipartForm(this.formBuilder.build());
        return this.request;
    }
}