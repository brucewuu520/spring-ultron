package org.springultron.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import okhttp3.*;
import okhttp3.internal.Util;
import org.springframework.lang.Nullable;
import org.springultron.core.exception.Exceptions;
import org.springultron.core.jackson.Jackson;
import org.springultron.core.io.IoUtils;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * HTTP 响应体封装
 * 响应流只能读一次
 *
 * @author brucewuu
 * @date 2019-06-30 10:37
 */
public class HttpResponse implements ResponseSpec, Closeable {
    private final Response response;
    private final ResponseBody responseBody;

    public static HttpResponse of(final Response response) {
        return new HttpResponse(response);
    }

    private HttpResponse(final Response response) {
        this.response = response;
        ResponseBody body = response.body();
        this.responseBody = (body == null) ? Util.EMPTY_RESPONSE : body;
    }

    @Override
    public int code() {
        return response.code();
    }

    @Override
    public String message() {
        return response.message();
    }

    @Override
    public boolean isSuccessful() {
        return response.isSuccessful();
    }

    @Override
    public boolean isRedirect() {
        return response.isRedirect();
    }

    @Override
    public Headers headers() {
        return response.headers();
    }

    @Override
    public List<Cookie> cookies() {
        return Cookie.parseAll(this.response.request().url(), headers());
    }

    @Override
    public InputStream asStream() {
        return responseBody.byteStream();
    }

    @Override
    public byte[] asBytes() {
        try {
            return responseBody.bytes();
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    @Override
    public JsonNode asJsonNode() {
        try {
            return Jackson.getInstance().readTree(responseBody.bytes());
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    @Override
    public String asString() {
        try {
            return responseBody.string();
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    @Override
    public <T> T asObject(Class<T> valueType) {
        try {
            return Jackson.parse(responseBody.bytes(), valueType);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    @Override
    public <T> T asObject(TypeReference<T> valueTypeRef) {
        try {
            return Jackson.parse(responseBody.bytes(), valueTypeRef);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    @Override
    public Map<String, Object> asMap() {
        try {
            return Jackson.parseMap(responseBody.bytes());
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    @Override
    public <V> Map<String, V> asMap(Class<V> valueClass) {
        try {
            return Jackson.parseMap(responseBody.bytes(), String.class, valueClass);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    @Override
    public <K, V> Map<K, V> asMap(Class<K> keyClass, Class<V> valueClass) {
        try {
            return Jackson.parseMap(responseBody.bytes(), keyClass, valueClass);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    @Override
    public <T> List<T> asList(Class<T> valueType) {
        try {
            return Jackson.parseList(responseBody.bytes(), valueType);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    @Override
    public <T> List<T> asList(TypeReference<? extends List<T>> valueTypeRef) {
        try {
            return Jackson.parseList(responseBody.bytes(), valueTypeRef);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    @Override
    public File asFile(File file) {
        asFile(file.toPath());
        return file;
    }

    @Override
    public Path asFile(Path path) {
        try {
            Files.copy(responseBody.byteStream(), path);
            return path;
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public Request rawRequest() {
        return response.request();
    }

    @Override
    public Response rawResponse() {
        return response;
    }

    @Override
    public ResponseBody rawBody() {
        return responseBody;
    }

    @Override
    public String toString() {
        return response.toString();
    }

    @Override
    public void close() throws IOException {
        IoUtils.closeQuietly(responseBody);
    }
}