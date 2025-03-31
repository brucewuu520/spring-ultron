/*
 * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springultron.http;

import okhttp3.Authenticator;
import okhttp3.*;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpMethod;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.lang.Nullable;
import org.springultron.core.exception.Exceptions;
import org.springultron.core.jackson.Jackson;
import org.springultron.http.ssl.DisableValidationTrustManager;
import org.springultron.http.ssl.TrustAllHostNames;

import javax.net.ssl.*;
import java.net.*;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

/**
 * okhttp3 请求封装
 *
 * @author brucewuu
 * @date 2019-06-29 22:15
 */
public class HttpRequest {
    private static final MediaType MEDIA_TYPE_JSON = MediaType.get("application/json; charset=utf-8");

    private static volatile OkHttpClient httpClient = new OkHttpClient();

    private final Request.Builder requestBuilder;
    private final HttpUrl.Builder urlBuilder;
    private final String method;
    private RequestBody requestBody;
    private Boolean followRedirects;
    private Boolean followSslRedirects;
    private CookieJar cookieJar;
    private Authenticator authenticator;
    private Interceptor interceptor;
    private Proxy proxy;
    private ProxySelector proxySelector;
    private Authenticator proxyAuthenticator;
    private Boolean disableSslValidation;
    private HostnameVerifier hostnameVerifier;
    private SSLSocketFactory sslSocketFactory;
    private X509TrustManager trustManager;
    private Duration connectTimeout;
    private Duration readTimeout;
    private Duration writeTimeout;
    private RetryPolicy retryPolicy;
    private static volatile HttpLoggingInterceptor globalLoggingInterceptor;
    private HttpLoggingInterceptor.Level level;

    private HttpRequest(final HttpUrl.Builder urlBuilder, final String method) {
        this.requestBuilder = new Request.Builder();
        this.urlBuilder = urlBuilder;
        this.method = method;
    }

    public static HttpRequest get(final String url) {
        return new HttpRequest(HttpUrl.get(url).newBuilder(), "GET");
    }

    public static HttpRequest get(final URI uri) {
        return new HttpRequest(Objects.requireNonNull(HttpUrl.get(uri)).newBuilder(), "GET");
    }

    public static HttpRequest get(final URL url) {
        return new HttpRequest(Objects.requireNonNull(HttpUrl.get(url)).newBuilder(), "GET");
    }

    public static HttpRequest get(final HttpUrl.Builder urlBuilder) {
        return new HttpRequest(urlBuilder, "GET");
    }

    public static HttpRequest post(final String url) {
        return new HttpRequest(HttpUrl.get(url).newBuilder(), "POST");
    }

    public static HttpRequest post(final URI uri) {
        return new HttpRequest(Objects.requireNonNull(HttpUrl.get(uri)).newBuilder(), "POST");
    }

    public static HttpRequest post(final URL url) {
        return new HttpRequest(Objects.requireNonNull(HttpUrl.get(url)).newBuilder(), "POST");
    }

    public static HttpRequest post(final HttpUrl.Builder urlBuilder) {
        return new HttpRequest(urlBuilder, "POST");
    }

    public static HttpRequest patch(final String url) {
        return new HttpRequest(HttpUrl.get(url).newBuilder(), "PATCH");
    }

    public static HttpRequest patch(final URI uri) {
        return new HttpRequest(Objects.requireNonNull(HttpUrl.get(uri)).newBuilder(), "PATCH");
    }

    public static HttpRequest patch(final URL url) {
        return new HttpRequest(Objects.requireNonNull(HttpUrl.get(url)).newBuilder(), "PATCH");
    }

    public static HttpRequest patch(final HttpUrl.Builder urlBuilder) {
        return new HttpRequest(urlBuilder, "PATCH");
    }

    public static HttpRequest put(final String url) {
        return new HttpRequest(HttpUrl.get(url).newBuilder(), "PUT");
    }

    public static HttpRequest put(final URI uri) {
        return new HttpRequest(Objects.requireNonNull(HttpUrl.get(uri)).newBuilder(), "PUT");
    }

    public static HttpRequest put(final URL url) {
        return new HttpRequest(Objects.requireNonNull(HttpUrl.get(url)).newBuilder(), "PUT");
    }

    public static HttpRequest put(final HttpUrl.Builder urlBuilder) {
        return new HttpRequest(urlBuilder, "PUT");
    }

    public static HttpRequest delete(final String url) {
        return new HttpRequest(HttpUrl.get(url).newBuilder(), "DELETE");
    }

    public static HttpRequest delete(final URI uri) {
        return new HttpRequest(Objects.requireNonNull(HttpUrl.get(uri)).newBuilder(), "DELETE");
    }

    public static HttpRequest delete(final URL url) {
        return new HttpRequest(Objects.requireNonNull(HttpUrl.get(url)).newBuilder(), "DELETE");
    }

