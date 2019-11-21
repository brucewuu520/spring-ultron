package org.springultron.boot.base;

import org.springframework.web.multipart.MultipartFile;
import org.springultron.boot.props.UltronUploadProperties;
import org.springultron.core.result.ApiResult;
import org.springultron.core.utils.IdUtils;
import org.springultron.core.utils.Maps;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;

/**
 * Controller 基础方法
 *
 * @author brucewuu
 * @date 2019/10/8 15:35
 */
public interface IController {

    /**
     * redirect跳转
     *
     * @param url 目标url
     * @return 跳转地址
     */
    default String redirect(String url) {
        return "redirect:".concat(url);
    }

    /**
     * 图片上传方法
     *
     * @param file       文件
     * @param properties 文件上传路径配置
     * @return 上传结果
     */
    default ApiResult uploadImage(MultipartFile file, UltronUploadProperties properties) {
        if (file.isEmpty()) {
            return ApiResult.failed("图片不能为空");
        }
        String fileName = file.getOriginalFilename();
        if (null == fileName) {
            return ApiResult.failed("图片名称不能为空");
        }
        // 后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        if (!".jpg".equals(suffixName) && !".png".equals(suffixName)
                && !".jpeg".equals(suffixName) && !".gif".equals(suffixName)) {
            return ApiResult.failed("不是图片文件");
        }
        fileName = IdUtils.randomUUID() + suffixName;
        LocalDate localDate = LocalDate.now();
        final String destFile = properties.getUploadPathPattern().replace("*", "") +
                "image/" + localDate.getYear() + "/" + localDate.getMonthValue() + "/" + fileName;
        try {
            Path path = Paths.get(properties.getSavePath() + destFile);
            file.transferTo(path);
            HashMap<String, String> hashMap = Maps.newHashMap(1);
            hashMap.put("url", destFile);
            return ApiResult.success(hashMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ApiResult.failed("图片上传失败");
    }
}
