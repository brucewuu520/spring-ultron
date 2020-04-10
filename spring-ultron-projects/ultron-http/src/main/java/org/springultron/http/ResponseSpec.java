package org.springultron.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import okhttp3.*;
import org.springframework.lang.Nullable;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * HttpResponse Interface
 *
 * @author brucewuu
 * @date 2020/4/5 20:47
 */
public interface ResponseSpec {
    /**
     * Return the HTTP code.
     *
     * @return code
     */
    int code();

    /**
     * Return the HTTP status message.
     *
     * @return message
     */
    String message();

    /**
     * Return the HTTP status is successful or not.
     * Default return false.
     *
     * @return boolean
     */
    boolean isSuccessful();

    /**
     * Return the HTTP is redirect or not.
     *
     * @return boolean
     */
    boolean isRedirect();

    /**
     * Return the HTTP Response Headers.
     *
     * @return {@link Headers}
     */
    Headers headers();

    /**
     * HTTP Response Headers Consumer.
     *
     * @param consumer Consumer
     * @return {@link ResponseSpec}
     */
    default ResponseSpec headers(Consumer<Headers> consumer) {
        consumer.accept(this.headers());
        return this;
    }

    /**
     * Return the HTTP Response Cookies.
     *
     * @return Cookie List
     */
    List<Cookie> cookies();

    /**
     * HTTP Response Cookies Consumer.
     *
     * @param consumer Consumer
     * @return {@link ResponseSpec}
     */
    default ResponseSpec cookies(Consumer<List<Cookie>> consumer) {
        consumer.accept(this.cookies());
        return this;
    }

    /**
     * Return Body To InputStream.
     *
     * @return {@link InputStream}
     */
    InputStream asStream();

    /**
     * Return Body To Byte Array.
     *
     * @return Byte Array
     */
    byte[] asBytes();

    /**
     * Return Body To JsonNode.
     *
     * @return {@link JsonNode}
     */
    JsonNode asJsonNode();

    /**
     * Return Body to String.
     *
     * @return Body String
     */
    String asString();

    /**
     * Return Body To Object.
     *
     * @param valueType Value Type
     * @param <T>       泛型
     * @return T
     */
    <T> T asObject(Class<T> valueType);

    /**
     * Return Body To Object.
     *
     * @param valueTypeRef {@link TypeReference}
     * @param <T>          泛型
     * @return T
     */
    <T> T asObject(TypeReference<T> valueTypeRef);

    /**
     * Return Body To Map.
     *
     * @return Map<String, Object>
     */
    Map<String, Object> asMap();

    /**
     * Return Body To Map.
     *
     * @param valueClass 值类型
     * @param <V>        键泛型
     * @return Map<String, V>
     */
    <V> Map<String, V> asMap(Class<V> valueClass);

    /**
     * Return Body To Map.
     *
     * @param keyClass   键类型
     * @param valueClass 值类型
     * @param <K>        键泛型
     * @param <V>        值泛型
     * @return Map<K, V>
     */
    <K, V> Map<K, V> asMap(Class<K> keyClass, Class<V> valueClass);

    /**
     * Return Body To List.
     *
     * @param valueType Value Type
     * @param <T>       泛型
     * @return List<T>
     */
    <T> List<T> asList(Class<T> valueType);

    /**
     * Return Body To List.
     *
     * @param valueTypeRef {@link TypeReference}
     * @param <T>          泛型
     * @return List<T>
     */
    <T> List<T> asList(TypeReference<? extends List<T>> valueTypeRef);

    /**
     * Return Body To File.
     *
     * @param file save file
     * @return {@link File}
     */
    File asFile(File file);

    /**
     * Return Body To Path.
     *
     * @param path Path
     * @return {@link Path}
     */
    Path asFile(Path path);

    /**
     * Return the body contentType.
     *
     * @return contentType
     */
    @Nullable
    MediaType contentType();

    /**
     * Return the body contentLength.
     *
     * @return contentLength
     */
    long contentLength();

    /**
     * Return rawResponse.
     *
     * @return Response
     */
    Response rawResponse();

    /**
     * rawResponse Consumer.
     *
     * @param consumer Consumer
     * @return Response
     */
    default ResponseSpec rawResponse(Consumer<Response> consumer) {
        consumer.accept(this.rawResponse());
        return this;
    }

    /**
     * Return rawBody.
     *
     * @return {@link ResponseBody}
     */
    ResponseBody rawBody();

    /**
     * rawBody Consumer.
     *
     * @param consumer Consumer
     * @return {@link ResponseSpec}
     */
    default ResponseSpec rawBody(Consumer<ResponseBody> consumer) {
        consumer.accept(this.rawBody());
        return this;
    }
}
