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

package org.springultron.captcha.draw;

import java.awt.*;
import java.util.Random;

/**
 * 验证码工具类
 *
 * @author L.cm
 */
public final class CaptchaUtils {

    /**
     * 生成指定范围的随机数
     */
    public static int randNum(Random random, int min, int max) {
        int diff = max - min;
        int rand = random.nextInt(diff);
        return min + rand;
    }

    /**
     * 给定范围获得随机颜色
     */
    public static Color randColor(Random random, int fc, int bc) {
        int colorMax = 255;
        if (fc > colorMax) {
            fc = 255;
        }
        if (bc > colorMax) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

}
