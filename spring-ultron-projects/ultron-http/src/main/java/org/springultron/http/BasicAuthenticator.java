package org.springultron.http;

import okhttp3.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * http basic认证
 *
 * @author brucewuu
 * @date 2020/4/5 19:58
 */
public class BasicAuthenticator implements Authenticator {
    private final String username;
    private final String password;

    public BasicAuthenticator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, @NonNull Response response) throws IOException {
        String credentials = Credentials.basic(username, password, StandardCharsets.UTF_8);
        return response.request().newBuilder().header("Authorization", credentials).build();
    }
}
