package org.springultron.core.utils;

import org.springframework.lang.Nullable;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.util.UriUtils;
import org.springultron.core.pool.StringPool;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 文件操作工具
 *
 * @author brucewuu
 * @date 2019-07-21 10:57
 */
public class FileUtils extends FileCopyUtils {

    private FileUtils() {
    }

    /**
     * 获取jar包运行时的当前目录
     *
     * @return {String}
     */
    public static String getJarPath() {
        String path = null;
        try {
            URL url = FileUtils.class.getResource(StringPool.SLASH).toURI().toURL();
            path = FileUtils.toFilePath(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == path) {
            path = FileUtils.class.getResource(StringPool.EMPTY).getPath();
            path = new File(path).getParentFile().getParentFile().getAbsolutePath();
        }
        return path;
    }

    @Nullable
    public static String toFilePath(@Nullable URL url) {
        if (url == null) {
            return null;
        }
        String protocol = url.getProtocol();
        String file = UriUtils.decode(url.getPath(), StandardCharsets.UTF_8);
        if (ResourceUtils.URL_PROTOCOL_FILE.equals(protocol)) {
            return new File(file).getParentFile().getParentFile().getAbsolutePath();
        } else if (ResourceUtils.URL_PROTOCOL_JAR.equals(protocol)
                || ResourceUtils.URL_PROTOCOL_ZIP.equals(protocol)) {
            int ipos = file.indexOf(ResourceUtils.JAR_URL_SEPARATOR);
            if (ipos > 0) {
                file = file.substring(0, ipos);
            }
            if (file.startsWith(ResourceUtils.FILE_URL_PREFIX)) {
                file = file.substring(ResourceUtils.FILE_URL_PREFIX.length());
            }
            return new File(file).getParentFile().getAbsolutePath();
        }
        return file;
    }

    /**
     * 获得输入流
     *
     * @param path {@link Path}
     * @return 输入流
     */
    public static BufferedInputStream getInputStream(Path path) throws IOException {
        return new BufferedInputStream(Files.newInputStream(path));
    }

    /**
     * 获得输入流
     *
     * @param file 文件
     * @return 输入流
     */
    public static BufferedInputStream getInputStream(File file) throws IOException {
        return new BufferedInputStream(new FileInputStream(file));
    }

    /**
     * 获得输入流
     *
     * @param path 文件绝对路径
     * @return 输入流
     */
    public static BufferedInputStream getInputStream(String path) throws IOException {
        return new BufferedInputStream(new FileInputStream(new File(path)));
    }
}
