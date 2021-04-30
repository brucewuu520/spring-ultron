package org.springultron.core.utils;

import org.springultron.core.exception.Exceptions;
import org.springultron.core.pool.PatternPool;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * IP地址工具类
 *
 * @author brucewuu
 * @date 2019/11/17 12:12
 */
public class IpUtils {

    private IpUtils() {
    }

    public static final String LOCAL_HOST = "127.0.0.1";

    /**
     * 获取服务器 Host Name
     *
     * @return Host Name
     */
    public static String getHostName() {
        String hostname;
        try {
            InetAddress address = InetAddress.getLocalHost();
            // force a best effort reverse DNS lookup
            hostname = address.getHostName();
            if (StringUtils.isEmpty(hostname)) {
                hostname = address.toString();
            }
        } catch (UnknownHostException ignore) {
            hostname = LOCAL_HOST;
        }
        return hostname;
    }

    /**
     * 获取服务器 Host Ip
     *
     * @return Host Ip
     */
    public static String getHostIp() {
        String hostAddress;
        try {
            InetAddress address = IpUtils.getLocalHostLanAddress();
            // force a best effort reverse DNS lookup
            hostAddress = address.getHostAddress();
            if (StringUtils.isEmpty(hostAddress)) {
                hostAddress = address.toString();
            }
        } catch (UnknownHostException ignore) {
            hostAddress = LOCAL_HOST;
        }
        return hostAddress;
    }

    /**
     * 将 IP 转成 InetAddress
     *
     * @param ip ip
     * @return InetAddress
     */
    public static InetAddress getInetAddress(String ip) {
        try {
            return InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 判断是否内网 ip
     *
     * @param ip ip
     * @return boolean
     */
    public static boolean isInternalIp(String ip) {
        return isInternalIp(getInetAddress(ip));
    }

    /**
     * 判断是否内网 ip
     *
     * @param address InetAddress
     * @return boolean
     */
    public static boolean isInternalIp(InetAddress address) {
        if (isLocalIp(address)) {
            return true;
        }
        return isInternalIp(address.getAddress());
    }

    /**
     * 判断是否本地 ip
     *
     * @param address InetAddress
     * @return boolean
     */
    public static boolean isLocalIp(InetAddress address) {
        return address.isAnyLocalAddress()
                || address.isLoopbackAddress()
                || address.isSiteLocalAddress();
    }

    /**
     * 判断是否内网 ip
     *
     * @param addr ip
     * @return boolean
     */
    public static boolean isInternalIp(byte[] addr) {
        final byte b0 = addr[0];
        final byte b1 = addr[1];
        //10.x.x.x/8
        final byte section1 = 0x0A;
        //172.16.x.x/12
        final byte section2 = (byte) 0xAC;
        final byte section3 = (byte) 0x10;
        final byte section4 = (byte) 0x1F;
        //192.168.x.x/16
        final byte section5 = (byte) 0xC0;
        final byte section6 = (byte) 0xA8;
        switch (b0) {
            case section1:
                return true;
            case section2:
                if (b1 >= section3 && b1 <= section4) {
                    return true;
                }
            case section5:
                if (b1 == section6) {
                    return true;
                }
            default:
                return false;
        }
    }

    /**
     * 根据long值获取IP V4地址
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

    private static InetAddress getLocalHostLanAddress() throws UnknownHostException {
        try {
            InetAddress candidateAddress = null;
            // Iterate all NICs (network interface cards)...
            for (Enumeration<NetworkInterface> iFaces = NetworkInterface.getNetworkInterfaces(); iFaces.hasMoreElements(); ) {
                NetworkInterface iFace = iFaces.nextElement();
                // Iterate all IP addresses assigned to each card...
                for (Enumeration<InetAddress> inetAdders = iFace.getInetAddresses(); inetAdders.hasMoreElements(); ) {
                    InetAddress inetAddr = inetAdders.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {
                        if (inetAddr.isSiteLocalAddress()) {
                            // Found non-loopback site-local address. Return it immediately...
                            return inetAddr;
                        } else if (candidateAddress == null) {
                            // Found non-loopback address, but not necessarily site-local.
                            // Store it as a candidate to be returned if site-local address is not subsequently found...
                            candidateAddress = inetAddr;
                            // Note that we don't repeatedly assign non-loopback non-site-local addresses as candidates,
                            // only the first. For subsequent iterations, candidate will be non-null.
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                // We did not find a site-local address, but we found some other non-loopback address.
                // Server might have a non-site-local address assigned to its NIC (or it might be running
                // IPv6 which deprecates the "site-local" concept).
                // Return this non-loopback candidate address...
                return candidateAddress;
            }
            // At this point, we did not find a non-loopback address.
            // Fall back to returning whatever InetAddress.getLocalHost() returns...
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress;
        } catch (Exception e) {
            UnknownHostException unknownHostException = new UnknownHostException("Failed to determine LAN address: " + e);
            unknownHostException.initCause(e);
            throw unknownHostException;
        }
    }
}