package org.springultron.http;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 异步请求
 *
 * @author brucewuu
 * @date 2020/4/7 11:26
 */
public class AsyncCall {
    private static final Consumer<ResponseSpec> DEFAULT_CONSUMER = r -> {};
    private static final BiConsumer<Request, IOException> DEFAULT_FAIL_CONSUMER = (r, e) -> {};
    private final Call call;
    private Consumer<ResponseSpec> callbackConsumer;
    private Consumer<ResponseSpec> successConsumer;
    private BiConsumer<Request, IOException> failConsumer;

    public AsyncCall(Call call) {
        this.call = call;
        this.callbackConsumer = DEFAULT_CONSUMER;
        this.successConsumer = DEFAULT_CONSUMER;
        this.failConsumer = DEFAULT_FAIL_CONSUMER;
    }

    public AsyncCall callback(Consumer<ResponseSpec> callbackConsumer) {
        this.callbackConsumer = callbackConsumer;
        return this;
    }

    public AsyncCall success(Consumer<ResponseSpec> successConsumer) {
        this.successConsumer = successConsumer;
        return this;
    }

    public AsyncCall fail(BiConsumer<Request, IOException> failConsumer) {
        this.failConsumer = failConsumer;
        return this;
    }

    public final void execute() {
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                failConsumer.accept(call.request(), e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (HttpResponse httpResponse = HttpResponse.of(response)) {
                    callbackConsumer.accept(httpResponse);
                    if (response.isSuccessful()) {
                        successConsumer.accept(httpResponse);
                    } else {
                        failConsumer.accept(call.request(), new IOException(response.message()));
                    }
                }
            }
        });
    }
}
