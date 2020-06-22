package org.springultron.qrcode.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

/**
 * 下载文件工具类
 *
 * @author brucewuu
 * @date 2020/6/10 13:21
 */
public class DownloadUtils {

    public static InputStream downloadFile(String src) throws IOException {
        return downloadFile(URI.create(src));
    }

    public static InputStream downloadFile(URI uri) {
        try {
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/octet-stream");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.connect();
            return connection.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
