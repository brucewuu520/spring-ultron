## 前言
在基于Spring boot/cloud的微服务项目中，要创建若干module，每个module都要依赖Redis、mybatis和一些基础工具类等等，本项目就是为了简化Spring boot的配置，在自己项目中提炼出来的公共服务配置和工具，简化大型项目的开发依赖

### Ultron
本项目命名SpringUltron，灵感来自复仇者联盟2:奥创纪元，奥创是一个强大的人工智能机器人，希望本项目越来越强大，基于更多的自动化配置和抽象，能在项目开发中节约更多的时间

[![996.icu](https://img.shields.io/badge/link-996.icu-red.svg)](https://996.icu)
[![License](https://img.shields.io/badge/apache-2.0-blue.svg?style=flat)](http://www.apache.org/licenses/ "Feel free to contribute.")

## 项目结构
```shell
*├── spring-ultron                  项目父级目录
    ├── spring-ultron-dependencies  依赖版本统一管理
    ├── ultron-core                 核心库(请求统一返回体、常用错误代码、自定义业务异常、Jackson序列化/反序列化配置、常用工具类等)
    ├── ultron-mybatis              mybatis plus自动化配置、分页工具等
    ├── ultron-redis                Redis自动化配置、操作客户端
    ├── ultron-boot                 Spring boot脚手架，servlet/reactive全局异常捕获、基于aop的注解API日志打印(支持配置文件配置日志开关，日志内容等)、WebClient http客户端封装
    ├── ultron-cloud                Spring cloud脚手架（待完善）
    ├── ultron-http                 基于OKhttp3 4.0.0版本封装的http客户端，使用起来倍儿爽
    ├── ultron-swagger              Swagger文档自动化配置(可在配置文件中开启/关闭)
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
    
    返回体使用示例：
        @GetMapping("/test")
        public Result<Test>> test() {
            Test test = new Test();
            return Result.success(test); // or return Result.failed(ResultCode.PARAM_VALID_FAILED);
        }
        
    自定义异常使用：
        @GetMapping("/test")
        public Result<Test>> test() {
            return ApiResult.throwFail(ResultCode.API_EXCEPTION);
        }
        
3、mybatis plus自动化配置、分页工具等

    <dependency>
       <groupId>org.springultron</groupId>
       <artifactId>ultron-mybatis</artifactId>
    </dependency> 
    
    分页工具使用示例：
    
        Query query = new Query();
        query.setCurrent(1);
        query.setSize(10);
        String[] asces = new String[] {"sort", "order"};
        query.setAscs(asces);
        IPage<T> page = PageUtils.getPage(query);
 
    
4、Redis自动化配置、操作客户端

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
        
    Spring cache 扩展cache name 支持 # 号分隔 cache name 和 超时 ttl(单位秒)。使用示例：
    
        @CachePut(value = "user#300", key = "#id")
    
5、Spring boot脚手架，servlet/全局异常捕获、基于aop的注解API日志打印(支持配置文件配置日志开关，日志内容等)、WebClient http客户端封装

    <dependency>
       <groupId>org.springultron</groupId>
       <artifactId>ultron-boot</artifactId>
    </dependency>
    
    请求日志使用示例(不支持reactive运行环境):
        @ApiLog(description = "用户登录")
        @GetMapping("/login")
        public Result<User>> login() {
            UserDTO userDTO = new UserDTO();
            ...
            return Result.success(userDTO, "登录成功");
        }
        
    请求日志配置（配置文件添加）:
        ultron:
          log:
            enable: true # 开启ApiLog打印
            level: headers # 打印包括请求头 none/body
            
    WebClient http客户端使用示例：
    需添加：
    
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
    
6、Spring cloud脚手架

    <dependency>
       <groupId>org.springultron</groupId>
       <artifactId>ultron-cloud</artifactId>
    </dependency>           

7、Swagger 接口文档（基于swagger-bootstrap-ui 1.9.6）

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
                
8、基于OkHttp3 4.x版本使用示例

    <properties>
        <okhttp3.version>4.2.2</okhttp3.version>
    </properties>
    
    <dependency>
        <groupId>org.springultron</groupId>
        <artifactId>ultron-http</artifactId>
    </dependency>  

    使用示例：
    
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
       JSONObject result = HttpRequest.post("https://xxx")
                         .bodyJson(params)
                         .log()
                         .execute()
                         .asObject(JSONObject.class);
                         
    异步示例：
    
       HttpRequest.get("https://xxx")
             .query("name1", "value1")
             .query("name2", "value2")
             .enqueue(new Callback() {
                 @Override
                 public void onFailure(@NotNull Call call, @NotNull IOException e) {
                     
                 }
     
                 @Override
                 public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
     
                 })
    
    
## 更新日志
* 2.0版本基于Spring boot 2.2.x
* 1.0版基于Spring boot 2.1.x 请切换分支1.x

## 鸣谢
感谢 [Mica](https://github.com/lets-mica/mica)，有些工具类和配置参考了mica            
                
## 许可证

[Apache License 2.0](https://github.com/brucewuu520/spring-ultron/blob/master/LICENSE)

Copyright (c) 2019-2020 brucewuu    