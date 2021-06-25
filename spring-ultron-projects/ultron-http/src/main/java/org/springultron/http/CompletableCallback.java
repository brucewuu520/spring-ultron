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
