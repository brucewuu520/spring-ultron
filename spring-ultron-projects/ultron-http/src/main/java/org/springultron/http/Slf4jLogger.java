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

import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

/**
 * Slf4j 打印日志
 *
 * @author brucewuu
 * @date 2019-06-30 16:23
 */
public class Slf4jLogger implements HttpLoggingInterceptor.Logger {
    private static final Logger log = LoggerFactory.getLogger("HttpRequest");

    static final HttpLoggingInterceptor.Logger LOGGER = new Slf4jLogger();

    @Override
    public void log(@NonNull String message) {
        log.info(message);
    }
}