    public static HttpRequest delete(final HttpUrl.Builder urlBuilder) {
        return new HttpRequest(urlBuilder, "DELETE");
    }

    public HttpRequest headers(final Headers headers) {
        this.requestBuilder.headers(headers);
        return this;
    }

    public HttpRequest addHeader(final String name, final String value) {
        this.requestBuilder.addHeader(name, value);
        return this;
    }

    public HttpRequest setHeader(final String name, final String value) {
        this.requestBuilder.header(name, value);
        return this;
    }

    public HttpRequest removeHeader(final String name) {
        this.requestBuilder.removeHeader(name);
        return this;
    }

    public HttpRequest addCookie(final Cookie cookie) {
        this.requestBuilder.addHeader("Cookie", cookie.toString());
        return this;
    }

    public HttpRequest query(String query) {
        this.urlBuilder.query(query);
        return this;
    }

    public HttpRequest queryEncoded(String query) {
        this.urlBuilder.encodedQuery(query);
        return this;
    }

    public HttpRequest query(final String name, final Object value) {
        this.urlBuilder.addQueryParameter(name, value == null ? null : String.valueOf(value));
        return this;
    }

    public HttpRequest queryEncoded(final String name, final Object value) {
        this.urlBuilder.addEncodedQueryParameter(name, value == null ? null : String.valueOf(value));
        return this;
    }

    public HttpRequest queryMap(Map<String, Object> queryMap) {
        if (queryMap != null && !queryMap.isEmpty()) {
            queryMap.forEach(this::query);
        }
        return this;
    }

    public HttpRequest form(final FormBody formBody) {
        this.requestBody = formBody;
        return this;
    }

    public FormBuilder formBuilder() {
        return FormBuilder.of(this);
    }

    public FormBuilder formBuilder(@Nullable Charset charset) {
        return FormBuilder.of(this, charset);
    }

    public HttpRequest multipartForm(final MultipartBody multipartBody) {
        this.requestBody = multipartBody;
        return this;
    }

    public MultipartFormBuilder multipartFormBuilder() {
        return MultipartFormBuilder.of(this);
    }

    public HttpRequest body(final RequestBody requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    public HttpRequest bodyValue(final String jsonValue) {
        this.requestBody = RequestBody.create(jsonValue, MEDIA_TYPE_JSON);
        return this;
    }

    public HttpRequest bodyValue(final Object bodyValue) {
        return bodyValue(Jackson.toJson(bodyValue));
    }

    public HttpRequest cacheControl(final CacheControl cacheControl) {
        this.requestBuilder.cacheControl(cacheControl);
        return this;
    }

    public HttpRequest followRedirects(final boolean followRedirects) {
        this.followRedirects = followRedirects;
        return this;
    }

    public HttpRequest followSslRedirects(final boolean followSslRedirects) {
        this.followSslRedirects = followSslRedirects;
        return this;
    }

    public HttpRequest cookieManager(final CookieJar cookieJar) {
        this.cookieJar = cookieJar;
        return this;
    }

    public HttpRequest basicAuth(String username, String password) {
        this.authenticator = new BasicAuthenticator(username, password);
        return this;
    }

    public HttpRequest authenticator(Authenticator authenticator) {
        this.authenticator = authenticator;
        return this;
    }

    public HttpRequest interceptor(Interceptor interceptor) {
        this.interceptor = interceptor;
        return this;
    }

    public HttpRequest proxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    public HttpRequest proxy(final InetSocketAddress address) {
        return proxy(Proxy.Type.HTTP, address);
    }

    public HttpRequest proxy(final Proxy.Type type, final InetSocketAddress address) {
        return proxy(new Proxy(type, address));
    }

    public HttpRequest proxySelector(final ProxySelector proxySelector) {
        this.proxySelector = proxySelector;
        return this;
    }

    public HttpRequest proxyAuthenticator(final Authenticator proxyAuthenticator) {
        this.proxyAuthenticator = proxyAuthenticator;
        return this;
    }

    public HttpRequest hostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
        return this;
    }

    public HttpRequest sslSocketFactory(SSLSocketFactory sslSocketFactory, X509TrustManager trustManager) {
        this.sslSocketFactory = sslSocketFactory;
        this.trustManager = trustManager;
        return this;
    }

    /**
     * 关闭 ssl 校验
     */
    public HttpRequest disableSslValidation() {
        this.disableSslValidation = Boolean.TRUE;
        return this;
    }

