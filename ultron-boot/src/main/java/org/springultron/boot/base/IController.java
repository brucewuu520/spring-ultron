package org.springultron.boot.base;

import org.springframework.web.multipart.MultipartFile;
import org.springultron.boot.props.UltronUploadProperties;
import org.springultron.core.result.ApiResult;
import org.springultron.core.utils.Maps;
import org.springultron.core.utils.Strings;

import java.io.File;
import java.io.IOException;
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
        if (!suffixName.equals(".jpg") && !suffixName.equals(".png")
                && !suffixName.equals(".jpeg") && !suffixName.equals(".gif")) {
            return ApiResult.failed("不是图片文件");
        }
        fileName = Strings.randomUUID() + suffixName;
        LocalDate localDate = LocalDate.now();
        StringBuilder fileBuilder = new StringBuilder(58)
                .append(properties.getUploadPathPattern().replace("*", ""))
                .append("image/")
                .append(localDate.getYear())
                .append("/")
                .append(localDate.getMonthValue())
                .append("/")
                .append(fileName);
        final String filePath = fileBuilder.toString();
        File dest = new File(properties.getSavePath() + filePath);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
            HashMap<String, String> hashMap = Maps.newHashMap(1);
            hashMap.put("url", filePath);
            return ApiResult.success(hashMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ApiResult.failed("图片上传失败");
    }
}
