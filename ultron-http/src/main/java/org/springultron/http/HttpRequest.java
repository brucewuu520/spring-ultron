package org.springultron.http;

import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springultron.core.exception.Exceptions;
import org.springultron.core.utils.Jackson;
import org.springultron.http.ssl.DisableValidationTrustManager;
import org.springultron.http.ssl.TrustAllHostNames;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Objects;

/**
 * okhttp3 请求封装
 *
 * @author brucewuu
 * @date 2019-06-29 22:15
 */
public class HttpRequest {
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8");

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

    public static HttpRequest get(final URL url) {
        return new HttpRequest(Objects.requireNonNull(HttpUrl.get(url)).newBuilder(), "GET");
    }

    public static HttpRequest get(final HttpUrl.Builder urlBuilder) {
        return new HttpRequest(urlBuilder, "GET");
    }

    public static HttpRequest post(final String url) {
        return new HttpRequest(HttpUrl.get(url).newBuilder(), "POST");
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

    public static HttpRequest patch(final URL url) {
        return new HttpRequest(Objects.requireNonNull(HttpUrl.get(url)).newBuilder(), "PATCH");
    }

    public static HttpRequest patch(final HttpUrl.Builder urlBuilder) {
        return new HttpRequest(urlBuilder, "PATCH");
    }

    public static HttpRequest put(final String url) {
        return new HttpRequest(HttpUrl.get(url).newBuilder(), "PUT");
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

    public HttpRequest query(final String name, final String value) {
        this.urlBuilder.addQueryParameter(name, value);
        return this;
    }

    public HttpRequest queryEncoded(final String name, final String value) {
        this.urlBuilder.addEncodedQueryParameter(name, value);
        return this;
    }

    public HttpRequest form(final FormBody formBody) {
        this.requestBody = formBody;
        return this;
    }

    public FormBuilder formBuilder() {
        return FormBuilder.of(this);
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

    public HttpRequest bodyJson(final String json) {
        this.requestBody = RequestBody.create(json, MEDIA_TYPE_JSON);
        return this;
    }

    public HttpRequest bodyJson(final Object jsonObject) {
        return bodyJson(Jackson.toJson(jsonObject));
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

    public HttpRequest log() {
        this.level = HttpLoggingInterceptor.Level.BODY;
        return this;
    }

    public HttpRequest log(HttpLoggingInterceptor.Level level) {
        this.level = level;
        return this;
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
            loggingInterceptor.level(level);
            builder.addInterceptor(loggingInterceptor);
        } else if (null != globalLoggingInterceptor) {
            builder.addInterceptor(globalLoggingInterceptor);
        }
        requestBuilder.url(urlBuilder.build()).method(method, requestBody);
        return builder.build().newCall(requestBuilder.build());
    }

    /**
     * 发起同步请求
     *
     * @return 响应体
     */
    public final HttpResponse execute() {
        try {
            return HttpResponse.of(newCall(httpClient).execute());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 发起异步请求
     *
     * @param callback 异步回调
     */
    public final void enqueue(final Callback callback) {
        newCall(httpClient).enqueue(callback);
    }

    public static void setHttpClient(OkHttpClient httpClient) {
        HttpRequest.httpClient = httpClient;
    }

    public static void setGlobalLog(HttpLoggingInterceptor.Level level) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(Slf4jLogger.LOGGER);
        loggingInterceptor.level(level);
        HttpRequest.globalLoggingInterceptor = loggingInterceptor;
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
