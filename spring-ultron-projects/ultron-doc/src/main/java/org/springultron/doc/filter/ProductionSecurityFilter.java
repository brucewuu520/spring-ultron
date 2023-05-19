/*
 * Copyright © 2017-2023 Knife4j(xiaoymin@foxmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springultron.doc.filter;

import com.github.xiaoymin.knife4j.extend.filter.BasicFilter;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

/***
 * <p>
 * {@code @since } 1.9.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2019/01/18 17:15
 */
public class ProductionSecurityFilter extends BasicFilter implements Filter {
    
    /***
     * 是否生产环境,如果是生成环境,过滤Swagger的相关资源请求
     */
    private boolean production;

    public ProductionSecurityFilter(boolean production) {
        this.production = production;
    }
    
    @Override
    public void init(FilterConfig filterConfig) {
        // 判断filterConfig
        Enumeration<String> enumeration = filterConfig.getInitParameterNames();
        // SpringMVC环境中,由此init方法初始化此Filter,SpringBoot环境中则不同
        if (enumeration.hasMoreElements()) {
            setProduction(Boolean.parseBoolean(filterConfig.getInitParameter("production")));
        }
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if (production) {
            String uri = httpServletRequest.getRequestURI();
            if (!match(uri)) {
                chain.doFilter(request, response);
            } else {
                response.setContentType("text/plain;charset=UTF-8");
                PrintWriter pw = response.getWriter();
                pw.write("You do not have permission to access this page");
                pw.flush();
            }
        } else {
            chain.doFilter(request, response);
        }
    }
    
    @Override
    public void destroy() {
        
    }
    
    public boolean isProduction() {
        return production;
    }
    
    public void setProduction(boolean production) {
        this.production = production;
    }

}
