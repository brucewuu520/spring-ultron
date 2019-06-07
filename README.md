## 前言
在基于Spring boot/cloud的微服务项目中，要创建若干module，每个module都要依赖Redis、mybatis和一些基础工具类等等，本项目就是为了简化Spring boot的配置，在自己项目中提炼出来的公共服务配置和工具，简化大型项目的开发依赖，未来会不断完善

### Ultron
本项目命名SpringUltron，灵感来自复仇者联盟2:奥创纪元，奥创是一个强大的人工智能机器人，希望本项目越来越强大，基于更多的自动化配置和抽象，能在项目开发中节约更多的时间

[![996.icu](https://img.shields.io/badge/link-996.icu-red.svg)](https://996.icu)
[![License](https://img.shields.io/badge/apache-2.0-blue.svg?style=flat)](http://www.apache.org/licenses/ "Feel free to contribute.")

## 项目结构
```shell
*├── spring-ultron              项目父级目录
    ├── ultron-core             API返回对象约束
    ├── ultron-httpclient       基于webflux的httpclinet
    ├── ultron-mybatis          mybatis-plus基础配置
    ├── ultron-redis            Redis配置和相关工具
    ├── ultron-utils            工具类
```    

## 使用步骤

### 第一步，下载本项目

    git clone https://github.com/brucewuu520/spring-ultron.git
    
### 第二步，编译安装本项目

    mvn clean install --项目会编译安装到本地maven创库
    
### 第三步，在自己的工程按需添加依赖库

1、核心库

    <dependency>
       <groupId>org.springultron</groupId>
       <artifactId>ultron-core</artifactId>
       <version>1.0.0</version>
    </dependency>  

2、http客户端库
    
    <dependency>
       <groupId>org.springultron</groupId>
       <artifactId>ultron-httpclient</artifactId>
       <version>1.0.0</version>
    </dependency> 
    
3、mybatis plus

    <dependency>
       <groupId>org.springultron</groupId>
       <artifactId>ultron-mybatis</artifactId>
       <version>1.0.0</version>
    </dependency> 
    
4、Redis

    <dependency>
       <groupId>org.springultron</groupId>
       <artifactId>ultron-redis</artifactId>
       <version>1.0.0</version>
    </dependency>
    
5、常用工具类

    <dependency>
       <groupId>org.springultron</groupId>
       <artifactId>ultron-utils</artifactId>
       <version>1.0.0</version>
    </dependency>         
            
## 许可证

[Apache License 2.0](https://github.com/brucewuu520/spring-ultron/blob/master/LICENSE)

Copyright (c) 2019-2020 brucewuu    