package org.springultron.core.utils;

import org.springframework.core.io.*;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * 资源工具
 *
 * @Auther: brucewuu
 * @Date: 2019-06-07 17:05
 * @Description:
 */
public class ResourceUtils extends org.springframework.util.ResourceUtils {
    private static final String HTTP_REGEX = "^https?:.+$";
    private static final String FTP_URL_PREFIX = "ftp:";

    /**
     * 获取资源
     * <p>
     * 支持以下协议：
     * <p>
     * 1. classpath:
     * 2. file:
     * 3. ftp:
     * 4. http: and https:
     * 5. C:/dir1/ and /Users/lcm
     * </p>
     *
     * @param resourceLocation 资源路径
     * @return {Resource}
     * @throws IOException IOException
     */
    public static Resource getResource(String resourceLocation) throws IOException {
        Assert.notNull(resourceLocation, "Resource location must not be null");
        if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
            return new ClassPathResource(resourceLocation);
        }
        if (resourceLocation.startsWith(FTP_URL_PREFIX)) {
            return new UrlResource(resourceLocation);
        }
        if (resourceLocation.matches(HTTP_REGEX)) {
            return new UrlResource(resourceLocation);
        }
        if (resourceLocation.startsWith(FILE_URL_PREFIX)) {
            return new FileUrlResource(resourceLocation);
        }
        return new FileSystemResource(resourceLocation);
    }

    /**
     * 读取资源文件字符串内容
     *
     * @param resourceLocation 资源路径
     * @return 读取内容
     */
    public static String readString(String resourceLocation) {
        try {
            return IOUtils.toString(getResource(resourceLocation).getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return Strings.EMPTY;
        }
    }
}