    public HttpRequest connectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public HttpRequest readTimeout(Duration readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public HttpRequest writeTimeout(Duration writeTimeout) {
        this.writeTimeout = writeTimeout;
        return this;
    }

    public HttpRequest retry() {
        this.retryPolicy = RetryPolicy.INSTANCE;
        return this;
    }

    public HttpRequest retryOn(Predicate<ResponseSpec> respPredicate) {
        this.retryPolicy = new RetryPolicy(respPredicate);
        return this;
    }

    public HttpRequest retry(int maxAttempts, long sleepMillis) {
        this.retryPolicy = new RetryPolicy(maxAttempts, sleepMillis);
        return this;
    }

    public HttpRequest retry(int maxAttempts, long sleepMillis, Predicate<ResponseSpec> respPredicate) {
        this.retryPolicy = new RetryPolicy(maxAttempts, sleepMillis);
        return this;
    }

    public HttpRequest log() {
        this.level = HttpLoggingInterceptor.Level.BODY;
        return this;
    }

    public HttpRequest log(HttpLoggingInterceptor.Level level) {
        this.level = level;
        return this;
    }

    @Override
    public String toString() {
        return requestBuilder.toString();
    }

    private Call newCall(final OkHttpClient httpClient) {
        OkHttpClient.Builder builder = httpClient.newBuilder();
        if (null != connectTimeout) {
            builder.connectTimeout(connectTimeout);
        }
        if (null != readTimeout) {
            builder.readTimeout(readTimeout);
        }
        if (null != writeTimeout) {
            builder.writeTimeout(writeTimeout);
        }
        if (null != followRedirects) {
            builder.followRedirects(followRedirects);
        }
        if (null != followSslRedirects) {
            builder.followSslRedirects(followSslRedirects);
        }
        if (null != cookieJar) {
            builder.cookieJar(cookieJar);
        }
        if (null != authenticator) {
            builder.authenticator(authenticator);
        }
        if (null != interceptor) {
            builder.addInterceptor(interceptor);
        }
        if (null != retryPolicy) {
            builder.addInterceptor(new RetryInterceptor(retryPolicy));
        }
        if (null != proxy) {
            builder.proxy(proxy);
        }
        if (null != proxySelector) {
            builder.proxySelector(proxySelector);
        }
        if (null != proxyAuthenticator) {
            builder.proxyAuthenticator(proxyAuthenticator);
        }
        if (null != hostnameVerifier) {
            builder.hostnameVerifier(hostnameVerifier);
        }
        if (null != sslSocketFactory && null != trustManager) {
            builder.sslSocketFactory(sslSocketFactory, trustManager);
        }
        if (Boolean.TRUE.equals(disableSslValidation)) {
            disableSslValidation(builder);
        }
        if (null != level && !HttpLoggingInterceptor.Level.NONE.equals(level)) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(Slf4jLogger.LOGGER);
            loggingInterceptor.setLevel(level);
            builder.addInterceptor(loggingInterceptor);
        } else if (null != globalLoggingInterceptor) {
            builder.addInterceptor(globalLoggingInterceptor);
        }
        requestBuilder.url(urlBuilder.build());
        if (HttpMethod.requiresRequestBody(method) && requestBody == null) {
            requestBuilder.method(method, Util.EMPTY_REQUEST);
        } else {
            requestBuilder.method(method, requestBody);
        }
        return builder.build().newCall(requestBuilder.build());
    }

    /**
     * 同步请求
     */
    public final SyncCall execute() {
        return new SyncCall(newCall(httpClient));
    }

    /**
     * 异步请求
     */
    public final AsyncCall async() {
        return new AsyncCall(newCall(httpClient));
    }

    /**
     * 异步请求
     */
    public CompletableFuture<ResponseSpec> enqueue() {
        CompletableFuture<ResponseSpec> future = new CompletableFuture<>();
        Call call = newCall(httpClient);
        call.enqueue(new CompletableCallback(future));
        return future;
    }

    public static void setHttpClient(OkHttpClient httpClient) {
        HttpRequest.httpClient = httpClient;
    }

    public static void setGlobalLog(HttpLoggingInterceptor.Level level) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(Slf4jLogger.LOGGER);
        loggingInterceptor.setLevel(level);
        HttpRequest.globalLoggingInterceptor = loggingInterceptor;
    }

    static String parseValue(Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof String) {
            return (String) value;
        }
        return String.valueOf(value);
    }

    /**
     * SECURE_RANDOM
     */
    private static volatile SecureRandom SECURE_RANDOM;

    private static SecureRandom getSecureRandom() {
        if (SECURE_RANDOM == null) {
            synchronized (HttpRequest.class) {
                if (SECURE_RANDOM == null) {
                    SECURE_RANDOM = new SecureRandom();
                }
            }
        }
        return SECURE_RANDOM;
    }

    private static void disableSslValidation(OkHttpClient.Builder builder) {
        try {
            X509TrustManager disabledTrustManager = DisableValidationTrustManager.INSTANCE;
            TrustManager[] trustManagers = new TrustManager[]{disabledTrustManager};
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManagers, HttpRequest.getSecureRandom());
            SSLSocketFactory disabledSslSocketFactory = sslContext.getSocketFactory();
            builder.sslSocketFactory(disabledSslSocketFactory, disabledTrustManager);
            builder.hostnameVerifier(TrustAllHostNames.INSTANCE);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw Exceptions.unchecked(e);
        }
    }
}
