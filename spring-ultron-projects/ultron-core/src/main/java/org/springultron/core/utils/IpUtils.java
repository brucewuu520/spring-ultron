package org.springultron.core.utils;

import org.springframework.lang.Nullable;
import org.springultron.core.pool.PatternPool;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * IP地址工具类
 *
 * @author brucewuu
 * @date 2019/11/17 12:12
 */
public class IpUtils {

    private IpUtils() {
    }

    /**
     * 获取ip
     *
     * @return {String}
     */
    @Nullable
    public static String getIP() {
        return getIP(WebUtils.getRequest());
    }

    /**
     * 获取IP地址
     * <p>
     * 使用Nginx则不能通过request.getRemoteAddr()获取IP地址
     * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非 unknown的有效IP字符串，则为真实IP地址
     * </p>
     *
     * @param request HttpServletRequest
     * @return ip address
     */
    @Nullable
    public static String getIP(HttpServletRequest request) {
        if (null == request) {
            return null;
        }
        final Predicate<String> predicate = (ip) -> StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip);
        String ip = request.getHeader("X-Requested-For");
        if (predicate.test(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (predicate.test(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (predicate.test(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (predicate.test(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (predicate.test(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (predicate.test(ip)) {
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

    /**
     * 根据long值获取ip v4地址
     *
     * @param ip IP的long表示形式
     * @return IP V4 地址
     */
    public static String longToIpv4(long ip) {
        // 直接右移24位
        return (ip >>> 24) +
                "." +
                // 将高8位置0，然后右移16位
                ((ip & 0x00FFFFFF) >>> 16) +
                "." +
                ((ip & 0x0000FFFF) >>> 8) +
                "." +
                (ip & 0x000000FF);
    }

    /**
     * 根据ip地址计算出long型的数据
     *
     * @param ipStr IP V4 地址
     * @return long值
     */
    public static long ipv4ToLong(String ipStr) {
        if (RegexUtils.isMatch(ipStr, PatternPool.IPV4)) {
            long[] ip = new long[4];
            // 先找到IP地址字符串中.的位置
            int position1 = ipStr.indexOf(".");
            int position2 = ipStr.indexOf(".", position1 + 1);
            int position3 = ipStr.indexOf(".", position2 + 1);
            // 将每个.之间的字符串转换成整型
            ip[0] = Long.parseLong(ipStr.substring(0, position1));
            ip[1] = Long.parseLong(ipStr.substring(position1 + 1, position2));
            ip[2] = Long.parseLong(ipStr.substring(position2 + 1, position3));
            ip[3] = Long.parseLong(ipStr.substring(position3 + 1));
            return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
        }
        return 0;
    }
}