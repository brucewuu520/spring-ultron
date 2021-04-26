package org.springultron.http;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * CompletableFuture callback
 *
 * @author brucewuu
 * @date 2021/4/23 上午10:48
 */
public class CompletableCallback implements Callback {
    private final CompletableFuture<ResponseSpec> future;

    public CompletableCallback(CompletableFuture<ResponseSpec> future) {
        this.future = future;
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        future.completeExceptionally(e);
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        try (HttpResponse httpResponse = HttpResponse.of(response)) {
            future.complete(httpResponse);
        }
    }
}
