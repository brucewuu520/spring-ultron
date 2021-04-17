package org.springultron.core.utils;

import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springultron.core.jackson.Jackson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Servlet 工具
 *
 * @author brucewuu
 * @date 2019-06-26 15:47
 */
public class WebUtils extends org.springframework.web.util.WebUtils {

    private WebUtils() {
    }

    /**
     * 获取 HttpServletRequest
     *
     * @return HttpServletRequest
     */
    @Nullable
    public static HttpServletRequest getRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .map(a -> (ServletRequestAttributes) a)
                .map(ServletRequestAttributes::getRequest)
                .orElse(null);
    }

    /**
     * 获取 HttpServletResponse
     *
     * @return HttpServletResponse
     */
    @Nullable
    public static HttpServletResponse getResponse() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .map(a -> (ServletRequestAttributes) a)
                .map(ServletRequestAttributes::getResponse)
                .orElse(null);
    }

    /**
     * http响应返回（json）
     *
     * @param response   HttpServletResponse
     * @param jsonObject ResponseBody对象
     */
    public static void renderJson(HttpServletResponse response, Object jsonObject) {
        if (jsonObject == null) {
            renderJson(response, null);
        } else {
            renderJson(response, Jackson.toJson(jsonObject));
        }
    }

    /**
     * http响应返回（json）
     *
     * @param response HttpServletResponse
     * @param json     json字符串
     */
    public static void renderJson(HttpServletResponse response, String json) {
        renderText(response, json, MediaType.APPLICATION_JSON_VALUE);
    }

    /**
     * http响应返回
     *
     * @param response HttpServletResponse
     * @param text     文本
     */
    public static void renderText(HttpServletResponse response, String text) {
        renderText(response, text, MediaType.TEXT_PLAIN_VALUE);
    }

    /**
     * http响应返回
     *
     * @param response    HttpServletResponse
     * @param text        返回文本
     * @param contentType Content-Type
     */
    public static void renderText(HttpServletResponse response, String text, String contentType) {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(contentType);
        try (PrintWriter writer = response.getWriter()) {
            if (text == null) {
                writer.write("null");
            } else {
                writer.write(text);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取远程请求IP地址
     *
     * @return {String}
     */
    @Nullable
    public static String getRemoteIP() {
        return getRemoteIP(WebUtils.getRequest());
    }

    private static final Predicate<String> IS_BLANK_IP = (ip) -> StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip);

    /**
     * 获取远程请求IP地址
     * <p>
     * 使用Nginx则不能通过request.getRemoteAddr()获取IP地址
     * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非 unknown的有效IP字符串，则为真实IP地址
     * </p>
     *
     * @param request HttpServletRequest
     * @return ip address
     */
    @Nullable
    public static String getRemoteIP(HttpServletRequest request) {
        if (null == request) {
            return null;
        }
        String ip = request.getHeader("X-Requested-For");
        if (IS_BLANK_IP.test(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (IS_BLANK_IP.test(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (IS_BLANK_IP.test(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (IS_BLANK_IP.test(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (IS_BLANK_IP.test(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (IS_BLANK_IP.test(ip)) {
            ip = request.getRemoteAddr();
        }
        if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (final UnknownHostException e) {
                // 未知主机异常
            }
        }
        return Optional.ofNullable(ip).map(p -> {
            if (p.contains(",")) {
                return p.split(",")[0];
            } else {
                return p;
            }
        }).orElse(null);
    }
}