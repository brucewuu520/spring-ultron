package org.springultron.boot.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springultron.core.utils.FileUtils;

/**
 * 文件上传配置
 *
 * @author brucewuu
 * @date 2019/10/8 09:47
 */
@ConfigurationProperties(prefix = "ultron.upload")
public class UltronUploadProperties {
    /**
     * 上传的文件 路径匹配
     */
    private String uploadPathPattern = "/upload/**";

    /**
     * 文件保存目录，默认：jar 包同级目录
     */
    private String savePath = FileUtils.getJarPath();

    public String getUploadPathPattern() {
        return uploadPathPattern;
    }

    public void setUploadPathPattern(String uploadPathPattern) {
        this.uploadPathPattern = uploadPathPattern;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }
}