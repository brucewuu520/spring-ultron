package org.springultron.boot.error;

import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 服务异常事件
 *
 * @author brucewuu
 * @date 2020/7/26 20:08
 */
public class UltronErrorEvent implements Serializable {
    private static final long serialVersionUID = 1333817733286975140L;

    /**
     * 应用名
     */
    @Nullable
    private String appName;
    /**
     * 环境
     */
    @Nullable
    private String env;
    /**
     * 远程ip 主机名
     */
    @Nullable
    private String remoteHost;
    /**
     * 请求方法名
     */
    @Nullable
    private String requestMethod;
    /**
     * 请求url
     */
    @Nullable
    private String requestUrl;
    /**
     * 堆栈信息
     */
    @Nullable
    private String stackTrace;
    /**
     * 异常名
     */
    @Nullable
    private String exceptionName;
    /**
     * 异常消息
     */
    @Nullable
    private String message;
    /**
     * 类名
     */
    @Nullable
    private String className;
    /**
     * 文件名
     */
    @Nullable
    private String fileName;
    /**
     * 方法名
     */
    @Nullable
    private String methodName;
    /**
     * 代码行数
     */
    @Nullable
    private Integer lineNumber;
    /**
     * 异常时间
     */
    @Nullable
    private LocalDateTime createdAt;

    @Nullable
    public String getAppName() {
        return appName;
    }

    public void setAppName(@Nullable String appName) {
        this.appName = appName;
    }

    @Nullable
    public String getEnv() {
        return env;
    }

    public void setEnv(@Nullable String env) {
        this.env = env;
    }

    @Nullable
    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(@Nullable String remoteHost) {
        this.remoteHost = remoteHost;
    }

    @Nullable
    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(@Nullable String requestMethod) {
        this.requestMethod = requestMethod;
    }

    @Nullable
    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(@Nullable String requestUrl) {
        this.requestUrl = requestUrl;
    }

    @Nullable
    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(@Nullable String stackTrace) {
        this.stackTrace = stackTrace;
    }

    @Nullable
    public String getExceptionName() {
        return exceptionName;
    }

    public void setExceptionName(@Nullable String exceptionName) {
        this.exceptionName = exceptionName;
    }

    @Nullable
    public String getMessage() {
        return message;
    }

    public void setMessage(@Nullable String message) {
        this.message = message;
    }

    @Nullable
    public String getClassName() {
        return className;
    }

    public void setClassName(@Nullable String className) {
        this.className = className;
    }

    @Nullable
    public String getFileName() {
        return fileName;
    }

    public void setFileName(@Nullable String fileName) {
        this.fileName = fileName;
    }

    @Nullable
    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(@Nullable String methodName) {
        this.methodName = methodName;
    }

    @Nullable
    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(@Nullable Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    @Nullable
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@Nullable LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "UltronErrorEvent{" +
                "appName='" + appName + '\'' +
                ", env='" + env + '\'' +
                ", remoteHost='" + remoteHost + '\'' +
                ", requestMethod='" + requestMethod + '\'' +
                ", requestUrl='" + requestUrl + '\'' +
                ", stackTrace='" + stackTrace + '\'' +
                ", exceptionName='" + exceptionName + '\'' +
                ", message='" + message + '\'' +
                ", className='" + className + '\'' +
                ", fileName='" + fileName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", lineNumber=" + lineNumber +
                ", createdAt=" + createdAt +
                '}';
    }
}
