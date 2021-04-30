package org.springultron.captcha.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.Base64Utils;
import org.springframework.util.FastByteArrayOutputStream;
import org.springultron.captcha.dto.CaptchaCode;

import java.io.OutputStream;

/**
 * 验证码服务
 *
 * @author brucewuu
 * @date 2021/4/13 下午12:05
 */
public interface CaptchaService {
    /**
     * 生成图形验证码
     *
     * @param cacheKey     自定义缓存key
     * @param outputStream OutputStream
     */
    void generate(String cacheKey, OutputStream outputStream);

    /**
     * 生成图形验证码 字节数组
     *
     * @param cacheKey 自定义缓存key
     * @return 验证码字节数组
     */
    default byte[] generateBytes(String cacheKey) {
        FastByteArrayOutputStream outputStream = new FastByteArrayOutputStream();
        this.generate(cacheKey, outputStream);
        return outputStream.toByteArray();
    }

    /**
     * 生成图形验证码
     *
     * @param cacheKey 自定义缓存key
     * @return {@link ByteArrayResource}
     */
    default ByteArrayResource generateByteResource(String cacheKey) {
        return new ByteArrayResource(this.generateBytes(cacheKey));
    }

    /**
     * 生成图形验证码 base64字符串
     *
     * @param cacheKey 自定义缓存key
     * @return base64 图片
     */
    default String generateBase64(String cacheKey) {
        FastByteArrayOutputStream outputStream = new FastByteArrayOutputStream();
        this.generate(cacheKey, outputStream);
        return "data:image/jpeg;base64," + Base64Utils.encodeToString(outputStream.toByteArray());
    }

    /**
     * 生成图形验证码
     *
     * @param cacheKey 自定义缓存key
     * @return {@link CaptchaCode}
     */
    default CaptchaCode generate(String cacheKey) {
        String base64Code = generateBase64(cacheKey);
        return CaptchaCode.of(cacheKey, base64Code);
    }

    /**
     * 校验验证码
     *
     * @param cacheKey         验证码缓存key
     * @param userInputCaptcha 用户输入的验证码
     * @return 是否成功
     */
    boolean validate(String cacheKey, String userInputCaptcha);
}
