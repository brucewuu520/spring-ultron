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

import okhttp3.FormBody;

import javax.annotation.Nullable;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * form表单构造器
 *
 * @author brucewuu
 * @date 2019-06-30 10:28
 */
public class FormBuilder {
    private final HttpRequest request;
    private final FormBody.Builder formBuilder;

    static FormBuilder of(final HttpRequest request) {
        return new FormBuilder(request);
    }

    static FormBuilder of(final HttpRequest request, @Nullable Charset charset) {
        return new FormBuilder(request, charset);
    }

    private FormBuilder(final HttpRequest request) {
        this.request = request;
        this.formBuilder = new FormBody.Builder();
    }

    private FormBuilder(final HttpRequest request, @Nullable Charset charset) {
        this.request = request;
        this.formBuilder = new FormBody.Builder(charset);
    }

    public FormBuilder add(final String name, final Object value) {
        this.formBuilder.add(name, HttpRequest.parseValue(value));
        return this;
    }

    public FormBuilder addEncoded(final String name, final Object value) {
        this.formBuilder.addEncoded(name, HttpRequest.parseValue(value));
        return this;
    }

    public FormBuilder addMap(final Map<String, Object> formMap) {
        if (formMap != null && !formMap.isEmpty()) {
            formMap.forEach(this::add);
        }
        return this;
    }

    public HttpRequest build() {
        FormBody formBody = formBuilder.build();
        this.request.form(formBody);
        return request;
    }
}
