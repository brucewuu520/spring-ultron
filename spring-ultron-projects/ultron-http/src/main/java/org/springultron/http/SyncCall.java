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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import okhttp3.Call;
import okhttp3.Request;
import org.springultron.core.exception.Exceptions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 同步请求
 *
 * @author brucewuu
 * @date 2020/4/7 12:40
 */
public class SyncCall {
    private final Call call;
    private BiConsumer<Request, IOException> failConsumer = (r, e) -> {};

    SyncCall(Call call) {
        this.call = call;
    }

    public SyncCall fail(BiConsumer<Request, IOException> failConsumer) {
        this.failConsumer = failConsumer;
        return this;
    }

    public void callback(Consumer<ResponseSpec> responseConsumer) {
        try (HttpResponse httpResponse = HttpResponse.of(call.execute())) {
            responseConsumer.accept(httpResponse);
        } catch (IOException e) {
            failConsumer.accept(call.request(), e);
        }
    }

    public <R> R response(Function<ResponseSpec, R> func) {
        try (HttpResponse httpResponse = HttpResponse.of(call.execute())) {
            return func.apply(httpResponse);
        } catch (IOException e) {
            failConsumer.accept(call.request(), e);
            throw Exceptions.unchecked(e);
        }
    }

    public HttpResponse response() {
        try {
            return HttpResponse.of(call.execute());
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    public InputStream asStream() {
        return response(ResponseSpec::asStream);
    }

    public byte[] asBytes() {
        return response(ResponseSpec::asBytes);
    }

    public JsonNode asJsonNode() {
        return response(ResponseSpec::asJsonNode);
    }

    public String asString() {
        return response(ResponseSpec::asString);
    }

    public <T> T asObject(Class<T> valueType) {
        return response(responseSpec -> responseSpec.asObject(valueType));
    }

    public <T> T asObject(TypeReference<T> valueTypeRef) {
        return response(responseSpec -> responseSpec.asObject(valueTypeRef));
    }

    public Map<String, Object> asMap() {
        return response(ResponseSpec::asMap);
    }

    public <V> Map<String, V> asMap(Class<V> valueClass) {
        return response(responseSpec -> responseSpec.asMap(valueClass));
    }

    public <K, V> Map<K, V> asMap(Class<K> keyClass, Class<V> valueClass) {
        return response(responseSpec -> responseSpec.asMap(keyClass, valueClass));
    }

    public <T> List<T> asList(Class<T> valueType) {
        return response(responseSpec -> responseSpec.asList(valueType));
    }

    public <T> List<T> asList(TypeReference<? extends List<T>> valueTypeRef) {
        return response(responseSpec -> responseSpec.asList(valueTypeRef));
    }

    public File asFile(File file) {
        return response(responseSpec -> responseSpec.asFile(file));
    }

    public Path asFile(Path path) {
        return response(responseSpec -> responseSpec.asFile(path));
    }
}
