package org.springultron.security.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springultron.security.jwt.JwtProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 退出登录处理器，我们可以从 logout 方法的 authentication 变量中 获取当前用户信息。
 * 你可以通过这个来实现你具体想要的业务。比如记录用户下线退出时间、IP 等等。
 *
 * @author brucewuu
 * @date 2020/1/10 10:43
 */
public class SimpleLogoutHandler implements LogoutHandler {
    private static Logger log = LoggerFactory.getLogger(SimpleLogoutHandler.class);

    private final JwtProcessor jwtProcessor;

    public SimpleLogoutHandler(JwtProcessor jwtProcessor) {
        this.jwtProcessor = jwtProcessor;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("logout authentication: {}", authentication);
        if (authentication != null) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            log.info("username: {}  is offline now", userDetails.getUsername());
            jwtProcessor.logout(userDetails.getUsername());
        }
    }
}