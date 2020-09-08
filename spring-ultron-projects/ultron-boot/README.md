## ultron-boot

### 基础功能

* 基础Controller `IController` 包含文件上传功能
* 通用请求返回体 `ApiResult`
* 基础状态码 `ResultStatus`
* 自定义状态码 实现 `IResultStatus` 参考 `ResultStatus`
* 自定义REST业务异常 `ServiceException`
* 全局异常处理 `GlobalExceptionHandler`

### 服务异常事件处理 `UltronErrorEvent`

这里处理 `GlobalExceptionHandler` 处理的异常
```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springultron.boot.error.UltronErrorEvent;

@Component
public class ErrorEventListener {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@EventListener
	public void listener(UltronErrorEvent event) {
		// 这里处理异常信息
		log.info("event = {}", event);
	}
}

```

### 请求日志
请求日志使用示例(不支持reactive运行环境):
```
@ApiLog(description = "用户登录")
@GetMapping("/login")
public ApiResult<User>> login() {
    UserDTO userDTO = new UserDTO();
    ...
    return ApiResult.success(userDTO, "登录成功");
}
```

配置
```yaml
#请求日志配置（配置文件添加）:
    ultron:
      log:
        enable: true # 开启ApiLog打印
        level: headers # 打印包括请求头 none/body
```

### 其他配置

```yaml
#异步任务线程池配置：
    
    ultron:
      async:
        core-pool-size: 3         # 核心线程数，默认：3
        max-pool-size: 300        # 线程池最大数量，默认：300  
        queue-capacity: 30000     # 线程池队列容量，默认：30000
        keep-alive-seconds: 300   # 空闲线程存活时间，默认：300秒
```

### WebClient http客户端使用示例

```
Map<String, Object> params = Maps.newHashMap(3);
params.put("id", 108);
params.put("name", "张三");
params.put("age", 23);
Mono<JSONObject> mono = WebClientUtil.postJSON("https://xxx", params, JSONObject.class)
```

