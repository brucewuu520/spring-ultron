package org.springultron.core.io;

import org.springframework.lang.Nullable;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.util.UriUtils;
import org.springultron.core.exception.Exceptions;
import org.springultron.core.pool.StringPool;
import org.springultron.core.utils.StringUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
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

    /**
     * 获取文件名 xxx.jpg
     *
     * @param path 文件路径
     * @return {String}
     */
    @Nullable
    public static String getFilename(@Nullable String path) {
        return StringUtils.getFilename(path);
    }

    /**
     * 获取文件后缀名 jpg
     *
     * @param fullName 文件全名
     * @return {String}
     */
    @Nullable
    public static String getFileExtension(@Nullable String fullName) {
        return StringUtils.getFilenameExtension(fullName);
    }

    /**
     * 获取文件后缀名 .jpg
     *
     * @param fullName 文件全名
     * @return {String}
     */
    @Nullable
    public static String getFileExtensionWithDot(@Nullable String fullName) {
        if (fullName == null) {
            return null;
        }

        int extIndex = fullName.lastIndexOf(StringPool.DOT);
        if (extIndex == -1) {
            return null;
        }

        int folderIndex = fullName.lastIndexOf(StringPool.SLASH);
        if (folderIndex > extIndex) {
            return null;
        }

        return fullName.substring(extIndex);
    }

    /**
     * 获取文件名，去除后缀名
     *
     * @param path 文件
     * @return {String}
     */
    @Nullable
    public static String getPathWithoutExtension(@Nullable String path) {
        if (path == null) {
            return null;
        }
        return StringUtils.stripFilenameExtension(path);
    }

    /**
     * Reads the contents of a file into a String.
     * The file is always closed.
     *
     * @param file the file to read, must not be {@code null}
     * @return the file contents, never {@code null}
     */
    public static String readToString(final File file) {
        return readToString(file, StandardCharsets.UTF_8);
    }

    /**
     * Reads the contents of a file into a String.
     * The file is always closed.
     *
     * @param file     the file to read, must not be {@code null}
     * @param encoding the encoding to use, {@code null} means platform default
     * @return the file contents, never {@code null}
     */
    public static String readToString(final File file, final Charset encoding) {
        try (final InputStream in = Files.newInputStream(file.toPath())) {
            return IOUtils.readString(in, encoding);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * Reads the contents of a file into a String.
     * The file is always closed.
     *
     * @param file the file to read, must not be {@code null}
     * @return the file contents, never {@code null}
     */
    public static byte[] readToByteArray(final File file) {
        try (final InputStream in = Files.newInputStream(file.toPath())) {
            return IOUtils.readByteArray(in);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * Writes a String to a file creating the file if it does not exist.
     *
     * @param file the file to write
     * @param data the content to write to the file
     */
    public static void writeToFile(final File file, final String data) {
        writeToFile(file, data, StandardCharsets.UTF_8, false);
    }

    /**
     * Writes a String to a file creating the file if it does not exist.
     *
     * @param file   the file to write
     * @param data   the content to write to the file
     * @param append if {@code true}, then the String will be added to the
     *               end of the file rather than overwriting
     */
    public static void writeToFile(final File file, final String data, final boolean append) {
        writeToFile(file, data, StandardCharsets.UTF_8, append);
    }

    /**
     * Writes a String to a file creating the file if it does not exist.
     *
     * @param file     the file to write
     * @param data     the content to write to the file
     * @param encoding the encoding to use, {@code null} means platform default
     */
    public static void writeToFile(final File file, final String data, final Charset encoding) {
        writeToFile(file, data, encoding, false);
    }

    /**
     * Writes a String to a file creating the file if it does not exist.
     *
     * @param file     the file to write
     * @param data     the content to write to the file
     * @param encoding the encoding to use, {@code null} means platform default
     * @param append   if {@code true}, then the String will be added to the
     *                 end of the file rather than overwriting
     */
    public static void writeToFile(final File file, final String data, final Charset encoding, final boolean append) {
        try (final FileOutputStream out = new FileOutputStream(file, append)) {
            IOUtils.write(data, out, encoding);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }
}
