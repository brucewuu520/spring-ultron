package org.springultron.boot.base;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.multipart.MultipartFile;
import org.springultron.boot.props.UltronUploadProperties;
import org.springultron.core.result.ApiResult;
import org.springultron.core.result.IResultCode;
import org.springultron.core.utils.IdUtils;
import org.springultron.core.utils.Maps;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;

/**
 * Controller常用方法
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
     * 返回成功
     *
     * @param <T> 泛型标记
     * @return ApiResult
     */
    default <T> ApiResult<T> success() {
        return ApiResult.success();
    }

    /**
     * 返回成功
     *
     * @param data 数据
     * @param <T>  泛型标记
     * @return ApiResult
     */
    default <T> ApiResult<T> success(T data) {
        return ApiResult.success(data);
    }

    /**
     * 返回成功
     *
     * @param message 消息
     * @param <T>     泛型标记
     * @return ApiResult
     */
    default <T> ApiResult<T> success(String message) {
        return ApiResult.success(message);
    }

    /**
     * 返回成功
     *
     * @param data    数据
     * @param message 消息
     * @param <T>     泛型标记
     * @return ApiResult
     */
    default <T> ApiResult<T> success(T data, String message) {
        return ApiResult.success(data, message);
    }

    /**
     * 返回失败
     *
     * @param <T> 泛型标记
     * @return ApiResult
     */
    default <T> ApiResult<T> fail() {
        return ApiResult.fail();
    }

    /**
     * 返回失败
     *
     * @param message 异常信息
     * @param <T>     泛型标记
     * @return ApiResult
     */
    default <T> ApiResult<T> fail(String message) {
        return ApiResult.fail(message);
    }

    /**
     * 返回失败
     *
     * @param resultCode 异常枚举
     * @param <T>        泛型标记
     * @return ApiResult
     */
    default <T> ApiResult<T> fail(IResultCode resultCode) {
        return ApiResult.fail(resultCode);
    }

    /**
     * 返回失败
     *
     * @param code    异常状态码
     * @param message 异常信息
     * @param <T>     泛型标记
     * @return ApiResult
     */
    default <T> ApiResult<T> fail(int code, String message) {
        return ApiResult.fail(code, message);
    }

    /**
     * 根据状态判断返回成功or失败
     *
     * @param status 状态
     * @param <T>    泛型标记
     * @return ApiResult
     */
    default <T> ApiResult<T> status(boolean status) {
        return ApiResult.status(status);
    }

    /**
     * 根据状态判断返回成功or失败
     *
     * @param status  状态
     * @param message 异常信息
     * @param <T>     泛型标记
     * @return ApiResult
     */
    default <T> ApiResult<T> status(boolean status, String message) {
        return ApiResult.status(status, message);
    }

    /**
     * 根据状态判断返回成功or失败
     *
     * @param status    状态
     * @param errorCode 异常枚举
     * @param <T>       泛型标记
     * @return ApiResult
     */
    default <T> ApiResult<T> status(boolean status, IResultCode errorCode) {
        return ApiResult.status(status, errorCode);
    }

    /**
     * 图片上传方法
     *
     * @param file       文件
     * @param properties 文件上传路径配置
     * @return ApiResult
     */
    default ApiResult<Object> uploadImage(MultipartFile file, UltronUploadProperties properties) {
        if (file.isEmpty()) {
            return ApiResult.fail("图片不能为空");
        }
        String fileName = file.getOriginalFilename();
        if (null == fileName) {
            return ApiResult.fail("图片名称不能为空");
        }
        // 后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        if (!".jpg".equals(suffixName) && !".png".equals(suffixName)
                && !".jpeg".equals(suffixName) && !".gif".equals(suffixName)) {
            return ApiResult.fail("不是图片文件");
        }
        fileName = IdUtils.randomUUID() + suffixName;
        LocalDate localDate = LocalDate.now();
        final String destFilePath = properties.getUploadPathPattern().replace("*", "") +
                "image/" + localDate.getYear() + "/" + localDate.getMonthValue() + "/" + fileName;
        try {
            File destFile = new File(properties.getSavePath() + destFilePath);
            if (!destFile.exists()) {
                if (!destFile.mkdirs()) {
                    return ApiResult.fail("图片创建失败");
                }
            }
            file.transferTo(destFile);
            HashMap<String, String> hashMap = Maps.newHashMap(1);
            hashMap.put("url", destFilePath);
            return ApiResult.success(hashMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ApiResult.fail("图片上传失败");
    }

    /**
     * 文件下载
     *
     * @param file 文件
     * @return {@link ResponseEntity}
     */
    default ResponseEntity<Resource> download(File file) {
        return download(file, file.getName());
    }

    /**
     * 文件下载
     *
     * @param file     文件
     * @param fileName 生成的文件名
     * @return {@link ResponseEntity}
     */
    default ResponseEntity<Resource> download(File file, String fileName) {
        Resource resource = new FileSystemResource(file);
        return download(resource, fileName);
    }

    /**
     * 文件下载
     *
     * @param resource 文件资源
     * @param fileName 生成的文件名
     * @return {@link ResponseEntity}
     */
    default ResponseEntity<Resource> download(Resource resource, String fileName) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.setContentDisposition(ContentDisposition.builder("attachment").name(fileName).filename(fileName, StandardCharsets.UTF_8).build());
        return new ResponseEntity<>(resource, httpHeaders, HttpStatus.OK);
    }
}
