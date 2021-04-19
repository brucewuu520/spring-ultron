## 前言
在基于Spring boot/cloud的微服务项目中，要创建若干module，每个module都要依赖Redis、mybatis和一些基础工具类等等，本项目就是为了简化Spring boot的配置，在自己项目中提炼出来的公共服务配置和工具，简化大型项目的开发依赖;
无论是基于Spring boot的单体应用架构还是基于Spring Cloud的微服务应用架构，均提供模块化支持。


### Ultron
本项目命名SpringUltron，灵感来自复仇者联盟2:奥创纪元，奥创是一个强大的人工智能机器人，希望本项目越来越强大，基于更多的自动化配置和抽象，能在项目开发中节约更多的时间；
按功能拆分了N多模块，可以按需依赖，减少打包代码


[![996.icu](https://img.shields.io/badge/link-996.icu-red.svg)](https://996.icu)
[![License](https://img.shields.io/badge/apache-2.0-blue.svg?style=flat)](http://www.apache.org/licenses/ "Feel free to contribute.")

## 项目结构
```shell
*├── spring-ultron                  项目父级目录
    ├── spring-ultron-dependencies  依赖版本统一管理
    ├── spring-ultron-projects      核心模块工程
        ├── ultron-boot                 Spring boot脚手架，servlet/reactive全局异常捕获、基于aop的注解API日志打印(支持配置文件配置日志开关，日志内容等)、WebClient http客户端封装
        ├── ultron-cloud                Spring cloud脚手架（基于Spring Cloud Alibaba）
        ├── ultron-core                 核心库(请求统一返回体、常用错误代码、自定义业务异常、Jackson序列化/反序列化配置、常用工具类等)
        ├── ultron-logging              logback 的日志扩展；支持输出到ELK；链路追踪
        ├── ultron-crypto               对称及非对称加密解密工具，实现了:AES、DES、RSA、国密SM2、SM4等；以及各种秘钥生成工具
        ├── ultron-mybatis              mybatis plus自动化配置、分页工具等
        ├── ultron-qrcode               二维码生成、美化、识别
        ├── ultron-redis                Redis自动化配置、操作客户端；基于Redis的Spring Cache配置
        ├── ultron-redis-lock           基于Redis的分布式锁
        ├── ultron-caffeine             基于Caffeine的Spring Cache配置(默认定义了四个不同过期时间的缓存空间)     
        ├── ultron-captcha              图形验证码生成和校验
        ├── ultron-http                 基于OKhttp3封装的http客户端，Fluent语法风格，使用非常简便
        ├── ultron-openfeign            openfeign集成sentinel限流，熔断降级
        ├── ultron-security             Spring Security通用配置，支持jwt登录鉴权，RBAC权限控制
        ├── ultron-swagger              Swagger文档自动化配置(可在配置文件中开启/关闭，支持http basic认证)
        ├── ultron-xxl-job              xxl-job集成
        ├── ultron-wechat               微信开发工具包
```    

## 使用步骤

### 第一步，下载本项目

    git clone https://github.com/brucewuu520/spring-ultron.git
    
### 第二步，编译安装本项目

    mvn clean install --项目会编译安装到本地maven仓库
    
### 第三步，在自己的工程按需添加依赖库

1、在项目parent pom.xml中添加：

    <properties>
        <spring-ultron.version>2.0.0</spring-ultron.version>
        <java.version>1.8</java.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springultron</groupId>
                <artifactId>spring-ultron-dependencies</artifactId>
                <version>${spring-ultron.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

2、核心库(请求统一返回体、常用错误代码、自定义业务异常、Jackson序列化/反序列化配置、常用工具类等)

    <dependency>
       <groupId>org.springultron</groupId>
       <artifactId>ultron-core</artifactId>
    </dependency>
    
    统一返回体使用示例：
        @GetMapping("/test")
        public ApiResult<Test>> test() {
            Test test = new Test();
            return ApiResult.success(test); // or return ApiResult.fail(ResultCode.PARAM_VALID_FAILED);
        }
        
    自定义异常使用：
        @GetMapping("/test")
        public ApiResult<Test>> test() {
            if (true) {
                ApiResult.throwFail(ResultCode.API_EXCEPTION);
            }
            return ApiResult.apiException(new ServiceException("操作异常"));
        }
    自定义错误状态码枚举类实现IResultCode接口，即可使用 ApiResult.fail(IResultCode)返回错误信息
    
    Jackson配置序列化和反序列化支持java8 Time
    
        
3、ultron-crypto 对称及非对称加密/解密/签名/验签工具，实现了:AES、DES、RSA、国密SM2、SM4等；以及各种秘钥生成工具

        <dependency>
           <groupId>org.springultron</groupId>
           <artifactId>ultron-crypto</artifactId>
        </dependency>
    
    部分加解密算法实现（如：国密SM2、SM4）需添加BC库依赖：
    
        <dependency>
            <groupId>org.bouncycastle</groupId>
            <artifactId>bcprov-jdk15on</artifactId>
            <version>${bouncycastle.version}</version>
        </dependency>
    
    使用示例见单元测试        
        
4、mybatis plus自动化配置、分页工具等

    <dependency>
       <groupId>org.springultron</groupId>
       <artifactId>ultron-mybatis</artifactId>
    </dependency> 
    
    分页工具使用示例：
    
        PageQuery query = new PageQuery();
        query.setCurrent(1);
        query.setSize(10);
        String[] asces = new String[] {"sort", "order"};
        query.setAscs(asces);
        IPage<T> page = PageUtils.getPage(query);
 
    
5、Redis自动化配置、操作客户端

    <dependency>
       <groupId>org.springultron</groupId>
       <artifactId>ultron-redis</artifactId>
    </dependency>
    
    Redis操作客户端使用示例:
    
        @Autowired
        private RedisClient redisClient;
     
        redisClient.setString("key", "value", Duration.ofSeconds(120))
        
        Object obj = new Object();
        redisClient.set("key", obj, Duration.ofSeconds(120))
        
        ...
    
    Redis反应式客户端使用:
        添加依赖:
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-webflux</artifactId>
            </dependency>
        
        @Autowired
        private ReactiveRedisClient reactiveRedisClient;
     
        Mono<Boolean> result = reactiveRedisClient.setString("key", "value", Duration.ofSeconds(120))
        
        Object obj = new Object();
        Mono<Boolean> result = reactiveRedisClient.set("key", obj, Duration.ofSeconds(120))
        
        
    Spring cache 扩展cache name 支持 # 号分隔 cache name 和 超时 ttl(默认单位秒)。使用示例：
    
		@Cacheable(value = "user_cache#30m", key = "#id")
		public String findUserById(Serializable id) {
			log.info("selectById");
			return "selectById:" + id;
		}
        
6、Spring boot脚手架，servlet/全局异常捕获、基于aop的注解API日志打印(支持配置文件配置日志开关，日志内容等)、WebClient http客户端封装

    <dependency>
       <groupId>org.springultron</groupId>
       <artifactId>ultron-boot</artifactId>
    </dependency>
    
    请求日志使用示例(不支持reactive运行环境):
        @ApiLog(description = "用户登录")
        @GetMapping("/login")
        public ApiResult<User>> login() {
            UserDTO userDTO = new UserDTO();
            ...
            return ApiResult.success(userDTO, "登录成功");
        }
        
    请求日志配置（配置文件添加）:
        ultron:
          log:
            enable: true # 开启ApiLog打印
            level: headers # 打印包括请求头 none/body
            
    WebClient http客户端使用示例：
    需添加依赖：
    
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
        </dependency>
     
    使用示例：
        
        Map<String, Object> params = Maps.newHashMap(3);
        params.put("id", 108);
        params.put("name", "张三");
        params.put("age", 23);
        Mono<JSONObject> mono = WebClientUtil.postJSON("https://xxx", params, JSONObject.class)
        
    异步任务线程池配置：
        
        ultron:
          async:
            core-pool-size: 3         # 核心线程数，默认：3
            max-pool-size: 300        # 线程池最大数量，默认：300  
            queue-capacity: 30000     # 线程池队列容量，默认：30000
            keep-alive-seconds: 300   # 空闲线程存活时间，默认：300秒
    
7、Spring cloud脚手架(基于Spring cloud Hoxton、Spring cloud alibaba 2.2.0)

    <dependency>
       <groupId>org.springultron</groupId>
       <artifactId>ultron-cloud</artifactId>
    </dependency>
    
    默认集成了sentinel熔断限流，支持传统servlet和reactive，统一配置熔断/限流的异常返回值
    
    负载均衡的http客户端使用（基于ReactorLoadBalancerExchangeFilterFunction的反应式负载均衡器，性能比RestTemplate要好得多）:
    添加依赖:
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
        </dependency>
    
        @Autowire
        private WebClient lbWebClient;           

8、Swagger 接口文档

    <dependency>
       <groupId>org.springultron</groupId>
       <artifactId>ultron-swagger</artifactId>
    </dependency> 
    
    文档配置(配置文件中添加):
    
        swagger:
          enable: true  # 默认
          title: xxx服务
          description: xxx服务接口文档
          contact-user: brucewuu
          contact-email: xxx@xxx.com
          contact-url: xxx
          authorization:
            enabled: true
            name: BearerToken
            header-name: Authorization
          knife4j:
            production: false
            basic:
              enable: true
              username: admin
              password: 123456
          
9、Spring Security通用配置，支持jwt登录鉴权，RBAC权限控制

    <dependency>
        <groupId>org.springultron</groupId>
        <artifactId>ultron-security</artifactId>
    </dependency> 
                
10、ultron-http 使用示例
    
    <dependency>
        <groupId>org.springultron</groupId>
        <artifactId>ultron-http</artifactId>
    </dependency>  

    使用示例(同步)：
       JSONObject result = HttpRequest.get("https://xxx")
                  .query("name1", "value1")
                  .query("name2", "value2")
                  .log()
                  .execute()
                  .asObject(JSONObject.class);
       
       Map<String, Object> params = Maps.newHashMap(3);
               params.put("id", 108);
               params.put("name", "张三");
               params.put("age", 23);           
       Map<String, Object> result = HttpRequest.post("https://xxx")
                         .bodyValue(params)
                         .log()
                         .execute()
                         .asMap();
                         
    异步示例：
    
       HttpRequest.get("https://xxx")
             .query("name1", "value1")
             .query("name2", "value2")
             .async()
             .success(responseSpec -> {
                 JSONObject result = responseSpec.asObject(JSONObject.class);
             })
             .fail(((request, e) -> {
                    log.error("请求失败", e);
             }))
             .execute()

11、ultron-logging 使用示例
若开启logstash必须添加下面依赖:

	<dependency>
            <groupId>net.logstash.logback</groupId>
            <artifactId>logstash-logback-encoder</artifactId>
	</dependency>

	ultron:
		logging:
			close-console: true -关闭控制台日志输出
			close-file: false - 关闭文件日志输出
			logstash:
				enabled: true -开启logstash
				destinations: localhost:4560,localhost:4561
开启链路追踪：
启动类上配置 @EnableTraceId 注解，配置文件添加如下配置：

	logging.pattern.console="%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(${LOG_LEVEL_PATTERN:-%5p}) %clr(${PID:- }){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr([%X{traceId}]){yellow} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n"
	
	logging.pattern.file="%d{yyyy-MM-dd HH:mm:ss.SSS} ${LOG_LEVEL_PATTERN:-%5p} ${PID:- } --- [%t] [%X{traceId}] %-40.40logger{39} : %m%n"
	

12、图形验证码使用

	<dependency>
		<groupId>org.springultron</groupId>
		<artifactId>ultron-captcha</artifactId>
	</dependency>	
	
	@Autowired
	private CaptchaService captchaService
	
	String code = captchaService.generateBase64(uuid)
	
	boolean checkResult = captchaService.validate(uuid,userInputCaptcha)
	
	自定义缓存：
		继承CaptchaCache实现自己的缓存实现，注入CaptchaCache bean覆盖默认的bean
		
	
13、ultron-caffeine 缓存使用

	<dependency>
		<groupId>org.springultron</groupId>
		<artifactId>ultron-caffeine</artifactId>
	</dependency>

	添加注解：@EnableCaching开启缓存
	ultron-caffeine默认配置了四个不同过期时间的缓存空间:
		1、cacheNames=FIVE_SECOND（过期时间5分钟）
		2、cacheNames=THIRTY_SECOND（过期时间30分钟）
		3、cacheNames=TWO_HOUR（过期时间2小时）
		4、cacheNames=A_WEEK（过期时间7天）
	同时也可以配置Spring cache的缓存配置：
		spring:
			cache:
				cache-names: my_cache
				type: caffeine
				caffeine:
					spec: maximumSize=1024,refreshAfterWrite=60s
	
		@Cacheable(value = "FIVE_SECOND", key = "#id")
		public String findUserById(Serializable id) {
			log.info("selectById");
			return "selectById:" + id;
		}
		
		@Autowired
		private CacheManager cacheManager;
		
		public String findUserById(Serializable id) {
			log.info("selectById");
			String value = cacheManager.getCache(CaffeineCacheEnum.FIVE_SECOND.getName()).get(id, String.class);
			return "selectById:" + id;
		}
		
14、微信开发工具包

	<dependency>
		<groupId>org.springultron</groupId>
		<artifactId>ultron-wechat</artifactId>
	</dependency>
	
	公众号配置：
		wechat:
			wx-conf:
				app-id: xxx
				app-secret: xxx
				token: xxx
				encoding-aes-key: xxx
				encrypt-message: true
				
	小程序配置：
		wechat:
			wxa-conf:
				app-id: xxx
				app-secret: xxx
				token: xxx
				encoding-aes-key: xxx
				encrypt-message: true
	
	公众号：
		@Autowired
		private WxApiService wxApiService;
		
		消息接收：
		@RestController
		@RequestMapping("/wechat")
		public class WechatMessageController extends WxMsgControllerAdapter {
			@Override
			protected void processTextMsg(InTextMsg textMsg) {
				log.info(Jackson.toJson(textMsg));
				renderOutTextMsg(textMsg, "欢迎关注");
			}
			
			@Override
			protected void processFollowEvent(InFollowEvent followEvent) {
				renderDefault();
			}
			
			@Override
			protected void processMenuEvent(InMenuEvent menuEvent) {
				renderDefault();
			}
		
		}
		
		小程序：
		@Autowired
		private WxaApiService wxaApiService;
	
		消息接收：
		@RestController
		public class MessageController extends WxaMsgController {
			@Override
			protected void processTextMsg(WxaTextMsg textMsg) {
			
			}
			
			@Override
			protected void processImageMsg(WxaImageMsg imageMsg) {
			
			}
			
			@Override
			protected void processMiniProgramPageMsg(WxaMiniProgramPageMsg miniProgramPageMsg) {
			
			}
			
			@Override
			protected void processUserEnterSessionMsg(WxaUserEnterSessionMsg userEnterSessionMsg) {
			
			}
			
			@Override
			protected void processUnknownMsg(WxaUnknownMsg unknownMsg) {
			
			}
		}
	
    
## 更新日志
* 2.0版本基于Spring boot 2.2.x
* 1.0版基于Spring boot 2.1.x 请切换分支1.x

## 鸣谢
感谢 [Mica](https://github.com/lets-mica/mica)，有些工具类和配置参考了mica            
                
## 许可证

[Apache License 2.0](https://github.com/brucewuu520/spring-ultron/blob/master/LICENSE)

Copyright (c) 2019-2020 brucewuu    