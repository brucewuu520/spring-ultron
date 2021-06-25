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

package org.springultron.captcha;

import org.springultron.captcha.draw.CaptchaDraw;
import org.springultron.captcha.draw.MathCaptchaDraw;
import org.springultron.captcha.draw.RandomCaptchaDraw;

/**
 * 验证码类型枚举
 *
 * @author L.cm
 */
public enum CaptchaType {
    /**
     * 随机字符串
     */
    RANDOM(new RandomCaptchaDraw()),

    /**
     * 算术
     */
    MATH(new MathCaptchaDraw());

    private final CaptchaDraw captchaDraw;

    CaptchaType(CaptchaDraw captchaDraw) {
        this.captchaDraw = captchaDraw;
    }

    public CaptchaDraw getCaptchaDraw() {
        return captchaDraw;
    }
}
