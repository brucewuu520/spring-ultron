//package org.springultron.logging.trace;
//
//import org.springframework.context.annotation.Import;
//
//import java.lang.annotation.*;
//
///**
// * 允许配置链路追踪注解
// * <p>
// * 配置文件加入以下配置:
// * logging.pattern.console="%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(${LOG_LEVEL_PATTERN:-%5p}) %clr(${PID:- }){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr([%X{traceId}]){yellow} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n"
// * logging.pattern.file="%d{yyyy-MM-dd HH:mm:ss.SSS} ${LOG_LEVEL_PATTERN:-%5p} ${PID:- } --- [%t] [%X{traceId}] %-40.40logger{39} : %m%n"
// * </p>
// *
// * @author brucewuu
// * @date 2021/4/17 下午5:09
// */
//@Target(ElementType.TYPE)
//@Retention(RetentionPolicy.RUNTIME)
//@Documented
//@Import({TraceIdAutoConfiguration.class})
//public @interface EnableTraceId {
//
//}
