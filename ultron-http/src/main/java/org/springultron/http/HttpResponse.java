package org.springultron.http;

import com.fasterxml.jackson.core.type.TypeReference;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springultron.core.exception.Exceptions;
import org.springultron.core.jackson.Jackson;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * HTTP 响应体封装
 *
 * @author brucewuu
 * @date 2019-06-30 10:37
 */
public class HttpResponse {
    private final Response response;

    public static HttpResponse of(final Response response) {
        return new HttpResponse(response);
    }

    private HttpResponse(final Response response) {
        this.response = response;
    }

    public boolean isSuccessful() {
        return this.response.isSuccessful();
    }

    public boolean isRedirect() {
        return response.isRedirect();
    }

    public byte[] asByte() {
        try {
            return Optional.ofNullable(response.body()).map(body -> {
                try {
                    return body.bytes();
                } catch (IOException e) {
                    throw Exceptions.unchecked(e);
                }
            }).orElseThrow(() -> new RuntimeException("response body is null"));
        } finally {
            response.close();
        }
    }

    public InputStream asStream() {
        try {
            return Optional.ofNullable(response.body()).map(ResponseBody::byteStream).orElseThrow(() -> new RuntimeException("response body is null"));
        } finally {
            response.close();
        }
    }

    public String asString() {
        try {
            return Optional.ofNullable(response.body()).map(body -> {
                try {
                    return body.string();
                } catch (IOException e) {
                    throw Exceptions.unchecked(e);
                }
            }).orElseThrow(() -> new RuntimeException("response body is null"));
        } finally {
            response.close();
        }
    }

    public <T> T asObject(final Class<T> valueType) {
        try {
            return Optional.ofNullable(response.body()).map(body -> Jackson.parse(body.byteStream(), valueType)).orElseThrow(() -> new RuntimeException("response body is null"));
        } finally {
            response.close();
        }
    }

    public <T> T asObject(final TypeReference<T> valueTypeRef) {
        try {
            return Optional.ofNullable(response.body()).map(body -> Jackson.parse(body.byteStream(), valueTypeRef)).orElseThrow(() -> new RuntimeException("response body is null"));
        } finally {
            response.close();
        }
    }

    public Map<String, Object> asMap() {
        try {
            return Optional.ofNullable(response.body()).map(body -> Jackson.parseMap(body.byteStream())).orElseThrow(() -> new RuntimeException("response body is null"));
        } finally {
            response.close();
        }
    }

    public <T> List<T> asList(final Class<T> valueType) {
        try {
            return Optional.ofNullable(response.body()).map(body -> Jackson.parseArray(body.byteStream(), valueType)).orElseThrow(() -> new RuntimeException("response body is null"));
        } finally {
            response.close();
        }
    }

    public <T> List<T> asList(final TypeReference<? extends List<T>> valueTypeRef) {
        try {
            return Optional.ofNullable(response.body()).map(body -> Jackson.parseArray(body.byteStream(), valueTypeRef)).orElseThrow(() -> new RuntimeException("response body is null"));
        } finally {
            response.close();
        }
    }

    public int code() {
        return response.code();
    }

    public String message() {
        return response.message();
    }

    public Response response() {
        return response;
    }

    public void close() {
        response.close();
    }

    @Override
    public String toString() {
        return response.toString();
    }
}
