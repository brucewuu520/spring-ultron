## ultron-oss

兼容S3 协议的通用文件存储工具类 ，支持 兼容S3 协议的云存储 

- MINIO
- 阿里云
- 华为云
- 腾讯云
- 京东云

...

## spring boot starter依赖

- 
```xml
<dependency>
    <groupId>org.springultro</groupId>
    <artifactId>ultron-oss</artifactId>
    <version>${version></version>
</dependency>
```

## 使用方法

### 配置文件

```yaml
ultron:
    oss:
        #使用云OSS  需要关闭
        path-style-access: false 
        #服务器地址
        endpoint: xxx.com或者IP
        # 上文创建的AK, 一定注意复制完整不要有空格
        access-key: xxx   
        # 上文创建的SK, 一定注意复制完整不要有空格
        secret-key: xxx   
        # 上文创建的桶名称
        bucketName: ultron 
```

### 代码使用

```java
@Autowire
private OssService ossService;
/**
 * 上传文件
 * 文件名采用uuid,避免原始文件名中带"-"符号导致下载的时候解析出现异常
 *
 * @param file 资源
 * @return R(bucketName, filename)
 */
@PostMapping("/upload")
public R upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
	ossService.putObject(CommonConstants.BUCKET_NAME, fileName, file.getInputStream());
	return R.ok(resultMap);
}
```
