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

package org.springultron.logging.listener;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springultron.logging.UltronLoggingProperties;
import org.springultron.logging.utils.LoggingUtils;

/**
 * 应用启动完毕监听
 *
 * @author L.cm
 * @author brucewuu
 * @date 2021/4/9 上午10:15
 */
public class LoggingStartedEventListener {
    private final UltronLoggingProperties properties;

    public LoggingStartedEventListener(UltronLoggingProperties properties) {
        this.properties = properties;
    }

    @Async
    @Order
    @EventListener(WebServerInitializedEvent.class)
    public void afterStart() {
        // 关闭控制台日志输出
        if (properties.isCloseConsole()) {
            LoggingUtils.detachAppender(LoggingUtils.CONSOLE_APPENDER_NAME);
        }
        // 关闭日志文件输出
        if (properties.isCloseFile()) {
            LoggingUtils.detachAppender(LoggingUtils.FILE_INFO_APPENDER_NAME);
            LoggingUtils.detachAppender(LoggingUtils.FILE_ERROR_APPENDER_NAME);
        }
    }
}
