package org.springultron.cloud.servlet;

import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlBlockHandler;
import com.alibaba.csp.sentinel.adapter.servlet.callback.WebCallbackManager;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springultron.core.result.ApiResult;
import org.springultron.core.result.ResultCode;
import org.springultron.core.utils.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Sentinel 配置类
 *
 * @auther brucewuu
 * @date 2019-08-11 11:22
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SentinelConfiguration {

    public SentinelConfiguration() {
        WebCallbackManager.setUrlBlockHandler(new CustomerUrlBlockHandler());
    }

    /**
     * 限流、熔断统一处理类
     */
    public static class CustomerUrlBlockHandler implements UrlBlockHandler {

        @Override
        public void blocked(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws IOException {
            ApiResult result = ApiResult.failed(ResultCode.FLOW_LIMITING);
            WebUtils.renderJson(httpServletResponse, result);
        }
    }
}
