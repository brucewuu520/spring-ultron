package org.springultron.wechat;

import okhttp3.MediaType;
import org.springultron.http.HttpResponse;
import org.springultron.wechat.dto.MediaFile;

import java.io.BufferedInputStream;

/**
 * 下载工具
 *
 * @author brucewuu
 * @date 2021/4/21 下午6:00
 */
public class DownloadUtils {

    public static MediaFile download(HttpResponse response) {
        MediaFile mediaFile = new MediaFile();
        MediaType mediaType = response.contentType();
        if (mediaType == null || "text".equals(mediaType.type())) {
            mediaFile.setError(response.asString());
        } else {
            BufferedInputStream bis = new BufferedInputStream(response.asStream());
            String ds = response.rawResponse().header("Content-disposition");
            if (ds != null) {
                String fullName = ds.substring(ds.indexOf("filename=\"") + 10, ds.length() - 1);
                String relName = fullName.substring(0, fullName.lastIndexOf("."));
                String suffix = fullName.substring(relName.length() + 1);
                mediaFile.setFullName(fullName);
                mediaFile.setFileName(relName);
                mediaFile.setSuffix(suffix);
            }
            mediaFile.setContentLength(response.contentLength());
            mediaFile.setContentType(mediaType.toString());
            mediaFile.setStream(bis);
        }
        return mediaFile;
    }
}
